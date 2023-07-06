package application;

import java.io.IOException;

import controllers.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Main Startup screen.
 * The player can select: New Game, Stats, Quit
 */
public class MainMenu {
	private Scene scene;
	
	/**
	 * Creates new instance of MainMenu.
	 * Loads fxml data and starts key handler for the current scene.
	 * @throws IOException
	 */
	public MainMenu() throws IOException {
		// loading MENU fxml and css
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/MainMenu.fxml"));
		Parent root = fxmlLoader.load();
		MenuController controller = fxmlLoader.getController();

		// set scene
		scene = new Scene(root);
		JBomberMan.stage.setScene(scene);

		// key handling
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.W) {
				controller.up();
			}
			if (event.getCode() == KeyCode.S) {
				controller.down();
			}
			if (event.getCode() == KeyCode.SPACE) {
				try {
					controller.select(); // selezione tra stats e level

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}
}