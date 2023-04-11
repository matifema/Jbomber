package entities.game;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.image.*;
import util.timer.CountDown;
import java.util.ArrayList;


public class GameController {
	@FXML GridPane grid;
	@FXML Text lives, time, score;
	
	private final int countTime = 200, nWalls = 50;
	private final String lv = "LIVES: ", tm = "TIME: ", sc = "SCORE: ";
	private final double cellW = 800/15, cellH = 800/17;
	
	public GameController() {
	}
	
	public void drawHeading() {
		// Creo intestazione (2 righe nere sul top)
		for(int i=0; i < 2; i++) {
			for(int j=0; j < 15; j++) {
				Rectangle rect = new Rectangle(cellW,cellH);
				rect.setFill(Color.BLACK);
				rect.toBack();
				grid.add(rect, j, i);
			}
		}
	}
	
	public void drawLevel() { 
		// ho 224 tiles libere -> 112 muri
		for (int i = 0; i<nWalls; i++) {
			drawWall();			
		}
		drawBarriers();
	}
	
	public void drawBarriers() {
		// ho un area iniziale di 16x14
		// lasciando una riga e una colonna ai lati diventa 14x12
		
		for(int y = 3; y<16; y++) {
			for(int x = 1; x<15; x++) {
				if((x+1)%2==0 && y%2 != 0) {
					drawBarrier(x, y);
				}
			}
		}
	}
	
	public void drawBarrier(int x, int y) {
		Image image = new Image("/barrier.png");
		ImageView imv = new ImageView();
		imv.setImage(image);
		imv.setFitWidth(cellW);
		imv.setFitHeight(cellH);
		imv.setId("barrier");	
		
		grid.add(imv, x, y);
	}

	public void drawWall() {
		Image image = new Image("/wall.png");
		ImageView imv = new ImageView();
		imv.setImage(image);
		imv.setFitWidth(cellW);
		imv.setFitHeight(cellH);
		imv.setId("wall");
		//imv.setSmooth(true);
		//imv.setCache(true);
		
		Random rand = new Random();
		int x = rand.nextInt(15);
		int y = rand.nextInt(2, 16);
		
		//imv.toFront();
		grid.add(imv, x, y);
		
		//System.out.print("coord: " + x + " " + y);
	}
	
	public void clearLevel() {
		for(Node img : new ArrayList<>(grid.getChildren())) {
			if(img.getId() == "wall") {
				grid.getChildren().remove(img);
			}
		}
	}
	
	public void getReady() {
		// press a button to start type shit
	}
	
	public void startCountDown() {
		// creo countdown con dentro time.sleep e lo metto dentro un thread cosi parallelizzo e non blocco la gui
		CountDown task = new CountDown(countTime, time, tm);
		Thread thread = new Thread(task);
		thread.start();
	}

	// TODO:
	//	1. getReady()
	//	2. leggi https://netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
	//	3. implementa event quando thread timer finisce
}
