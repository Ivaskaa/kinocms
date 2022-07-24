package com.example.kinocms.service;

import com.example.kinocms.entities.City;
import com.example.kinocms.entities.Role;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.RoleRepository;
import com.example.kinocms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Page<User> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return userRepository.findAll(pageable);
    }

    @Override               // знаходить за username           
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {          // повертає всіх Users з бази даних
        return (List<User>) userRepository.findAll();
    }

    public boolean saveUser(User user) {       // зберігає User в базу даних
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setId(1L);
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {            // видаляє User з бази даних
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public User loadUserByAuthentication(Authentication authentication) {
        return this.loadUserByUsername(authentication.getName());
    }


//    public List<User> usergtList(Long idMin) {         // повертає user з id меньшим ніж idMin
//        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
//                .setParameter("paramId", idMin).getResultList();
//    }
}
