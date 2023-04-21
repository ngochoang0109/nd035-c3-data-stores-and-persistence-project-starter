package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleRepos extends JpaRepository<Schedule, Long> {
    @Query("SELECT s " +
            "FROM Schedule s join s.employee e " +
            "WHERE e.id =:empId")
    List<Schedule> getScheduleByEmpId(@Param("empId") long empId);

    @Query("SELECT s " +
            "FROM Schedule s join s.pet p " +
            "WHERE p.id =:petId")
    List<Schedule> getScheduleByPetId(@Param("petId") long petId);

    @Query("SELECT s " +
            "FROM Schedule s join s.pet p " +
            "WHERE p.id IN :petIds")
    List<Schedule> getScheduleByCusIds(@Param("petIds") List<Long> petIds);
}
