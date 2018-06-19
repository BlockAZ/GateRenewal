package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Job implements Serializable {
	private String name;
	private Customer customer;
	private Date jobDate;
	private String creationDate;
	private HashMap<Person, Double> workers;
	
	private Double bidPrice;
	private Double paidPrice;
	
	private boolean isComplete;
	private boolean isPaid;
	
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	
	public Job(String name, Person customer) {
		this.name 		= name;
		this.customer 	= (Customer)customer;
		
		this.isComplete	= false;
		this.isPaid		= false;
		
		
		
		
		this.creationDate 	= df.format(new Date());
	}
	
	
	public StringProperty nameProperty() {
		return new SimpleStringProperty(name);
	}
	
	public StringProperty customerProperty() {
		return new SimpleStringProperty(customer.getName());
	}
	public StringProperty dateProperty() {
		return new SimpleStringProperty(df.format(jobDate));
	}
	public StringProperty completeProperty() {
		if (isComplete)
			return new SimpleStringProperty("yes");
		else
			return new SimpleStringProperty("no");
	}
	public StringProperty paidProperty() {
		if (isPaid)
			return new SimpleStringProperty("yes");
		else
			return new SimpleStringProperty("no");
	}
	
	public void flipCompleted() {
		isComplete = !isComplete;
	}
	public void flipPaid() {
		isPaid = !isPaid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Date getJobDate() {
		return jobDate;
	}


	public void setJobDate(Date jobDate) {
		this.jobDate = jobDate;
	}


	public String getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}


	public HashMap<Person, Double> getWorkers() {
		return workers;
	}


	public void setWorkers(HashMap<Person, Double> workers) {
		this.workers = workers;
	}


	public Double getBidPrice() {
		return bidPrice;
	}


	public void setBidPrice(Double bidPrice) {
		this.bidPrice = bidPrice;
	}


	public Double getPaidPrice() {
		return paidPrice;
	}


	public void setPaidPrice(Double paidPrice) {
		this.paidPrice = paidPrice;
	}


	public boolean isComplete() {
		return isComplete;
	}


	public void setComplete(boolean complete) {
		this.isComplete = complete;
	}


	public boolean isPaid() {
		return isPaid;
	}


	public void setPaid(boolean paid) {
		this.isPaid = paid;
	}
	
}
