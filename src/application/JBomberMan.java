package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class JBomberMan extends Application {
	public static Stage stage;

	@Override
	public void start(Stage stage) throws IOException {
		JBomberMan.stage = stage;
		
		new MainMenu();
		
		stage.setTitle("JBomberMan");
		stage.sizeToScene();
		stage.setMaxWidth(850);
		stage.show();
	}

	public static void main(String[] args) {
		launch(); // starts game
	}
}
