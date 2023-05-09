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

	public Scene select() throws IOException {
		switch (selected) {
			case 1:
				if (checkForPlayerData()){
					Level level = new Level();
					return level.getScene();
				}
				else{
					return getAndSaveData();
				}
			
			case 2:
				Stats stats = new Stats();
				return stats.getScene();
			
			case 3: // quit
				System.exit(0);
				return null;
			
			default:
				return null;
		}
	}


	private Scene getAndSaveData() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/NewGame.fxml"));
		Parent root = fxmlLoader.load();
		NewGameController controller = fxmlLoader.getController();
		Scene scene = new Scene(root);
		
		controller.startKeyHandler(scene);
		
		return scene;
	}

	public boolean checkForPlayerData(){
		ReadFromFile read = new ReadFromFile();

		return (read.getData().get(0) != null);
	}
}
