import javax.swing.*;
import java.awt.*;

public class GameUI {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private double height = screenSize.getHeight();
    private double width = screenSize.getWidth();

    public void drawGame(){

        JFrame gameWindow = new JFrame();
        JPanel gamePanel = new JPanel();
        gameWindow.setSize((int)width/2,(int)height/2);
        gameWindow.setTitle("Ranger");
        gameWindow.getContentPane().add(gamePanel);
        gameWindow.setBackground(Color.WHITE);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
