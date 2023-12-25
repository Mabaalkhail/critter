package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.CustomerEntity;
import com.udacity.jdnd.course3.critter.entities.PetEntity;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repositries.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositries.PetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, ModelMapper modelMapper, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.modelMapper = modelMapper;
        this.customerRepository = customerRepository;
    }

    public PetDTO convertToDTO(PetEntity petEntity) {
        return modelMapper.map(petEntity, PetDTO.class);
    }

    public PetEntity findById(long petId) {
        return petRepository.getOne(petId);
    }

    public PetEntity save(PetDTO petDTO) {
        PetEntity petEntity = modelMapper.map(petDTO, PetEntity.class);
        CustomerEntity owner = customerRepository.getOne(petDTO.getOwnerId());
        List<PetEntity> petEntityList = new ArrayList<>();
        if (owner.getPetEntities() == null) {
            owner.setPetEntities(petEntityList);
        } else {
            petEntityList = owner.getPetEntities();
        }
        petEntity.setOwner(owner);
        petEntity = petRepository.save(petEntity);
        petEntityList.add(petEntity);
        owner.setPetEntities(petEntityList);
        customerRepository.save(owner);

        return petEntity;
    }

    public PetEntity FindById(long Id) {
        return petRepository.getOne(Id);
    }

    public List<PetEntity> findAll() {
        return petRepository.findAll();
    }

    public List<PetEntity> findByOwnerId(Long customerId) {
        return petRepository.findPetEntitiesByOwner_Id(customerId);
    }
}
