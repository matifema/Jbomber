package application;

import java.io.IOException;

import controllers.EndScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class EndScreen {
	private Scene scene;
	
	public EndScreen(Stage stage, String title) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/EndScreen.fxml"));
		Parent root = fxmlLoader.load();
		EndScreenController controller = fxmlLoader.getController();
		
		controller.setStage(stage);
		controller.setText(title);

		this.scene = new Scene(root);
		stage.setScene(scene);

		startKeyHandler(scene, controller);
	}

	private void startKeyHandler(Scene scene, EndScreenController controller) {
		AudioManager audio = new AudioManager();

		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.LEFT) {
				controller.selectYes();
				audio.playSelect();
			}
			if (event.getCode() == KeyCode.RIGHT) {
				controller.selectNo();
				audio.playSelect();
			}
			if (event.getCode() == KeyCode.ENTER){
				try {
					controller.selected();	} catch (IOException e) {e.printStackTrace();}
			}
		});
	}

	public Scene getScene() {
		return this.scene;
	}
}
