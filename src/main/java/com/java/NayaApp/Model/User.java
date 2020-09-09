package com.java.NayaApp.Model;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User {
	
    @Id
    @Column(name="username", unique = true)
    private String username; 
    
    @Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "passwordConfirm")
	@Transient
    private String passwordConfirm;
	
	private String token;
	
	@Column(columnDefinition = "TIMESTAMP")	
	private LocalDateTime tokenCreationDate;
	
	@Column(name="isActive")
	private boolean isActive;

    @ManyToMany(cascade = CascadeType.ALL)
 // Both are correct way for many to many mapping
    
//	@JoinTable(name = "USER_ROLES", joinColumns={
//			@JoinColumn(name = "Username", referencedColumnName = "username") }, inverseJoinColumns = {
//					@JoinColumn(name = "RoleDesc", referencedColumnName = "roleDesc") })
    private List<Role> roles; 
    
    public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public User() {

	}
    
}
