package entities.gameField;

import java.util.HashMap;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Player {
	private Rectangle playerBox;
	private int currentX, currentY;
	public int spawnX, spawnY;
	HashMap<List<Integer>, Rectangle> levelMap;
	
	public Player(int x, int y, Rectangle player, HashMap<List<Integer>, Rectangle> levelMap) {
		this.levelMap = levelMap;
		this.currentX = x;
		this.currentY = y;
		
		this.playerBox = player;
		spawnPlayer(this.playerBox);
	}

	private void spawnPlayer(Rectangle playerBox2) {
		// tutto cio per non avere glitch col piazzamento della bomba nello spawn player
		
		this.playerBox.setViewOrder(-1);
		
		int x = -16, y = 2;
		
		TranslateTransition translate = new TranslateTransition();
		translate.setNode(this.playerBox);
		
		translate.setByX((x+currentX-1)*50);
		translate.setByY((y+currentY-2)*50);
		
		translate.setDuration(Duration.millis(1));
		translate.play();
	}

	public void movePlayer(String code) {
		TranslateTransition translate = new TranslateTransition();
		translate.setNode(this.playerBox);
		
		switch(code){
		case "W":
			translate.setByY(-50);
			currentY += -1;
			break;
		case "A":
			translate.setByX(-50);
			currentX += -1;
			break;
		case "S":
			translate.setByY(50);
			currentY += 1;
			break;
		case "D":
			translate.setByX(50);	
			currentX += 1;
			break;
		}
		translate.setDuration(Duration.millis(1));
		translate.play();
		
		System.out.println("x: "+currentX + " y: "+currentY);
	}
	
	public boolean isMoveValid(int deltaX, int deltaY){
		//se next position ha tile di colore verde o purple (start)
		return this.levelMap.get(List.of(currentX + deltaX, currentY + deltaY)).getFill().equals(Color.GREEN) ||
				this.levelMap.get(List.of(currentX + deltaX, currentY + deltaY)).getFill().equals(Color.PURPLE);
	}
	
	public Rectangle getPlayerNode(){
		return this.playerBox;
	}
	
	public int getX() {
		return this.currentX;
	}
	
	public int getY() {
		return this.currentY;
	}
}
