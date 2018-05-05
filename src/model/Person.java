package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person implements Serializable{
	private String name;
	private String phoneNo;
	private String email;
	private String address;
	private String notes;
	private String date;
	

	public Person(String name, String phoneNo, String email,String street, String city, String zip, String state, String notes) {
		this.name 		= name;
		this.phoneNo	= phoneNo;
		this.email 		= email;
		this.address 	= street + "\n" + city + ", " + state + " " + zip;
		this.notes 		= notes;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		this.date 	= df.format(new Date());
	}
	
	
	public String toString() {
		return name + "\n"
				+ address + "\n" 
				+ date + "\n"
				+ notes + "\n";
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
	public void setAddress(String address) {
		this.address = address;
	}
	public void setPhoneNumber(String number) {
		this.phoneNo = number;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	
}
