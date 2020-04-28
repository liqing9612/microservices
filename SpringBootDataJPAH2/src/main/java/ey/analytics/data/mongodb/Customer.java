package ey.analytics.data.mongodb;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

	// "customer_seq" is Oracle sequence name.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
    String name;
    
	String email;

    @Column(name = "CREATED_DATE")
    Date date;

    //getters and setters, contructors
}
