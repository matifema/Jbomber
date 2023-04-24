package entities.gameField;

import java.util.HashMap;
import java.util.List;

import entities.audio.AudioManager;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bomb {
	private AudioManager audio = new AudioManager();
	private HashMap<List<Integer>, ImageView> map;
	private int destructedWalls = 0, x, y;
	public boolean isExploded;
	private LevelController lvl;
	private Image wall, boom;

	public Bomb(LevelController level, Image wall,  int placedX, int placedY) {
		this.boom = new Image(getClass().getResourceAsStream("/resources/boom.png"));
		this.map = level.getMap();
		this.wall = wall;
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
		// bomb itself
		audio.playBoom();
		map.get(List.of(x, y)).setImage(boom);
		explosion(this.lvl.getNearWalls(x, y));
	}

	private void explosion(List<ImageView> nearWalls) {
		for (ImageView wall : nearWalls) {
			if (wall.getImage() == null) {
				this.lvl.checkPlayerDamage(wall.getLayoutX()/50, wall.getLayoutY()/50);
				
				wall.setImage(boom);
				continue;
			}
			if (wall.getImage().equals(this.wall)) {
				wall.setImage(boom);
				destructedWalls++;
			}
			else {
				wall.setImage(boom); 
			}
		}

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				clear(nearWalls);
				updateLevelScore();
			}

		}, 200);

	}

	private void updateLevelScore() {
		this.lvl.setScore(destructedWalls * 100);

	}

	private void clear(List<ImageView> nearWalls) {

		for (ImageView wall : nearWalls) {
			if (wall.getImage() != null) {
				if(wall.getImage().equals(boom)) {
					wall.setImage(null);					
				}
			}
		}
	}

}
