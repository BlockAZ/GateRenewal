package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import model.Person;

public class LoadSaveData {

	public static void writeCustomersToFile(ArrayList<Person> customers) {
		try {
			FileOutputStream outFile = new FileOutputStream("data/customerData");
			ObjectOutputStream outStream = new ObjectOutputStream(outFile);
			outStream.writeObject(customers);
			outStream.close();
			System.out.println("Wrote Customers to file. Length: "+ customers.size());
		} catch (FileNotFoundException e) {
			System.out.println("File not found: writing customers");
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("IOException error: writing customers");
		}
		
	}
	
	public static ArrayList<Person> readCustomersFromFile() {
		ArrayList<Person> customers = new ArrayList<Person>();
		try {
			FileInputStream inFile = new FileInputStream("data/customerData");
			ObjectInputStream inStream = new ObjectInputStream(inFile);
			customers = (ArrayList<Person>) inStream.readObject();
			inStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: reading customers");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException error: reading customers");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException error: reading customers");
		}
		System.out.println("Read customers from file. Length: " + customers.size());
		return customers;
	}
}
