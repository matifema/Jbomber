package application;

import java.io.IOException;

import controllers.GameOverController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class GameOver {
	private Scene scene;
	private Stage mainStage;
	
	public GameOver(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/GameOver.fxml"));
		Parent root = fxmlLoader.load();
		GameOverController controller = fxmlLoader.getController();
		controller.setStage(stage);

		this.scene = new Scene(root);
		mainStage.setScene(scene);

		startKeyHandler(scene, controller);
	}

	private void startKeyHandler(Scene scene, GameOverController controller) {
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.LEFT) {
				controller.selectYes();
			}
			if (event.getCode() == KeyCode.RIGHT) {
				controller.selectNo();
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
