package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepos extends JpaRepository<Customer, Long> {
    @Query("SELECT c " +
            "FROM Customer c join c.pet p " +
            "WHERE p.id = :petId")
    Customer getOwnerByPet(@Param("petId") long petId);
}
