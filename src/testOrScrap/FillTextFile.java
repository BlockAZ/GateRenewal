package testOrScrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.LoadSaveData;
import model.Customer;
import model.Item;
import model.Person;
import model.StockEntry;

public class FillTextFile {
	public static void main(String args[]) {
		initPeople();
	}
	
	//Testing creating, storing, and loading people objects
	public static void initPeople() {
		Person kyle = new Customer("Kyle Block", "(623)262-7817", "blockie@gmail.com", 
				"1539 East Prince Road", "Tucson", "AZ", "85719");
		kyle.setNotes("he likes \ntwo \nthree lines");
		Person chris = new Customer("Chris Block", "(623)680-9294", "cjblock@gmail.com", 
				"24834 North 36th Drive", "Phoenix", "AZ", "85310");
		chris.setNotes("this dude liesk to  iajsdfkj the fthe cat and the dog went to the park and ate"
				+ "chicken for likettwo and a half hours hwen the guy went to do the gate and blew "
				+ "a tire mnan that sucked");
		
		ArrayList<Person> peeps = new ArrayList<Person>();
		peeps.add(kyle);
		peeps.add(chris);
		
		LoadSaveData.writeCustomersToFile(peeps);
		/*
		ArrayList<Person> data = LoadSaveData.readCustomersFromFile();
		for (Person peep:data) {
			System.out.println(peep);
		}*/
		
		
		
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
