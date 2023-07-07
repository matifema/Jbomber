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

import application.AudioManager;
import application.Bomb;
import application.EndScreen;
import application.Enemy;
import application.Level;
import application.Player;
import application.PowerUp;
import application.PowerUpObserver;
import application.save.GameData;
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

/**
 * Controller for Level class.
 */
public class LevelController implements PowerUpObserver{
	@FXML
	Text score;
	@FXML
	Text lives;
	@FXML
	TilePane tilePane;

	private final int nCols = 17, nRows = 17, nWalls = 60;
	private static Integer scorePoints = 0, explosionPower = 1, livesScore = 3;
	private Player player;
	private Bomb placedBomb;
	private AudioManager audio = new AudioManager();
	private HashMap<List<Integer>, ImageView> map = new HashMap<>();
	private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
	public List<PowerUp> powerUps = new ArrayList<>();

	/**
	 * Creates new instance of LevelController.
	 * Starts the soundtrack.
	 */
	public LevelController() {
		this.audio.playSoundtrack(true);
	}

	/**
	 * Creates a single tile (ImageView) for the map.
	 * @return
	 */
	public ImageView createTile() {
		ImageView tile = new ImageView();

		tile.setFitHeight(50);
		tile.setFitWidth(50);

		return tile;
	}

	/**
	 * Populates the screen (TilePane) with tiles, sets the background.
	 * Also creates a Map<List<Integer>, Imageview>.
	 * The Keys are the coordiates on screen.
	 * The Values are the ImageView (or tiles).
	 */
	public void populateSpace() {
		BackgroundImage myBg = new BackgroundImage(Level.grass, BackgroundRepeat.REPEAT, null, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);

		tilePane.setBackground(new Background(myBg));

		for (int x = 0; x < nCols; x++) {
			for (int y = 0; y < nRows; y++) {
				ImageView tile = createTile();
				tile.setId("");

				tilePane.getChildren().add(tile);
				map.put(List.of(x, y), tile);

			}
		}
	}

	/**
	 * Renders the undestructible border around and inside the level.
	 */
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

	/**
	 * Renders the destructible walls inside the level.
	 */
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

	/**
	 * Renders a general entity (enemy/player).
	 * Finds an empty spot randomly and spawns the entity.
	 * @param entity
	 */
	public void renderEntity(Object entity) {
		Random rand = new Random();
		int x, y;

		do { // trova posto (inefficiente)
			x = rand.nextInt(1, nCols - 2);
			y = rand.nextInt(0, nRows - 2);
		} while ((map.get(List.of(x, y)).getImage() != null));

		if (entity instanceof Player) { // render player
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

	/**
	 * Places a bomb on the map given the coordinates.
	 * @param placedBy
	 * @param x
	 * @param y
	 */
	public void placeBomb(String placedBy, int x, int y) {
		if (placedBy.equals("player") && (this.placedBomb == null || this.placedBomb.isExploded)) {
			this.placedBomb = new Bomb(this, this.player.getX(), this.player.getY(), placedBy, LevelController.explosionPower);
		}
		if (placedBy.equals("enemy")) {
			new Bomb(this, x, y, placedBy, 2 + Level.levelNum/2);
		}
	}

	/**
	 * Moves player by x and y.
	 * @param x
	 * @param y
	 */
	public void movePlayer(int x, int y) {
		if (this.player.isMoveValid(x, y)) {
			this.player.movePlayer(this.getMap(), x, y);
			
			PowerUp powerUp = checkForPowerUpAtPosition(this.player.getX(), this.player.getY());
			if (powerUp != null) {
				powerUp.onCollect();
				this.powerUps.remove(powerUp);
			}

		} else {
			System.out.println("-- collision detected");
		}
	}

	private PowerUp checkForPowerUpAtPosition(int x, int y) {
		return powerUps.stream()
				.filter(powerUp -> powerUp.getX() == x && powerUp.getY() == y)
				.findFirst()
				.orElse(null);
	}

	@Override
    public void onPowerUpCollected(PowerUp.PwrUpType powerUpType) {
        switch (powerUpType) {
            case LIFE:
                addLives(1);
                break;
            case BOMB:
                addExpPower(1);
                break;
            case GOLDEN:
                addScore(1000);
                break;
            default:
                break;
        }
    }

	/**
	 * Checks if a player is on the given x and y, and if true the player takes damage.
	 * @param x
	 * @param y
	 * Returns true if game has ended (player has no more lives)
	 * @return
	 */
	public boolean checkPlayerDamage(double x, double y) {
		if (this.player.getX() == x && this.player.getY() == y) {
			if (Integer.parseInt(this.lives.getText()) <= 1) {
				this.player.dieEvent();
				this.audio.playGameOver();
				this.audio.playSoundtrack(false);

				GameData save = new GameData(); // record in stats
				save.lostGame();

				return true;

			} else {
				this.audio.playDamageTaken();
				addLives(-1);
			}

		}
		return false;
	}

	/**
	 * Checks if an enemy is on the given x and y, and if true it takes damage.
	 * @param x
	 * @param y
	 * Returns true if game has ended (there are no more enemies)
	 * @return
	 */
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

			GameData saveFile = new GameData();
			saveFile.wonGame();

			Platform.runLater(() -> { // per thread del timer
				try {
					new EndScreen("LEVEL\nCOMPLETE");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			return true;
		}
		return false;
	}

	/**
	 * Given the coordinates (x,y), this method looks up in the map in all cardinal directions (for a certain radius or until it finds a border tile) 
	 * and returns a list of all the tiles that has encountered.
	 * @param x
	 * @param y
	 * @param radius
	 * @return
	 */
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

	/**
	 * Adds points to the current score.
	 * @param points
	 */
	public void addScore(int points) {
		LevelController.scorePoints += points;
		this.score.setText("" + scorePoints);
	}

	/**
	 * Adds i lives to the current lives.
	 * @param i
	 */
	public void addLives(int i) {
		LevelController.livesScore = Integer.parseInt(this.lives.getText()) + i;
		this.lives.setText("" + LevelController.livesScore);
	}

	/**
	 * Adds i power to the current explosion power.
	 * @param i
	 */
	public void addExpPower(int i) {
		LevelController.explosionPower += i;
	}

	/**
	 * Returns the map.
	 * @return
	 */
	public HashMap<List<Integer>, ImageView> getMap() {
		return this.map;
	}

	/**
	 * Sets and renders the enemies.
	 * @param enemies
	 */
	public void setEnemies(LinkedList<Enemy> enemies) {
		for (Enemy e : enemies) {
			renderEntity(e);
		}

		this.enemies.addAll(enemies);
	}

	/**
	 * Returns the player object.
	 * @return
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Returns the list of enemies
	 * @return
	 */
	public List<Enemy> getEnemies() {
		return this.enemies;
	}

	/**
	 * Stops the enemies and their timeline.
	 */
	public void stopEnemyTimeline() {
		for (Enemy e : this.enemies) {
			e.die();
		}
		this.enemies.removeAll(this.enemies);

	}
}
