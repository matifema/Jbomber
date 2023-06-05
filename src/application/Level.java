package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controllers.LevelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Level {
	private Scene scene;
	private Stage mainStage;

	public Level(Stage stage) throws IOException {
		this.mainStage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/Level.fxml"));
		Parent root = fxmlLoader.load();
		LevelController controller = fxmlLoader.getController();
		controller.setStage(stage);

		scene = new Scene(root);
		mainStage.setScene(scene);

		Player player = new Player(controller, this);

		controller.populateSpace();
		controller.renderBorder();
		controller.renderWalls();
		controller.renderEntity(player);

		generateEnemies(controller);

		startKeyHandler(scene, controller);
	}

	private void startKeyHandler(Scene scene, LevelController controller) {
		// Key Handler
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.SPACE) {
				controller.placeBomb("player", 0, 0);
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

	private void generateEnemies(LevelController controller) {
		Enemy walker = new Enemy(controller, "walker");
		Enemy bomber = new Enemy(controller, "bomber");

		List<Enemy> enemies = new ArrayList<>();

		enemies.add(walker);
		enemies.add(bomber);

		controller.setEnemies(enemies);
	}

	public void playerDied() {
		try {
			new EndScreen(this.mainStage, "GAME OVER");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Scene getScene() {
		return scene;
	}

}
