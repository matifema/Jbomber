package entities.gameField;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Level {
	private Scene scene;
	private Player player;
	private HBox text = new HBox();
	private int nCols = 17, nRows = 18, scorePoints = 0, nWalls = 50, explosionPower = 1;
	private TileSpace tileSpace = new TileSpace(nCols, nRows);
	private HashMap<List<Integer>, Rectangle> levelMap = tileSpace.getMap();
	private StackPane parentContainer = new StackPane();
	private Text score = new Text(" score: 0"), time = new Text(" time: 200"), lives = new Text(" lives: 3");
	private Bomb placedBomb;

	public Level() {
		renderBorder();
		renderWalls();
		renderPlayer();

		parentContainer.setAlignment(Pos.TOP_CENTER);
		parentContainer.getChildren().addAll(tileSpace.getPane(), text);
		scene = new Scene(parentContainer);
		startKeyHandler(scene);

		renderData();
	}

	private void renderBorder() {
		for (int y = 0; y < nRows; y++) {
			for (int x = 0; x < nCols; x++) {

				if (x == 0 || x == nCols - 1 || y == nRows - 1 || y == 0 || y == 1) {
					levelMap.get(List.of(x, y)).setFill(Color.BLACK);
				} else {
					if (x % 2 == 0 && y % 2 != 0) {
						levelMap.get(List.of(x, y)).setFill(Color.BLACK);
					}
				}
			}
		}
	}

	private void renderData() {
		Font customFont = new Font("Computer Pixel-7", 75);

		score.setFill(Color.WHITE);
		score.setFont(customFont);
		time.setFill(Color.WHITE);
		time.setFont(customFont);
		lives.setFill(Color.WHITE);
		lives.setFont(customFont);

		text.getChildren().addAll(score, time, lives);
		text.setAlignment(Pos.TOP_CENTER);
	}

	private void renderWalls() {
		for (int i = 0; i < nWalls; i++) {
			int x, y;
			do {
				Random rand = new Random();
				x = rand.nextInt(1, nCols - 1);
				y = rand.nextInt(2, nRows - 1);

			} while (!levelMap.get(List.of(x, y)).getFill().equals(Color.GREEN));

			levelMap.get(List.of(x, y)).setFill(Color.BROWN);
		}
	}

	private void renderPlayer() {
		Random rand = new Random();
		Rectangle player = new Rectangle(50, 50);

		int x, y;

		do {
			x = rand.nextInt(1, nCols - 2);
			y = rand.nextInt(3, nRows - 2);

		} while (!levelMap.get(List.of(x, y)).getFill().equals(Color.GREEN));

		player.setFill(Color.PURPLE);
		tileSpace.getPane().getChildren().add(player);
		this.player = new Player(x, y, player, levelMap);
	}

	private void startKeyHandler(Scene scene) {
		// Key Handler
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				clearLevel();
				renderWalls();
				renderPlayer();
			}
			if (event.getCode() == KeyCode.E) {
				if (this.placedBomb == null || this.placedBomb.isExploded) {
					placeBomb();
				}
			}
			if (event.getCode() == KeyCode.W && player.isMoveValid(0, -1)) {
				player.movePlayer("W");
			}
			if (event.getCode() == KeyCode.A && player.isMoveValid(-1, 0)) {
				player.movePlayer("A");
			}
			if (event.getCode() == KeyCode.S && player.isMoveValid(0, 1)) {
				player.movePlayer("S");
			}
			if (event.getCode() == KeyCode.D && player.isMoveValid(1, 0)) {
				player.movePlayer("D");
			}
		});
	}

	private void placeBomb() {
		this.placedBomb = new Bomb(this, this.player.getX(), this.player.getY());
	}

	public List<Rectangle> getNearWalls(int x, int y) {
		int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
		List<Rectangle> nearWalls = new LinkedList<>();
		Set<List<Integer>> visited = new HashSet<>();
		List<Integer> pos = List.of(x, y);
		nearWalls.add(levelMap.get(pos));
		visited.add(pos);

		for (int[] dir : directions) {
			pos = List.of(x, y);
			for (int i = 1; i <= explosionPower; i++) {
				pos = Arrays.asList(pos.get(0) + dir[0], pos.get(1) + dir[1]);
				if (!visited.add(pos)) {
					break;
				}
				Rectangle wall = levelMap.get(pos);
				if (wall.getFill().equals(Color.BLACK)) {
					break;
				}
				nearWalls.add(wall);
			}
		}

		return nearWalls;
	}
	
	public void clearLevel() {
		for (int y = 0; y < nRows; y++) {
			for (int x = 0; x < nCols; x++) {
				if (levelMap.get(List.of(x, y)).getFill().equals(Color.BROWN)) {
					levelMap.get(List.of(x, y)).setFill(Color.GREEN);
				}
			}
		}
		// 306 perche il player è aggiunto per ultimo 17*18-1
		tileSpace.getPane().getChildren().remove(306);
	}

	public Scene getScene() {
		return this.scene;
	}
	
	public HashMap<List<Integer>, Rectangle> getMap(){
		return this.levelMap;
	}
	
	public void setScore(int points) {
		scorePoints += points;
		score.setText(" score: " + scorePoints);
	}
}
