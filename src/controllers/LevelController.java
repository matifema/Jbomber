package controllers;

import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import application.AudioManager;
import application.Bomb;
import application.EndScreen;
import application.Enemy;
import application.Level;
import application.Player;
import application.PowerUp;
import application.save.ReadFromFile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LevelController {
	@FXML
	Text score;
	@FXML
	Text lives;
	@FXML
	TilePane tilePane;

	private final int nCols = 17, nRows = 17, nWalls = 60;
	private Integer scorePoints = 0, explosionPower = 1, livesScore = 3;
	private Player player = new Player();
	private Bomb placedBomb;
	private AudioManager audio = new AudioManager();
	private HashMap<List<Integer>, ImageView> map = new HashMap<>();
	private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
	public List<PowerUp> powerUps = new ArrayList<>();
	private Stage mainStage;

	public LevelController() {
		this.audio.playSoundtrack(true);
	}

	public ImageView createTile() {
		ImageView tile = new ImageView();

		tile.setFitHeight(50);
		tile.setFitWidth(50);

		return tile;
	}

	public void populateSpace() {
		BackgroundImage myBI = new BackgroundImage(Level.grass, BackgroundRepeat.REPEAT, null, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);

		tilePane.setBackground(new Background(myBI));

		for (int x = 0; x < nCols; x++) {
			for (int y = 0; y < nRows; y++) {
				ImageView tile = createTile();
				tile.setId("");

				tilePane.getChildren().add(tile);
				map.put(List.of(x, y), tile);

			}
		}
	}

	public void renderBorder() {
		ColorAdjust ca = new ColorAdjust();
		ca.setHue(new Random().nextDouble(-1, 1));
		ca.setSaturation(new Random().nextDouble(0, 1));

		for (int y = 0; y < nRows; y++) {
			for (int x = 0; x < nCols; x++) {
				if (x == 0 || y == 0|| x == nCols - 1 || y == nRows - 1) {
					map.get(List.of(x, y)).setImage(Level.border);
					map.get(List.of(x, y)).setId("border");
					map.get(List.of(x, y)).setEffect(ca);
				} else {
					if (x % 2 == 0 && y % 2 == 0) {
						map.get(List.of(x, y)).setImage(Level.border);
						map.get(List.of(x, y)).setId("border");
						map.get(List.of(x, y)).setEffect(ca);
					}
				}
			}
		}
	}

	public void renderWalls() {
		ColorAdjust ca = new ColorAdjust();
		ca.setHue(new Random().nextDouble(-1, 1));
		ca.setSaturation(new Random().nextDouble(0, 1));

		for (int i = 0; i < nWalls; i++) {
			int x, y;
			do {
				Random rand = new Random();
				x = rand.nextInt(1, nCols - 1);
				y = rand.nextInt(0, nRows - 1);

			} while (map.get(List.of(x, y)).getImage() != null);

			map.get(List.of(x, y)).setImage(Level.wall);
			map.get(List.of(x, y)).setEffect(ca);
			map.get(List.of(x, y)).setId("wall");
		}
	}

	public void renderEntity(Object entity) {
		Random rand = new Random();
		int x, y;

		do { // trova posto (inefficiente)
			x = rand.nextInt(1, nCols - 2);
			y = rand.nextInt(0, nRows - 2);
		} while ((map.get(List.of(x, y)).getImage() != null));

		if (entity.getClass().equals(this.player.getClass())) { // render player
			this.player = (Player) entity;

			this.player.currentX = x;
			this.player.currentY = y;
			this.player.spawnPlayer();

			tilePane.getChildren().add(this.player.getPlayerNode());
			this.lives.setText("" + livesScore);

		} else { // render enemy

			System.out.println("-- enemy spawned.. " + x + "," + y);

			((Enemy) entity).currentX = x;
			((Enemy) entity).currentY = y;
			((Enemy) entity).spawnEnemy(this.map);

			this.enemies.add(((Enemy) entity));
		}
	}

	public void clearLevel() {
		IntStream.range(0, nRows)
				.forEach(y -> IntStream.range(0, nCols)
						.filter(x -> map.get(List.of(x, y)).getImage() != Level.wall)
						.forEach(x -> {
							ImageView tile = map.get(List.of(x, y));
							tile.setImage(null);
							tile.setId("");
						}));

		// 306 perche il player è aggiunto per ultimo, ovvero 17*18-1
		tilePane.getChildren().remove(306);
	}

	public void placeBomb(String placedBy, int x, int y) {
		if (placedBy.equals("player") && (this.placedBomb == null || this.placedBomb.isExploded)) {
			this.placedBomb = new Bomb(this, this.player.getX(), this.player.getY(), placedBy, this.explosionPower);
		}
		if (placedBy.equals("enemy")) {
			new Bomb(this, x, y, placedBy, 2);
		}
	}

	public void movePlayer(int x, int y) {
		if (this.player.isMoveValid(x, y)) {
			this.player.movePlayer(this.getMap(), x, y);
			checkEntities();

		} else {
			System.out.println("-- collision detected");
		}
	}

	private boolean checkEntities() {
		this.powerUps.stream()
				.filter(p -> this.player.currentX == p.x && this.player.currentY == p.y)
				.forEach(p -> {
					p.onCollect();
				});

		this.powerUps.removeIf((pwrUp) -> {
			return pwrUp.collected;
		}); // rimuove i collezionati

		return false;
	}

	public boolean checkPlayerDamage(double x, double y) {
		if (this.player.getX() == x && this.player.getY() == y) {
			if (Integer.parseInt(this.lives.getText()) <= 1) {
				this.player.dieEvent();
				this.audio.playGameOver();
				this.audio.playSoundtrack(false);
				addLives(-1);

				ReadFromFile save = new ReadFromFile(); // record in stats
				save.lostGame();

				return true;

			} else {
				this.audio.playDamageTaken();
				addLives(-1);
			}

		}
		return false;
	}

	public boolean checkEnemyDamage(double x, double y) {
		List<Enemy> temp = new ArrayList<>();

		for (Enemy e : this.enemies) {
			if (e.currentX == x && e.currentY == y) {
				e.die();
				this.audio.playDamageTaken();
				temp.add(e);
			}
		}

		if (temp.size() > 0) {
			this.enemies.removeAll(temp);
		}

		// check per vittoria
		if (this.enemies.size() == 0) {
			this.audio.playSoundtrack(false);
			this.audio.playGameStart();

			ReadFromFile saveFile = new ReadFromFile();
			saveFile.wonGame();

			Platform.runLater(() -> { // per thread del timer
				try {
					new EndScreen(this.mainStage, "LEVEL\nCOMPLETE");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			return true;
		}
		return false;
	}

	public List<ImageView> getNearTiles(int x, int y, int radius) {
		int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
		List<ImageView> nearWalls = new LinkedList<>();
		Set<List<Integer>> visited = new HashSet<>();
		List<Integer> pos = List.of(x, y);

		nearWalls.add(map.get(pos));
		visited.add(pos);

		for (int[] dir : directions) {
			pos = List.of(x, y);
			for (int i = 1; i <= radius; i++) {
				pos = Arrays.asList(pos.get(0) + dir[0], pos.get(1) + dir[1]);
				if (!visited.add(pos)) {
					break;
				}
				ImageView tile = map.get(pos);

				if (map.values().contains(tile)) {
					if (tile.getId().equals("border")) {
						break;
					}
					nearWalls.add(tile);
				}
			}
		}
		return nearWalls;
	}

	public void addScore(int points) {
		this.scorePoints += points;
		this.score.setText("" + scorePoints);
	}

	public void addLives(int i) {
		this.livesScore = Integer.parseInt(this.lives.getText()) + i;
		this.lives.setText("" + this.livesScore);
	}

	public void addExpPower(int i) {
		this.explosionPower += i;
	}

	public HashMap<List<Integer>, ImageView> getMap() {
		return this.map;
	}

	public TilePane getTilePane() {
		return this.tilePane;
	}

	public void setEnemies(LinkedList<Enemy> enemies) {
		for (Enemy e : enemies) {
			renderEntity(e);
		}

		this.enemies.addAll(enemies);
	}

	public void renderPla(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setStage(Stage stage) {
		this.mainStage = stage;
	}

	public LinkedList<Enemy> getEnemies() {
		return this.enemies;
	}

	public void stopEnemyTimeline() {
		for (Enemy e : this.enemies) {
			e.die();
		}
		this.enemies.removeAll(this.enemies);

	}

}
