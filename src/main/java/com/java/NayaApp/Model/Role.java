package com.java.NayaApp.Model;

import javax.persistence.*;


import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role {
    
	@Id
    @Column(name="roleDesc")
    private String roleDesc;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;  
    
    public Role(String roleDesc, List<User> users) {
		this.roleDesc = roleDesc;
		this.users = users;
	}

	public Role() {
	}

	public Role(String roleDesc) {
		this.roleDesc = roleDesc;
	}
}
