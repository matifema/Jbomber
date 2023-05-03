package controllers;

import java.io.IOException;

import application.AudioManager;
import application.Level;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.text.Text;

public class MenuController {
	@FXML
	Text newGame;
	@FXML
	Text goblinMode;
	@FXML
	Text playerProfile;

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

		changeTextColor(newGame, selected == 1 ? "red" : "black");
		changeTextColor(goblinMode, selected == 2 ? "red" : "black");
		changeTextColor(playerProfile, selected == 3 ? "red" : "black");
	}

	private void changeTextColor(Text text, String color) {
		text.setStyle("-fx-fill: " + color + ";");
	}

	public Scene select() throws IOException {
		switch (selected) {
		case 1:
			Level level = new Level();
			return level.getScene();
		case 2:
			return null;
		case 3:
			return null;
		default:
			return null;
		}
	}

}
