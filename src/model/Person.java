package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Person implements Serializable{
	protected String name;
	protected String phoneNo;
	protected String email;
	protected String address;
	protected String notes;
	protected String date;

	
	public Person(String name, String phoneNo, String email ,
				String street, String city, String zip, String state) {
		this.name = name;
		this.phoneNo = phoneNo;
		this.email = email;
		this.address 	= street + "\n" + city + ", " + state + " " + zip;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		this.date 	= df.format(new Date());
	}
	
	public String toString() {
		return name;
	}
	
	public StringProperty nameProperty() {
		return new SimpleStringProperty(name);
	}
	public StringProperty phoneProperty() {
		return new SimpleStringProperty(phoneNo);
	}
	public StringProperty addressProperty() {
		return new SimpleStringProperty(address.substring(0, address.indexOf(",")).replace("\n", " "));
	}
	
	

	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDate() {
		return date;
	}
	public String getNotes() {
		return notes;
	}
	public String getPhoneNumber() {
		return phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPhoneNumber(String number) {
		this.phoneNo = number;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	
}
