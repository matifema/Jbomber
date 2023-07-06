package application;

import java.util.HashMap;
import java.util.List;

import controllers.LevelController;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Player {
	private Level level;
	private ImageView playerBox;
	public Integer currentX, currentY;
	private HashMap<List<Integer>, ImageView> levelMap;
	private LevelController levelcontroller;

	public Player(LevelController controller, Level level) {
		this.playerBox = new ImageView(Level.playerImg);
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
		this.levelMap.get(List.of(currentX, currentY)).setImage(Level.playerImg);
		this.levelMap.get(List.of(currentX, currentY)).setId("player");
	}

	public void dieEvent() {
		this.playerBox.setImage(Level.playerDeath1);

		PauseTransition pause = new PauseTransition(Duration.millis(700));
		pause.setOnFinished(event -> {
			this.playerBox.setImage(Level.playerDeath2);
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
		int oldX = this.currentX;
		int oldY = this.currentY;
		this.currentX += x;
		this.currentY += y;
	
		// cancella vecchia posizione player
		if (levelMap.get(List.of(oldX, oldY)).getImage().equals(Level.playerImg) ||
			levelMap.get(List.of(oldX, oldY)).getId().equals("player")) {
			
			levelMap.get(List.of(oldX, oldY)).setImage(null);
			levelMap.get(List.of(oldX, oldY)).setId("");
		}
	
		// piazza player a nuova posizione
		levelMap.get(List.of(currentX, currentY)).setImage(Level.playerImg);
		levelMap.get(List.of(currentX, currentY)).setId("player");
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

	public Scene getScene() {
		return null;
	}

}