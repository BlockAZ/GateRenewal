package model;

import javafx.scene.image.Image;

public class Item {
	private String name;
	private double cost;
	private double stock;
	private String imgString;
	private Image pic;
	
	
	public Item(String name, double cost, double stock) {
		this.name  = name;
		this.cost  = cost;
		this.stock = stock;
	}
	
	public Item(String fromFile, boolean useImage) {
		String data[] = fromFile.split(",");
		this.name 	= data[0];
		this.cost 	= Double.parseDouble(data[1]);
		this.stock 	= Double.parseDouble(data[2]);
		if (data.length > 3) {
			imgString = data[3];
			if (useImage)
				pic = new Image("file:images/items/" + imgString);
		}
			
	}
	
	public String getName() {
		return name;
	}
	public double getCost() {
		return cost;
	}
	public String getCostString() {
		return String.format("%.2f", cost);
	}
	public double getStock() {
		return stock;
	}
	public String getStockString() {
		return Double.toString(stock);
	}
	public Image getImg() {
		return pic;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	public void updateStock(double more) {
		this.stock += more;
	}

	
	public String toString() {
		return name + "," + cost + "," + stock + "," + imgString
				+ "\n";
	}
}
