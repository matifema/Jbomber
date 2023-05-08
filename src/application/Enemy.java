package application;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import controllers.LevelController;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Enemy {
	private ImageView enemyBox;
	private Image enemyImg;
	private LevelController levelController;
	private String bomber = "/resources/enemy1-static.png", walker = "/resources/enemy2-static.png";
	private HashMap<List<Integer>, ImageView> levelMap;
	public int currentX, currentY;
	public String enemyType;

	public Enemy(HashMap<List<Integer>, ImageView> levelMap, LevelController controller, String type) {
		this.levelController = controller;
		this.levelMap = levelMap;
		this.enemyType = type;
		this.currentX = 0;
		this.currentY = 0;

		if (type.equals("bomber")) {
			this.enemyImg = new Image(getClass().getResourceAsStream(bomber));
		}
		if (type.equals("walker")) {
			this.enemyImg = new Image(getClass().getResourceAsStream(walker));
		}

		spawnEnemy();
	}

	public void spawnEnemy() {
		this.enemyBox = new ImageView(enemyImg);
		this.enemyBox.setFitHeight(50);
		this.enemyBox.setFitWidth(50);

		TranslateTransition translate = new TranslateTransition();
		translate.setNode(this.enemyBox);

		translate.setByX((-17 + currentX) * 50);
		translate.setByY((currentY - (this.levelController.getTilePane().getChildren().size() - 272)) * 50);  // TODO Fixa a morte enemy

		translate.setDuration(Duration.millis(1));
		translate.play();
	}

	public void moveEnemy() {
		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		int[] randomDirection = directions[new Random().nextInt(directions.length)];
		int randomX = randomDirection[0], randomY = randomDirection[1];

		if (isMoveValid(randomX, randomY)) {			
			move(randomX, randomY);
		}
	}

	public void placeBomb() {
		this.levelController.placeBomb("enemy", this.currentX, this.currentY);
	}

	public void move(int x, int y) {
		TranslateTransition translate = new TranslateTransition();
		translate.setNode(this.enemyBox);

		currentX += x;
		currentY += y;

		translate.setByX(x * 50);
		translate.setByY(y * 50);
		translate.setDuration(Duration.millis(1));
		translate.play();

	}

	public boolean isMoveValid(int deltaX, int deltaY) {
		if (currentX + deltaX > 17 || currentY + deltaY > 16 || currentX + deltaX < 0 || currentY + deltaY < 0) {
			return false;
		}

		return this.levelMap.get(List.of(currentX + deltaX, currentY + deltaY)).getImage() == null;
	}

	public void deathAnimation() {
		Image death1 = new Image(getClass().getResourceAsStream("/resources/death1-" + this.enemyType + ".png" ));
		Image death2 = new Image(getClass().getResourceAsStream("/resources/death2-" + this.enemyType + ".png" ));
		
		try {
			enemyBox.setImage(death1);
			Thread.sleep(200);
			enemyBox.setImage(death2);
			Thread.sleep(200);
			enemyBox.setImage(null);
		} catch (InterruptedException e) {e.printStackTrace();}
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

}
