package com.java.NayaApp.ServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.NayaApp.Model.Role;
import com.java.NayaApp.Model.User;
import com.java.NayaApp.Repository.RoleRepository;
import com.java.NayaApp.Repository.UserRepository;
import com.java.NayaApp.Service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = new Role("USER");
		List<Role> roles = new ArrayList<>();
		roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }
	
	@Override
	public void createAdmin(User newAdmin) {
		newAdmin.setPassword(bCryptPasswordEncoder.encode(newAdmin.getPassword())); 
		newAdmin.setActive(true);
		Role userRole = new Role("ADMIN");
		List<Role> roles = new ArrayList<>();
		roles.add(userRole);
		newAdmin.setRoles(roles);
		
		System.out.println("user desc == " + newAdmin );
		System.out.println("role desc == " + roles );
		userRepository.save(newAdmin);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public List<User> findByFirstName(String name) {
		return userRepository.findByFirstNameLike("%"+name+"%");
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByToken(String token) {
		return userRepository.findByToken(token);
	}

	@Override
	public String registerUser(String email) {
		Optional<User> userOptional = Optional
				.ofNullable(userRepository.findByEmail(email));

		if (!userOptional.isPresent()) {
			return "Invalid email id.";
		}

		User user = userOptional.get();
		user.setToken(generateToken());
		user.setTokenCreationDate(LocalDateTime.now());

		user = userRepository.save(user);

		return user.getToken();
	}

	@Override
	public String confirmUser(String token) {
		
		Optional<User> userOptional = Optional
				.ofNullable(userRepository.findByToken(token));

		if (!userOptional.isPresent()) {
			return "Invalid token.";
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

		if (isTokenExpired(tokenCreationDate)) {
			return "Invalid Token.Token seems to be expired.";
		}

		User user = userOptional.get();

		user.setActive(true);;
		user.setToken(null);
		user.setTokenCreationDate(null);

		userRepository.save(user);

		return "Email Successfully verified.";
		
	}
	
	@Override
	public String forgotPassword(String email) {
		
		Optional<User> userOptional = Optional
				.ofNullable(userRepository.findByEmail(email));

		if (!userOptional.isPresent()) {
			return "Invalid email id.";
		}

		User user = userOptional.get();
		user.setToken(generateToken());
		user.setTokenCreationDate(LocalDateTime.now());

		user = userRepository.save(user);

		return user.getToken();
	}
	
	@Override
	public String resetPassword(String token, String password, String passwordConfirm) {
		
		Optional<User> userOptional = Optional
				.ofNullable(userRepository.findByToken(token));

		if (!userOptional.isPresent()) {
			return "Invalid token.";
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

		if (isTokenExpired(tokenCreationDate)) {
			return "Invalid Token.Token seems to be expired.";

		}		
				
		if(password.equals(passwordConfirm)) {
		
			System.out.println("password === " + password);
			System.out.println("confirmPassword === " + passwordConfirm);

		User user = userOptional.get();

		user.setPassword(bCryptPasswordEncoder.encode(password));
		user.setToken(null);
		user.setTokenCreationDate(null);

		userRepository.save(user);

		return "Your password successfully updated.";
		}
		else {
			
			return "Password and Confirm Password do not match";
		}

	}
	
	private String generateToken() {
		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString())
				.append(UUID.randomUUID().toString()).toString();
	}
	
	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);

		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}

}
