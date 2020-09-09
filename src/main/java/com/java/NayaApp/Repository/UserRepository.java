package com.java.NayaApp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.NayaApp.Model.User;

public interface UserRepository extends JpaRepository<User, String>{
	
    User findByUsername(String username);
    
    List<User> findByFirstNameLike(String username);
	
	User findByEmail(String email);
	
	User findByToken(String token);
}
