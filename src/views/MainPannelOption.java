package views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainPannelOption extends VBox {
	private BorderPane basePane;
	private Node[] workspaceViews;
	
	private final double WIDTH;  // width and height of itself
	private final double HEIGHT; // gotten from constructor
	
	public MainPannelOption(BorderPane pane, double height, double width, Node[] views) {
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
		ImageView stockPic = new ImageView(new Image("file:images/stock.png"));
		stockPic.setFitWidth(WIDTH/2 - 20);
		stockPic.setFitHeight(WIDTH/2 - 30);
		Button stock = new Button("Stock", stockPic);
		stock.setOnMouseClicked((MouseEvent event) -> {
			basePane.setRight(workspaceViews[0]);
			((StockView) workspaceViews[0]).reload();
		});
		
		Button fill1 = new Button();
		Button fill2 = new Button();
		Button fill3 = new Button();
		Button fill4 = new Button();
		this.getChildren().addAll(stock, fill1, fill2, fill3, fill4);
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
}
