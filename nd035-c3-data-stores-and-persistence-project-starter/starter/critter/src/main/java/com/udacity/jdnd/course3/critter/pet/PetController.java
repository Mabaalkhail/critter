package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entities.PetEntity;
import com.udacity.jdnd.course3.critter.services.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO pet) {
        return petService.convertToDTO(petService.save(pet));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {

        PetEntity petEntity = petService.FindById(petId);
        return petService.convertToDTO(petEntity);
    }

    @GetMapping
    public List<PetDTO> getPets() {
        try {
            return retrieveAllPets();
        } catch (Exception e) {
            handleException(e);
        }

        return Collections.emptyList();
    }

    private List<PetDTO> retrieveAllPets() {
        List<PetEntity> pets = petService.findAll();
        return pets.stream()
                .map(petService::convertToDTO)
                .collect(Collectors.toList());
    }


    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        try {
            return retrievePetsByOwner(ownerId);
        } catch (Exception e) {
            handleException(e);
        }

        return Collections.emptyList();
    }

    private List<PetDTO> retrievePetsByOwner(long ownerId) {
        List<PetEntity> pets = petService.findByOwnerId(ownerId);
        return pets.stream()
                .map(petService::convertToDTO)
                .collect(Collectors.toList());
    }

    private void handleException(Exception e) {
        throw new RuntimeException(e);
    }

}

