package entities.gameField;

import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bomb {
	private HashMap<List<Integer>, Rectangle> map;
	private int destructedWalls = 0, x, y;
	public boolean isExploded;
	private Level lvl;

	public Bomb(Level level, int placedX, int placedY) {
		this.map = level.getMap();
		this.lvl = level;
		this.x = placedX;
		this.y = placedY;

		// place bomb
		if (map.get(List.of(x, y)).getFill().equals(Color.GREEN)) {
			map.get(List.of(x, y)).setFill(Color.GRAY);
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
		map.get(List.of(x, y)).setFill(Color.GREEN);

		explosion(lvl.getNearWalls(x, y));
	}

	private void explosion(List<Rectangle> nearWalls) {
		for (Rectangle wall : nearWalls) {
			if (wall.getFill().equals(Color.BROWN)) {
				destructedWalls++;
			}
			if (!wall.getFill().equals(Color.BLACK)) {
				wall.setFill(Color.RED);
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

	private void clear(List<Rectangle> nearWalls) {

		for (Rectangle wall : nearWalls) {
			if (wall.getFill().equals(Color.RED)) {
				wall.setFill(Color.GREEN);
			}
		}
	}

}
