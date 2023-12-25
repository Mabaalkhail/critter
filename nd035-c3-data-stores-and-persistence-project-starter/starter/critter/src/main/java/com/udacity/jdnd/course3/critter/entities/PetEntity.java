package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.pet.PetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private PetType type;
    private String name;
    @ManyToOne(optional = false,
            targetEntity = CustomerEntity.class)
    private CustomerEntity owner;
    private LocalDate birthDate;
    private String notes;
    @ManyToMany(targetEntity = ScheduleEntity.class)
    private List<ScheduleEntity> scheduleEntities;

}
