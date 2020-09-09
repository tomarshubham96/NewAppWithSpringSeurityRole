package com.java.NayaApp.Service;

import java.util.List;

import com.java.NayaApp.Model.User;

public interface UserService {
	
	void createUser(User user);
	
	User findByUsername(String username);
    
    User findByEmail(String email);
    
    User findByToken(String token);
    
    String registerUser(String email);
    
    String confirmUser(String token);

	void createAdmin(User newAdmin);

	List<User> findByFirstName(String name);

	String forgotPassword(String email);

	String resetPassword(String token, String password, String passwordConfirm);
}
