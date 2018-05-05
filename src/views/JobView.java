package views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class JobView extends BorderPane {
	private final double WIDTH;  // width and height of itself
	private final double HEIGHT; // gotten from constructor

	
	public JobView(double height, double width) {
		this.HEIGHT = height;
		this.WIDTH 	= width;
		
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);
		
		setup();
	}
	
	public void reload() {
		
	}
	private void setup() {
		Label lbl = new Label("Blank Page");
		lbl.setFont(Font.font(32));
		lbl.setAlignment(Pos.CENTER);
		lbl.setPrefWidth(WIDTH);
		this.setCenter(lbl);
	}
}
