package com.example.kinocms.repository.pages;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<com.example.kinocms.entities.pages.Page, Long> {
    Page<com.example.kinocms.entities.pages.Page> findAll(Pageable pageable);
    @Query("SELECT p FROM Page p WHERE p.mainPage = false")
    Page<com.example.kinocms.entities.pages.Page> findAllCommon(Pageable pageable);

    @Query("SELECT p FROM Page p WHERE p.mainPage = true")
    List<com.example.kinocms.entities.pages.Page> findAllMainPage();

    @Query("SELECT p FROM Page p WHERE p.mainPage = false and p.active = true")
    Page<com.example.kinocms.entities.pages.Page> findActiveCommonPage(Pageable pageable);
}
