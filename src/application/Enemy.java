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

public class Enemy {
	private ImageView enemyBox;
	private Image enemyImg;
	private LevelController levelController;
	private String bomber = "/resources/enemy1-static.png", walker = "/resources/enemy2-static.png";
	public String enemyType;
	public Integer currentX, currentY;
	private Timeline enemyMovementTimeline;
	private Timeline enemyBombsTimeline;
	private boolean isAlive;

	public Enemy(LevelController controller, String type) {
		this.isAlive = true;
		this.enemyBox = new ImageView(enemyImg);
		this.enemyBox.setFitHeight(50);
		this.enemyBox.setFitWidth(50);
		this.levelController = controller;
		this.enemyType = type;
		this.currentX = 0;
		this.currentY = 0;

		if (type.equals("bomber")) {
			this.enemyImg = new Image(getClass().getResourceAsStream(bomber));
		} else {
			this.enemyImg = new Image(getClass().getResourceAsStream(walker));
		}
	}

	public void spawnEnemy(HashMap<List<Integer>, ImageView> levelMap) {
		this.enemyBox = levelMap.get(List.of(currentX, currentY));
		this.enemyBox.setImage(this.enemyImg);
		this.enemyBox.setId("enemy");
		startBehaviour(levelMap);
	}

	public void moveEnemy(HashMap<List<Integer>, ImageView> levelMap) {
		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		int[] randomDirection = directions[new Random().nextInt(directions.length)];
		int randomX = randomDirection[0], randomY = randomDirection[1];

		if (isMoveValid(randomX, randomY, levelMap)) {
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

	public void placeBomb() {
		this.levelController.placeBomb("enemy", this.currentX, this.currentY);
		this.moveEnemy(this.levelController.getMap());
	}

	public boolean isMoveValid(int deltaX, int deltaY, HashMap<List<Integer>, ImageView> levelMap) {
		if (this.currentX + deltaX > 15 ||
				this.currentY + deltaY > 14 ||
				this.currentX + deltaX < 1 ||
				this.currentY + deltaY < 0) {
			return false;
		}

		boolean imageIsNull = levelMap.get(List.of(currentX + deltaX, currentY + deltaY)).getImage() == null;

		if (imageIsNull) {
			return true;
		}

		return false;
	}

	public void startBehaviour(HashMap<List<Integer>, ImageView> levelMap) {

		// Create a Timeline to move the enemies randomly
		this.enemyMovementTimeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
			moveEnemy(levelController.getMap());
			levelController.checkPlayerDamage(currentX, currentY);

		}));
		this.enemyMovementTimeline.setCycleCount(Timeline.INDEFINITE);
		this.enemyMovementTimeline.play();

		if (this.enemyType.equals("bomber")) {
			// Create a Timeline to place bombs
			this.enemyBombsTimeline = new Timeline(new KeyFrame(Duration.millis(5000), event -> {
				placeBomb();

			}));
			this.enemyBombsTimeline.setCycleCount(Timeline.INDEFINITE);
			this.enemyBombsTimeline.play();
		}
	}

	public void die() {

		if (!isAlive) {
			return; // Se il nemico non è vivo, non facciamo nulla
		}

		isAlive = false;

		Image death1 = new Image(getClass().getResourceAsStream("/resources/death1-" + this.enemyType + ".png"));
		Image death2 = new Image(getClass().getResourceAsStream("/resources/death2-" + this.enemyType + ".png"));

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

	public ImageView getEnemyNode() {
		return this.enemyBox;
	}

	public int getX() {
		return this.currentX;
	}

	public int getY() {
		return this.currentY;
	}

	public void stopBehaviour() {
		
		this.enemyMovementTimeline.stop();

		if(this.enemyType.equals("bomber")){
			this.enemyBombsTimeline.stop();
		}

		System.out.println("-- enemy timelines stopped");
	}

}
