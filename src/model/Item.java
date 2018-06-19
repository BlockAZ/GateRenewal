package model;

import javafx.scene.image.Image;


public class Item {
	private ItemGroup group;
	private String name;
	private double cost;
	private double count;
	private String imgString;
	private Image pic;
	
	
	public Item(ItemGroup group, String name, double cost, double stock) {
		this.group = group;
		this.name  = name;
		this.cost  = cost;
		this.count = stock;
	}
	
	public Item(String fromFile, boolean useImage) {
		String data[] = fromFile.split(",");
		this.group  = ItemGroup.valueOf(data[0].toUpperCase());
		this.name 	= data[1];
		this.cost 	= Double.parseDouble(data[2]);
		this.count 	= Double.parseDouble(data[3]);
		if (data.length > 4) {
			imgString = data[4];
			if (useImage)
				pic = new Image("file:images/items/" + imgString);
		}
			
	}
	
	public ItemGroup getGroup() {
		return group;
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
	public double getCount() {
		return count;
	}
	public String getStockString() {
		return Double.toString(count);
	}
	public Image getImg() {
		return pic;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	public void updateCount(double more) {
		this.count += more;
	}

	
	public String toString() {
		return group.toString() + "," + name + "," + cost + "," + count + "," + imgString
				+ "\n";
	}
}
