package application;

import java.io.IOException;

import controllers.StatsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Stats {
	private Scene scene;

	public Stats(Stage mainStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/Stats.fxml"));
		Parent root = fxmlLoader.load();
		StatsController controller = fxmlLoader.getController();

		this.scene = new Scene(root);
		mainStage.setScene(scene);

		controller.loadData();

		// key handling
		AudioManager audio = new AudioManager();
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				audio.playSelect();
				try {
					new MainMenu(mainStage);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

	}

	public Scene getScene() {
		return this.scene;
	}
}
