package application;

import java.io.IOException;

import controllers.GameOverController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class GameOver {
    private Scene scene;

    public GameOver() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/GameOver.fxml"));
        Parent root = fxmlLoader.load();
        GameOverController controller = fxmlLoader.getController();

        this.scene = new Scene(root);
        
        controller.setBackGround();
        startKeyHandler(scene, controller);
    }

    private void startKeyHandler(Scene scene, GameOverController controller) {
    	scene.setOnKeyPressed(event -> {
    		if(event.getCode() == KeyCode.LEFT) {
    			controller.selectYes();
    		}
    		if(event.getCode() == KeyCode.RIGHT) {
    			controller.selectNo();
    		}
    	});
	}

	public Scene getScene() {
        return this.scene;
    }
}
