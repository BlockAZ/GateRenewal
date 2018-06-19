package controller;


import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Person;
import views.PersonView;
import views.InventoryView;
import views.JobView;
import views.TitleBar;

public class AppStart extends Application{
	private static double APP_WIDTH;
	private static double APP_HEIGHT;
	
	private final static double TITLE_BAR_HEIGHT = 40;
	private static double WORKSPACE_HEIGHT;
	
	private static double MAIN_PANNEL_WIDTH; // Buttons on left
	private static double WORKSPACE_WIDTH;

	BorderPane basePane;
	Node[] workspaceViews;
	
	private static InventoryView inventory;
	private static JobView		 jobs;
	private static PersonView customers;
	private static PersonView employees;
	
	public static void main(String args[]) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		if (instanceRunning()) { //only allows one instance to run
			Platform.exit();
		}
		
		APP_WIDTH = Screen.getPrimary().getBounds().getWidth() * 3/6;
		APP_HEIGHT = Screen.getPrimary().getBounds().getHeight() * 4/6;
		MAIN_PANNEL_WIDTH = APP_WIDTH * 1/8;
		WORKSPACE_HEIGHT = APP_HEIGHT - TITLE_BAR_HEIGHT;
		WORKSPACE_WIDTH = APP_WIDTH - MAIN_PANNEL_WIDTH;
		
		primaryStage.setOnCloseRequest((WindowEvent close) -> {;
			/*
			 * Handles exit request from OS. Exit button calls this method also
			 * from its listener in TitleBar.java
			 */
			saveDataAndExit();
		});
		
		
	
		Image icon = new Image("file:images/icons/raw.png");
		basePane = initStage(primaryStage, icon);
		workspaceViews = initViews();
		
		// working on this view
		basePane.setRight(workspaceViews[0]);
		//
		
		basePane.setTop(new TitleBar(basePane, primaryStage, icon, this));
		basePane.setLeft(new MenuButtons(basePane, WORKSPACE_HEIGHT, MAIN_PANNEL_WIDTH, workspaceViews));
		
		
		primaryStage.setScene(new Scene(basePane, APP_WIDTH, APP_HEIGHT));
        primaryStage.show();
        basePane.requestFocus();  //fixes button highlight issue on start
	}

	/**
	 * Creates foundation pane (borderPane)
	 * Sets custom icon
	 * Creates custom title bar (title, minimize, exit)
	 * Sets top of foundation pane to the custom title bar
	 */
	private BorderPane initStage(Stage primaryStage, Image icon) {
		primaryStage.getIcons().add(icon);
		BorderPane pane = new BorderPane();
		return pane;
	}
	
	/*
	 * Initializes all possible views from main panels
	 * Puts them in list where [0] is top, [4] is bottom
	 */
	private Node[] initViews() {
		ArrayList<Person> customerData = LoadSaveData.readCustomersFromFile();
		ArrayList<Person> employeeData = LoadSaveData.readEmployeesFromFile();
		
		
		inventory = new InventoryView(WORKSPACE_HEIGHT, WORKSPACE_WIDTH);
		jobs	  = new JobView(WORKSPACE_HEIGHT, WORKSPACE_WIDTH, customerData);
		customers = new PersonView(WORKSPACE_HEIGHT, WORKSPACE_WIDTH, true, customerData);
		employees = new PersonView(WORKSPACE_HEIGHT, WORKSPACE_WIDTH, false, employeeData);
		
		return new Node[] {inventory, jobs, null, customers, employees};
	}
	
	
	/**
	 * Saves relevant data from each view
	 */
	public void saveDataAndExit() {
		((InventoryView) workspaceViews[0]).saveData();
		((PersonView)  workspaceViews[3]).saveData(); // customer
		((PersonView)  workspaceViews[4]).saveData(); // employee
		
		Platform.exit();
	}
	
	/**
	 * only allows one instance of program to run using sockets
	 * @return
	 */
	private static boolean instanceRunning() {
		try{
		    @SuppressWarnings("resource")
			RandomAccessFile randomFile = 
		        new RandomAccessFile("single.class","rw");

		    FileChannel channel = randomFile.getChannel();

		    if(channel.tryLock() == null) 
		       	return true;
		}catch( Exception e ) { 
		    System.out.println(e.toString());
		}
		return false;
	}

}
