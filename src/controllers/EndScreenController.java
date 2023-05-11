package controllers;

import java.io.IOException;

import application.Level;
import application.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EndScreenController {
	@FXML
	Text title;
	@FXML
	Text yes;
	@FXML
	Text no;
	@FXML
	AnchorPane pane;

	private Stage mainStage;
	private int selected = 1;

	public EndScreenController() {
	}

	public void selectYes() {
		yes.setStyle("-fx-fill:white;");
		no.setStyle("-fx-fill:black;");

		selected = 1;
	}

	public void selectNo() {
		yes.setStyle("-fx-fill:black;");
		no.setStyle("-fx-fill:white;");

		selected = 0;
	}

	public void selected() throws IOException {
		if (selected == 1) {
			new Level(this.mainStage);
		} else {
			new MainMenu(this.mainStage);
		}
	}

	public void setStage(Stage stage) {
		this.mainStage = stage;
	}

	public void setText(String title) {
		this.title.setText(title);
	}
}
