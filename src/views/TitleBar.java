/**
 * This is the entire top bar that replaces the windows bar.
 * Has the title, minimize, and exit.
 * 
 * 
 * 
 */



package views;

import controller.AppStart;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TitleBar extends ToolBar {
	private BorderPane basePane;
	private Stage stage;
	private ImageView icon;
	private AppStart controller;
	
	double initX; //start position for screen dragging
	double initY;
	
	public TitleBar(BorderPane basePane, Stage primaryStage, Image icon, AppStart controller) {
		this.basePane = basePane;
		this.stage = primaryStage;
		this.icon = new ImageView(icon);
		this.controller = controller;
		
		setup();
	}
	
	/**
	 * Creates the buttons for minimize, maximize, and exit
	 * Spacer included to float right the buttons
	 */
	private void setup() {
		stage.initStyle(StageStyle.UNDECORATED);
		// ICON
		int nodeHeight = 30;
		icon.setFitWidth(nodeHeight);
		icon.setFitHeight(nodeHeight);
		
		// TEXT
		Label title = new Label(" Gate Renewal LLC  --  Pre Pre Pre Alpha");
		title.setFont(new Font(16));
		
		// SPACE
		HBox spacer = new HBox();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		// BUTTONS
		Button minimize = new Button("-");
		minimize.setOnAction((ActionEvent e) -> {
			stage.setIconified(true);
		});
		/*    sleeping on this..
		Button maximize = new Button("0");  
		maximize.setOnAction((ActionEvent e) -> {
			stage.setMaximized(!stage.isMaximized());
			((MainPannelOption)basePane.getLeft()).updateSizes(stage.isMaximized());
			basePane.requestFocus();
		});
		*/
		Button exit = new Button("X");
		exit.setOnAction((ActionEvent e) -> {
			controller.saveDataAndExit();
		});
		
		exit.setPrefWidth(nodeHeight);
		minimize.setPrefWidth(nodeHeight);
		//maximize.setPrefWidth(nodeHeight);
		
		this.getItems().addAll(icon, title, spacer, minimize, exit); //maximize missing
		
		// enables screen to be dragged around
		this.setOnMousePressed((MouseEvent event) -> {
            initX = event.getScreenX() - stage.getX();
            initY = event.getScreenY() - stage.getY();
        });
		this.setOnMouseDragged((MouseEvent event) -> {
			stage.setX(event.getScreenX() - initX);
			stage.setY(event.getScreenY() - initY);
	    });
	}
}
