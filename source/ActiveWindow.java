import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ActiveWindow implements FocusListener {
    private boolean focus = false;
    private OptionsMenu Parent;
    private JFrame MainWindow;

    /**
     * @param p Creates a Active window for the OptionsMenu given
     *          this is to allow us to check if its in focus or not.
     */
    public ActiveWindow(OptionsMenu p){
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
