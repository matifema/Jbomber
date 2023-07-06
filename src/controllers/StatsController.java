package controllers;

import java.util.List;

import application.save.GameData;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * Controller for Stats class.
 */
public class StatsController {
    @FXML
    Text name;
    @FXML
    ImageView avatar;
    @FXML
    Text level;
    @FXML
    Text lostGames;
    @FXML
    Text wonGames;
    @FXML
    Text playedGames;

    /**
     * Creates new instance of StatsController
     */
    public StatsController() {}

    /**
     * Loads data from gamedata and displays it on screen.
     */
    public void loadData() {
        System.out.println("-- loading data from savefile");

        GameData read = new GameData();
        List<String> data = read.getData();

        this.name.setText(data.get(0));
        this.wonGames.setText(data.get(1));
        this.lostGames.setText(data.get(2));
        this.playedGames.setText(data.get(3));
        this.level.setText(data.get(4));

        if (data.get(5) != null) {
            Image avatar = new Image(getClass().getResourceAsStream("/resources/" + data.get(5)));
            this.avatar.setImage(avatar);
        }
    }

    /**
     * Writes data to disk.
     * @param data
     */
    public void writeData(List<String> data) {
        GameData read = new GameData();
        read.setData(data);
        return;
    }

}
