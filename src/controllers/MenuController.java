package controllers;

import java.io.IOException;

import application.AudioManager;
import application.Level;
import application.Stats;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import save.ReadFromFile;

public class MenuController {
	@FXML
	Text newGame;
	@FXML
	Text playerProfile;
	@FXML
	Text quit;

	private AudioManager audio = new AudioManager();
	private int selected = 1;

	public MenuController() {
	}

	public void up() {
		if (selected == 1 || selected == 0) {
			selected = 4;
		}
		selected--;

		updateSelection();
	}

	public void down() {
		if (selected == 3) {
			selected = 0;
		}
		selected++;

		updateSelection();
	}

	public void updateSelection() {
		audio.playSelect();

		changeTextColor(newGame, selected == 1 ? "white" : "black");
		changeTextColor(playerProfile, selected == 2 ? "white" : "black");
		changeTextColor(quit, selected == 3 ? "white" : "black");
	}

	private void changeTextColor(Text text, String color) {
		text.setStyle("-fx-fill: " + color + ";");
	}

	public void select(Stage mainStage) throws IOException {
		switch (selected) {
			case 1:
				if (!isDataSaved()) {
					new Level(mainStage);
				} else {
					setPlayerInfo(mainStage);
				}
				break;

			case 2:
				if (!isDataSaved()) {
					new Stats(mainStage);
				} else {
					setPlayerInfo(mainStage);
				}
				break;

			case 3: // quit
				System.exit(0);
				break;

			default:
				break;
		}
	}

	private void setPlayerInfo(Stage mainStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/NewGame.fxml"));
		Parent root = fxmlLoader.load();
		NewGameController controller = fxmlLoader.getController();
		Scene scene = new Scene(root);
		controller.setStage(mainStage);
		controller.startKeyHandler(scene);

		mainStage.setScene(scene);

	}

	public boolean isDataSaved() {
		ReadFromFile read = new ReadFromFile();

		return (read.getData().get(2) == null);
	}
}
