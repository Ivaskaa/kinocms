package com.example.kinocms.repository;


import com.example.kinocms.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


//public interface UserRepository extends JpaRepository<User, Long> {
//    User findByUserName(String userName);
//}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Page<User> findAll(Pageable pageable);
    @Query("select count(u) from User u")
    String findCountUsers();
    @Query("select count(u) from User u where u.active = true")
    String findCountActiveUsers();
    @Query("select u from User u where u.email is not null")
    List<User> findUserByWriteEmail();
}
