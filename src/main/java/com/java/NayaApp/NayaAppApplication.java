package com.java.NayaApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.java.NayaApp.Model.User;
import com.java.NayaApp.Service.UserService;

@SpringBootApplication
public class NayaAppApplication implements  CommandLineRunner{
	
	@Autowired
	   private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(NayaAppApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		  {
    		  User newAdmin = new User("admin@mail.com", "Admin", "Admin@12345");
    		  userService.createAdmin(newAdmin); 
    	  }
	}

}
