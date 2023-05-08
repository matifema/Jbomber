package application;

import java.util.HashMap;
import java.util.List;

import controllers.LevelController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerUp {
	private HashMap<List<Integer>, ImageView> map;
	private LevelController lvlController;
	private String type;
	private Image img;
	public int x, y;
	
	public PowerUp(LevelController controller, int x, int y, String typ) {
		this.img = new Image(getClass().getResourceAsStream("/resources/powerup-"+typ+".png"));
		this.lvlController = controller;
		this.map = controller.getMap();
		this.type = typ;
		this.x = x;
		this.y = y;

		map.get(List.of(x, y)).setImage(this.img);
	}

	public void onCollect() {
		map.get(List.of(this.x, this.y)).setImage(null);
		
		switch (this.type){
			case "life":
				this.lvlController.addLives(1);
				return;
			case "bomb":
				this.lvlController.addExpPower(1);
				return;
			case "golden":
				
				return;
		}
	}
}
