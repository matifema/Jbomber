package application;
	
import java.io.*;

import entities.menu.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

    	MainMenu menu = new MainMenu();
    	Scene scene = menu.getScene();
		
    	
        stage.setTitle("JBomber");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setMaxWidth(850);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // lancia la finestra
    }
}
