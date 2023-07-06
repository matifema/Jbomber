package application;

import java.util.HashMap;
import java.util.List;

import controllers.LevelController;
import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Player Object Class.
 */
public class Player {
	private Level level;
	private ImageView playerBox;
	public Integer currentX, currentY;
	private HashMap<List<Integer>, ImageView> levelMap;
	private LevelController levelcontroller;

	/**
	 * Creates new instance of a Player.
	 * Loads img, map and position.
	 * @param controller
	 * @param level
	 */
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

	/**
	 * Spawns player in map @ currentX, currentY position
	 */
	public void spawnPlayer() {
		this.levelMap.get(List.of(currentX, currentY)).setImage(Level.playerImg);
		this.levelMap.get(List.of(currentX, currentY)).setId("player");
	}

	/**
	 * Die Event.
	 * Plays little 2 frame animation.
	 */
	public void dieEvent() {
		this.playerBox.setImage(Level.playerDeath1);

		PauseTransition pause = new PauseTransition(Duration.millis(700));
		pause.setOnFinished(event -> {
			this.playerBox.setImage(Level.playerDeath2);
			die();
		});
		pause.play();
	}

	/**
	 * Called by dieEvent.
	 * Removes player from map,
	 * Informs the level of the death of the player,
	 * Stops enemy timelines.
	 */
	private void die() {
		this.levelMap.get(List.of(currentX, currentY)).setImage(null);
		this.level.playerDied();
		this.levelcontroller.stopEnemyTimeline();
	}

	/**
	 * Moves the player to the selected cardinal tile.
	 * Removes old player img from position in map and replaces it with new one.
	 * @param levelMap
	 * @param x <- delta X
	 * @param y	<- delta Y
	 */
	public void movePlayer(HashMap<List<Integer>, ImageView> levelMap, int x, int y) {
		int oldX = this.currentX;
		int oldY = this.currentY;
		this.currentX += x;
		this.currentY += y;
	
		// removes old player pos
		if (levelMap.get(List.of(oldX, oldY)).getImage().equals(Level.playerImg) ||
			levelMap.get(List.of(oldX, oldY)).getId().equals("player")) {
			
			levelMap.get(List.of(oldX, oldY)).setImage(null);
			levelMap.get(List.of(oldX, oldY)).setId("");
		}
	
		// updates player pos
		levelMap.get(List.of(currentX, currentY)).setImage(Level.playerImg);
		levelMap.get(List.of(currentX, currentY)).setId("player");
	}
	
	/**
	 * Returns true if move is valid.
	 * Checks if the tile we want to move to:
	 * 	- is within the map border.
	 * 	- is free or has a powerup to pick up.
	 * @param deltaX
	 * @param deltaY
	 * @return
	 */
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

	/**
	 * Returns player's ImageView
	 * @return
	 */
	public ImageView getPlayerNode() {
		return this.playerBox;
	}

	/**
	 * Returns player's current X
	 * @return
	 */
	public int getX() {
		return this.currentX;
	}

	/**
	 * Returns player's current Y
	 * @return
	 */
	public int getY() {
		return this.currentY;
	}

}