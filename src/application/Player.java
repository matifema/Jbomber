package application;

import java.util.HashMap;
import java.util.List;

import controllers.LevelController;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Player {
	private Level level;
	private ImageView playerBox;
	public Integer currentX, currentY;
	private HashMap<List<Integer>, ImageView> levelMap;
	private Image playerImg = new Image(getClass().getResourceAsStream("/resources/player-static.png")),
			playerDeath1 = new Image(getClass().getResourceAsStream("/resources/player-death-1.png")),
			playerDeath2 = new Image(getClass().getResourceAsStream("/resources/player-death-2.png"));
	private LevelController levelcontroller;

	public Player(LevelController controller, Level level) {
		this.playerBox = new ImageView(playerImg);
		this.playerBox.setFitHeight(50);
		this.playerBox.setFitWidth(50);
		this.levelcontroller = controller;
		this.levelMap = controller.getMap();
		this.level = level;
		this.currentX = 0;
		this.currentY = 0;

	}

	public Player() {
	}

	public void spawnPlayer() {
		this.levelMap.get(List.of(currentX, currentY)).setImage(playerImg);
		this.levelMap.get(List.of(currentX, currentY)).setId("player");
	}

	public void dieEvent() {
		this.playerBox.setImage(playerDeath1);

		PauseTransition pause = new PauseTransition(Duration.millis(800));
		pause.setOnFinished(event -> {
			this.playerBox.setImage(playerDeath2);
			die();
		});
		pause.play();
	}

	private void die() {
		this.levelMap.get(List.of(currentX, currentY)).setImage(null);
		this.level.playerDied();
		this.levelcontroller.stopEnemyTimeline();
	}

	public void movePlayer(HashMap<List<Integer>, ImageView> levelMap, int x, int y) {
		if (levelMap.get(List.of(currentX, currentY)).getId().equals("player")) {
			levelMap.get(List.of(currentX, currentY)).setImage(null);
			levelMap.get(List.of(currentX, currentY)).setId("");
		}
		levelMap.get(List.of(currentX + x, currentY + y)).setImage(playerImg);
		levelMap.get(List.of(currentX + x, currentY + y)).setId("player");
		this.currentX += x;
		this.currentY += y;
	}

	public boolean isMoveValid(int deltaX, int deltaY) {
		if (currentX + deltaX > 17 || currentY + deltaY > 16 || currentX + deltaX < 0 || currentY + deltaY < 0) {
			return false;
		}

		return this.levelMap.get(List.of(currentX + deltaX, currentY + deltaY)).getId().equals("") ||
				this.levelMap.get(List.of(currentX + deltaX, currentY + deltaY)).getId().equals("powerup");// ||
																											// collisione
																											// enemies
																											// disabilitata
		// this.levelMap.get(List.of(currentX + deltaX, currentY +
		// deltaY)).getId().equals("enemy");
	}

	public ImageView getPlayerNode() {
		return this.playerBox;
	}

	public int getX() {
		return this.currentX;
	}

	public int getY() {
		return this.currentY;
	}

	public void damageAnimation() {
		playerBox.setImage(playerDeath1);

		// Cambio l'immagine a 'death2' dopo 100 millisecondi
		Timeline deathAnimation = new Timeline();
		deathAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(100), event -> {
			playerBox.setImage(playerDeath2);
		}));

		// Rimuovo l'immagine dopo altri 100 millisecondi
		deathAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(200), event -> {
			this.playerBox.setImage(null);
			this.playerBox.setId("");
		}));

		deathAnimation.play();
	}

	public Scene getScene() {
		return null;
	}

}