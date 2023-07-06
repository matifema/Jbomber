package application;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import controllers.LevelController;
import javafx.scene.image.ImageView;

/**
 * Bomb object class.
 */
public class Bomb {
	private String placedBy;
	public boolean isExploded, gameEnded = false;
	private LevelController lvl;
	private Integer destructedWalls = 0, x, y, expRadius;
	private HashMap<List<Integer>, ImageView> map;
	private AudioManager audio = new AudioManager();

	/**
	 * Constructor
	 * @param levelController
	 * @param placedX
	 * @param placedY
	 * @param placedBy <- enemy/player
	 * @param expR <- explosion radius
	 */
	public Bomb(LevelController level, int placedX, int placedY, String placedBy, int expR) {
		this.map = level.getMap();
		this.placedBy = placedBy;
		this.expRadius = expR;
		this.lvl = level;
		this.x = placedX;
		this.y = placedY;

		// place bomb
		map.get(List.of(x, y)).setImage(Level.bomb);
		map.get(List.of(x, y)).setId("bomb");
		startCountDown();
		isExploded = false;

	}

	/**
	 * Starts explosion countdown, runs method boom() in 1500ms
	 */
	public void startCountDown() {
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				isExploded = true;
				boom();
			}
		}, 1500);
	}

	/**
	 * Plays the boom sound from the AudioManger and calls the explosion() method.
	 * Also uses the getNearTiles with the position of the bomb and the set explosionRadius.
	 */
	private void boom() {
		this.audio.playBoom();
		explosion(this.lvl.getNearTiles(x, y, expRadius));
	}

	/**
	 * Draws the explosion on the map, checks for damage (player on enemy or enemy on player) and clears the explosion after 200ms.
	 * @param nearTiles
	 */
	private void explosion(List<ImageView> nearTiles) {

		HashMap<List<Integer>, ImageView> map = this.lvl.getMap();

		map.get(List.of(this.x, this.y)).setImage(Level.boom);

		for (ImageView tile : nearTiles) {
			int tileX = (int) (tile.getLayoutX() / 50), tileY = (int) (tile.getLayoutY() / 50);

			if (tile.getId().equals("")) {
				map.get(List.of(tileX, tileY)).setImage(Level.boom);
			}

			if (tile.getId().equals("wall")) {
				map.get(List.of(tileX, tileY)).setImage(Level.boom);

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

	/**
	 * Spawns a powerup on the given tile 1/4th of the time.
	 * Powerups can be:
	 * 	- Life +1
	 * 	- Bomb +1 Radius
	 * 	- Golden +1000 points
	 * @param tile
	 */
	private void spawnPowerUp(ImageView tile) {
		int rand = new Random().nextInt(0, 3);

		if (rand == 0) {
			PowerUp.PwrUpType type = null;
			rand = new Random().nextInt(0, 3);

			switch (rand) {
				case 0:
					type = PowerUp.PwrUpType.LIFE;
					break;
				case 1:
					type = PowerUp.PwrUpType.BOMB;
					break;
				case 2:
					type = PowerUp.PwrUpType.GOLDEN;
					break;
				default:
					break;
			}
			// create powerup and add lvlcontroller as observer
			PowerUp pw = new PowerUp(this.lvl, (int) tile.getLayoutX() / 50, (int) tile.getLayoutY() / 50, type);
			pw.addObserver(lvl);
			this.lvl.powerUps.add(pw);

			System.out.println("-- spawning.. " + type.toString());
		}
	}

	/**
	 * Updates the score depending on the number of distructed walls.
	 * Called after every (player's) explosion.
	 */
	private void updateLevelScore() {
		this.lvl.addScore(destructedWalls * 100);
	}

	/**
	 * Clears the map after the explosion.
	 * Removes the "boom.png" and replaces it with null (grass background).
	 * @param nearTiles
	 */
	private void clear(List<ImageView> nearTiles) {
		this.lvl.getEnemies().stream().forEach((e)->{
			if(e.currentX == this.x && e.currentY == this.y){

				if(e.enemyType == EnemyType.WALKER){
					map.get(List.of(x, y)).setImage(Level.walker);
				}else{
					map.get(List.of(x, y)).setImage(Level.bomber);
				}

				map.get(List.of(x, y)).setId("enemy");
			}
		});

		if(	this.lvl.getPlayer().currentX == this.x 
			&& this.lvl.getPlayer().currentY == this.y){
			
			this.lvl.checkPlayerDamage(this.x, this.y);

			map.get(List.of(x, y)).setImage(Level.playerImg);
			map.get(List.of(x, y)).setId("player");
		}

		for (ImageView tile : nearTiles) {
			// not null necessario
			if (tile.getImage() != null && tile.getImage().equals(Level.boom)) {
				tile.setImage(null);
				tile.setId("");
			}
			tile.setEffect(null);
		}
	}

}
