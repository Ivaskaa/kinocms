package com.example.kinocms.repository.cinemas;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    Page<Hall> findAll(Pageable pageable);

    @Query("SELECT h FROM Hall h WHERE h.cinema = ?1")
    Page<Hall> findAll(Cinema cinema, Pageable pageable);

    @Query("SELECT h FROM Hall h WHERE h.cinema = ?1")
    List<Hall> findHallsByCinema(Cinema cinema);
    @Transactional
    @Modifying
    @Query("DELETE FROM Hall h WHERE h.id = ?1")
    void myDeleteById(Long hallId);
}
