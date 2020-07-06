import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadingHighscore {

    public static String getHighScore(){
        FileReader readFile;
        BufferedReader reader= null;
        try {
            readFile = new FileReader("source/highscore.txt");
            reader = new BufferedReader(readFile);
            return reader.readLine();
        }
        catch (Exception e){
            return "Nobody:500";
        }
        finally{
            try {
                if (reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
