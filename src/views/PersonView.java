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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import model.Customer;
import model.Employee;
import model.Person;


public class PersonView extends BorderPane{
	private final double WIDTH;  // width and height of itself
	private final double HEIGHT; // gotten from constructor

	private final double TOP_HEIGHT;
	private final double BOT_HEIGHT;
	private final double TABLE_WIDTH;  
	
	private final boolean CUSTOMER;
	
	private ArrayList<Person> people;
	private ObservableList<Person> tableData;
	private ObservableList<Person> searchData;
	TableView<Person> personTable;
	
	String wrapPadding = "-fx-padding: 30px;";
	
	public PersonView(double height, double width, boolean isCustomer, ArrayList<Person> people) {
		this.CUSTOMER = isCustomer;
		this.HEIGHT = height;
		this.WIDTH 	= width;
		this.people = people;
		if (CUSTOMER) {
			TOP_HEIGHT  = HEIGHT * 4/10;
			BOT_HEIGHT  = HEIGHT * 6/10;
		}
		else {
			TOP_HEIGHT  = HEIGHT * 5/10;
			BOT_HEIGHT  = HEIGHT * 5/10;
		}
		TABLE_WIDTH = WIDTH * 3/4;

		
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);
		
		tableData = FXCollections.observableArrayList(this.people);
		searchData = FXCollections.observableArrayList();
		
		
		setup();
	}
	
	public void reload() {
		
	}
	public void saveData() {
		if (CUSTOMER)
			LoadSaveData.writeCustomersToFile(new ArrayList<Person>(tableData));
		else
			LoadSaveData.writeEmployeesToFile(new ArrayList<Person>(tableData));
	}
	
	private void setup() {
		
		setupTop(); // tableview and buttons
		
	}

	private void setupTop() {
		HBox topHalf = new HBox();
		topHalf.setPrefWidth(WIDTH);
		topHalf.setPrefHeight(TOP_HEIGHT);
		topHalf.setAlignment(Pos.CENTER_LEFT);
		topHalf.setStyle(wrapPadding);
		topHalf.setSpacing(15);
		
		
		if (CUSTOMER)
			personTable = getCustomerTable();
		else 
			personTable = getEmployeeTable();
		VBox userFields = getNewUserFields();
		
		
		
		topHalf.getChildren().addAll(userFields, personTable);
		
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
				personTable.setItems(tableData);
				return;
			}
			searchData = FXCollections.observableArrayList();
			for (Person peep:tableData) {
				if (peep.getName().toLowerCase().contains(searchStr))
					searchData.add(peep);
			}
			personTable.setItems(searchData);
			
			
		});
		Label newUserLbl;
		if (CUSTOMER)
			newUserLbl = new Label("New Customer");
		else
			newUserLbl = new Label("New Employee");
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
		
		TextField wage = new TextField();
		wage.setPromptText("Wage");
		
		TextField title = new TextField();
		title.setPromptText("Title");

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
			if (name.getText().equals(""))							// name
				resultLabel = new Label("No Name", exMark);
			else if (!validPhoneNumber(phone.getText()))			// phone
				resultLabel = new Label("Bad Phone #", exMark);
			else if (!validEmail(email.getText()))					// email
				resultLabel = new Label("Bad Email", exMark);
			else if (!validStreet(street.getText()))				// street
				resultLabel = new Label("Bad Street", exMark);
			else if (!validCity(city.getText(), street.getText()))	// city
				resultLabel = new Label("Bad City", exMark);
			else if (!validZip(zip.getText(), street.getText()))	// zip
				resultLabel = new Label("Bad Zip", exMark);
			else if (!validWage(wage.getText()))
				resultLabel = new Label("Bad Wage", exMark);
			else if (title.getText().equals("") && !CUSTOMER)
				resultLabel = new Label("No Title", exMark);
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
					streetData = "NA";
					cityData = "";
					zipData = "";
					stateData = "";
				}
				Person person;
				if (CUSTOMER) {
					person = new Customer(nameData, phoneData, email.getText(),
						streetData, cityData, zipData, stateData);
				}
				else {
					Double wageData		= Double.parseDouble(wage.getText());
					String titleData	= title.getText();
					person = new Employee(nameData, phoneData, email.getText(),
							streetData, cityData, zipData, stateData,
							titleData, wageData);
				}
				name.setText("");
				phone.setText("");
				email.setText("");
				street.setText("");
				city.setText("");
				zip.setText("");
				//state.setText("");
				title.setText("");
				wage.setText("");
				
				tableData.add(person);
				people.add(person);
			}
			
			if (submitWrap.getChildren().size() == 2)
				submitWrap.getChildren().remove(1);
			submitWrap.getChildren().add(resultLabel);
		});
		
		
		fields.getChildren().addAll(search, newUserLbl, name, 
				phone, email, street, city, zip, state, submitWrap);
		
		if (!CUSTOMER) {
			fields.getChildren().remove(fields.getChildren().size()-1);
			fields.getChildren().addAll(title, wage, submitWrap);
		}
		return fields;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TableView<Person> getEmployeeTable() {
		TableView<Person> view = new TableView<Person>();
		view.setPrefWidth(TABLE_WIDTH);
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
		
		//TableColumn<Person, String> idCol	    = new TableColumn<Person, String>("Title"); 
		TableColumn<Person, String> titleCol    = new TableColumn<Person, String>("Title"); 
		TableColumn<Person, String> nameCol		= new TableColumn<Person, String>("Name");
		TableColumn<Person, String> phoneCol	= new TableColumn<Person, String>("Phone No.");
		//idCol.setPrefWidth(TABLE_WIDTH * 1/10);
		titleCol.setPrefWidth(TABLE_WIDTH * 3/10);
		nameCol.setPrefWidth(TABLE_WIDTH * 3/10);
		phoneCol.setPrefWidth(TABLE_WIDTH * 3/10 -2); // -2px removes horizontal scroll bar
		
		//idCol.setCellValueFactory(new PropertyValueFactory("id"));
		titleCol.setCellValueFactory(new PropertyValueFactory("title"));
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
		phoneCol.setCellValueFactory(new PropertyValueFactory("phone"));
		
		view.getColumns().addAll(nameCol, titleCol, phoneCol);
		view.setItems(tableData);
		return view;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableView<Person> getCustomerTable() {
		TableView<Person> view = new TableView<Person>();
		view.setPrefWidth(TABLE_WIDTH);
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
		nameCol.setPrefWidth(TABLE_WIDTH * 2/10);
		addressCol.setPrefWidth(TABLE_WIDTH * 5/10); 
		phoneCol.setPrefWidth(TABLE_WIDTH * 3/10 -2); // -2px removes horizontal scroll bar
		
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
		
		VBox personInfo = getPersonInfo(person);
		VBox rightSide;
		if (CUSTOMER)
			rightSide = getJobInfo(person);
		else
			rightSide = getExtraInfo(person);
		
		bottomHalf.getChildren().addAll(personInfo, rightSide);
		
		this.setBottom(bottomHalf);
	}

	private VBox getPersonInfo(Person person) {
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
			personTable.refresh();
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
			personTable.refresh();
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
			personTable.refresh();
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
			personTable.refresh();
		});
		
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
		
		HBox dateDeleteWrap = new HBox();
		dateDeleteWrap.setSpacing(35);
		Label date 		= new Label("Added: " + person.getDate());
		Button delete;
		if (CUSTOMER)
			delete   = new Button("Delete Customer");
		else
			delete   = new Button("Delete Employee");
		delete.setOnMouseClicked( (MouseEvent event) -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + person.getName() + "?",
					ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			if (alert.getResult().equals(ButtonType.NO))
				return;
			tableData.remove(person);
			personTable.refresh();
			this.setBottom(new Label()); // removes all info about person from screen
		});
		dateDeleteWrap.getChildren().addAll(date, delete);
		
		
		info.getChildren().addAll(name, phone, email, address, noteTitle, notes, dateDeleteWrap);
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
	private VBox getExtraInfo(Person person) {
		Employee employee = (Employee)person;
		VBox info = new VBox();
		info.setSpacing(10);
		info.setPrefWidth(WIDTH/2);
		info.setStyle("-fx-font-size: 20;");
		
		
		HBox titleWrap = new HBox();
		Label titleLbl = new Label("Title: ");
		// nodes and listeners to edit title
		Label title 		= new Label(employee.getTitle());
		TextField editTitle = new TextField("");
		title.setOnMouseClicked( (MouseEvent event) -> {
			titleWrap.getChildren().remove(1);
			editTitle.setText(title.getText());
			titleWrap.getChildren().add(1, editTitle);
		});
		editTitle.setOnKeyPressed( (KeyEvent event) -> {
			if (!event.getCode().equals(KeyCode.ENTER))
				return;
			String newTitle = editTitle.getText();
			employee.setTitle(newTitle);
			titleWrap.getChildren().remove(1);
			title.setText(newTitle);
			titleWrap.getChildren().add(1, title);
			personTable.refresh();
		});
		titleWrap.getChildren().addAll(titleLbl, title);
		
		
		HBox wageWrap = new HBox();
		Label wageLbl = new Label("Wage: $");
		// nodes and listeners to edit title
		Label wage 		= new Label(employee.getWageStr());
		TextField editWage = new TextField("");
		editWage.setMaxWidth(80);
		wage.setOnMouseClicked( (MouseEvent event) -> {
			wageWrap.getChildren().remove(1);
			editWage.setText(wage.getText());
			wageWrap.getChildren().add(1, editWage);
		});
		editWage.setOnKeyPressed( (KeyEvent event) -> {
			if (!event.getCode().equals(KeyCode.ENTER))
				return;
			Double newWage = Double.parseDouble(editWage.getText());
			employee.setWage(newWage);
			wageWrap.getChildren().remove(1);
			wage.setText(String.format("%.2f", newWage));
			wageWrap.getChildren().add(1, wage);
			personTable.refresh();
		});
		
		wageWrap.getChildren().addAll(wageLbl, wage, new Label(" \\ h"));
		
		info.getChildren().addAll(titleWrap, wageWrap);
		
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
		if (email.toLowerCase().equals("na"))
			return true;
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
	private boolean validCity(String city, String street) {
		if (city.toLowerCase().equals("na") || street.toLowerCase().equals("na"))
			return true;
		if (city.equals(""))
			return false;
		if (city.matches(".*\\d+.*"))  //any digits
			return false;
		return true;
	}
	private boolean validZip(String zip, String street) {
		if (zip.toLowerCase().equals("na") || street.toLowerCase().equals("na"))
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
	private boolean validWage(String wage) {
		if (CUSTOMER)
			return true;
		try {
			Double.parseDouble(wage);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
