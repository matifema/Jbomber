package controllers;

import java.io.IOException;
import java.util.List;

import application.Level;
import application.save.GameData;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;

/**
 * Controller for NewGame Screen.
 */
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

    /**
     * Creates new instance of NewGameController.
     */
    public NewGameController() {}

    /**
     * Starts key handler on the current scene.
     * @param scene
     */
    public void startKeyHandler(Scene scene) {
        btn1.setToggleGroup(toggleGroup);
        btn2.setToggleGroup(toggleGroup);
        btn3.setToggleGroup(toggleGroup);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.E) {
                GameData write = new GameData();

                if (btn1.isSelected()) {
                    this.avatar = "avatar1.png";
                }

                if (btn2.isSelected()) {
                    this.avatar = "avatar2.png";
                }

                if (btn3.isSelected()) {
                    this.avatar = "avatar3.png";
                }

                write.setData(List.of(nameField.getText(), "0", "0", "0", "0", this.avatar));

                System.out.println(this.avatar);
                
                try {
                    new Level();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
