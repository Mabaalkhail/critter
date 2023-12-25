package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.EmployeeEntity;
import com.udacity.jdnd.course3.critter.repositries.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public EmployeeEntity save(EmployeeDTO employee) {
        return employeeRepository.save(modelMapper.map(employee, EmployeeEntity.class));
    }

    public EmployeeDTO convertToDto(EmployeeEntity employeeEntity) {
        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    public EmployeeEntity findById(Long id) throws NoSuchElementException {
        return employeeRepository.getOne(id);
    }

    public void setAvailability(Long id, Set<DayOfWeek> daysAvailable) {
        EmployeeEntity employeeEntity = employeeRepository.getOne(id);
        employeeEntity.setDaysAvailable(daysAvailable);

        employeeRepository.save(employeeEntity);
    }

    public Set<DayOfWeek> findDaysAvailable(Long id) throws NoSuchElementException {
        return employeeRepository.findById(id).get().getDaysAvailable();
    }

    public List<EmployeeEntity> findAll() {
        return employeeRepository.findAll();
    }

    public List<EmployeeEntity> getAvailableEmployees(LocalDate date, Set<EmployeeSkill> employeeSkills) {
        try {
            List<EmployeeEntity> allEmployeeEntities = retrieveAllEmployees();
            return filterAvailableEmployees(allEmployeeEntities, date, employeeSkills);
        } catch (Exception e) {
            handleException(e);
        }

        return Collections.emptyList();
    }

    private List<EmployeeEntity> retrieveAllEmployees() {
        return findAll();
    }

    private List<EmployeeEntity> filterAvailableEmployees(List<EmployeeEntity> employees, LocalDate date, Set<EmployeeSkill> employeeSkills) {
        return employees.stream()
                .filter(employee ->
                        employee.getDaysAvailable().contains(date.getDayOfWeek())
                                && employee.getSkills().containsAll(employeeSkills))
                .collect(Collectors.toList());
    }

    private void handleException(Exception e) {
        throw new RuntimeException(e);
    }

}
