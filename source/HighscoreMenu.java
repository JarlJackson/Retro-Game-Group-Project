import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.*;
import java.lang.*;

public class HighscoreMenu extends Threaded {

    private JFrame MainWindow;
    private JPanel MainPanel;
    private Display parent;
    private int id;
    private int highscore;
    private String highscoreName = "";
    private long score = Pickup.getEndtime();
    private String newName;
    private long newScore;

    private ImageIcon back = new ImageIcon("Assets/new button--back.png");
    private Image imgBack = back.getImage();

    private ImageIcon exit = new ImageIcon("Assets/new button--exit.png");
    private Image imgExit = exit.getImage();


    Display getParent() {
        return parent;
    }

    void setParent(Display Parent) {
        parent = Parent;
    }

    public HighscoreMenu(Display d, int id) {
        MainWindow = new JFrame();
        MainPanel = new JPanel();
        setParent(d);
        this.id = id;
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (id == 0){
            highscoreName = ThreadMaster.highscoreName;
            highscore = ThreadMaster.highscore;
            checkScore();
        }
        if (id == 1){
            highscoreName = ThreadMaster.highscoreName;
            highscore = ThreadMaster.highscore;
        }
    }

    public static void createHighscpre(Display d, int id){
        MoveListener.hopen = true;
        d.getMasterThread().addThread(new HighscoreMenu(d,id), 3);
        if(Settings.getDebug()) {
            System.out.println("HIGHSCORE: Thread Count " + d.getMasterThread().getThreads());
        }
    }


