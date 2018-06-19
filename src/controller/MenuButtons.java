package controller;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import views.InventoryView;
import views.JobView;
import views.PersonView;

public class MenuButtons extends VBox {
	private BorderPane basePane;
	private Node[] workspaceViews;
	
	private final double WIDTH;  // width and height of itself
	private final double HEIGHT; // gotten from constructor
	
	public MenuButtons(BorderPane pane, double height, double width, Node[] views) {
		this.HEIGHT = height;
		this.WIDTH = width;
		this.basePane = pane;
		this.workspaceViews = views;
		
		setup();
	}
	
	/**
	 * Sets up all the labels that act as buttons
	 * Sets on click listeners and sets the correct size
	 */
	private void setup() {
		Button inventoryButton = getInventoryButton();
		Button jobButton	   = getJobButton();
		Button fill2 = new Button();
		Button customerButton = getCustomerButton();
		Button employeeButton = getEmployeeButton();
		this.getChildren().addAll(inventoryButton, jobButton, fill2, customerButton, employeeButton);
		for (Node btn:this.getChildren()) {
			Button temp = (Button)btn;
			temp.setPrefHeight(HEIGHT/this.getChildren().size());
			temp.setPrefWidth(WIDTH);
			temp.setStyle("-fx-border-color: rgba(0, 0, 0, .5)");
			temp.setAlignment(Pos.CENTER);
			temp.setFont(Font.font("", FontWeight.BOLD, 20));
		}
	}
	
	public void updateSizes(boolean isMax) {
		for (Node btn:this.getChildren()) {
			Button temp = (Button)btn;
			//temp.setPrefHeight((pane.getHeight()-TITLE_BAR_HEIGHT)/btns.getChildren().size());
			temp.setPrefHeight(this.getHeight() / this.getChildren().size());
			if (isMax)
				temp.setPrefWidth(WIDTH*1.5);
			else
				temp.setPrefWidth(WIDTH);
		}
	}
	
	
	// FORMATTING FOR ALL BUTTONS ON THE LEFT SIDE OF BASE PANE
	private Button getInventoryButton() {
		//ImageView stockPic = new ImageView(new Image("file:images/stock.png"));
		//stockPic.setFitWidth(WIDTH/2 - 20);
		//stockPic.setFitHeight(WIDTH/2 - 30);
		//Button stock = new Button("Inventory", stockPic);
		Button stock = new Button("Inventory");
		stock.setFont(Font.font(26));
		stock.setContentDisplay(ContentDisplay.TOP);
		stock.setOnMouseClicked((MouseEvent event) -> {
			basePane.setRight(workspaceViews[0]);
			((InventoryView) workspaceViews[0]).reload();
		});
		return stock;
	}
	private Button getJobButton() {
		Button button = new Button("Jobs\n(TODO)");
		button.setFont(Font.font(26));
		button.setContentDisplay(ContentDisplay.TOP);
		button.setOnMouseClicked((MouseEvent event) -> {
			basePane.setRight(workspaceViews[1]);
			((JobView) workspaceViews[1]).reload();
		});
		return button;
	}
	private Button getCustomerButton() {
		Button button = new Button("Customers");
		button.setFont(Font.font(26));
		button.setContentDisplay(ContentDisplay.TOP);
		button.setOnMouseClicked((MouseEvent event) -> {
			basePane.setRight(workspaceViews[3]);
			((PersonView) workspaceViews[3]).reload();
		});
		return button;
	}
	private Button getEmployeeButton() {
		Button button = new Button("Employees");
		button.setFont(Font.font(26));
		button.setContentDisplay(ContentDisplay.TOP);
		button.setOnMouseClicked((MouseEvent event) -> {
			basePane.setRight(workspaceViews[4]);
			((PersonView) workspaceViews[4]).reload();
		});
		return button;
	}
	
	

	
	
}
