package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.services.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {

        return scheduleService.converToDto(scheduleService.save(scheduleDTO));

    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        return scheduleService.findAll().stream()
                .map(scheduleService::converToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {

        return scheduleService.findByPetId(petId).stream()
                .map(scheduleService::converToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.findByEmployeeId(employeeId)
                .stream().map(scheduleService::converToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {

        return scheduleService.findByCustomerId(customerId)
                .stream().map(scheduleService::converToDto)
                .collect(Collectors.toList());

    }
}

