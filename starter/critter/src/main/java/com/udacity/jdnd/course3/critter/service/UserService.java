package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.*;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.CustomerRepos;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepos;
import com.udacity.jdnd.course3.critter.repository.PetRepos;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final CustomerRepos customerRepos;
    private final PetRepos petRepos;
    private final EmployeeRepos employeeRepos;

    public UserService(CustomerRepos customerRepos, PetRepos petRepos, EmployeeRepos employeeRepos) {
        this.customerRepos = customerRepos;
        this.petRepos = petRepos;
        this.employeeRepos = employeeRepos;
    }

    @Transactional
    public CustomerDTO addNewCus(CustomerDTO customerDTO) {
        Customer customer = new Customer();

        if (customerDTO.getId() != 0) {
            return new CustomerDTO();
        }

        customer.setName(customerDTO.getName());
        customer.setPhone(customerDTO.getPhoneNumber());
        customer.setNote(customerDTO.getNotes());

        if (customerDTO.getPetIds() != null && !customerDTO.getPetIds().isEmpty()) {
            List<Pet> petsOfCus = this.petRepos.getPetsByIds(customerDTO.getPetIds());
            customer.setPet(petsOfCus);
        }

        customer = this.customerRepos.save(customer);

        List<Long> idOfPet = new LinkedList<>();
        if (customer.getPet() != null){
            idOfPet = customer.getPet().stream().map(el -> el.getId()).collect(Collectors.toList());
        }

        return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhone(),
                customer.getNote(), idOfPet);
    }

    public List<CustomerDTO> getAllCus() {
        List<CustomerDTO> resp = new LinkedList<>();
        List<Customer> customers = this.customerRepos.findAll();
        resp = customers.stream().map(el -> {
            List<Long> petId = new ArrayList<>();
            if (el.getPet() != null && !el.getPet().isEmpty()){
                petId = el.getPet().stream()
                        .map(p -> p.getId())
                        .collect(Collectors.toList());
            }
            return new CustomerDTO(el.getId(), el.getName(), el.getPhone(), el.getNote(), petId);}).collect(Collectors.toList());
        return resp;
    }

    @Transactional
    public EmployeeDTO addNewEmp(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        EmployeeDTO resp = new EmployeeDTO();
        employee.setName(employeeDTO.getName());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        employee.setSkills(employeeDTO.getSkills());

        employee = employeeRepos.save(employee);
        resp.setId(employee.getId());
        resp.setName(employee.getName());
        resp.setSkills(employee.getSkills());
        resp.setDaysAvailable(employee.getDaysAvailable());

        return resp;
    }

    public EmployeeDTO getEmpById(Long emplId){
        EmployeeDTO resp = new EmployeeDTO();
        Employee employee = this.employeeRepos.getOne(emplId);

        resp.setId(employee.getId());
        resp.setName(employee.getName());
        resp.setSkills(employee.getSkills());
        resp.setDaysAvailable(employee.getDaysAvailable());
        return resp;
    }

    public CustomerDTO getOwnerByPet(long petId){
        Customer customer = this.customerRepos.getOwnerByPet(petId);
        List<Long> id = new ArrayList<>();
        if (customer.getPet() != null && !customer.getPet().isEmpty()){
            id = customer.getPet().stream()
                    .map(p -> p.getId())
                    .collect(Collectors.toList());
        }
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhone(), customer.getNote(), id);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId){
        Employee employee = this.employeeRepos.getOne(employeeId);
        if(employee == null){
            return;
        }

        Set<DayOfWeek> utpDaysAvailable = new HashSet<>();
        if (employee.getDaysAvailable() != null && !employee.getDaysAvailable().isEmpty()){
            utpDaysAvailable.addAll(employee.getDaysAvailable());
        }
        utpDaysAvailable.addAll(daysAvailable);
        employee.setDaysAvailable(utpDaysAvailable);
        this.employeeRepos.save(employee);
    }

    public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeRepos.findEmployeesForService(employeeDTO.getDate().getDayOfWeek(), employeeDTO.getSkills(), employeeDTO.getSkills().size());
        List<EmployeeDTO> resp = new ArrayList<>();
        resp = employees.stream().map(el ->{
            EmployeeDTO e = new EmployeeDTO();
            e.setId(el.getId());
            e.setName(el.getName());
            e.setSkills(el.getSkills());
            e.setDaysAvailable(el.getDaysAvailable());
            return e;
        }).collect(Collectors.toList());
        return resp;
    }
}
