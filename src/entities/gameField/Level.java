package entities.gameField;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;


public class Level {
	private Scene scene;
	private Player player;
	private int scorePoints;
	private int nWalls = 50;
	private HBox text = new HBox();
	private int nCols = 17, nRows = 18;
	private TileSpace tileSpace = new TileSpace(nCols, nRows);
	private HashMap<List<Integer>, Rectangle> levelMap = tileSpace.getMap();
	private StackPane parentContainer = new StackPane();
	private Text score = new Text(" score: 0"), time = new Text(" time: "), lives = new Text(" lives: ");
	
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
		for(int y = 0; y < nRows; y++) {
			for(int x = 0; x < nCols; x++) {

				if(x == 0 || x == nCols-1 || y == nRows-1 || y == 0 || y == 1) {
					levelMap.get(List.of(x,y)).setFill(Color.BLACK);					
				}
				else {
					if(x %2 == 0 && y %2 != 0) {
						levelMap.get(List.of(x,y)).setFill(Color.BLACK);					
					}
				}
			}
		}
	}
	
	private void renderData() {
		score.setFill(Color.WHITE);
		time.setFill(Color.WHITE);
		lives.setFill(Color.WHITE);
		
		text.getChildren().addAll(score, time, lives);
	}
	
	private void renderWalls() {
		for(int i = 0; i < nWalls; i++) {
			int x,y;
			do {
				Random rand = new Random();
				x = rand.nextInt(1, nCols-1);
				y = rand.nextInt(2, nRows-1);
				
			}while( !levelMap.get(List.of(x,y)).getFill().equals(Color.GREEN));		
			
			levelMap.get(List.of(x,y)).setFill(Color.BROWN);
		}
	}
	
	private void renderPlayer() {
		Random rand = new Random();
		Rectangle player = new Rectangle(50,50);

		int x,y;
		
		do {
			x = rand.nextInt(1, nCols-2);
			y = rand.nextInt(3, nRows-2);
			
		}while( !levelMap.get(List.of(x,y)).getFill().equals(Color.GREEN));		
		
		player.setFill(Color.PURPLE);
		tileSpace.getPane().getChildren().add(player);
		this.player = new Player(x, y, player, levelMap);
	}

	private void startKeyHandler(Scene scene) {
		// Key Handler
		scene.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER) {
				clearLevel();
				renderWalls();
				renderPlayer();
			}
			if(event.getCode() == KeyCode.E) {
				placeBomb();
			}
			if(event.getCode() == KeyCode.W && player.isMoveValid(0, -1)) {
				player.movePlayer("W");
			}
			if(event.getCode() == KeyCode.A && player.isMoveValid(-1, 0)) {
				player.movePlayer("A");
			}
			if(event.getCode() == KeyCode.S && player.isMoveValid(0, 1)) {
				player.movePlayer("S");
			}
			if(event.getCode() == KeyCode.D && player.isMoveValid(1, 0)) {
				player.movePlayer("D");
			}
		});
	}
	
	private void placeBomb() {
		new Bomb(this, levelMap, this.player.getX(), this.player.getY());
	}

	public void clearLevel() {
		for(int y = 0; y < nRows; y++) {
			for(int x = 0; x < nCols; x++) {
				if(levelMap.get(List.of(x,y)).getFill().equals(Color.BROWN)) {
					levelMap.get(List.of(x,y)).setFill(Color.GREEN);
				}
			}
		}
		// 306 perche il player è aggiunto per ultimo 17*18-1
		tileSpace.getPane().getChildren().remove(306);
	}
	
	public Scene getScene() {
		return this.scene;
	}

	public void setScore(int points) {
		scorePoints += points;
		score.setText(" score: " + scorePoints); 
	}
}
