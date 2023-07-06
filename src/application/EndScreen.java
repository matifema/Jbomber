package application;

import java.io.IOException;

import controllers.EndScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Screen for end-of-level event (win/loss).
 */
public class EndScreen {
	private Scene scene;

	/**
	 * Creates new EndScreen loading the fxml.
	 * Customizable title from String parameter.
	 * @param title
	 * @throws IOException
	 */
	public EndScreen(String title) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/EndScreen.fxml"));
		Parent root = fxmlLoader.load();
		EndScreenController controller = fxmlLoader.getController();

		controller.setText(title);

		this.scene = new Scene(root);
		JBomberMan.stage.setScene(scene);

		startKeyHandler(scene, controller);
	}

	/**
	 * Starts key handler on the scene for yes or no selection (A/D/SPACE)
	 * @param scene
	 * @param controller
	 */
	private void startKeyHandler(Scene scene, EndScreenController controller) {
		AudioManager audio = new AudioManager();

		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.A) {
				controller.selectYes();
				audio.playSelect();
			}
			if (event.getCode() == KeyCode.D) {
				controller.selectNo();
				audio.playSelect();
			}
			if (event.getCode() == KeyCode.SPACE) {
				try {
					controller.selected();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
