package application;

import java.util.HashMap;
import java.util.List;

import controllers.LevelController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bomb {
	private AudioManager audio = new AudioManager();
	private HashMap<List<Integer>, ImageView> map;
	private int destructedWalls = 0, x, y;
	public boolean isExploded;
	private LevelController lvl;
	private Image wall, boom;
	private String placedBy;
	private int expRadius;

	public Bomb(LevelController level, int placedX, int placedY, String placedBy, int expR) {
		this.boom = new Image(getClass().getResourceAsStream("/resources/boom.png"));
		this.wall = new Image("file:/home/a/eclipse-workspace/Jbomber/src/resources/wall.png", 50, 50, false, true);
		this.map = level.getMap();
		this.placedBy = placedBy;
		this.expRadius = expR;
		this.lvl = level;
		this.x = placedX;
		this.y = placedY;

		// place bomb
		if (map.get(List.of(x, y)).getImage() == null) {
			map.get(List.of(x, y)).setImage(new Image(getClass().getResourceAsStream("/resources/bomb.png")));
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
		audio.playBoom();
		explosion(this.lvl.getNearTiles(x, y, expRadius));

		System.out.println("boom! " + destructedWalls + " walls destroyed");
	}

	private void explosion(List<ImageView> nearTiles) {
		// posizione corrente 
		map.get(List.of(x, y)).setImage(null);
		nearTiles.add(map.get(List.of(x, y)));

		// raggio intorno posizione corrente (piazzamento)
		for (ImageView tile : nearTiles) {
			if (tile.getImage() == null) {

				this.lvl.checkPlayerDamage(tile.getLayoutX() / 50, tile.getLayoutY() / 50);
				
				if(this.placedBy.equals("player")) {
					this.lvl.checkEnemyDamage(tile.getLayoutX() / 50, tile.getLayoutY() / 50);					
				}
				
				tile.setImage(boom);
			}
			if (tile.getImage().equals(this.wall)) { // TODO FIXA QUESTO
				tile.setImage(boom);
				destructedWalls++;
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

	private void updateLevelScore() {
		this.lvl.setScore(destructedWalls * 100);
	}

	private void clear(List<ImageView> nearTiles) {

		for (ImageView wall : nearTiles) {
			if (wall.getImage() != null) {
				if (wall.getImage().equals(boom)) {
					wall.setImage(null);
				}
			}
		}
	}

}
