package entities.gameField;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bomb {
	private int destructedWalls = 0, expPower = 2, x, y;
	private HashMap<List<Integer>, Rectangle> map;
	public boolean isExploded;
	private Level lvl;

	public Bomb(Level level, HashMap<List<Integer>, Rectangle> levelMap, int placedX, int placedY) {
		this.map = levelMap;
		this.lvl = level;
		this.x = placedX;
		this.y = placedY;

		if (levelMap.get(List.of(x, y)).getFill().equals(Color.GREEN)) {
			levelMap.get(List.of(x, y)).setFill(Color.GRAY);
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

		explosion(getNearWalls());
	}

	private List<Rectangle> getNearWalls() {
		int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
		List<Rectangle> nearWalls = new LinkedList<>();
		Set<List<Integer>> visited = new HashSet<>();
		List<Integer> pos = List.of(x, y);
		nearWalls.add(map.get(pos));
		visited.add(pos);

		for (int[] dir : directions) {
			pos = List.of(x, y);
			for (int i = 1; i <= expPower; i++) {
				pos = Arrays.asList(pos.get(0) + dir[0], pos.get(1) + dir[1]);
				if (!visited.add(pos)) {
					break;
				}
				Rectangle wall = map.get(pos);
				if (wall.getFill().equals(Color.BLACK)) {
					break;
				}
				nearWalls.add(wall);
			}
		}

		return nearWalls;
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
