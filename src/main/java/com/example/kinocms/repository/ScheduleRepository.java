package com.example.kinocms.repository;


import com.example.kinocms.entities.Film;
import com.example.kinocms.entities.Hall;
import com.example.kinocms.entities.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findAll(Pageable pageable);

    @Query("SELECT s FROM Schedule s WHERE s.active = true")
    Page<Schedule> findActivePage(Pageable pageable);
    @Query("SELECT s FROM Schedule s WHERE s.film = ?1 and s.active = true")
    List<Schedule> findSchedulesByFilm(Film film);
    @Query("SELECT s FROM Schedule s WHERE s.hall = ?1 and s.active = true")
    List<Schedule> findSchedulesByHall(Hall hall);


}
