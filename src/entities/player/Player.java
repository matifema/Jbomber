package entities.player;

import java.util.List;
import java.util.Set;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {
	private Rectangle playerBox;
	private int spawnX, spawnY;
	
	public Player(double width, double height, Set<List<Integer>> occupiedCell) {
		this.playerBox = new Rectangle(width, height);
		this.playerBox.setFill(Color.PURPLE);
		
		findSpawn(occupiedCell);
	}
	
	
	public void findSpawn(Set<List<Integer>> spots) {
		Random rand = new Random();
		int x,y;
		
		// leggermente inefficente ma ok
		do {
			x = rand.nextInt(15);
			y = rand.nextInt(2, 16);			
		}
		while (!spots.contains(List.of(x,y)));
		this.spawnX = x;
		this.spawnY = y;
	}
	
	public Rectangle getPlayerNode(){
		return this.playerBox;
	}
	
	public int getX() {
		return this.spawnX;
	}
	public int getY() {
		return this.spawnY;
	}
}
