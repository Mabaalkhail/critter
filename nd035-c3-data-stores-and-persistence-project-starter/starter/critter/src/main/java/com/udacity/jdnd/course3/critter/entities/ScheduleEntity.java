package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany(targetEntity = EmployeeEntity.class)
    private List<EmployeeEntity> employeeEntities;
    @ManyToMany(targetEntity = PetEntity.class)
    private List<PetEntity> petEntities;
    private LocalDate date;
    @ElementCollection
    private Set<EmployeeSkill> activities;


}
