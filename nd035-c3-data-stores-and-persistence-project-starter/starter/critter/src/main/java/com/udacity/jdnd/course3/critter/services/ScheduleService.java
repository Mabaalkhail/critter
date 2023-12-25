package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.EmployeeEntity;
import com.udacity.jdnd.course3.critter.entities.PetEntity;
import com.udacity.jdnd.course3.critter.entities.ScheduleEntity;
import com.udacity.jdnd.course3.critter.repositries.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositries.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositries.PetRepository;
import com.udacity.jdnd.course3.critter.repositries.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PetRepository petRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public ScheduleService(ScheduleRepository scheduleRepository, PetService petService, PetRepository petRepository, EmployeeService employeeService, EmployeeRepository employeeRepository, CustomerService customerService, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public ScheduleDTO converToDto(ScheduleEntity scheduleEntity) {
        ScheduleDTO scheduleDTO = modelMapper.map(scheduleEntity, ScheduleDTO.class);
        if (scheduleEntity.getEmployeeEntities() != null && !scheduleEntity.getEmployeeEntities().isEmpty()) {
            scheduleDTO.setEmployeeIds(scheduleEntity.getEmployeeEntities()
                    .stream().map(EmployeeEntity::getId)
                    .collect(Collectors.toList()));
        }
        if (scheduleEntity.getPetEntities() != null && !scheduleEntity.getPetEntities().isEmpty()) {
            scheduleDTO.setPetIds(scheduleEntity.getPetEntities()
                    .stream().map(PetEntity::getId)
                    .collect(Collectors.toList()));
        }

        return scheduleDTO;
    }

    public ScheduleEntity save(ScheduleDTO scheduleDTO) {
        try {
            ScheduleEntity scheduleEntity = mapToScheduleEntity(scheduleDTO);

            if (scheduleDTO.getEmployeeIds() != null && !scheduleDTO.getEmployeeIds().isEmpty()) {
                setEmployeeEntities(scheduleEntity, scheduleDTO.getEmployeeIds());
            }

            if (scheduleDTO.getPetIds() != null && !scheduleDTO.getPetIds().isEmpty()) {
                setPetEntities(scheduleEntity, scheduleDTO.getPetIds());
            }

            return persistSchedule(scheduleEntity);
        } catch (Exception e) {
            handleException(e);
        }

        return null;
    }

    private ScheduleEntity mapToScheduleEntity(ScheduleDTO scheduleDTO) {
        return modelMapper.map(scheduleDTO, ScheduleEntity.class);
    }

    private void setEmployeeEntities(ScheduleEntity scheduleEntity, List<Long> employeeIds) {
        try {
            List<EmployeeEntity> employeeEntities = retrieveEmployeeEntitiesByIds(employeeIds);
            updateScheduleWithEmployeeEntities(scheduleEntity, employeeEntities);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private List<EmployeeEntity> retrieveEmployeeEntitiesByIds(List<Long> employeeIds) {
        return employeeRepository.findAllById(employeeIds);
    }

    private void updateScheduleWithEmployeeEntities(ScheduleEntity scheduleEntity, List<EmployeeEntity> employeeEntities) {
        scheduleEntity.setEmployeeEntities(employeeEntities);
    }

    private void setPetEntities(ScheduleEntity scheduleEntity, List<Long> petIds) {
        List<PetEntity> petEntities = petRepository.findAllById(petIds);
        scheduleEntity.setPetEntities(petEntities);
    }

    private ScheduleEntity persistSchedule(ScheduleEntity scheduleEntity) {
        return scheduleRepository.save(scheduleEntity);
    }

    private void handleException(Exception e) {
        throw new RuntimeException(e);
    }


    public List<ScheduleEntity> findAll() {
        return scheduleRepository.findAll();
    }

    public List<ScheduleEntity> findByEmployeeId(long employeeId) {
        return scheduleRepository.findScheduleEntitiesByEmployeeEntitiesContains(employeeRepository.getOne(employeeId));
    }

    public List<ScheduleEntity> findByPetId(long petId) {
        return scheduleRepository.findScheduleEntitiesByPetEntitiesContains(petRepository.getOne(petId));
    }

    public List<ScheduleEntity> findByCustomerId(long customerId) {
        return scheduleRepository.findAllByPetEntitiesIn(customerRepository.getOne(customerId).getPetEntities());
    }

}
