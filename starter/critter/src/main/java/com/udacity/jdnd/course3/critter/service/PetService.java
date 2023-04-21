package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.CustomerRepos;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepos;
import com.udacity.jdnd.course3.critter.repository.PetRepos;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {
    private PetRepos petRepos;
    private CustomerRepos customerRepos;

    public PetService(PetRepos petRepos, CustomerRepos customerRepos){
        this.petRepos = petRepos;
        this.customerRepos = customerRepos;
    }

    @Transactional
    public PetDTO savePet(PetDTO petDTO){
        if (petDTO.getId() != 0){
            return new PetDTO();
        }
        Pet pet = new Pet();
        List<Pet> pets = new ArrayList<>();
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNote(petDTO.getName());
        Optional<Customer> cusOpt = this.customerRepos.findById(petDTO.getOwnerId());
        if (cusOpt.isPresent()){
            pet.setCustomer(cusOpt.get());
        }

        pet = this.petRepos.save(pet);
        Customer customer = cusOpt.get();
        if (customer.getPet() != null &&  !customer.getPet().isEmpty() ) {
            pets = customer.getPet();
        }
        pets.add(pet);
        customer.setPet(pets);
        customerRepos.save(customer);

        Long id = new Long(0);
        if (pet.getCustomer() != null){
            id = pet.getCustomer().getId();
        }
        return new PetDTO(pet.getId(), pet.getType(), pet.getName(), id, pet.getBirthDate(), pet.getNote());
    }

    public PetDTO getPetById(Long id){
        Optional<Pet> petOptional = this.petRepos.findById(id);
        if (!petOptional.isPresent()){
            return new PetDTO();
        }
        Pet pet = petOptional.get();
        return new PetDTO(pet.getId(), pet.getType(), pet.getName(), pet.getCustomer().getId(),
                pet.getBirthDate(), pet.getNote());
    }

    public List<PetDTO> getPetOfCus(Long id){
        List<Pet> pets = this.petRepos.getPetOwnerId(id);
        List<PetDTO> petDTO = new ArrayList<>();
        if (pets != null && !pets.isEmpty()){
            pets.stream().map(pet ->
                    petDTO.add(new PetDTO(pet.getId(), pet.getType(), pet.getName(), id, pet.getBirthDate(), pet.getNote())))
                    .collect(Collectors.toList());
        }
        return petDTO;
    }
}
