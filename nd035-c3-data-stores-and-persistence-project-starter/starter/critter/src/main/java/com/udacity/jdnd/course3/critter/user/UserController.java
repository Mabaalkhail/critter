package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.CustomerEntity;
import com.udacity.jdnd.course3.critter.entities.EmployeeEntity;
import com.udacity.jdnd.course3.critter.services.CustomerService;
import com.udacity.jdnd.course3.critter.services.EmployeeService;
import com.udacity.jdnd.course3.critter.services.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final ScheduleService scheduleService;

    public UserController(CustomerService customerService, EmployeeService employeeService, ScheduleService scheduleService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.scheduleService = scheduleService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.convertToDTO(customerService.save(customerDTO));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        try {
            return retrieveAllCustomers();
        } catch (Exception e) {
            handleException(e);
        }

        return Collections.emptyList();
    }

    private List<CustomerDTO> retrieveAllCustomers() {
        List<CustomerEntity> customers = customerService.findAll();
        return customers.stream()
                .map(customerService::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        try {
            return retrieveOwnerByPetId(petId);
        } catch (NoSuchElementException e) {
            handleNoSuchElementException(e);
        } catch (Exception e) {
            handleException(e);
        }

        return null;
    }

    private CustomerDTO retrieveOwnerByPetId(long petId) {
        CustomerEntity customer = customerService.findByPetId(petId);
        return customerService.convertToDTO(customer);
    }


    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            return employeeService.convertToDto(employeeService.save(employeeDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            return retrieveEmployeeDTO(employeeId);
        } catch (NoSuchElementException e) {
            handleNoSuchElementException(e);
        } catch (Exception e) {
            handleException(e);
        }

        return null;
    }

    private EmployeeDTO retrieveEmployeeDTO(long employeeId) {
        EmployeeEntity employee = employeeService.findById(employeeId);
        return employeeService.convertToDto(employee);
    }

    private void handleNoSuchElementException(NoSuchElementException e) {
        throw new RuntimeException("Employee not found", e);
    }


    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            updateEmployeeAvailability(employeeId, daysAvailable);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void updateEmployeeAvailability(long employeeId, Set<DayOfWeek> daysAvailable) {
        employeeService.setAvailability(employeeId, daysAvailable);
    }

    private void handleException(Exception e) {
        throw new RuntimeException(e);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> availableEmployees = new ArrayList<>();

        try {
            List<EmployeeEntity> employees = employeeService.getAvailableEmployees(employeeDTO.getDate(), employeeDTO.getSkills());

            for (EmployeeEntity employee : employees) {
                EmployeeDTO employeeDto = employeeService.convertToDto(employee);
                availableEmployees.add(employeeDto);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return availableEmployees;
    }

}


