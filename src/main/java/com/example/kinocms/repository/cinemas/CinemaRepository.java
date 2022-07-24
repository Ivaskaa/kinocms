package com.example.kinocms.repository.cinemas;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.City;
import com.example.kinocms.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Page<Cinema> findAll(Pageable pageable);

    @Query("SELECT c FROM Cinema c WHERE c.active = true")
    Page<Cinema> findActive(Pageable pageable);

    @Query("SELECT c FROM Cinema c WHERE c.city = ?1")
    List<Cinema> findCinemasByCity(City city);
}

