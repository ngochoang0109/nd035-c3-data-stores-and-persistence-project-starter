package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface EmployeeRepos extends JpaRepository<Employee, Long> {
    @Query("SELECT e " +
            "FROM Employee e join e.daysAvailable da join e.skills s " +
            "WHERE s IN :skills " +
            " and da = :daysAvailable " +
            "GROUP BY e " +
            "HAVING COUNT(s) = :skillsCount")
    List<Employee> findEmployeesForService(@Param("daysAvailable") DayOfWeek daysAvailable, @Param("skills") Set<EmployeeSkill> skills,
                                           @Param("skillsCount") long skillsCount);

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE e.id IN :ids")
    List<Employee> getEmplsByIds(@Param("ids") List<Long> ids);
}
