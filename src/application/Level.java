package application;

import java.io.IOException;
import java.util.LinkedList;

import controllers.LevelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Level {
	private Scene scene;
	private Stage mainStage;
	private static String sysPath = "file:/home/a/eclipse-workspace/Jbomber/src/resources/";
	public static Image grass = new Image(sysPath+"grass.png", 50, 50, false, true),
						border = new Image(sysPath+"barrier.png", 50, 50, false, true),
						wall = new Image(sysPath+"wall.png", 50, 50, false, true),
	 					bomb = new Image(sysPath+"bomb.png", 50, 50, false, true),
						boom = new Image(sysPath+"boom.png", 50, 50, false, true),
						walker = new Image(sysPath+"enemy2-static.png", 50, 50, false, true),
						bomber = new Image(sysPath+"enemy1-static.png", 50, 50, false, true),
						playerImg = new Image(sysPath+"player-static.png", 50, 50, false, true),
						playerDeath1 = new Image(sysPath+"player-death-1.png", 50, 50, false, true),
						playerDeath2 = new Image(sysPath+"player-death-2.png", 50, 50, false, true);

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

		LinkedList<Enemy> enemies = new LinkedList<Enemy>();

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
