package com.example.kinocms.repository;

import com.example.kinocms.entities.News;
import com.example.kinocms.entities.SpecialOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialOffersRepository extends JpaRepository<SpecialOffer, Long> {
    Page<SpecialOffer> findAll(Pageable pageable);
    @Query("SELECT so FROM SpecialOffer so WHERE so.active = true")
    Page<SpecialOffer> findActiveAll(Pageable pageable);
    @Query("SELECT count(so) FROM SpecialOffer so WHERE so.active = true")
    Integer findActiveSpecialOffersCount();
}
