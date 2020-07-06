import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ActiveWindowHighscore implements FocusListener {
    private boolean focus = false;
    private HighscoreMenu Parent;
    private JFrame MainWindow;
    public ActiveWindowHighscore(HighscoreMenu p){
        this.Parent=p;
        MainWindow=Parent.getMainWindow();
    }
    public boolean isFocus(){
        return focus;
    }
    public void focusGained(FocusEvent e) {
        System.out.println("Focus Gained");
        focus = true;
    }


    public void focusLost(FocusEvent e) {
        if(focus){
            System.out.println("Focus Lost");
            //MainWindow.dispose();
        }
    }
}
