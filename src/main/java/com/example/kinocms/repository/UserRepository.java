package com.example.kinocms.repository;


import com.example.kinocms.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


//public interface UserRepository extends JpaRepository<User, Long> {
//    User findByUserName(String userName);
//}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Page<User> findAll(Pageable pageable);
}
