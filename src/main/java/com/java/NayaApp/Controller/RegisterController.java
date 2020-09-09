package com.java.NayaApp.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.NayaApp.Model.User;
import com.java.NayaApp.Service.UserService;
import com.java.NayaApp.ServiceImpl.EmailService;

@Controller
public class RegisterController {
	
	@Autowired UserService userService;
	
	@Autowired
    private EmailService emailService;
	
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("user", new User());
		return "views/registerForm";
	}
		
	@PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model, HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return "views/registerForm";
		}
		if(userService.findByEmail(user.getEmail()) != null) {
			model.addAttribute("exist",true);

			return "views/registerForm";

		}
		else {
			
		  userService.createUser(user);
		  
		  String response = userService.registerUser(user.getEmail());
		  
		  if(!response.startsWith("Invalid")) {    		
	    		String confirmUrl = request.getRequestURL().toString().replace("/register", "");
	    		
	    		SimpleMailMessage registrationEmail = new SimpleMailMessage();
				registrationEmail.setTo(user.getEmail());
				registrationEmail.setSubject("Email Verification");
				registrationEmail.setText("To verify your email please click the link below:\n"
						+ confirmUrl + "/confirmRegistration?token=" + response);
				registrationEmail.setFrom("noreply@domain.com");
				
				emailService.sendEmail(registrationEmail);
				
				model.addAttribute("displayMessage", "A verification link has been sent to your Email-Id.");
				
	    		return "views/success";
	    	}
		  else {
			  model.addAttribute("displayMessage", "Something went wrong please try again.");
			  return "views/registerForm";
		  }
		  		
		}
	}
	
	@RequestMapping(value="/confirmRegistration", method= RequestMethod.GET)
	public String confirmUserAccount(Model model, @RequestParam("token")String token, RedirectAttributes redir) {
		
		if(token != null)
		{
			User user = userService.findByToken(token);
			
			if (user == null) {
				redir.addFlashAttribute("displayMessage", "Oops!  This is an invalid link////.");
				
				return "redirect:views/registerForm";
			} else {				
				String response = userService.confirmUser(token);
				model.addAttribute("response", response);
				return "views/confirm";
			}			
		}
		else
		{
			model.addAttribute("message","The link is invalid or broken!");
			return "error";
		}
	}

}
