package com.example.kinocms.repository;

import com.example.kinocms.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//public interface RoleRepository extends JpaRepository<Role, Long> {
//}

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}