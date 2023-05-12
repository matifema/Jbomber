package application;

import java.io.IOException;
import java.util.List;

import controllers.LevelController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Level {
	private Scene scene;
	private Stage mainStage;
	private Timeline enemyMovementTimeline, enemyBombsTimeline;

	public Level(Stage stage) throws IOException {
		this.mainStage = stage;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/Level.fxml"));
		Parent root = fxmlLoader.load();
		LevelController controller = fxmlLoader.getController();
		controller.setStage(stage);

		scene = new Scene(root);
		mainStage.setScene(scene);

		Player player = new Player(controller.getMap(), this);

		controller.populateSpace();
		controller.renderBorder();
		controller.renderWalls();

		controller.setPlayer(player);
		controller.renderPlayer(player);

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
		Enemy bomber = new Enemy(controller.getMap(), controller, "bomber");
		controller.renderEnemies(bomber);
	
		Enemy walker = new Enemy(controller.getMap(), controller, "walker");
		controller.renderEnemies(walker);
	
		List<Enemy> enemies = List.of(walker, bomber);

		controller.setEnemies(enemies);
	
		// Create a Timeline to move the enemies randomly
		this.enemyMovementTimeline = new Timeline(new KeyFrame(Duration.millis(800), event -> {
			enemies.forEach(e -> {
				e.moveEnemy();
				controller.checkPlayerDamage(e.currentX, e.currentY);
			});
		}));
		this.enemyMovementTimeline.setCycleCount(Timeline.INDEFINITE);
		this.enemyMovementTimeline.play();
	
		// Create a Timeline to place bombs
		this.enemyBombsTimeline = new Timeline(new KeyFrame(Duration.millis(3500), event -> {
			enemies.stream()
					.filter(e -> e.enemyType.equals("bomber"))
					.forEach(e -> {
						e.placeBomb();
						e.moveEnemy();
						controller.checkPlayerDamage(e.currentX, e.currentY);
					});
		}));
	
		this.enemyBombsTimeline.setCycleCount(Timeline.INDEFINITE);
		this.enemyBombsTimeline.play();
	}
	

	public void playerDied() {
		try {
			new EndScreen(this.mainStage, "GAME OVER");

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.enemyBombsTimeline.stop();
		this.enemyMovementTimeline.stop();
	}

	public Scene getScene() {
		return scene;
	}

}
