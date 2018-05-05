package views;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.text.MaskFormatter;

import com.sun.xml.internal.ws.util.StringUtils;

import controller.LoadSaveData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Person;


public class CustomerView extends BorderPane{
	private final double WIDTH;  // width and height of itself
	private final double HEIGHT; // gotten from constructor

	private final double TOP_HEIGHT;
	private final double BOT_HEIGHT;
	
	private final double customerTableWidth;  
	
	private ArrayList<Person> customers;
	private ObservableList<Person> tableData;
	private ObservableList<Person> searchData;
	TableView<Person> customerTable;
	
	String wrapPadding = "-fx-padding: 30px;";
	
	public CustomerView(double height, double width) {
		this.HEIGHT = height;
		this.WIDTH 	= width;
		TOP_HEIGHT  = HEIGHT * 4/10;
		BOT_HEIGHT  = HEIGHT * 6/10;
		customerTableWidth = WIDTH * 3/4;
		
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);
		
		setup();
	}
	
	public void reload() {
		
	}
	public void saveData() {
		LoadSaveData.writeCustomersToFile(new ArrayList<Person>(tableData));
	}
	
	private void setup() {
		customers = LoadSaveData.readCustomersFromFile();
		tableData = FXCollections.observableArrayList(customers);
		searchData = FXCollections.observableArrayList();
		setupTop(); // tableview and buttons
		
	}

	private void setupTop() {
		HBox topHalf = new HBox();
		topHalf.setPrefWidth(WIDTH);
		topHalf.setPrefHeight(TOP_HEIGHT);
		topHalf.setAlignment(Pos.CENTER_LEFT);
		topHalf.setStyle(wrapPadding);
		topHalf.setSpacing(15);
		
		VBox userFields = getNewUserFields();
		customerTable = getCustomerTable();
		
		
		
		
		topHalf.getChildren().addAll(userFields, customerTable);
		
		this.setTop(topHalf);
	}
	
	private VBox getNewUserFields() {
		VBox fields = new VBox();
		fields.setStyle("-fx-font-size: 14");
		fields.setSpacing(5);
		
		TextField search = new TextField();
		search.setPromptText("Search");
		search.setOnKeyPressed( (KeyEvent event) -> {
			// Getting text in search field
			String searchStr = getFullText(event, search.getText());
			if (searchStr.equals("")) {
				customerTable.setItems(tableData);
				return;
			}
			searchData = FXCollections.observableArrayList();
			for (Person peep:tableData) {
				if (peep.getName().toLowerCase().contains(searchStr))
					searchData.add(peep);
			}
			customerTable.setItems(searchData);
			
			
		});
		
		Label newUserLbl = new Label("New Customer");
		newUserLbl.setFont(Font.font(26));
		newUserLbl.setAlignment(Pos.CENTER);
		
		TextField name = new TextField();
		name.setPromptText("Name");
		
		TextField phone = new TextField();
		phone.setPromptText("Phone No.");
		
		TextField email = new TextField();
		email.setPromptText("Email");
		
		TextField street = new TextField();
		street.setPromptText("Street");
		TextField city = new TextField();
		city.setPromptText("City");
		TextField zip = new TextField();
		zip.setPromptText("Zip Code");
		TextField state = new TextField("AZ");

		HBox submitWrap = new HBox();
		submitWrap.setSpacing(10);
		Button saveButton = new Button("Save");
		submitWrap.getChildren().addAll(saveButton);
		saveButton.setOnMouseClicked( (MouseEvent event) -> {
			Label resultLabel = null;
			ImageView exMark = new ImageView(new Image("file:images/icons/exmark.png"));
			exMark.setFitHeight(30);
			exMark.setFitWidth(30);
			ImageView checkMark = new ImageView(new Image("file:images/icons/checkmark.png"));
			checkMark.setFitHeight(30);
			checkMark.setFitWidth(30);
			
			// Entry validation
			if (name.getText().equals(""))
				resultLabel = new Label("No Name", exMark);
			else if (!validPhoneNumber(phone.getText()))
				resultLabel = new Label("Bad Phone #", exMark);
			else if (!validEmail(email.getText()))
				resultLabel = new Label("Bad Email", exMark);
			else if (!validStreet(street.getText()))
				resultLabel = new Label("Bad Street", exMark);
			else if (!validCity(city.getText()))
				resultLabel = new Label("Bad City", exMark);
			else if (!validZip(zip.getText()))
				resultLabel = new Label("Bad Zip", exMark);
			else {
				resultLabel = new Label("Success", checkMark);
				String phoneMask= "(###) ###-####";
				String phoneData;
				try {
					MaskFormatter maskFormatter= new MaskFormatter(phoneMask);
					maskFormatter.setValueContainsLiteralCharacters(false);
					phoneData = maskFormatter.valueToString(phone.getText());
				} catch (ParseException e) {
					phoneData = "NA";
				}
				String nameData   = StringUtils.capitalize(name.getText());
				String streetData = street.getText();
				String cityData   = StringUtils.capitalize(city.getText());
				String zipData	  = zip.getText();
				String stateData  = state.getText();
				if (streetData.toLowerCase().equals("na")) {
					streetData = "";
					cityData = "";
					zipData = "";
					stateData = "";
				}
				
				Person customer = new Person(nameData, phoneData, email.getText(),
						streetData, cityData, zipData, stateData, "");
				
				tableData.add(customer);
			}
			
			if (submitWrap.getChildren().size() == 2)
				submitWrap.getChildren().remove(1);
			submitWrap.getChildren().add(resultLabel);
		});
		

		fields.getChildren().addAll(search, newUserLbl, name, 
				phone, email, street, city, zip, state, submitWrap);
		return fields;
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableView<Person> getCustomerTable() {
		TableView<Person> view = new TableView<Person>();
		view.setPrefWidth(customerTableWidth);
		view.setStyle("-fx-font-size: 15;");
		
		
		// Handling row clicked to show customer info at bottom of screen
		view.setRowFactory(tr -> {
			TableRow<Person> row = new TableRow<>();
			row.setOnMouseClicked( (MouseEvent e) -> {
				if (row.isEmpty())
					return;
				viewCustomer(row.getItem());
			});
			return row;
		});
		
		TableColumn<Person, String> nameCol		= new TableColumn<Person, String>("Name");
		TableColumn<Person, String> addressCol	= new TableColumn<Person, String>("Address");
		TableColumn<Person, String> phoneCol	= new TableColumn<Person, String>("Phone No.");
		nameCol.setPrefWidth(customerTableWidth * 2/10);
		addressCol.setPrefWidth(customerTableWidth * 5/10); 
		phoneCol.setPrefWidth(customerTableWidth * 3/10); // -2px removes horizontal scroll bar
		
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
		addressCol.setCellValueFactory(new PropertyValueFactory("address"));
		phoneCol.setCellValueFactory(new PropertyValueFactory("phone"));
		
		view.getColumns().addAll(nameCol, addressCol, phoneCol);
		view.setItems(tableData);
		return view;
	}
	
	/* 
	 * Shows the customer info on the left and any jobs on the right 
	 */
	private void viewCustomer(Person person) {
		HBox bottomHalf = new HBox();
		bottomHalf.setPrefHeight(BOT_HEIGHT);
		bottomHalf.setSpacing(50);
		bottomHalf.setStyle(wrapPadding);
		bottomHalf.setPrefWidth(WIDTH);
		bottomHalf.setAlignment(Pos.CENTER_LEFT);
		
		VBox customerInfo = getCustomerInfo(person);
		VBox jobInfo 	  = getJobInfo(person);
		
		bottomHalf.getChildren().addAll(customerInfo, jobInfo);
		
		this.setBottom(bottomHalf);
	}

	private VBox getCustomerInfo(Person person) {
		VBox info = new VBox();
		info.setSpacing(10);
		info.setPrefWidth(WIDTH/2);
		info.setStyle("-fx-font-size: 20;");
		
		// nodes and listeners to edit name
		Label name 		= new Label(person.getName());
		name.setFont(Font.font("", FontWeight.BOLD, 26));
		TextField editName = new TextField("");
		name.setOnMouseClicked( (MouseEvent event) -> {
			info.getChildren().remove(0);
			editName.setText(name.getText());
			info.getChildren().add(0, editName);
		});
		editName.setOnKeyPressed( (KeyEvent event) -> {
			if (!event.getCode().equals(KeyCode.ENTER))
				return;
			String newName = editName.getText();
			person.setName(newName);
			info.getChildren().remove(0);
			name.setText(newName);
			info.getChildren().add(0, name);
			customerTable.refresh();
		});
		
		// nodes and listeners to edit phone Number
		Label phone 	= new Label(person.getPhoneNumber());
		TextField editPhone = new TextField("");
		phone.setOnMouseClicked( (MouseEvent event) -> {
			info.getChildren().remove(1);
			editPhone.setText(phone.getText());
			info.getChildren().add(1, editPhone);
		});
		editPhone.setOnKeyPressed( (KeyEvent event) -> {
			if (!event.getCode().equals(KeyCode.ENTER))
				return;
			String newPhoneNumber = editPhone.getText().trim();
			person.setPhoneNumber(newPhoneNumber);
			info.getChildren().remove(1);
			phone.setText(newPhoneNumber);
			info.getChildren().add(1, phone);
			customerTable.refresh();
		});
		
		// nodes and listeners to edit email
		Label email 	= new Label(person.getEmail());
		TextField editEmail = new TextField("");
		email.setOnMouseClicked( (MouseEvent event) -> {
			info.getChildren().remove(2);
			editEmail.setText(email.getText());
			info.getChildren().add(2, editEmail);
		});
		editEmail.setOnKeyPressed( (KeyEvent event) -> {
			if (!event.getCode().equals(KeyCode.ENTER))
				return;
			String newEmail = editEmail.getText().trim();
			person.setEmail(newEmail);
			info.getChildren().remove(2);
			email.setText(newEmail);
			info.getChildren().add(2, email);
			customerTable.refresh();
		});
		
		// nodes and listeners to edit address
		Label address 	= new Label(person.getAddress());
		TextArea editAddress = new TextArea("");
		editAddress.setPrefHeight(80);
		address.setOnMouseClicked( (MouseEvent event) -> {
			info.getChildren().remove(3);
			editAddress.setText(address.getText());
			info.getChildren().add(3, editAddress);
		});
		editAddress.setOnKeyPressed( (KeyEvent event) -> {
			if (!event.getCode().equals(KeyCode.ENTER))
				return;
			String newAddress = editAddress.getText().trim();
			person.setAddress(newAddress);
			info.getChildren().remove(3);
			address.setText(newAddress);
			info.getChildren().add(3, address);
			customerTable.refresh();
		});
		
		
		Label date 		= new Label("Added: " + person.getDate());
		Label noteTitle = new Label("Notes: ");
		TextArea notes		= new TextArea(person.getNotes());
	
		notes.setOnKeyPressed((KeyEvent event) -> {
			String newNote = getFullText(event, notes.getText());
			person.setNotes(newNote);
		});
		notes.setWrapText(true);
		notes.setPrefWidth(WIDTH * 2/6);
		notes.setPrefHeight(HEIGHT / 6);
		notes.setFont(Font.font(16));
		
		info.getChildren().addAll(name, phone, email, address, noteTitle, notes, date);
		return info;
	}
	private VBox getJobInfo(Person person) {
		VBox info = new VBox();
		info.setStyle("-fx-font-size: 24;");
		info.setPrefWidth(WIDTH/2);
		
		Label todo = new Label("Any jobs go here (todo)");
		
		info.getChildren().addAll(todo);
		
		return info;
	}

	
	/* phew.. the text area isn't immediately updated with the event that keypressed
	 * creates... but the event gives us the key that was pressed, so the text we save is
	 * the exising text area plus the new character (if it's valid) or minus a character if
	 * that character is backspace
	 */	
	private String getFullText(KeyEvent event, String newNote) {
		
		if (event.getText().length() > 0
				&& event.getText().charAt(0) >= 32 
				&& event.getText().charAt(0) < 127) {
			newNote += event.getText();
		}
		else if (event.getCode().equals(KeyCode.BACK_SPACE)) {
			if (newNote.equals(""))
				return "";
			newNote = newNote.substring(0, newNote.length()-1);
		}
		return newNote.toLowerCase();
		
	}

	
	private boolean validPhoneNumber(String number) {
		if (number.toLowerCase().equals("na"))
			return true;
		if (number.length() != 10)
			return false;
		try {
			Double.parseDouble(number);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	private boolean validEmail(String email) {
		String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)"
				+ "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(emailRegex);
		return pattern.matcher(email).matches();
	}
	private boolean validStreet(String street) {
		if (street.toLowerCase().equals("na"))
			return true;
		if (street.equals(""))
			return false;
		String[] split = street.split("\\s+");
		if (split.length < 2)
			return false;
		if (split[0].contains("f") || split[0].contains("d"))
			return false;
		try {
			Double.parseDouble(split[0]);
		}
		catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
	private boolean validCity(String city) {
		if (city.toLowerCase().equals("na"))
			return true;
		if (city.equals(""))
			return false;
		if (city.matches(".*\\d+.*"))  //any digits
			return false;
		return true;
	}
	private boolean validZip(String zip) {
		if (zip.toLowerCase().equals("na"))
			return true;
		if (zip.length() < 3 || zip.length() > 5)
			return false;
		try {
			Double.parseDouble(zip);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}
