package application.save;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads/Saves from file the gamedata info.
 *  - Player Name
 *  - Player Level
 *  - Player Avatar
 *  - Won Levels
 *  - Lost Levels
 *  - Tot Levels Played
 */
public class GameData {
    private List<String> data = new ArrayList<>();
    private String filePath = "src/application/save/gamedata.txt";

    /**
     * New Gamedata Instance
     */
    public GameData() {}

    /**
     * Saves data to file.
     * Data is a list of strings containing all gamedata info.
     * @param data
     */
    public void setData(List<String> data) {
        System.out.println("-- writing savefile to disk");

        try {
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String line : data) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("Error while saving to file: " + e.getMessage());
        }

        this.data = data;
    }

    /**
     * Reads data from file.
     * Returns a list of strings containing all gamedata info.
     * @return
     */
    public List<String> getData() {
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            data.add(bufferedReader.readLine()); // name
            data.add(bufferedReader.readLine()); // won
            data.add(bufferedReader.readLine()); // losses
            data.add(bufferedReader.readLine()); // total
            data.add(bufferedReader.readLine()); // level
            data.add(bufferedReader.readLine()); // avatar

            bufferedReader.close(); // Close the BufferedReader

        } catch (IOException e) {
            System.err.println("Error reading from savefile: " + e.getMessage());
        }

        return this.data;
    }

    /**
     * Modifies the gamedata file accordingly when a game is won.
     */
    public void wonGame() {

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath));
            lines.set(1, (Integer.parseInt(lines.get(1)) + 1) + ""); // won
            lines.set(3, (Integer.parseInt(lines.get(3)) + 1) + ""); // tot

            setData(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies the gamedata file accordingly when a game is lost.
     */
    public void lostGame() {

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath));
            lines.set(2, (Integer.parseInt(lines.get(2)) + 1) + ""); // lost
            lines.set(3, (Integer.parseInt(lines.get(3)) + 1) + ""); // tot

            setData(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes to file new data when gamedata is empty.
     */
    public void newLevel() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath));
            lines.set(4, (Integer.parseInt(lines.get(4)) + 1) + ""); // level

            setData(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
