package entities.gameField;

import java.util.HashMap;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileSpace {
	private HashMap<List<Integer>, Rectangle> map = new HashMap<>();
	private TilePane tilePane = new TilePane();
	private int nCols, nRows;
	private Scene scene;

	public TileSpace(int nCols, int nRows) {
		this.nCols = nCols;
		this.nRows = nRows;

		tilePane.setOrientation(Orientation.VERTICAL);
		tilePane.setTileAlignment(Pos.CENTER);
		tilePane.setPrefRows(nRows);
		tilePane.setPrefColumns(nCols);
		tilePane.setMinWidth(850);
		populateSpace(tilePane);
		// this.scene = new Scene(tilePane);
	}

	public void populateSpace(TilePane space) {
		space.setBackground(Background.fill(Color.GREEN));

		for (int x = 0; x < nCols; x++) {
			for (int y = 0; y < nRows; y++) {
				Rectangle tile = createTile();

				space.getChildren().add(tile);
				map.put(List.of(x, y), tile);
			}
		}
	}

	public Rectangle createTile() {
		Rectangle tile = new Rectangle(50, 50);
		tile.setFill(Color.GREEN);
		return tile;
	}

	public TilePane getPane() {
		return this.tilePane;
	}

	public Scene getScene() {
		return this.scene;
	}

	public HashMap<List<Integer>, Rectangle> getMap() {
		return this.map;
	}

	public ObservableList<?> getList() {
		return tilePane.getChildren();
	}

}