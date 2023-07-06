package controllers;

import java.io.IOException;

import application.AudioManager;
import application.JBomberMan;
import application.Level;
import application.Stats;
import application.save.GameData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;

/**
 * Controller for MainMenu class.
 */
public class MenuController {
	@FXML
	Text newGame;
	@FXML
	Text playerProfile;
	@FXML
	Text quit;

	private AudioManager audio = new AudioManager();
	private int selected = 1;

	/**
	 * Creates new instance of MenuController
	 */
	public MenuController() {}

	/**
	 * Changes the selected Text.
	 * Selects the Text directly above (or wraps to bottom).
	 */
	public void up() {
		if (selected == 1 || selected == 0) {
			selected = 4;
		}
		selected--;

		updateSelection();
	}

	/**
	 * Changes the selected Text.
	 * Selects the Text directly below (or wraps to top).
	 */
	public void down() {
		if (selected == 3) {
			selected = 0;
		}
		selected++;

		updateSelection();
	}

	/**
	 * Updates screen after changing selection
	 */
	public void updateSelection() {
		audio.playSelect();

		changeTextColor(newGame, selected == 1 ? "white" : "black");
		changeTextColor(playerProfile, selected == 2 ? "white" : "black");
		changeTextColor(quit, selected == 3 ? "white" : "black");
	}

	/**
	 * Changes text color
	 * @param text
	 * @param color <- used in javafx setstyle
	 */
	private void changeTextColor(Text text, String color) {
		text.setStyle("-fx-fill: " + color + ";");
	}

	/**
	 * Enters selected text. (new game/stats/quit)
	 * if gamedata is empty, loads up the NewGame screen.
	 * 
	 * @throws IOException
	 */
	public void select() throws IOException {
		switch (selected) {
			case 1: // new game
				if (!isDataSaved()) {
					new Level();
				} else {
					setPlayerInfo();
				}
				break;

			case 2: // stats
				if (!isDataSaved()) {
					new Stats();
				} else {
					setPlayerInfo();
				}
				break;

			case 3: // quit
				System.exit(0);
				break;

			default:
				break;
		}
	}

	/**
	 * Starts up the NewGame screen
	 * @throws IOException
	 */
	private void setPlayerInfo() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/NewGame.fxml"));
		Parent root = fxmlLoader.load();
		NewGameController controller = fxmlLoader.getController();
		Scene scene = new Scene(root);
		controller.startKeyHandler(scene);

		JBomberMan.stage.setScene(scene);

	}

	/**
	 * Checks for gamedata saved.
	 * @return
	 */
	public boolean isDataSaved() {
		GameData read = new GameData();

		return (read.getData().get(2) == null);
	}
}
