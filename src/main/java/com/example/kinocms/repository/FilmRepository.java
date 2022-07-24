package com.example.kinocms.repository;

import com.example.kinocms.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Page<Film> findAll(Pageable pageable);

    @Query("SELECT f FROM Film f WHERE f.active = true")
    Page<Film> findActiveAll(Pageable pageable);

//    @Query("select e from Employees e where e.salary > :salary")
//    List<Employees> findEmployeesWithMoreThanSalary(@Param("salary") Long salary, Sort sort);

//    @Modifying
//    @Query("update Employees e set e.firstName = ?1 where e.employeeId = ?2")
//    int setFirstnameFor(String firstName, String employeeId);
}
