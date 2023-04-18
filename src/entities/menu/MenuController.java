package entities.menu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import java.io.IOException;

import entities.gameField.Level;

public class MenuController {
	@FXML Text newGame;
	@FXML Text goblinMode;
	@FXML Text playerProfile;
	
	int selected = 0;
	
	public MenuController() {
	}
	
	public void up() {
		if(selected == 1 || selected == 0) {
			selected = 4;
		}
		selected--;
		
		updateSelection();		
	}
	
	public void down() {
		if(selected == 3) {
			selected = 0;
		}
		selected++;
		
		updateSelection();		
	}
	
	public void updateSelection() {
		switch (selected) {
		case 1: // new game
			changeTextColor(newGame, "red");
			changeTextColor(goblinMode, "black");
			changeTextColor(playerProfile, "black");
			break;
			
		case 2: // goblin mode
			changeTextColor(newGame, "black");
			changeTextColor(goblinMode, "red");
			changeTextColor(playerProfile, "black");
			break;
			
		case 3: // player profile
			changeTextColor(newGame, "black");
			changeTextColor(goblinMode, "black");
			changeTextColor(playerProfile, "red");
			break;
		default:
			// nd
				break;
		}
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
