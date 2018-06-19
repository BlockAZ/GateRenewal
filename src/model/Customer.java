package model;

import java.util.ArrayList;

public class Customer extends Person {
	ArrayList<Job> jobs = new ArrayList<Job>();
	
	public Customer(String name, String phoneNo, String email, String street, String city, 
			String zip, String state) {
		super(name, phoneNo, email, street, city, zip, state);
		
	}

	
}
