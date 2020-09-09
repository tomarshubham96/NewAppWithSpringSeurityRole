package com.java.NayaApp.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.java.NayaApp.Model.User;
import com.java.NayaApp.Service.UserService;

@Controller
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public String showProfilePage(Model model, Principal principal) {
		
		String username = principal.getName();
		User user = userService.findByUsername(username);
		
		model.addAttribute("accountProps", "account props will go here");
		
		
		return "views/profile";
	}

}
