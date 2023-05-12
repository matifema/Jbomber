package application;

import java.util.HashMap;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Player {
	private Level level;
	private ImageView playerBox;
	public Integer currentX, currentY;
	private HashMap<List<Integer>, ImageView> levelMap;
	private Image   playerImg = new Image(getClass().getResourceAsStream("/resources/player-static.png")),
					playerDeath1 = new Image(getClass().getResourceAsStream("/resources/player-death-1.png")),
					playerDeath2 = new Image(getClass().getResourceAsStream("/resources/player-death-2.png"));

	public Player(HashMap<List<Integer>, ImageView> levelMap, Level level) {
		this.levelMap = levelMap;
		this.level = level;
		this.currentX = 0;
		this.currentY = 0;

		spawnPlayer();
	}

	public void spawnPlayer() {
		// spawn player outside level and translate over

		this.playerBox = new ImageView(playerImg);
		this.playerBox.setFitHeight(50);
		this.playerBox.setFitWidth(50);

		TranslateTransition translate = new TranslateTransition();
		translate.setNode(this.playerBox);

		translate.setByX((-17 + currentX) * 50);
		translate.setByY((currentY) * 50);

		translate.setDuration(Duration.millis(1));
		translate.play();
	}

	public void dieEvent() {
		this.playerBox.setImage(playerDeath1);

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					die();
				});
			}
		}, 800);
	}

	private void die() {
		this.playerBox.setImage(playerDeath2);
		level.playerDied();
	}

	public void movePlayer(int x, int y) {
		TranslateTransition translate = new TranslateTransition();
		translate.setNode(this.playerBox);

		currentX += x;
		currentY += y;

		translate.setByX(x * 50);
		translate.setByY(y * 50);
		translate.setDuration(Duration.millis(1));
		translate.play();

		System.out.println("x: " + currentX + " y: " + currentY);
	}

	public boolean isMoveValid(int deltaX, int deltaY) {
		if (currentX + deltaX > 17 || currentY + deltaY > 16 || currentX + deltaX < 0 || currentY + deltaY < 0) {
			return false;
		}

		return this.levelMap.get(List.of(currentX + deltaX, currentY + deltaY)).getImage() == null ||
				this.levelMap.get(List.of(currentX + deltaX, currentY + deltaY)).getId().equals("powerup");
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
		try {
			this.playerBox.setImage(null);
			Thread.sleep(50);
			this.playerBox.setImage(playerImg);
			Thread.sleep(50);
			this.playerBox.setImage(null);
			Thread.sleep(50);
			this.playerBox.setImage(playerImg);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Scene getScene() {
		return null;
	}

}