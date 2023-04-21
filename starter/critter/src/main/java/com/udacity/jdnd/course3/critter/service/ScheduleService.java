package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepos;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepos;
import com.udacity.jdnd.course3.critter.repository.PetRepos;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepos;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepos scheduleRepos;
    @Autowired
    private PetRepos petRepos;
    @Autowired
    private EmployeeRepos employeeRepos;
    @Autowired
    private CustomerRepos customerRepos;

    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        ScheduleDTO resp = new ScheduleDTO();

        List<Pet> pets = petRepos.getPetsByIds(scheduleDTO.getPetIds());
        List<Employee> employees = employeeRepos.getEmplsByIds(scheduleDTO.getEmployeeIds());
        schedule.setPet(pets);
        schedule.setDay(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setEmployee(employees);
        schedule = scheduleRepos.save(schedule);

        resp.setId(schedule.getId());
        resp.setEmployeeIds(schedule.getEmployee().stream().map(e -> e.getId()).collect(Collectors.toList()));
        resp.setDate(schedule.getDay());
        resp.setActivities(schedule.getActivities());
        resp.setPetIds(schedule.getPet().stream().map(p -> p.getId()).collect(Collectors.toList()));
        return resp;
    }

    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        List<Schedule> schedules = scheduleRepos.findAll();
        scheduleDTOS = schedules.stream().map(s -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setId(s.getId());
            scheduleDTO.setEmployeeIds(s.getEmployee().stream().map(e -> e.getId()).collect(Collectors.toList()));
            scheduleDTO.setDate(s.getDay());
            scheduleDTO.setActivities(s.getActivities());
            scheduleDTO.setPetIds(s.getPet().stream().map(p -> p.getId()).collect(Collectors.toList()));
            return scheduleDTO;
        }).collect(Collectors.toList());
        return scheduleDTOS;
    }

    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        List<Schedule> schedules = scheduleRepos.getScheduleByEmpId(employeeId);
        scheduleDTOS = schedules.stream().map(s -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setId(s.getId());
            scheduleDTO.setEmployeeIds(s.getEmployee().stream().map(e -> e.getId()).collect(Collectors.toList()));
            scheduleDTO.setDate(s.getDay());
            scheduleDTO.setActivities(s.getActivities());
            scheduleDTO.setPetIds(s.getPet().stream().map(p -> p.getId()).collect(Collectors.toList()));
            return scheduleDTO;
        }).collect(Collectors.toList());
        return scheduleDTOS;
    }

    public List<ScheduleDTO> getScheduleForPet(long petId) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        List<Schedule> schedules = scheduleRepos.getScheduleByPetId(petId);
        scheduleDTOS = schedules.stream().map(s -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setId(s.getId());
            scheduleDTO.setEmployeeIds(s.getEmployee().stream().map(e -> e.getId()).collect(Collectors.toList()));
            scheduleDTO.setDate(s.getDay());
            scheduleDTO.setActivities(s.getActivities());
            scheduleDTO.setPetIds(s.getPet().stream().map(p -> p.getId()).collect(Collectors.toList()));
            return scheduleDTO;
        }).collect(Collectors.toList());
        return scheduleDTOS;
    }

    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        Customer customer = this.customerRepos.getOne(customerId);
        List<Long> petIds = customer.getPet().stream().map(p -> p.getId()).collect(Collectors.toList());
        List<Schedule> schedules = scheduleRepos.getScheduleByCusIds(petIds);
        scheduleDTOS = schedules.stream().map(s -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setId(s.getId());
            scheduleDTO.setEmployeeIds(s.getEmployee().stream().map(e -> e.getId()).collect(Collectors.toList()));
            scheduleDTO.setDate(s.getDay());
            scheduleDTO.setActivities(s.getActivities());
            scheduleDTO.setPetIds(s.getPet().stream().map(p -> p.getId()).collect(Collectors.toList()));
            return scheduleDTO;
        }).collect(Collectors.toList());
        return scheduleDTOS;
    }
}
