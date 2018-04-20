package testOrScrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Item;
import model.StockEntry;

public class FillTextFile {
	public static void main(String args[]) {
		flushStock();
	}
	
	public static void addEntry(StockEntry entry) {
		FileWriter out = null;
		try {
			out = new FileWriter(new File("data/stock/stockHistorySmall.txt"), true);
			out.write(entry.toString());
			out.close();
		} catch (IOException e) {
			System.out.println("Error: stockHistorySmall.txt cannot be accessed");
			e.printStackTrace();
		}
	}
	
	public static void flushStock() {
		List<Item> tempItems = new ArrayList<Item>();
		Scanner in = null;
		try {
			in = new Scanner(new File("data/stock/currentStock.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: stockHistory.txt cannot be accessed");
			e.printStackTrace();
		}
		// Reading in all data from stockFile, creating Item for each
		while (in.hasNextLine())
			tempItems.add(new Item(in.nextLine(), false));
		
		for (Item item:tempItems) {
			item.updateStock(-1 * item.getStock());
		}
		
		FileWriter out = null;
		try {
			out = new FileWriter(new File("data/stock/currentStock.txt"), false);
			for (int i=0; i<tempItems.size(); i++)
				out.write(tempItems.get(i).toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("Error: one of currentStock.txt cannot be accessed");
			e.printStackTrace();
		}
		
		
	}
}
