package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StockEntry {
	private StringProperty date;
	private StringProperty itemName;
	private StringProperty itemQty;
	private StringProperty cost;
	private StringProperty totalCost;
	private StringProperty notes;
	
	
	public StockEntry(Date date, String item, double qty, double cost, String notes) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		this.date 		= new SimpleStringProperty(df.format(date));
		this.itemName 	= new SimpleStringProperty(item);
		this.itemQty 	= new SimpleStringProperty(Double.toString(qty));
		this.cost 		= new SimpleStringProperty("$" + String.format("%.2f", cost));
		this.totalCost  = new SimpleStringProperty("$" +
				String.format("%.2f", cost * qty));
		this.notes		= new SimpleStringProperty(notes); 
	}
	
	public StockEntry(String fromFile) {
		try {
			String data[] = fromFile.split(",");
			this.date 		= new SimpleStringProperty(data[0]);
			this.itemName 	= new SimpleStringProperty(data[1]);
			this.itemQty	= new SimpleStringProperty(data[2]);
			this.cost 		= new SimpleStringProperty(data[3]);
			this.totalCost 	= new SimpleStringProperty(data[4]);
			if (data.length > 5)
				this.notes 	= new SimpleStringProperty(data[5]);
			else
				this.notes 	= new SimpleStringProperty("");
		} catch(Exception o) {
			System.out.println("Error building stock entry from file");
		}
	}
	
	public StringProperty dateProperty() {
        return date;
    }
	public StringProperty itemNameProperty() {
        return itemName;
    }
	public StringProperty itemQtyProperty() {
        return itemQty;
    }
	public StringProperty costProperty() {
        return cost;
    }
	public StringProperty totalCostProperty() {
        return totalCost;
    }
	public StringProperty notesProperty() {
        return notes;
    }
	
	public String toString() {
		return date.getValue() + "," + itemName.getValue()+ "," + 
				itemQty.getValue()  + "," + cost.getValue() + "," +
				totalCost.getValue() + "," + notes.getValue() 
		+ "\n";
	}
}
