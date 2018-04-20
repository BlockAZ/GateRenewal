package views;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Item;
import model.StockEntry;

public class StockView extends BorderPane {
	private final double HEIGHT;
	private final double WIDTH;
	
	private final double STOCK_H;
	private final double ENTRY_H;
	private final double HISTORY_H;
	
	// rgba(238, 204, 255, 1)
	private String boxFormatBase = "-fx-border-color: rgba(238, 204, 255, .4);"
				+ "-fx-border-width: 5 0 0 5;"
				+ "-fx-border-radius: 1 1 1 1;";
	private String boxFormatBaseV2 = "-fx-border-color: rgba(200, 203, 209, .4);";
	
	private final int    HISTORY_LIMIT = 50;
	
	private List<Item> items;
	private ObservableList<StockEntry> historyData;
	
	public StockView(double height, double width) {
		this.HEIGHT = height;
		this.WIDTH 	= width;
		STOCK_H 	= HEIGHT * 6/10;
		ENTRY_H 	= HEIGHT * 2/10;
		HISTORY_H 	= HEIGHT * 2/10;
		
		
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);
		
		
		
		setupStockItems(false); //top
		setupEntry();	  
		setupHistory();  // bottom (table view)
	}
	
	public void reload() {
		setupEntry();
	}
	
	/**
	 * Table view at bottom that shows recent stock entries
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setupHistory() {
		historyData = FXCollections.observableArrayList();
		historyData.addAll(getHistoryData());
		
		TableView<StockEntry> view = new TableView<StockEntry>();
		view.setPrefHeight(HISTORY_H);
		view.setPrefWidth(WIDTH);
		
		//column names
		TableColumn<StockEntry, String> dateCol 	= new TableColumn<StockEntry, String>("Date");
		TableColumn<StockEntry, String> itemCol 	= new TableColumn<StockEntry, String>("Item");
		TableColumn<StockEntry, String> qtyCol 		= new TableColumn<StockEntry, String>("Quantity");
		TableColumn<StockEntry, String> costCol	 	= new TableColumn<StockEntry, String>("Cost Per");
		TableColumn<StockEntry, String> totCostCol 	= new TableColumn<StockEntry, String>("Total Cost");
		TableColumn<StockEntry, String> notesCol 	= new TableColumn<StockEntry, String>("Notes");
		dateCol.setPrefWidth(WIDTH    * 2/12 - 20); // 20 px accounts for vertical scroll bar
		itemCol.setPrefWidth(WIDTH    * 3/12); 
		qtyCol.setPrefWidth(WIDTH     * 1/12);
		costCol.setPrefWidth(WIDTH    * 1/12);
		totCostCol.setPrefWidth(WIDTH * 1/12);
		notesCol.setPrefWidth(WIDTH   * 4/12);
		
		// setup so the table grabs correct values from object (Item) that the list is filled with
		dateCol.setCellValueFactory(new PropertyValueFactory("date"));
		itemCol.setCellValueFactory(new PropertyValueFactory("itemName"));
		qtyCol.setCellValueFactory(new PropertyValueFactory("itemQty"));
		costCol.setCellValueFactory(new PropertyValueFactory("cost"));
		totCostCol.setCellValueFactory(new PropertyValueFactory("totalCost"));
		notesCol.setCellValueFactory(new PropertyValueFactory("notes"));
	
		view.getColumns().addAll(dateCol, itemCol, qtyCol, costCol, totCostCol, notesCol);
		
		view.setItems(historyData);
		this.setBottom(view);
	}
	
	/**
	 * Reads in item data from text file and creates stock info boxes
	 */
	private void setupStockItems(boolean refresh) {
		HBox wrapper = new HBox(); // holds two columns (2 vert boxes)
		wrapper.setPrefHeight(STOCK_H);
		wrapper.setPrefWidth(WIDTH);
		/*
		wrapper.setStyle("-fx-border-color: green;"
				+ "-fx-border-width: 10px;"
				+ "-fx-padding: 30px;");
		*/
	
		VBox colOne = new VBox();
		VBox colTwo = new VBox();
		if (!refresh)
			items = getItemData();
		double boxHeight = STOCK_H/(items.size()/2);
		double boxWidth  = WIDTH/2;
		
		// each box broken up into 3 parts
		// image | name | stock
	
		double imageWidth = boxWidth * 2/10;
		
		// Formatting item boxes, setting to left or right column
		for (int i=0; i<items.size(); i++) {
			Item item = items.get(i);
			HBox box = new HBox();
			box.setPrefHeight(boxHeight);
			box.setPrefWidth(boxWidth);
			
			ImageView imgNode = new ImageView(item.getImg());
			imgNode.setFitWidth(imageWidth/2);
			imgNode.setFitHeight(imageWidth/2);
			BorderPane imgWrap = new BorderPane(imgNode);
			imgWrap.setPrefHeight(boxHeight);
			imgWrap.setPrefWidth(imageWidth);
			
			Label nameNode = new Label(item.getName());
			nameNode.setPrefHeight(boxHeight);
			nameNode.setFont(Font.font(24));
			nameNode.setAlignment(Pos.CENTER);
			
			HBox spacer = new HBox();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			
			Label stockNode = new Label(item.getStockString() + "   "); // padding
			stockNode.setPrefHeight(boxHeight);
			stockNode.setFont(Font.font(24));
			
			
		
			box.getChildren().addAll(imgWrap, nameNode, spacer, stockNode);
	
			box.setOnMouseClicked((MouseEvent event) -> {
				showStockEntry(item);
			});
			
			formatBorderMessV2(box, i, items.size());
			
			if (i < items.size()/2)
				colOne.getChildren().add(box);
			else
				colTwo.getChildren().add(box);
		}
		
		wrapper.getChildren().addAll(colOne, colTwo);
		
		this.setTop(wrapper);
	}
	
	/**
	 * On init of screen, sets up the bottom entry to be the happy worker
	 * with instructions
	 */
	private void setupEntry() {
		HBox content = new HBox();
		content.setPrefHeight(ENTRY_H);
		content.setPrefWidth(WIDTH);
		content.setAlignment(Pos.CENTER);
		
		ImageView happyWorker = new ImageView(new Image("file:images/happyworker.png"));
		happyWorker.setFitWidth(150);
		happyWorker.setFitHeight(150);
		BorderPane imgWrap = new BorderPane(happyWorker);
		imgWrap.setLeft(new Label("           "));  //padding 
		
		Label instructions = new Label("    Click an Item to update its stock and/or price");
		instructions.setPrefHeight(ENTRY_H);
		instructions.setAlignment(Pos.CENTER);
		instructions.setFont(Font.font("", FontWeight.BOLD, 26));
		
		content.getChildren().addAll(imgWrap, instructions);
		
		this.setCenter(content);
	}
	
	/**
	 *  When an item is clicked on, this entry area is shown on the bottom
	 *  When an entry is made, the item stock/price is updated as well as 
	 *  the stock entry history
	 */
	private void showStockEntry(Item item) {
		// Holds all content
		HBox content = new HBox();
		content.setPrefWidth(WIDTH);
		content.setPrefHeight(ENTRY_H);
		content.setSpacing(30);
		content.setStyle("-fx-padding: 30px;");
		
		// Image on left
		ImageView img = new ImageView(item.getImg());
		img.setFitWidth(80);
		img.setFitHeight(80);
		BorderPane imgWrap = new BorderPane(img);
		
	
		// Name Node
		VBox name = formatEntryItem(item);
		
		// Amount and Cost Nodes ( need field references for submit lambda)
		TextField amountField 	= new TextField();
		TextField costField 	= new TextField(item.getCostString());
		GridPane entries = formatEntryTextFields(item, amountField, costField);
		
		// TextField for notes
		GridPane notePane = new GridPane();
		TextArea noteField = new TextArea();
		noteField.setPrefWidth(WIDTH/4);
		noteField.setPrefHeight(ENTRY_H);
		notePane.add(new Label("Notes:"), 0, 0);
		notePane.add(noteField, 0, 1);
		
		
		// Submit button
		Button submit = new Button("Submit");
		submit.setPrefHeight(40);
		submit.setMinWidth(50);
		submit.setFont(Font.font("", FontWeight.BOLD, 14));
		BorderPane submitWrap = new BorderPane(submit);
		
		// Handles submit!
		submit.setOnAction((ActionEvent click) -> {
			// Entry validation
			if (invalidAmount(amountField.getText())) {
				ImageView exMark = new ImageView(new Image("file:images/icons/exmark.png"));
				exMark.setFitHeight(30);
				exMark.setFitWidth(30);
				submitWrap.setBottom(new Label("Invalid amount", exMark));
			}
			else if (invalidAmount(costField.getText())) {
				ImageView exMark = new ImageView(new Image("file:images/icons/exmark.png"));
				exMark.setFitHeight(30);
				exMark.setFitWidth(30);
				submitWrap.setBottom(new Label("Invalid cost", exMark));
			}
			else if (noteField.getText().contains(",")) {
				ImageView exMark = new ImageView(new Image("file:images/icons/exmark.png"));
				exMark.setFitHeight(30);
				exMark.setFitWidth(30);
				submitWrap.setBottom(new Label("Remove commas \nfrom notes", exMark));
			}
			// Valid entries
			else {
				// update cost
				double tempCost  = Double.parseDouble(costField.getText());
				double tempQty   = Double.parseDouble(amountField.getText());
				String tempNotes = noteField.getText();
				item.setCost(tempCost);
				item.updateStock(tempQty);
				setupStockItems(true);
				
				// reset / update field
				amountField.setText("");
				noteField.setText("");
				costField.setText(item.getCostString());
				
				// add entry to history
				historyData.add(0, new StockEntry(new Date(), item.getName(), tempQty, tempCost, tempNotes));
				
				ImageView checkMark = new ImageView(new Image("file:images/icons/checkmark.png"));
				checkMark.setFitHeight(30);
				checkMark.setFitWidth(30);
				submitWrap.setBottom(new Label("Success", checkMark));
			}
			
		});
		
		HBox spacer = new HBox();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		content.getChildren().addAll(entries, notePane, submitWrap, name, spacer, imgWrap);
		
		this.setCenter(content);
	}
	
	/**
	 * Called on exit of program. 
	 * 		Updates currentStock.txt with new prices/costs
	 * 		Updates stockHistorySmall with items in historyData
	 */
	public void saveData() {
		FileWriter out = null;
		try {
			out = new FileWriter(new File("data/stock/currentStock.txt"), false);
			for (int i=0; i<items.size(); i++)
				out.write(items.get(i).toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("Error: one of currentStock.txt cannot be accessed");
			e.printStackTrace();
		}
		try {
			out = new FileWriter(new File("data/stock/stockHistorySmall.txt"), false);
			for (int i=historyData.size()-1; i>=0; i--)  // oldest first
				out.write(historyData.get(i).toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("Error: one of stockHistorySmall.txt cannot be accessed");
			e.printStackTrace();
		}
	}
	
	
	//HELPER METHODS BELOW
	
	/**
	 * Accesses the txt file where last 50 adds/removes are stored
	 * @return
	 */
	private List<StockEntry> getHistoryData() {
		
		List<StockEntry> data = new LinkedList<StockEntry>();
		Scanner in = null;
		try {
			in = new Scanner(new File("data/stock/stockHistorySmall.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: stockHistory.txt cannot be accessed");
			e.printStackTrace();
		}
		// Reading in all data from smallFile, creating Entry item for each
		while (in.hasNextLine()) {
			data.add(0, new StockEntry(in.nextLine()));
		}
		
		if (data.size() <= HISTORY_LIMIT)
			return data;
		
		/* 
		 * If small history file has over 30 lines, rewrite the most recent 30
		 * and append the rest to the large.txt file
		 * 
		 * This prevents the smallHistory from becoming too large to parse on startup
		 */
		FileWriter out = null;
		try {
			out = new FileWriter(new File("data/stock/stockHistorySmall.txt"), false);
			for (int i=HISTORY_LIMIT-1; i>=0; i--)  // oldest first
				out.write(data.get(i).toString());
			out.flush();
			out.close();
			
			out = new FileWriter(new File("data/stock/stockHistoryLarge.txt"), true);
			for (int i=HISTORY_LIMIT; i<data.size(); i++) 
				out.write(data.get(i).toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("Error: one of stockHistoryXXXXX.txt cannot be accessed");
			e.printStackTrace();
		}
		return data;
	}
	
	/*
	 * This is a mess because each corner box needs its own border radius
	 * values, the two bottom boxes need bottom borders, and the right column
	 * needs right borders... IF ONLY JAVAFX SUPPORTED BORDER-COLLAPSE
	 */
	@SuppressWarnings("unused")
	private void formatBorderMess(HBox box, int i, int itemSize) {
		// Formatting border radius
			if (i == 0) { 						//top left
				box.setStyle(boxFormatBase
						+ "-fx-border-radius: 5 1 1 1;");
			}
			else if (i == itemSize/2 - 1) { // bottom left
				box.setStyle(boxFormatBase
						+ "-fx-border-radius: 1 1 1 5;"
						+ "-fx-border-width: 5 0 5 5;");
			}
			else if (i >= itemSize/2) {		// 2nd column	
				if (i == itemSize/2) {		// top right
					box.setStyle(boxFormatBase
							+ "-fx-border-radius: 1 5 1 1;"
							+ "-fx-border-width: 5 5 0 5;");
				}
				else if (i == itemSize-1) {		// bottom right
					box.setStyle(boxFormatBase
							+ "-fx-border-radius: 1 1 5 1;"
							+ "-fx-border-width: 5 5 5 5;");
				}
				else {
					box.setStyle(boxFormatBase
							+ "-fx-border-width: 5 5 0 5;");
				}
			}
			else {
				box.setStyle(boxFormatBase);
			}
	}
	
	/**
	 * Setting up borders without top or sides
	 */
	private void formatBorderMessV2(HBox box, int i, int itemSize) {
		if (i < itemSize/2) {		// First column
			box.setStyle(boxFormatBaseV2 + "-fx-border-width: 0 5 5 0;");
			//if (i == itemSize/2-1) // bottom left
			//	box.setStyle(boxFormatBaseV2 + "-fx-border-width: 0 5 5 0;");
		}
		else {						// Second column
			box.setStyle(boxFormatBaseV2 + "-fx-border-width: 0 0 5 0;");
			if (i == itemSize-1) // bottom right
				box.setStyle(boxFormatBaseV2 + "-fx-border-width: 0 0 5 0;");
		}
	}
	
	/**
	 * Parses info about items from stock file held in data folder
	 * data/stock/currentStock.txt
	 * @return - list of items
	 */
	private List<Item> getItemData() {
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
			tempItems.add(new Item(in.nextLine(), true));
		
		return tempItems;
	}
	
	
	/**
	 * In the stock entry area (middle), formats the item name section
	 */
	private VBox formatEntryItem(Item item) {
		VBox name = new VBox();
		name.setPrefHeight(ENTRY_H);
		name.setAlignment(Pos.CENTER_LEFT);
		
		Text boldName = new Text("Item:");
		boldName.setStyle("-fx-font-weight: bold");
		boldName.setFont(Font.font("", FontWeight.BOLD, 20));
		
		Text itemName = new Text(item.getName());
		itemName.setFont(new Font(20));
		
		name.getChildren().addAll(boldName, itemName);
		return name;
	}
	
	/**
	 * In the stock entry area (middle), formats the text fields for entering
	 * amount bought and the cost of the item
	 */
	private GridPane formatEntryTextFields(Item item, TextField amountField, TextField costField) {
		GridPane entries = new GridPane();  //Holds labels and text Fields
		entries.setAlignment(Pos.CENTER);
		entries.setHgap(3.0);		
		entries.setVgap(10.0);
		Label amountLabel 		= new Label("Amount: ");
		Label costLabel	   		= new Label("Cost:    $");
		amountLabel.setFont(Font.font("", FontWeight.BOLD, 20));
		costLabel.setFont(Font.font("", FontWeight.BOLD, 20));
		// height and width of text fields
		costField.setPrefWidth(50);
		costField.setPrefHeight(35);
		amountField.setPrefWidth(50);
		amountField.setPrefHeight(35);
		// adding to pane
		entries.add(amountLabel, 0, 0);
		entries.add(amountField, 1, 0);
		entries.add(costLabel, 0, 1);
		entries.add(costField, 1, 1);
		return entries;
	}

	private boolean invalidAmount(String entry) {
		try {
			Double.parseDouble(entry);
		} catch (Exception o) {
			return true;
		}
		return false;
	}
	
}


