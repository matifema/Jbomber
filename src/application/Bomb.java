package application;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controllers.LevelController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bomb {
	private AudioManager audio = new AudioManager();
	private HashMap<List<Integer>, ImageView> map;
	private int destructedWalls = 0, x, y;
	public boolean isExploded;
	private LevelController lvl;
	private Image boom;
	private String placedBy;
	private int expRadius;
	private List<ImageView> spawnableTiles = new ArrayList<>();

	public Bomb(LevelController level, int placedX, int placedY, String placedBy, int expR) {
		this.boom = new Image(getClass().getResourceAsStream("/resources/boom.png"));
		this.map = level.getMap();
		this.placedBy = placedBy;
		this.expRadius = expR;
		this.lvl = level;
		this.x = placedX;
		this.y = placedY;

		// place bomb
		if (map.get(List.of(x, y)).getImage() == null) {
			map.get(List.of(x, y)).setImage(new Image(getClass().getResourceAsStream("/resources/bomb.png")));
			map.get(List.of(x, y)).setId("bomb");
			startCountDown();
			isExploded = false;
		}
	}

	public void startCountDown() {
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				boom();
				isExploded = true;
			}
		}, 3000);
	}

	private void boom() {
		this.audio.playBoom();
		explosion(this.lvl.getNearTiles(x, y, expRadius));
	}

	private void explosion(List<ImageView> nearTiles) {
		for (ImageView tile : nearTiles) {
			if (tile.getImage() == null || (tile.getLayoutX() / 50 == x && tile.getLayoutY() / 50 == y)) {
				
				this.lvl.checkPlayerDamage(tile.getLayoutX() / 50, tile.getLayoutY() / 50);
				
				if(this.placedBy.equals("player")) {
					this.lvl.checkEnemyDamage(tile.getLayoutX() / 50, tile.getLayoutY() / 50);					
				}
				
				tile.setImage(boom);
				tile.setId(null);
			}
			else {
				tile.setImage(boom);
				if(this.placedBy.equals("player")) {
					this.destructedWalls++;
					// aggiungo ex wall tile a lista per spawnare powerup
					this.spawnableTiles.add(tile);
				}
			}
		}

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				clear(nearTiles);
				updateLevelScore();
			}

		}, 200);

	}

	private void spawnPowerUp(ImageView tile) {
		int rand = new Random().nextInt(0, 3);
		
		if(rand == 0) {
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
			
			tile.setId("powerup"); // setta id come powerup
			System.out.println("spawning.. " + type);

			new PowerUp(this.lvl, (int)tile.getLayoutX()/50, (int)tile.getLayoutY()/50, type);
		}
	}
	
	private void updateLevelScore() {
		this.lvl.addScore(destructedWalls * 100);
	}

	private void clear(List<ImageView> nearTiles) {

		for (ImageView wall : nearTiles) {
			if (wall.getImage() != null) {
				if (wall.getImage().equals(boom)) {
					wall.setImage(null);
				}
				if (this.spawnableTiles.contains(wall)) {
					spawnPowerUp(wall);
					wall.setId("powerup");
				}
			}
		}
	}

}
