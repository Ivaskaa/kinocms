package com.example.kinocms.repository.pages;

import com.example.kinocms.entities.pages.Contact;
import com.example.kinocms.entities.pages.MainPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MainPageRepository extends JpaRepository<MainPage, Long> {

//    @Query("SELECT c FROM Contact c WHERE c.active = true")
//    Page<Contact> findActiveAll(Pageable pageable);
}
