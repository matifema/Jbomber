package entities.game;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class GameBoard {
	private Scene scene;
	
	public GameBoard() throws IOException{
		// loading MENU fxml and css 
		FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource("GameBoard.fxml"));
		Parent root = fxmlLoader.load();
		GameController controller = fxmlLoader.getController();
		
		controller.drawHeading();
		controller.drawLevel();
		controller.getReady();
		controller.startCountDown();
		
		// set scene
		this.scene = new Scene(root);
		
		
		//DEBUG
		this.scene.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER) {
				controller.clearLevel();
				controller.drawLevel();
			}
		});
	}
	
	public Scene getScene() {
		return this.scene;
	}
}
