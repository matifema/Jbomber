package application;

import java.io.IOException;
import java.util.LinkedList;

import controllers.LevelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

/**
 * Level Object class.
 */
public class Level {
	private Scene scene;
	public static int levelNum;
	private static String sysPath = "file:/home/a/eclipse-workspace/Jbomber/src/resources/";
	
	public static Image grass 		 = new Image(sysPath+"grass.png", 50, 50, false, true),
						border 		 = new Image(sysPath+"barrier.png", 50, 50, false, true),
						wall 		 = new Image(sysPath+"wall.png", 50, 50, false, true),
	 					bomb 		 = new Image(sysPath+"bomb.png", 50, 50, false, true),
						boom 		 = new Image(sysPath+"boom.png", 50, 50, false, true),
						walker 		 = new Image(sysPath+"enemy2-static.png", 50, 50, false, true),
						bomber 	     = new Image(sysPath+"enemy1-static.png", 50, 50, false, true),
						playerImg 	 = new Image(sysPath+"player-static.png", 50, 50, false, true),
						playerDeath1 = new Image(sysPath+"player-death-1.png", 50, 50, false, true),
						playerDeath2 = new Image(sysPath+"player-death-2.png", 50, 50, false, true);

	/**
	 * Creates new level.
	 * Loads Fxml, gets controller and renders map, borders, walls and entities.
	 * @throws IOException
	 */
	public Level() throws IOException {
		Level.levelNum ++;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/Level.fxml"));
		Parent root = fxmlLoader.load();
		LevelController controller = fxmlLoader.getController();

		scene = new Scene(root);
		JBomberMan.stage.setScene(scene);

		Player player = new Player(controller, this);

		controller.populateSpace();
		controller.renderBorder();
		controller.renderWalls();
		controller.renderEntity(player);


		generateEnemies(controller);
		startKeyHandler(scene, controller);
	}

	/**
	 * Starts Key Handler for the scene.
	 * Movement: WASD
	 * Placing Bombs: SPACE
	 * @param scene
	 * @param controller
	 */
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

	/**
	 * Spawns enemies.
	 * Every 2nd level the enemies spawned double.
	 * @param controller
	 */
	private void generateEnemies(LevelController controller) {

		for(int i=0; i<Level.levelNum; i++){
			if(i%2 == 0){
				Enemy walker = new Enemy(controller, EnemyType.WALKER);
				Enemy bomber = new Enemy(controller, EnemyType.BOMBER);
		
				LinkedList<Enemy> enemies = new LinkedList<Enemy>();
		
				enemies.add(walker);
				enemies.add(bomber);
				
				controller.setEnemies(enemies);
			}
		}
	}

	/**
	 * Player Died Event.
	 * Displays end screen "GAME OVER".
	 */
	public void playerDied() {
		try {
			new EndScreen("GAME OVER");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
