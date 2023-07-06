package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import controllers.LevelController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Powerup Object Class.
 */
public class PowerUp {
	public enum PwrUpType{
		LIFE,
		BOMB,
		GOLDEN
	}

	private List<PowerUpObserver> observers = new ArrayList<>();
	private HashMap<List<Integer>, ImageView> map;
	private LevelController lvlController;
	public boolean collected = false;
	private PwrUpType type;
	private Image img;
	public int x, y;

	/**
	 * Creates new instance of PowerUp.
	 * Loads correct image for powerup type.
	 * Spawns powerup in game map.
	 * @param controller
	 * @param x
	 * @param y
	 * @param typ
	 */
	public PowerUp(LevelController controller, int x, int y, PwrUpType typ) {
		this.img = new Image(getClass().getResourceAsStream("/resources/powerup-" + typ.toString() + ".png"));
		this.lvlController = controller;
		this.map = controller.getMap();
		this.type = typ;
		this.x = x;
		this.y = y;

		map.get(List.of(x, y)).setImage(this.img);
		map.get(List.of(x, y)).setId("powerup");
	}

	public void addObserver(PowerUpObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PowerUpObserver observer) {
        observers.remove(observer);
    }

	/**
	 * Called when a player moves on top of the powerup, collecting it.
	 * if life -> addLives()
	 * if bomb -> addExpPower()
	 * if golden -> addScore()
	 * 
	 */
	public void onCollect() {
		this.collected = true;
		this.lvlController.getMap().get(List.of(this.x, this.y)).setId("");

		System.out.println("-- picked up.. " + this.type);
		
		for (PowerUpObserver observer : observers) { // list observers contains only 1 observer...
			observer.onPowerUpCollected(this.type);
        }
	}


	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
}
