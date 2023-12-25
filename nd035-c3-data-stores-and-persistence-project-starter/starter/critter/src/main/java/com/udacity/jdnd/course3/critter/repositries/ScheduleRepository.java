package com.udacity.jdnd.course3.critter.repositries;

import com.udacity.jdnd.course3.critter.entities.EmployeeEntity;
import com.udacity.jdnd.course3.critter.entities.PetEntity;
import com.udacity.jdnd.course3.critter.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findScheduleEntitiesByPetEntitiesContains(PetEntity pets);

    List<ScheduleEntity> findScheduleEntitiesByEmployeeEntitiesContains(EmployeeEntity employees);

    List<ScheduleEntity> findAllByPetEntitiesIn(List<PetEntity> petEntities);

}
