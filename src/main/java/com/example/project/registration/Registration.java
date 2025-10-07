package com.example.project.registration;

import jakarta.persistence.*;

@Entity
@Table(name = "users") 
public class Registration {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @Column(nullable = false, unique = true)
	    private String username;

	    @Column(nullable = false)
	    private String password;

    

}
