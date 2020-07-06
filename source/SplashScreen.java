import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SplashScreen {

    private static JFrame SplashScreen;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double height = screenSize.getHeight();

    private int Screenprop=2;
    private int windowHeight = (int)height/Screenprop;


    public SplashScreen(){
        SplashScreen = new JFrame();
        System.out.println("Splash Created");
        drawSplash();
    }

    public void drawSplash(){
        ImageIcon background = new ImageIcon("Assets/TitleScreen.png");
        Image imgBackground = background.getImage();
        Image backgroundEdited = imgBackground.getScaledInstance(windowHeight, windowHeight, Image.SCALE_SMOOTH);
        ImageIcon backgroundFinished = new ImageIcon(backgroundEdited);
        SplashScreen.getContentPane().add(new JLabel(backgroundFinished));

        SplashScreen.setSize(windowHeight, windowHeight);
        SplashScreen.setLocationRelativeTo(null);
        SplashScreen.setUndecorated(true);
        SplashScreen.setVisible(true);
        SplashScreen.toFront();
    }

    public static void close(){
        SplashScreen.dispose();
    }
}
