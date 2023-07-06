package controllers;

import java.io.IOException;

import application.Level;
import application.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Controller Class for EndScreen.
 */
public class EndScreenController {
	@FXML
	Text title;
	@FXML
	Text yes;
	@FXML
	Text no;
	@FXML
	AnchorPane pane;

	private int selected = 1;

	/**
	 * Creates new instance of EndScreenController 
	 */
	public EndScreenController() {
	}

	/**
	 * Selects "yes" on screen using javafx's setStyle.
	 */
	public void selectYes() {
		yes.setStyle("-fx-fill:white;");
		no.setStyle("-fx-fill:black;");

		selected = 1;
	}

	/**
	 * Selects "no" on screen using javafx's setStyle.
	 */
	public void selectNo() {
		yes.setStyle("-fx-fill:black;");
		no.setStyle("-fx-fill:white;");

		selected = 0;
	}

	/**
	 * Either continue playing or go to menu.
	 * if yes selected -> new level
	 * if no selected -> main menu
	 * @throws IOException
	 */
	public void selected() throws IOException {
		if (selected == 1) {
			new Level();
		} else {
			new MainMenu();
		}
	}

	/**
	 * Sets title
	 * @param title
	 */
	public void setText(String title) {
		this.title.setText(title);
	}
}
