package com.java.NayaApp.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.NayaApp.Model.User;
import com.java.NayaApp.Service.UserService;
import com.java.NayaApp.ServiceImpl.EmailService;


@Controller
public class ForgotPasswordController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	EmailService emailService;
	
	
	@GetMapping("/forgotPassword")
	public String forgotPassword(Model model) {
		return "views/forgotPassword";
	}
	
    
    @PostMapping("/forgotPassword")
    public String forgotPassword(@ModelAttribute("userForm") User userForm, Model model, HttpServletRequest request) {
    	
    	User userExists = userService.findByEmail(userForm.getEmail());
    	
    	String response = userService.forgotPassword(userForm.getEmail());
    	
    	if(!response.startsWith("Invalid")) {  
    		
    		String resetUrl = request.getRequestURL().toString().replace("/forgotPassword", "");
    		
    		SimpleMailMessage registrationEmail = new SimpleMailMessage();
			registrationEmail.setTo(userForm.getEmail());
			registrationEmail.setSubject("Password Reset");
			registrationEmail.setText("To reset your password please click the link below:\n"
					+ resetUrl + "/resetPassword?token=" + response);
			registrationEmail.setFrom("noreply@domain.com");
			
			emailService.sendEmail(registrationEmail);
			
			model.addAttribute("displayMessage", "Link has been sent to your registered mail.");
			
    		return "views/forgotPassword";
    	}
    	else {
    		model.addAttribute("displayMessage", "Not a registered Email Id. Enter the registered Email-Id.");
    		return "views/forgotPassword";
    	}
    }


@GetMapping(value="/resetPassword")
public String resetPassword(@ModelAttribute("userForm") User userForm, @RequestParam("token") String token, Model model, RedirectAttributes redir) {
	
	User user = userService.findByToken(token);
		
	if (user == null) {
		redir.addFlashAttribute("displayMessage", "Oops!  This is an invalid link////.");
		
		return "redirect:views/forgotPassword";
	} else {
		model.addAttribute("token", user.getToken());
	}
	
	return "views/resetPassword";		
 }

@PostMapping("/resetPassword")
public String resetPassword(@ModelAttribute("userForm") User userForm, Model model) {
	
	System.out.println("user is ==" + userForm);
	
     String response = userService.resetPassword(userForm.getToken(), userForm.getPassword(), userForm.getPasswordConfirm());
   
     
     if(!response.startsWith("Invalid") && !response.startsWith("Password")) {
    	 
    	model.addAttribute("successMessage", response);
 		
 		return "views/resetSuccess"; 
     }
     else 
     {
    	 if(response.startsWith("Password")) {
    	 model.addAttribute("errorMessage", response);		
 		return "views/resetPassword";
    	}
    	 else {
    		 model.addAttribute("errorMessage", response);		
    	 		return "views/resetPassword"; 
    	 }
    	 
     }
}
	

}
