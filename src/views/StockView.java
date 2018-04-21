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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
	
	private final double TITLE_H;
	private final double STOCK_H;
	private final double ENTRY_H;
	private final double HISTORY_H;
	
	private final int    HISTORY_LIMIT = 50;
	private final int 	 ITEMS_LIMIT   = 5;
	
	// rgba(238, 204, 255, 1)
	private String boxFormatBase = "-fx-border-color: rgba(238, 204, 255, .4);"
				+ "-fx-border-width: 5 0 0 5;"
				+ "-fx-border-radius: 1 1 1 1;";
	private String boxFormatBaseV2 = "-fx-border-color: rgba(200, 203, 209, .4);";
	private String entryAreaFormat = "-fx-border-color: rgba(200, 203, 209, .4);"
			+ "-fx-border-width: 5 0 0 0;";
	

	
	private List<Item> items;
	private ObservableList<StockEntry> historyData;
	
	public StockView(double height, double width) {
		this.HEIGHT = height;
		this.WIDTH 	= width;
		TITLE_H		= HEIGHT * 1/20;
		STOCK_H 	= HEIGHT * 11/20;
		ENTRY_H 	= HEIGHT * 4/20;
		HISTORY_H 	= HEIGHT * 4/20;
		
		
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);
		
		
		setupStockItems(false, 0); //top
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
		view.setFixedCellSize(HISTORY_H/6);
		view.setStyle("-fx-font-size: 15;");
		
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
	private void setupStockItems(boolean refresh, double vertScrollValue) {
		// This got a little messy.. basePane is very base..
		// title is at top
		// scrollWrap is in middle which holds colWrap
		
		VBox basePane = new VBox();
		HBox title = formatMainTitle();
		HBox wrapper = new HBox(); // holds two columns (2 vert boxes)
		wrapper.setPrefWidth(WIDTH);
		
		ScrollPane scrollWrap = new ScrollPane();
		scrollWrap.setPrefHeight(STOCK_H);
		scrollWrap.setHbarPolicy(ScrollBarPolicy.NEVER);
		int scrollBarWidth = 15;
		scrollWrap.setStyle("-fx-font-size: " + scrollBarWidth + ";"
				+ "-fx-background-color: transparent;");
		scrollWrap.setVvalue(vertScrollValue);
		
		/*
		wrapper.setStyle("-fx-border-color: green;"
				+ "-fx-border-width: 10px;"
				+ "-fx-padding: 30px;");
		*/
	
		VBox colOne = new VBox();
		VBox colTwo = new VBox();
		if (!refresh)
			items = getItemData();
		double boxHeight = STOCK_H/ITEMS_LIMIT;
		double boxWidth  = (WIDTH - scrollBarWidth)/2;
		
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
				showStockEntry(item, scrollWrap);
			});
			
			formatBorderMessV2(box, i, items.size());
			
			if (i < items.size()/2)
				colOne.getChildren().add(box);
			else
				colTwo.getChildren().add(box);
		}
		
		wrapper.getChildren().addAll(colOne, colTwo);
		scrollWrap.setContent(wrapper);
		basePane.getChildren().addAll(title, scrollWrap);
		
		this.setTop(basePane);
	}
	
	

	/**
	 * On init of screen, sets up the bottom entry to be the happy worker
	 * with instructions
	 */
	private void setupEntry() {
		HBox content = new HBox();
		content.setStyle(entryAreaFormat);
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
	private void showStockEntry(Item item, ScrollPane scrollpane) {
		// Holds all content
		BorderPane wrap = new BorderPane();
		wrap.setStyle(entryAreaFormat);
		HBox title = formatTitle(item);

		// Holds image and input stuff
		HBox content = new HBox();
		content.setPrefWidth(WIDTH);
		content.setPrefHeight(ENTRY_H);
		content.setSpacing(50);
		content.setStyle("-fx-padding: 0 0 0 " + WIDTH/12 + ";");  //left padding
		content.setAlignment(Pos.CENTER_LEFT);
	
		// Product image
		ImageView img = new ImageView(item.getImg());
		img.setFitWidth(ENTRY_H / 2);
		img.setFitHeight(ENTRY_H / 2);
		BorderPane imgWrap = new BorderPane(img);
		
		// Amount and Cost Nodes ( need field references for submit lambda)
		TextField amountField 	= new TextField();
		TextField costField 	= new TextField(item.getCostString());
		GridPane entries = formatEntryTextFields(item, costField, amountField);
		
		// TextField for notes and check box for correction
		TextArea noteField = new TextArea();
		VBox notePane = formatNotePane(noteField);

		// Submit and check box area
		CheckBox correctionBox = new CheckBox("This is a correction");
		Button submit = new Button("Submit");
		VBox submitWrap = formatSubmitArea(submit, correctionBox);
		
		
		content.getChildren().addAll(imgWrap, entries, notePane, submitWrap);
		
		wrap.setTop(title);
		wrap.setCenter(content);
		
		this.setCenter(wrap);
		
		// Handles submit!
		submit.setOnAction((ActionEvent click) -> {
			int contentNodeSize = 5;
			Label resultLabel = null;
			// Entry validation
			ImageView exMark = new ImageView(new Image("file:images/icons/exmark.png"));
			exMark.setFitHeight(30);
			exMark.setFitWidth(30);
			if (invalidAmount(amountField.getText())) {
				resultLabel = new Label("Invalid amount", exMark);
			}
			else if (invalidAmount(costField.getText())) {
				resultLabel = new Label("Invalid cost", exMark);
			}
			else if (noteField.getText().contains(",")) {
				resultLabel = new Label("Remove commas from notes", exMark);
			}
			else if (correctionBox.isSelected() && noteField.getText().equals("")) {
				resultLabel = new Label("Corrections require a note", exMark);
			}
			// Valid entries from here on out
			else {
				// update cost
				double tempCost  = Double.parseDouble(costField.getText());
				double tempQty   = Double.parseDouble(amountField.getText());
				String tempNotes = "";
				if (correctionBox.isSelected())
					tempNotes = "CORRECTION: ";
				tempNotes += noteField.getText();
				item.setCost(tempCost);
				item.updateStock(tempQty);
				setupStockItems(true, scrollpane.getVvalue());
				
				// reset / update field
				amountField.setText("");
				noteField.setText("");
				costField.setText(item.getCostString());
				correctionBox.setSelected(false);
				
				// add entry to history
				historyData.add(0, new StockEntry(new Date(), item.getName(), tempQty, tempCost, tempNotes));
				
				ImageView checkMark = new ImageView(new Image("file:images/icons/checkmark.png"));
				checkMark.setFitHeight(30);
				checkMark.setFitWidth(30);
				if (content.getChildren().size() == contentNodeSize)
					content.getChildren().remove(contentNodeSize-1);
				resultLabel = new Label("Success", checkMark);
			}
			
			if (content.getChildren().size() == contentNodeSize)
				content.getChildren().remove(contentNodeSize-1);
			resultLabel.setFont(Font.font(18));
			content.getChildren().add(resultLabel);
		});
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
			box.setStyle(boxFormatBaseV2 + "-fx-border-width: 0 0 5 0;");
			if (i == itemSize/2-1 && itemSize%2 == 0) // bottom left (checking even for formatting)
				box.setStyle(boxFormatBaseV2 + "-fx-border-width: 0 0 0 0;");
		}
		else {						// Second column
			box.setStyle(boxFormatBaseV2 + "-fx-border-width: 0 0 5 5;");
			if (i == itemSize-1) // bottom right
				box.setStyle(boxFormatBaseV2 + "-fx-border-width: 0 0 0 5;");
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
	
	/*
	 * Formats the title at the top of the entry area. Inventory Submission: ...
	 */
	private HBox formatTitle(Item item) {
		HBox box = new HBox();
		box.setSpacing(30);
		Text boldName = new Text("  Inventory Submission: ");
		boldName.setFont(Font.font("", FontWeight.BOLD, 26));
		
		Text itemName = new Text(item.getName());
		itemName.setFont(new Font(26));
		
		box.getChildren().addAll(boldName, itemName);
		return box;
	}
	/**
	 * Formats the main title at very top of stock view page
	 */
	private HBox formatMainTitle() {
		HBox ret = new HBox();
		ret.setPrefHeight(TITLE_H);
		ret.setPrefWidth(WIDTH);
		ret.setAlignment(Pos.CENTER);
		
		Text title = new Text("Inventory   ");
		title.setFont(Font.font("", FontWeight.BOLD, 36));
		
		Text extra = new Text(" current stock (avaliable to use)");
		extra.setFont(new Font(28));
		
		ret.getChildren().addAll(title, extra);
		
		return ret;
	}
	

	/**
	 * In the stock entry area (middle), formats the text fields for entering
	 * amount bought and the cost of the item
	 */
	private GridPane formatEntryTextFields(Item item, TextField costField, TextField amountField) {
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

	/*
	 *	Optional note area
	 */
	private VBox formatNotePane(TextArea noteField) {
		VBox notePane = new VBox();
		notePane.setAlignment(Pos.CENTER_LEFT);
		Label noteslbl = new Label("Notes (optional):");
		noteslbl.setFont(Font.font("", FontWeight.BOLD, 20));
		
		noteField.setPrefWidth(WIDTH/4);
		noteField.setPrefHeight(ENTRY_H * 1/3);
		
		// label is for a little bit of spacing
		notePane.getChildren().addAll(noteslbl, noteField, new Label(" ")); 
		
		return notePane;
	}
	
	/*
	 * Submit Area with submit button, check box, and submission result
	 */
	private VBox formatSubmitArea(Button submit, CheckBox correctionBox) {
		VBox wrap = new VBox();
		wrap.setSpacing(10);
		wrap.setAlignment(Pos.CENTER_LEFT);
		
		correctionBox.setFont(Font.font(16));
		submit.setPrefWidth(80);
		submit.setPrefHeight(35);
		submit.setFont(Font.font(16));
		
		wrap.getChildren().addAll(correctionBox, submit);
		return wrap;
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


