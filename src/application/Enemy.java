package application;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import controllers.LevelController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

enum EnemyType{
	WALKER,
	BOMBER
}

/**
 * Enemy object class.
 */
public class Enemy {
	private ImageView enemyBox;
	private Image enemyImg;
	private LevelController levelController;
	public EnemyType enemyType;
	public Integer currentX, currentY;
	private Timeline enemyMovementTimeline;
	private Timeline enemyBombsTimeline;
	private boolean isAlive;

	/**
	 * Creates new enemy.
	 * @param controller
	 * @param type <- ("bomber" / "walker")
	 */
	public Enemy(LevelController controller, EnemyType type) {
		this.isAlive = true;
		this.enemyBox = new ImageView(enemyImg);
		this.enemyBox.setFitHeight(50);
		this.enemyBox.setFitWidth(50);
		this.levelController = controller;
		this.enemyType = type;
		this.currentX = 0;
		this.currentY = 0;

		if (type == EnemyType.BOMBER) {
			this.enemyImg = Level.bomber;
		} else {
			this.enemyImg = Level.walker;
		}
	}

	/**
	 * Spawns enemy in a free space in the map.
	 * @param levelMap
	 */
	public void spawnEnemy(HashMap<List<Integer>, ImageView> levelMap) {
		this.enemyBox = levelMap.get(List.of(currentX, currentY));
		this.enemyBox.setImage(this.enemyImg);
		this.enemyBox.setId("enemy");
		startBehaviour(levelMap);
	}

	/**
	 * Tries to move the enemy in a random cardinal direction.
	 * If the chosen move is not valid the enemy does not move. 
	 * @param levelMap
	 */
	public void moveEnemy(HashMap<List<Integer>, ImageView> levelMap) {
		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		int[] randomDirection = directions[new Random().nextInt(directions.length)];
		int randomX = randomDirection[0], randomY = randomDirection[1];

		if (isMoveValid(randomX, randomY)) {
			if (this.enemyBox.getId().equals("enemy")) {
				this.enemyBox.setImage(null);
				this.enemyBox.setId("");
			}
			this.enemyBox = levelMap.get(List.of(currentX + randomX, currentY + randomY));
			this.enemyBox.setImage(this.enemyImg);
			this.enemyBox.setId("enemy");

			this.currentX += randomX;
			this.currentY += randomY;
		}
	}

	/**
	 * Places bomb on current coordinates.
	 */
	public void placeBomb() {
		this.levelController.placeBomb("enemy", this.currentX, this.currentY);
		this.moveEnemy(this.levelController.getMap());
	}

	/**
	 * Returns true if move is valid.
	 * Checks if the tile we want to move to:
	 * 	- is within the map border.
	 * 	- is free (id null).
	 * @param deltaX
	 * @param deltaY
	 * @param levelMap
	 * @return
	 */
	public boolean isMoveValid(int deltaX, int deltaY) {
		if (currentX + deltaX > 17 || currentY + deltaY > 16 || currentX + deltaX < 0 || currentY + deltaY < 0) {
			return false;
		}

		return this.levelController.getMap()
					.get(List.of(currentX + deltaX, currentY + deltaY)).getId().equals("");
	}

	/**
	 * Starts enemy behaviour timeline (walking and placing bombs if bomber).
	 * @param levelMap
	 */
	public void startBehaviour(HashMap<List<Integer>, ImageView> levelMap) {

		// Create a Timeline to move the enemies randomly
		this.enemyMovementTimeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
			moveEnemy(levelController.getMap());
			levelController.checkPlayerDamage(currentX, currentY);

		}));
		this.enemyMovementTimeline.setCycleCount(Timeline.INDEFINITE);
		this.enemyMovementTimeline.play();

		if (this.enemyType == EnemyType.BOMBER) {
			// Create a Timeline to place bombs
			this.enemyBombsTimeline = new Timeline(new KeyFrame(Duration.millis(5000), event -> {
				placeBomb();

			}));
			this.enemyBombsTimeline.setCycleCount(Timeline.INDEFINITE);
			this.enemyBombsTimeline.play();
		}
	}

	/**
	 * Stops enemy behaviour timeline.
	 */
	public void stopBehaviour() {
		
		this.enemyMovementTimeline.stop();

		if(this.enemyType == EnemyType.BOMBER){
			this.enemyBombsTimeline.stop();
		}

		System.out.println("-- enemy timelines stopped");
	}

	/**
	 * Die Event.
	 * Stops timeline and shows 2 frame death "animation".
	 */
	public void die() {

		if (!isAlive) {
			return; // Se il nemico non è vivo, non facciamo nulla
		}

		isAlive = false;

		Image death1 = new Image(getClass().getResourceAsStream("/resources/death1-" + this.enemyType.toString() + ".png"));
		Image death2 = new Image(getClass().getResourceAsStream("/resources/death2-" + this.enemyType.toString() + ".png"));

		stopBehaviour();
		this.levelController.getMap().get(List.of(currentX, currentY)).setId("");

		enemyBox.setImage(death1);

		// Cambio l'immagine a 'death2' dopo 100 millisecondi
		Timeline deathAnimation = new Timeline();
		deathAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(100), event -> {
			enemyBox.setImage(death2);
		}));

		// Rimuovo l'immagine dopo altri 100 millisecondi
		deathAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(200), event -> {
			this.enemyBox.setImage(null);
			this.enemyBox.setId("");
		}));

		deathAnimation.play();
	}

}
