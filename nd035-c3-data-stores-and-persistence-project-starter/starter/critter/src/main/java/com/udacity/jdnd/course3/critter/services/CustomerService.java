package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.CustomerEntity;
import com.udacity.jdnd.course3.critter.entities.PetEntity;
import com.udacity.jdnd.course3.critter.repositries.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositries.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PetRepository petRepository;


    public CustomerEntity save(CustomerDTO customerDTO) {
        try {
            CustomerEntity owner = modelMapper.map(customerDTO, CustomerEntity.class);

            if (customerDTO.getPetIds() != null) {
                owner = setPetIds(customerDTO.getPetIds(), owner);
            }

            return persistCustomer(owner);
        } catch (Exception e) {
            handleException(e);
        }

        return null;
    }

    private CustomerEntity persistCustomer(CustomerEntity owner) {
        return customerRepository.save(owner);
    }

    private void handleException(Exception e) {
        throw new RuntimeException(e);
    }


    private CustomerEntity setPetIds(Set<Long> petIds, CustomerEntity owner) {
        return owner;
    }

    private CustomerEntity setPetIds(List<Long> petIds, CustomerEntity customerEntity) {
        try {
            List<PetEntity> petEntities = retrievePetEntitiesByIds(petIds);
            return updateCustomerWithPets(customerEntity, petEntities);
        } catch (Exception e) {
            handleException(e);
        }

        return customerEntity;
    }

    private List<PetEntity> retrievePetEntitiesByIds(List<Long> petIds) {
        return petRepository.findAllById(petIds);
    }

    private CustomerEntity updateCustomerWithPets(CustomerEntity customerEntity, List<PetEntity> petEntities) {
        customerEntity.setPetEntities(petEntities);
        return customerEntity;
    }

    ///////////////
    public CustomerDTO convertToDTO(CustomerEntity customerEntity) {
        try {
            CustomerDTO customerDTO = modelMapper.map(customerEntity, CustomerDTO.class);

            if (customerEntity.getPetEntities() != null && !customerEntity.getPetEntities().isEmpty()) {
                setPetIdsInDTO(customerDTO, customerEntity.getPetEntities());
            }

            return customerDTO;
        } catch (Exception e) {
            handleException(e);
        }

        return null;
    }

    private void setPetIdsInDTO(CustomerDTO customerDTO, List<PetEntity> petEntities) {
        List<Long> petIds = petEntities.stream().map(PetEntity::getId).collect(Collectors.toList());
        customerDTO.setPetIds(petIds);
    }

    public List<CustomerEntity> findAll() {
        return customerRepository.findAll();
    }

    public CustomerEntity findById(long id) throws NoSuchElementException {
        return customerRepository.findById(id).get();
    }

    public CustomerEntity findByPetId(long petId) throws NoSuchElementException {
        return customerRepository.findCustomerEntitiesByPetEntitiesContains(petRepository.getOne(petId));
    }
}
