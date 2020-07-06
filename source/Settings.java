import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Settings {

    public static boolean music;
    public static float defaultVolume;
    public static boolean debug;
    public static boolean loop;

    public static float getDefaultVolume() {
        return defaultVolume;
    }

    public static float volume;
    static int i = 0;

    public static boolean getMusic() {
        return music;
    }

    public static float getVolume() {
        return volume;
    }

    public static boolean getDebug(){
        return debug;
    }

    public static boolean getLoop(){
        return loop;
    }

    public static void readFile(){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("source/settings.txt"));

            String eachLine;

            while((eachLine = br.readLine()) != null){
                i++;
                switch (i){
                    case 2:
                        music = Boolean.valueOf(eachLine);
                        break;

                    case 4:
                        defaultVolume = Integer.valueOf(eachLine);
                        volume = Integer.valueOf(eachLine);
                        break;

                    case 6:
                        debug = Boolean.valueOf(eachLine);
                        break;

                    case 8:
                        loop = Boolean.valueOf(eachLine);
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
