package com.example.kinocms.repository;

import com.example.kinocms.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {    // JpaRepository
    Page<News> findAll(Pageable pageable);
    @Query("SELECT n FROM News n WHERE n.active = true")
    Page<News> findActiveAll(Pageable pageable);
    @Query("SELECT count(n) FROM News n WHERE n.active = true")
    Integer findActiveNewsCount();
}
