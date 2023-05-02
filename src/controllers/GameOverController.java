package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class GameOverController {
	@FXML
	Text yes;
	@FXML
	Text no;
	@FXML
	AnchorPane pane;
	
	public GameOverController() {}
	
	public void selectYes() {
		yes.setStyle("-fx-fill:white;");
		no.setStyle("-fx-fill:black;");
	}
	
	public void selectNo() {
		yes.setStyle("-fx-fill:black;");
		no.setStyle("-fx-fill:white;");
	}

	public void setBackGround() {
		this.pane.setBackground(Background.fill(Color.BLACK));
		selectYes();
	}
}
