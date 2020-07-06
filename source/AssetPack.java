import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Texture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class AssetPack{
    private static String AssetPath="Assets/";
    public static HashMap<String, Texture> Textures=new HashMap<>();
   // private MoveListener move;
    //private Thread t;
    public static HashMap<String,Sound> Sounds=new HashMap<>();
    public static Font f=new Font();
    /**
     * Constructor to pull in all files in the Assets folder
     * if given @param String file path
     * it will look in that given location
     */
    public AssetPack(){
        this(AssetPath);
    }
    public AssetPack(String AssetPathString){
        File dir = new File(AssetPathString);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                int dotindex=child.getPath().lastIndexOf(".");
                String type=child.getPath().substring(dotindex + 1);
                if(type.equals("png")) {
                    Texture a = new Texture();
                    try {
                        a.loadFromFile(child.toPath());
                        Textures.put(child.getName(), a);
                        //System.out.println(child.getName());
                    } catch (IOException e) {
                        System.out.println("ASSET PACK: Failed to grab texture from path");
                    }
                }
                else if(type.equals("wav")) {
                    SoundBuffer msoundBuffer = new SoundBuffer();
                    Sound msound = new Sound();
                    try {
                        msoundBuffer.loadFromFile(child.toPath());
                        msound.setBuffer(msoundBuffer);
                        //System.out.println(child.getName());

                    } catch (IOException e) {
                        System.out.println("ASSET PACK: Failed to grab sound from path");
                    }
                    Sounds.put(child.getName(),msound);

                }
                else if(type.equals("ttf")){
                    try {
                        f.loadFromFile(child.toPath());
                    }catch (IOException e) {
                        System.out.println("ASSET PACK: Failed to grab sound from path");
                    }
                    }

                }
            }
        }


    /**
     * @param s HashMap of all Files,
     *  Dumps found files into a list on the CommandLine
     */
    public static void printKeys(HashMap s){
        ArrayList<String> keys =new ArrayList<String>();
        keys.addAll(s.keySet());
        keys.forEach((n)->System.out.println(n));
    }

    /**
     * @param name file name thats been found
     * @return Returns the Texture found at that file name,
     */
    public static Texture getTexture(String name) {
            return Textures.get(name);
    }
    /**
     *
     * @return Returns the Font Being used ,
     */
    public static Font getFont(){
        return f;
    }
    /**
     * @param name file name thats been found
     * @return Returns the Sound found at that file name,
     */
    public static Sound getSound(String name){
        return Sounds.get(name);
    }

}
