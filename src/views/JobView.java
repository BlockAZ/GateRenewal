package views;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
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
import model.Job;
import model.Person;

public class JobView extends BorderPane {
	private final double WIDTH;  // width and height of itself
	private final double HEIGHT; // gotten from constructor

	private final double TOP_HEIGHT;  // for row of buttons
	private final double BOT_HEIGHT;  // workspace 
	
	private final double TABLE_WIDTH; 
	
	private ArrayList<Person> customers;
	private ObservableList<Job> tableData;
	
	String wrapPadding = "-fx-padding: 30px;";
	
	public JobView(double height, double width, ArrayList<Person> customers) {
		this.HEIGHT = height;
		this.WIDTH 	= width;
		this.TOP_HEIGHT = height * 1/8;
		this.BOT_HEIGHT = height * 7/8;
		this.TABLE_WIDTH = WIDTH * 1/2;
		this.customers = customers;
		
		
		
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);
		
		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.add(new Job("Job 1", customers.get(0)));
		
		tableData = FXCollections.observableArrayList(jobs);
		
		setup();
	}
	
	public void reload() {
		
	}
	private void setup() {
		initButtons();
	}
	
	
	private void initButtons() {
		HBox buttons = new HBox();
		buttons.setPrefHeight(TOP_HEIGHT);
		buttons.setPrefWidth(WIDTH);
		buttons.setSpacing(30);
		buttons.setAlignment(Pos.CENTER);
		buttons.setStyle(wrapPadding);
		
		Button newJob    = new Button("New Job");
		newJob.setOnMouseClicked((MouseEvent event) -> {
			viewNewJob();
		});
		
		Button allJobs   = new Button("All Jobs");
		allJobs.setOnMouseClicked( (MouseEvent event) -> {
			viewHomeInfo();
		});
		
		Button hoursButton  = new Button("Idk nothing");
		
		buttons.getChildren().addAll(newJob, allJobs, hoursButton);
		
		this.setTop(buttons);
	}
	
	
	private void viewNewJob() {
		HBox wrap = new HBox();
		wrap.setPrefHeight(BOT_HEIGHT);
		wrap.setPrefWidth(WIDTH);
		wrap.setStyle(wrapPadding);
		
		// GRIDPANE INFO ON LEFT
		GridPane grid = new GridPane();
		grid.setPrefWidth(WIDTH * 4/10);
		grid.setHgap(5);
		grid.setVgap(10);
		
		Label nameLabel = formatLabel("Name");
		TextField nameField = new TextField();
		grid.add(nameLabel, 0, 0);
		grid.add(nameField, 1, 0);
		
		Label customerLabel = formatLabel("Customer");
		ComboBox<Person> customerField = new ComboBox<Person>();
		customerField.getItems().addAll(customers);
		grid.add(customerLabel, 0, 1);
		grid.add(customerField, 1, 1);
		
		Label dateLabel = formatLabel("Date");
		DatePicker dateField = new DatePicker();
		grid.add(dateLabel, 0, 2);
		grid.add(dateField, 1, 2);
	
		Label timeLabel = formatLabel("Time");
		ComboBox<Integer> hour 	 = new ComboBox<Integer>();
		ComboBox<Integer> minute = new ComboBox<Integer>();
		ComboBox<String>  amPM   = new ComboBox<String>();
		HBox timeField = formatTimeField(hour, minute, amPM);
		grid.add(timeLabel, 0, 3);
		grid.add(timeField, 1, 3);
		
		
		// TASK OPTIONS ON RIGHT
		VBox rightSide = new VBox();
		rightSide.setPrefWidth(WIDTH * 6/10);
		rightSide.setAlignment(Pos.TOP_LEFT);
		rightSide.setSpacing(10);
		
		// wood type buttons
		Label woodLabel = new Label("Wood Options");
		woodLabel.setFont(Font.font(20));
		ToggleGroup woodTypeGroup = new ToggleGroup();
		ImageView brownWoodPic = new ImageView(new Image("file:images/items/3inch_brown_board.png"));
		ImageView blackWoodPic = new ImageView(new Image("file:images/items/3inch_black_board.png"));
		ImageView greyWoodPic = new ImageView(new Image("file:images/items/3inch_brown_board.png"));
		ToggleButton brownTog = new ToggleButton("Brown", brownWoodPic);
		ToggleButton blackTog = new ToggleButton("Black", blackWoodPic);
		ToggleButton greyTog  = new ToggleButton("Grey", greyWoodPic);
		HBox woodOptions = formatImageRadio(woodTypeGroup, new ImageView[] {brownWoodPic, blackWoodPic, greyWoodPic}, 
				new ToggleButton[] {brownTog, blackTog, greyTog});
		// 3 and 5 inch options
		ToggleGroup woodSizeGroup = new ToggleGroup();
		RadioButton threeInchTog = new RadioButton("3 Inch");
		RadioButton fiveInchTog = new RadioButton("5 Inch");
		formatWoodSizeRadioButtons(new RadioButton[] {threeInchTog, fiveInchTog}, woodSizeGroup);
		VBox woodSizeOptions = new VBox(threeInchTog, fiveInchTog);
		woodSizeOptions.setSpacing(10);
		woodOptions.getChildren().add(woodSizeOptions);
		
		/////////// paint color options
		Label paintLabel = new Label("Paint Options");
		paintLabel.setFont(Font.font(20));
		
		ToggleGroup colorGroup = new ToggleGroup();
		
		HBox topColor = new HBox();
		ToggleButton redButton   = new ToggleButton();
		ToggleButton greenButton = new ToggleButton();
		ToggleButton blueButton  = new ToggleButton();
		redButton.setStyle("-fx-background-color: RED");
		greenButton.setStyle("-fx-background-color: GREEN");
		blueButton.setStyle("-fx-background-color: BLUE");
		topColor.getChildren().addAll(redButton, greenButton, blueButton);
		
		HBox botColor = new HBox();
		botColor.setAlignment(Pos.BOTTOM_LEFT);
		ToggleButton orangeButton = new ToggleButton();
		ToggleButton pinkButton   = new ToggleButton();
		orangeButton.setStyle("-fx-background-color: ORANGE");
		pinkButton.setStyle("-fx-background-color: PINK");
		
		VBox customWrap = new VBox();
		VBox spacer = new VBox();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		ToggleButton customButton = new ToggleButton("Custom");
		customButton.setPrefWidth(WIDTH * 1/8);
		customButton.setPrefHeight(40);
		TextField customColorField = new TextField();
		customColorField.setPrefWidth(WIDTH * 1/8);
		customColorField.setDisable(true);
		customButton.setOnMouseClicked((MouseEvent event) -> {
			if (customColorField.isDisable())
				customColorField.setDisable(false);
		});
		
		
		
		customWrap.getChildren().addAll(customButton, spacer, customColorField);
		
		botColor.getChildren().addAll(orangeButton, pinkButton, customWrap);
		
		formatColorOptions(new HBox[] {topColor, botColor}, new ToggleButton[] {redButton, greenButton, blueButton,
				orangeButton, pinkButton}, colorGroup, customColorField);
		
		
		
		rightSide.getChildren().addAll(woodLabel, woodOptions, new Label(""), paintLabel,
				topColor, botColor);
		
		wrap.getChildren().addAll(grid, rightSide);
		this.setBottom(wrap);
	}


	private void viewHomeInfo() {
		BorderPane infoWrap = new BorderPane();
		infoWrap.setPrefHeight(BOT_HEIGHT);
		infoWrap.setPrefWidth(WIDTH);
		infoWrap.setStyle(wrapPadding);
		
		
		TableView<Job> jobTable = formatJobTable(infoWrap);
		
		infoWrap.setLeft(jobTable);
		
		this.setBottom(infoWrap);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableView<Job> formatJobTable(BorderPane pane) {
		TableView<Job> jobTable = new TableView<Job>();
		
		jobTable.setRowFactory(tr -> {
			TableRow<Job> row = new TableRow<>();
			row.setOnMouseClicked( (MouseEvent e) -> {
				if (row.isEmpty())
					return;
				pane.setRight(viewJob(row.getItem()));
			});
			return row;
		});
		
		TableColumn<Job, String> nameCol		= new TableColumn<Job, String>("Name");
		TableColumn<Job, String> customerCol	= new TableColumn<Job, String>("Customer");
		TableColumn<Job, String> completeCol 	= new TableColumn<Job, String>("Completed");
		TableColumn<Job, String> paidCol 		= new TableColumn<Job, String>("Paid");
		
		nameCol.setPrefWidth(TABLE_WIDTH * 2/6);
		customerCol.setPrefWidth(TABLE_WIDTH * 2/6);
		completeCol.setPrefWidth(TABLE_WIDTH * 1/6);
		paidCol.setPrefWidth(TABLE_WIDTH * 1/6 - 2);
		
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
		customerCol.setCellValueFactory(new PropertyValueFactory("customer"));
		completeCol.setCellValueFactory(new PropertyValueFactory("complete"));
		paidCol.setCellValueFactory(new PropertyValueFactory("paid"));
		
		jobTable.getColumns().addAll(nameCol, customerCol, completeCol, paidCol);
		
		jobTable.setItems(tableData);
		return jobTable;
	}
	
	private VBox viewJob(Job job) {
		VBox infoWrap = new VBox();
		Label name = new Label("Name: " + job.getName());
		
		
		infoWrap.getChildren().addAll(name);
		
		return infoWrap;
	}
	
	
	private Label formatLabel(String title) {
		Label label = new Label(title + ":");
		label.setFont(Font.font(16));
		return label;
	}
	private HBox formatTimeField(ComboBox<Integer> hour, ComboBox<Integer> minute, ComboBox<String> amPM) {
		HBox timeField = new HBox();
		timeField.setSpacing(5);
		
		hour.setPromptText("hour");
		minute.setPromptText("min");
		
		hour.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		minute.getItems().addAll(0, 15, 30, 45);
		amPM.getItems().addAll("AM", "PM");
		amPM.setValue("AM");
		
		timeField.getChildren().addAll(hour, minute, amPM);
		return timeField;
	}
	
	private HBox formatImageRadio(ToggleGroup woodTypeGroup, ImageView[] images, ToggleButton[] buttons) {
		HBox box = new HBox();
		
		for (ImageView pic:images) {
			pic.setFitHeight(100);
			pic.setFitWidth(WIDTH * 1/10);
		}
		for (ToggleButton button:buttons) {
			button.setToggleGroup(woodTypeGroup);
			button.setContentDisplay(ContentDisplay.TOP);
		}
		
		box.setSpacing(20);
		box.getChildren().addAll(buttons);
		
		return box;
	}
	
	private void formatWoodSizeRadioButtons(RadioButton[] buttons, ToggleGroup woodSizeGroup) {
		for (RadioButton button:buttons) {
			button.setToggleGroup(woodSizeGroup);
			button.setFont(Font.font(16));
		}
		
	}
	
	private void formatColorOptions(HBox[] colRows, ToggleButton[] buttons, 
			ToggleGroup group, TextField customField) {
		for (HBox row:colRows) {
			row.setSpacing(30);
		}
		for (ToggleButton button:buttons) {
			button.setPrefWidth(WIDTH * 1/8);
			button.setPrefHeight(70);
			button.setToggleGroup(group);
			
			button.setOnMouseClicked((MouseEvent event) -> {
				if (!customField.isDisabled())
					customField.setDisable(true);
			});
		}
	}
	
	
	


	
}
