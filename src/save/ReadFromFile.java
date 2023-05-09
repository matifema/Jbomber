package save;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class ReadFromFile {
    private List<String> data = new ArrayList<>();
    private String filePath = "src/save/saveData.txt";
     
    public ReadFromFile(){
    }

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
            
        } catch (IOException e) {System.err.println("Error while saving to file: " + e.getMessage());}

        this.data = data;
    }

    public List<String> getData(){
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

        } catch (IOException e) {System.err.println("Error reading from savefile: " + e.getMessage());}

        return this.data;
    }
   
    public void wonGame(){

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath));
            lines.set(1, (Integer.parseInt(lines.get(1))+1)+""); // won
            lines.set(3, (Integer.parseInt(lines.get(3))+1)+""); // tot
    
            setData(lines);        
        } catch (IOException e) { e.printStackTrace(); }

    }

    public void lostGame(){

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath));
            lines.set(2, (Integer.parseInt(lines.get(1))+1)+""); // lost
            lines.set(3, (Integer.parseInt(lines.get(3))+1)+""); // tot
    
            setData(lines);        
        } catch (IOException e) { e.printStackTrace(); }

    }
}
