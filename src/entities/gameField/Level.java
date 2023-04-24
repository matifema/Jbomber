package entities.gameField;

import java.io.IOException;

import entities.audio.AudioManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;


public class Level {
	private LevelController controller;
	private Scene scene;

	public Level() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Level.class.getResource("Level.fxml"));
		Parent root = fxmlLoader.load();
		
		controller = fxmlLoader.getController();
		
		controller.initLives();
		controller.populateSpace();
		controller.renderBorder();
		controller.renderWalls();
		controller.renderPlayer();
		
		scene = new Scene(root);
		startKeyHandler(scene);
	}

	private void startKeyHandler(Scene scene) {
		// Key Handler
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
			}
			if (event.getCode() == KeyCode.E) {
				controller.placeBomb();
			}
			if (event.getCode() == KeyCode.W) {
				controller.movePlayer(0, -1);
			}
			if (event.getCode() == KeyCode.A) {
				controller.movePlayer(-1, 0);
			}
			if (event.getCode() == KeyCode.S) {
				controller.movePlayer(0, 1);
			}
			if (event.getCode() == KeyCode.D) {
				controller.movePlayer(1, 0);
			}
		});
	}
	
	public Scene getScene() {
		return scene;
	}
	
}
