import java.awt.*;
import javax.swing.*;


public class OptionsMenu extends Threaded{

    private JFrame MainWindow;
    private JFrame SoundSettings;
    private JPanel SoundSettingsPanel;
    private JPanel MainPanel;
    private Display parent;

    ActiveWindow f;

//  COULD BE CONVERTED TO ASSETPACK.
    private ImageIcon resume = new ImageIcon("Assets/new button--resume.png");
    private Image imgResume = resume.getImage();

    private ImageIcon sound = new ImageIcon("Assets/new button--sound.png");
    private Image imgSound = sound.getImage();

    private ImageIcon game = new ImageIcon("Assets/new button--highscore.png");
    private Image imgGame = game.getImage();

    private ImageIcon back = new ImageIcon("Assets/new button--back.png");
    private Image imgBack = back.getImage();

    private ImageIcon on = new ImageIcon("Assets/new button--on.png");
    private Image imgOn = on.getImage();

    private ImageIcon off = new ImageIcon("Assets/new button--off.png");
    private Image imgOff = off.getImage();

    Display getParent() {
        return parent;
    }

    void setParent(Display Parent) {
        parent = Parent;
    }


    public OptionsMenu(Display d) {
        MainWindow = new JFrame();
        f = new ActiveWindow(this);
        MainPanel = new JPanel();
        MainWindow.addFocusListener(f);
        setParent(d);
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void createOptionsmenu(Display d){
        MoveListener.oopen = true;
        d.getMasterThread().addThread(new OptionsMenu(d), 2);
        if(Settings.getDebug()) {
            System.out.println("OPTIONS: Thread Count " + d.getMasterThread().getThreads());
        }

    }


    public void run() {

        MainWindow.getContentPane().add(MainPanel);

        MainWindow.setSize(getParent().getWindowWidth() / 2, (int) ((parent.getWindowHeight() / 2) + ((parent.getWindowHeight() / 2) * 0.9)));
        MainWindow.setBackground(Color.WHITE);
        MainWindow.setLocation((int) ((parent.getWindowWidth() / 2) + ((parent.getWindowWidth() / 2) * 0.5)), (int) ((parent.getWindowHeight() / 2) + ((parent.getWindowHeight() / 2) * 0.2)));
        MainWindow.setResizable(false);
        MainWindow.setTitle("Settings");

        int windowHeight = MainWindow.getHeight();
        int windowWidth = MainWindow.getWidth();

        double[] widthSizeOffset = new double[3];
        double[] heightSizeOffset = new double[3];

        JButton[] MainButtons = new JButton[3];
        double[] widthLocationOffset = new double[3];
        double[] heightLocationOffset = new double[3];

        widthSizeOffset[0] = 0.7;
        heightSizeOffset[0] = 0.2;
        widthLocationOffset[0] = 0.5;
        heightLocationOffset[0] = 0.2;

        widthSizeOffset[1] = 0.7;
        heightSizeOffset[1] = 0.2;
        widthLocationOffset[1] = 0.5;
        heightLocationOffset[1] = 0.46;

        widthSizeOffset[2] = 0.7;
        heightSizeOffset[2] = 0.2;
        widthLocationOffset[2] = 0.5;
        heightLocationOffset[2] = 0.72;

        Image soundEdited = imgSound.getScaledInstance((int) ((double) windowWidth * widthSizeOffset[0]), (int) ((double) windowHeight * heightSizeOffset[0]), Image.SCALE_SMOOTH);
        ImageIcon soundFinished = new ImageIcon(soundEdited);
        MainButtons[0] = new JButton(soundFinished);
        Image gameEdited = imgGame.getScaledInstance((int) ((double) windowWidth * widthSizeOffset[1]), (int) ((double) windowHeight * heightSizeOffset[1]), Image.SCALE_SMOOTH);
        ImageIcon gameFinished = new ImageIcon(gameEdited);
        MainButtons[1] = new JButton(gameFinished);
        Image resumeEdited = imgResume.getScaledInstance((int) ((double) windowWidth * widthSizeOffset[2]), (int) ((double) windowHeight * heightSizeOffset[2]), Image.SCALE_SMOOTH);
        ImageIcon resumeFinished = new ImageIcon(resumeEdited);
        MainButtons[2] = new JButton(resumeFinished);

        int[] ButtonWindowWidth = new int[3];
        int[] ButtonWindowHeight = new int[3];

        for(int i=0; i<3; i++){
            MainButtons[i].setSize((int) ((double) windowWidth * widthSizeOffset[i]), (int) ((double) windowHeight * heightSizeOffset[i]));

            ButtonWindowWidth[i] = MainButtons[i].getWidth();
            ButtonWindowHeight[i] = MainButtons[i].getHeight();

            MainButtons[i].setOpaque(false);
            MainButtons[i].setContentAreaFilled(false);
            MainButtons[i].setBorderPainted(true);
            MainButtons[i].setFocusPainted(false);
            MainButtons[i].setLocation(((int) ((double) windowWidth * widthLocationOffset[i])) - ButtonWindowWidth[i] / 2, ((int) ((double) windowHeight * heightLocationOffset[i])) - ButtonWindowHeight[i] / 2);
            if(i == 0){
                MainButtons[0].addActionListener(e -> {
                    MainWindow.setVisible(false);
                    soundSettings();
                });
            }else if(i == 1){
                MainButtons[1].addActionListener(e -> {
                    HighscoreMenu.createHighscpre(parent, 1);
                });
            }else{
                MainButtons[2].addActionListener(e -> {
                    close();
                });
            }
            MainPanel.add(MainButtons[i]);
            MainWindow.add(MainButtons[i]);
        }
        MainWindow.setLayout(null);
        MainWindow.setVisible(true);
        MainWindow.toFront();

        if(Settings.getDebug()) {
            System.out.println("OPTIONS: Menu Created");
        }
    }

    public void close() {
        if(Display.timerOn) {
            Display.startTimer();
        }
        MainWindow.dispose();
        MoveListener.oopen = false;
    }

    public JFrame getMainWindow() {
        return MainWindow;
    }


    public void soundSettings() {
        MainWindow.setVisible(false);
        SoundSettings = new JFrame();
        SoundSettingsPanel = new JPanel();
        SoundSettings.setSize(getParent().getWindowWidth() / 2, (int) ((getParent().getWindowHeight() / 2)+ ((getParent().getWindowHeight() / 2 )* 0.9)));
        SoundSettings.setTitle("Sound");
        SoundSettings.getContentPane().add(SoundSettingsPanel);
        SoundSettings.setBackground(Color.WHITE);
        SoundSettings.setLocation((int) ((getParent().getWindowWidth()/2)+((getParent().getWindowWidth() /2)*0.5)), (int) ((getParent().getWindowHeight()/2)+((getParent().getWindowHeight() /2)*0.2)));
        SoundSettings.setResizable(false);
        SoundSettings.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        int windowHeight = SoundSettings.getHeight();
        int windowWidth = SoundSettings.getWidth();

        double[] widthSizeOffset = new double[6];
        double[] heightSizeOffset = new double[6];

        JLabel volumeLevel = new JLabel("Volume Level");
        JLabel currentVol = new JLabel(ThreadMaster.getValue() + "%");
        JSlider MainSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, ThreadMaster.getValue());
        MainSlider.setMajorTickSpacing(10);
        MainSlider.setPaintTicks(true);
        MainSlider.addChangeListener(e -> {
            ThreadMaster.setValue(MainSlider.getValue());
            currentVol.setText(ThreadMaster.getValue() + "%");
        });


        JButton[] MainButtons = new JButton[3];
        double[] widthLocationOffset = new double[6];
        double[] heightLocationOffset = new double[6];

        widthSizeOffset[0] = 0.7;
        heightSizeOffset[0] = 0.2;
        widthLocationOffset[0] = 0.5;
        heightLocationOffset[0] = 0.2;

        widthSizeOffset[1] = 0.3;
        heightSizeOffset[1] = 0.2;
        widthLocationOffset[1] = 0.3;
        heightLocationOffset[1] = 0.46;

        widthSizeOffset[2] = 0.3;
        heightSizeOffset[2] = 0.2;
        widthLocationOffset[2] = 0.7;
        heightLocationOffset[2] = 0.46;

        widthSizeOffset[3] = 0.7;
        heightSizeOffset[3] = 0.2;
        widthLocationOffset[3] = 0.75;
        heightLocationOffset[3] = 0.65;

        widthSizeOffset[4] = 0.7;
        heightSizeOffset[4] = 0.2;
        widthLocationOffset[4] = 0.5;
        heightLocationOffset[4] = 0.78;

        widthSizeOffset[5] = 0.7;
        heightSizeOffset[5] = 0.2;
        widthLocationOffset[5] = 0.80;
        heightLocationOffset[5] = 0.90;

        Image backEdited = imgBack.getScaledInstance((int) ((double) windowWidth * widthSizeOffset[0]), (int) ((double) windowHeight * heightSizeOffset[0]), Image.SCALE_SMOOTH);
        ImageIcon backFinished = new ImageIcon(backEdited);
        MainButtons[0] = new JButton(backFinished);
        Image onEdited = imgOn.getScaledInstance((int) ((double) windowWidth * widthSizeOffset[1]), (int) ((double) windowHeight * heightSizeOffset[1]), Image.SCALE_SMOOTH);
        ImageIcon onFinished = new ImageIcon(onEdited);
        MainButtons[1] = new JButton(onFinished);
        Image offEdited = imgOff.getScaledInstance((int) ((double) windowWidth * widthSizeOffset[2]), (int) ((double) windowHeight * heightSizeOffset[2]), Image.SCALE_SMOOTH);
        ImageIcon offFinished = new ImageIcon(offEdited);
        MainButtons[2] = new JButton(offFinished);

        int[] ButtonWindowWidth = new int[3];
        int[] ButtonWindowHeight = new int[3];

        for(int i = 0; i<3; i++) {
            MainButtons[i].setSize((int) ((double) windowWidth * widthSizeOffset[i]), (int) ((double) windowHeight * heightSizeOffset[i]));

            ButtonWindowWidth[i] = MainButtons[i].getWidth();
            ButtonWindowHeight[i] = MainButtons[i].getHeight();

            MainButtons[i].setLocation(((int) ((double) windowWidth * widthLocationOffset[i])) - ButtonWindowWidth[i] / 2, ((int) ((double) windowHeight * heightLocationOffset[i])) - ButtonWindowHeight[i] / 2);
            MainButtons[i].setOpaque(false);
            MainButtons[i].setContentAreaFilled(false);
            MainButtons[i].setBorderPainted(true);
            MainButtons[i].setFocusPainted(false);
            if(i == 0) {
                MainButtons[i].addActionListener(e -> {
                    System.out.println(ThreadMaster.getValue());
                    System.out.println(Settings.defaultVolume /100);
                    Settings.volume = Math.round(ThreadMaster.getValue() * (Settings.getDefaultVolume()/100));
                    System.out.println(Settings.getVolume());
                    ThreadMaster.setValue(ThreadMaster.getValue());
                    SoundSettings.dispose();
                    MainWindow.setVisible(true);
                });
            }else if(i == 1){
                MainButtons[1].addActionListener(e -> {
                    Settings.music = true;
                });
            }else{
                MainButtons[2].addActionListener(e -> {
                    Settings.music = false;
                });
            }
            SoundSettingsPanel.add(MainButtons[i]);
        }

        volumeLevel.setSize((int) ((double) windowWidth * widthSizeOffset[3]), (int) ((double) windowHeight * heightSizeOffset[3]));
        int volumeLevelWidth = volumeLevel.getWidth();
        int volumeLevelHeight = volumeLevel.getHeight();
        volumeLevel.setLocation(((int) ((double) windowWidth * widthLocationOffset[3])) - volumeLevelWidth / 2, ((int) ((double) windowHeight * heightLocationOffset[3])) - volumeLevelHeight / 2);

        SoundSettingsPanel.add(volumeLevel);

        MainSlider.setSize((int) ((double) windowWidth * widthSizeOffset[4]), (int) ((double) windowHeight * heightSizeOffset[4]));
        int sliderWidth = MainSlider.getWidth();
        int sliderHeight = MainSlider.getHeight();
        MainSlider.setLocation(((int) ((double) windowWidth * widthLocationOffset[4])) - sliderWidth / 2, ((int) ((double) windowHeight * heightLocationOffset[4])) - sliderHeight / 2);

        SoundSettingsPanel.add(MainSlider);

        currentVol.setSize((int) ((double) windowWidth * widthSizeOffset[5]), (int) ((double) windowHeight * heightSizeOffset[5]));
        int currentVolWidth = currentVol.getWidth();
        int curentVolHeight = currentVol.getHeight();
        currentVol.setLocation(((int) ((double) windowWidth * widthLocationOffset[5])) - currentVolWidth / 2, ((int) ((double) windowHeight * heightLocationOffset[5])) - curentVolHeight / 2);

        SoundSettingsPanel.add(currentVol);

        SoundSettingsPanel.setLayout(null);
        SoundSettings.setVisible(true);
        SoundSettings.toFront();
        if(Settings.getDebug()) {
            System.out.println("OPTIONS: Sound Menu Created");
        }
    }
}

