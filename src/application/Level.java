package application;

import java.io.IOException;

import controllers.LevelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import util.timer.*;
import javafx.stage.*;

public class Level {
	private Scene scene;
	
	public Level() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/Level.fxml"));
		Parent root = fxmlLoader.load();
		scene = new Scene(root);

		
		LevelController controller = fxmlLoader.getController();
		controller.populateSpace();
		controller.renderBorder();
		controller.renderWalls();
		Player player = new Player(0, 0, controller.getMap(), this);
		controller.setPlayer(player);
		controller.renderPlayer(player);
		

		startKeyHandler(scene, controller);
	}

	private void startKeyHandler(Scene scene, LevelController controller) {
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
	
	public void gameOver() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/MainMenu.fxml"));
		Parent root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.scene = new Scene(root);
	}
	
    public void playerDied() {
        try {
            GameOver gameOverScreen = new GameOver();
            Stage currentStage = (Stage) scene.getWindow();
            currentStage.setScene(gameOverScreen.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
