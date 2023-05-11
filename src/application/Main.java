package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws IOException {

		new MainMenu(stage);

		stage.setTitle("JBomber");
		stage.sizeToScene();
		stage.setMaxWidth(850);
		stage.show();
	}

	public static void main(String[] args) {
		launch(); // lancia la finestra
	}
}