    public void run() {

        MainWindow = new JFrame();
        MainPanel = new JPanel();
        MainWindow.setResizable(false);
        MainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        MainWindow.getContentPane().add(MainPanel);

        MainWindow.setSize(getParent().getWindowWidth() / 2, (int) ((parent.getWindowHeight() / 2) + ((parent.getWindowHeight() / 2) * 0.9)));
        MainWindow.setBackground(Color.WHITE);
        MainWindow.setLocation((int) ((parent.getWindowWidth() / 2) + ((parent.getWindowWidth() / 2) * 0.5)), (int) ((parent.getWindowHeight() / 2) + ((parent.getWindowHeight() / 2) * 0.2)));
        MainWindow.setResizable(false);
        MainWindow.setTitle("High Score");
        MainPanel.setLayout(null);

        int windowHeight = MainWindow.getHeight();
        int windowWidth = MainWindow.getWidth();

        double backwidthSizeOffset = 0.7;
        double backheightSizeOffset = 0.2;
        double backwidthLocationOffset = 0.5;
        double backheightLocationOffset = 0.8;

        Image backEdited = imgBack.getScaledInstance((int) ((double) windowWidth * backwidthSizeOffset), (int) ((double) windowHeight * backheightSizeOffset), Image.SCALE_SMOOTH);
        ImageIcon backFinished = new ImageIcon(backEdited);
        JButton back = new JButton(backFinished);

        Image exitEdited = imgExit.getScaledInstance((int) ((double) windowWidth * backwidthSizeOffset), (int) ((double) windowHeight * backheightSizeOffset), Image.SCALE_SMOOTH);
        ImageIcon exitFinished = new ImageIcon(exitEdited);
        JButton exit = new JButton(exitFinished);

        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(true);
        exit.setFocusPainted(false);

        exit.setSize((int) ((double) windowWidth * backwidthSizeOffset), (int) ((double) windowHeight * backheightSizeOffset));
        int exitWidth = exit.getWidth();
        int exitHeight = exit.getHeight();
        exit.setLocation((int) ((double) windowWidth * backwidthLocationOffset) - exitWidth / 2, ((int) ((double) windowHeight * backheightLocationOffset)) - exitHeight / 2);

        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(true);
        back.setFocusPainted(false);
        back.addActionListener(e -> MainWindow.dispose());

        back.setSize((int) ((double) windowWidth * backwidthSizeOffset), (int) ((double) windowHeight * backheightSizeOffset));
        int backWidth = back.getWidth();
        int backHeight = back.getHeight();
        back.setLocation((int) ((double) windowWidth * backwidthLocationOffset) - backWidth / 2, ((int) ((double) windowHeight * backheightLocationOffset)) - backHeight / 2);

        JLabel title = new JLabel("HighScore");

        double titlewidthSizeOffset = 0.7;
        double titleheightSizeOffset = 0.2;
        double titlewidthLocationOffset = 0.75;
        double titleheightLocationOffset = 0.05;

        title.setSize((int) ((double) windowWidth * titlewidthSizeOffset), (int) ((double) windowHeight * titleheightSizeOffset));
        int titleWidth = title.getWidth();
        int titleHeight = title.getHeight();
        title.setLocation(((int) ((double) windowWidth * titlewidthLocationOffset)) - titleWidth / 2, ((int) ((double) windowHeight * titleheightLocationOffset)) - titleHeight / 2);

        MainPanel.add(title);

        double scoreLabelwidthSizeOffset = 0.8;
        double scoreLabelheightSizeOffset = 0.5;
        double scoreLabelwidthLocationOffset = 0.5;
        double scoreLabelheightLocationOffset = 0.20;

        if(id == 0) {
            JLabel scoreLabel = new JLabel("<html> Name: " + newName + "<br/>" + "Score: " + newScore + "</html>");
            scoreLabel.setFont(new Font("FreeSans", Font.PLAIN, 50));
            scoreLabel.setSize((int) ((double) windowWidth * scoreLabelwidthSizeOffset), (int) ((double) windowHeight * scoreLabelheightSizeOffset));
            int scoreWidth = title.getWidth();
            int scoreHeight = title.getHeight();
            scoreLabel.setLocation(((int) ((double) windowWidth * scoreLabelwidthLocationOffset)) - scoreWidth / 2, ((int) ((double) windowHeight * scoreLabelheightLocationOffset)) - scoreHeight / 2);

            MainPanel.add(scoreLabel);
        }
        if (id == 1) {
            JLabel scoreLabel = new JLabel("<html> Name: " + highscoreName + "<br/>" + "Score: " + highscore + "</html>");
            scoreLabel.setFont(new Font("FreeSans", Font.PLAIN, 50));
            scoreLabel.setSize((int) ((double) windowWidth * scoreLabelwidthSizeOffset), (int) ((double) windowHeight * scoreLabelheightSizeOffset));
            int scoreWidth = title.getWidth();
            int scoreHeight = title.getHeight();
            scoreLabel.setLocation(((int) ((double) windowWidth * scoreLabelwidthLocationOffset)) - scoreWidth / 2, ((int) ((double) windowHeight * scoreLabelheightLocationOffset)) - scoreHeight / 2);

            MainPanel.add(scoreLabel);
        }

        if (id == 0) {
            exit.addActionListener(e -> close());
            MainPanel.add(exit);
        }

        if (id == 1) {
            MainPanel.add(back);
        }

        MainWindow.setVisible(true);
        MainWindow.toFront();

        if (Settings.getDebug()) {
            System.out.println("HighScore Menu Created");
        }

    }

    public void checkScore(){
        if(score < highscore) {
            String name = JOptionPane.showInputDialog("New HIGHSCORE! what is your name ?");
            newName = name;
            newScore = score;
            if (Settings.getDebug()) {
                System.out.println("End Time" + Pickup.getEndtime());
            }
            File scoreFile = new File("source/highscore.txt");
            if (!scoreFile.exists()) {
                try {
                    scoreFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileWriter writeFile;
            BufferedWriter writer = null;
            try {
                writeFile = new FileWriter(scoreFile);
                writer = new BufferedWriter(writeFile);
                writer.write(name+":"+score);
            } catch (Exception e) {

            } finally {
                try {
                    if (writer != null)
                        writer.close();
                } catch (Exception e) {
                }

            }
        }else{
            newName = highscoreName;
            newScore = highscore;
        }
    }

    public void close() {
        MainWindow.dispose();
        System.exit(0);
    }

    public JFrame getMainWindow() {
        return MainWindow;
    }
}
