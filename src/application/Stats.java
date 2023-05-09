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

	public Stats() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view/Stats.fxml"));
		Parent root = fxmlLoader.load();
		StatsController controller = fxmlLoader.getController();

		this.scene = new Scene(root);

        controller.loadData();

		// key handling
		scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE){
                try {
                    Stage currentStage = (Stage) scene.getWindow();
                    MainMenu mn = new MainMenu();
                    currentStage.setScene(mn.getScene());
                } catch (IOException e) {e.printStackTrace();}
                
            }
		});

	}

	public Scene getScene() {
		return this.scene;
	}
}
