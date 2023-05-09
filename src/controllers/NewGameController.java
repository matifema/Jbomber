package controllers;

import java.io.IOException;
import java.util.List;

import application.Level;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import save.ReadFromFile;

public class NewGameController {
    @FXML
    TextField nameField;
    @FXML
    RadioButton btn1;
    @FXML
    RadioButton btn2;
    @FXML
    RadioButton btn3;

    private String avatar = "";
    private ToggleGroup toggleGroup = new ToggleGroup();
    
    public NewGameController(){
    }

    public void startKeyHandler(Scene scene){
        btn1.setToggleGroup(toggleGroup);
        btn2.setToggleGroup(toggleGroup);
        btn3.setToggleGroup(toggleGroup);

        scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.E) {
                ReadFromFile write = new ReadFromFile();
                
                if(btn1.isSelected()){
                    this.avatar = "avatar1.png";
                }

                if(btn2.isSelected()){
                    this.avatar = "avatar2.png";
                }

                if(btn3.isSelected()){
                    this.avatar = "avatar3.png";
                }

                write.setData(List.of(nameField.getText(), "0", "0", "0", "0", this.avatar));
            
                try {
                    Stage currentStage = (Stage) scene.getWindow();
                    Level lvl = new Level();
                    currentStage.setScene(lvl.getScene());
                } catch (IOException e) {e.printStackTrace();}
            }
            if (event.getCode() == KeyCode.ESCAPE){
                // TODO back to menu
            }
		});
    
        
    }


}
