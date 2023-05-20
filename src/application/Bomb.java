package application;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import controllers.LevelController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bomb {
	private String placedBy;
	public boolean isExploded, gameEnded = false;
	private LevelController lvl;
	private Integer destructedWalls = 0, x, y, expRadius;
	private HashMap<List<Integer>, ImageView> map;
	private AudioManager audio = new AudioManager();
	private Image bomb = new Image(getClass().getResourceAsStream("/resources/bomb.png")),
			boom = new Image(getClass().getResourceAsStream("/resources/boom.png"));

	public Bomb(LevelController level, int placedX, int placedY, String placedBy, int expR) {
		this.map = level.getMap();
		this.placedBy = placedBy;
		this.expRadius = expR;
		this.lvl = level;
		this.x = placedX;
		this.y = placedY;

		// place bomb
		map.get(List.of(x, y)).setImage(this.bomb);
		map.get(List.of(x, y)).setId("bomb");
		startCountDown();
		isExploded = false;

	}

	public void startCountDown() {
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				isExploded = true;
				boom();
			}
		}, 1500);
	}

	private void boom() {
		this.audio.playBoom();
		explosion(this.lvl.getNearTiles(x, y, expRadius));
	}

	private void explosion(List<ImageView> nearTiles) {

		this.lvl.getMap().get(List.of(this.x, this.y)).setImage(boom);

		for (ImageView tile : nearTiles) {
			int tileX = (int) (tile.getLayoutX() / 50), tileY = (int) (tile.getLayoutY() / 50);

			if (tile.getId().equals("")) {
				this.lvl.getMap().get(List.of(tileX, tileY)).setImage(boom);
			}

			if (tile.getId().equals("wall")) {
				this.lvl.getMap().get(List.of(tileX, tileY)).setImage(boom);

				if (this.placedBy.equals("player")) {
					spawnPowerUp(tile);
					this.destructedWalls++;
				}
			}

			if (tile.getId().equals("enemy") && this.placedBy.equals("player")) {
				this.gameEnded = this.lvl.checkEnemyDamage(tileX, tileY);
				if (this.gameEnded) {
					break;
				}
			}

			if (tile.getId().equals("player")) {
				this.gameEnded = this.lvl.checkPlayerDamage(tileX, tileY);

				if (this.gameEnded) {
					break;
				}
			}

		}

		if (!gameEnded) {
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					clear(nearTiles);
					updateLevelScore();
				}

			}, 200);
		}

	}

	private void spawnPowerUp(ImageView tile) {
		int rand = new Random().nextInt(0, 3);

		if (rand == 0) {
			String type = "";
			rand = new Random().nextInt(0, 3);

			switch (rand) {
				case 0:
					type += "life";
					break;
				case 1:
					type += "bomb";
					break;
				case 2:
					type += "golden";
					break;
				default:
					break;
			}
			this.lvl.powerUps.add(
					new PowerUp(this.lvl, (int) tile.getLayoutX() / 50, (int) tile.getLayoutY() / 50, type));

			System.out.println("-- spawning.. " + type);
		}
	}

	private void updateLevelScore() {
		this.lvl.addScore(destructedWalls * 100);
	}

	private void clear(List<ImageView> nearTiles) {
		for (ImageView tile : nearTiles) {
			if (tile.getImage() != null && tile.getImage().equals(boom)) {
				tile.setImage(null);
				tile.setId("");
			}
			tile.setEffect(null);
		}
	}

}
