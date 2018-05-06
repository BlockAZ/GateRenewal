package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee extends Person{
	private static int nextId;

	private int id;
	private double wage;
	private String title;
	
	public Employee(String name, String phoneNo, String email,
				String street, String city, String zip, String state, 
				String title, double wage) {
		super(name, phoneNo, email, street, city, zip, state);
		this.wage = wage;
		this.title = title;
		
		
		this.id = nextId;
		nextId++;
		
	}
	
	public StringProperty titleProperty() {
		return new SimpleStringProperty(title);
	}
	public StringProperty idProperty() {
		return new SimpleStringProperty(Integer.toString(id));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getWage() {
		return wage;
	}
	public String getWageStr() {
		return String.format("%.2f", wage);
	}

	public void setWage(double wage) {
		this.wage = wage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
