import org.jsfml.window.Keyboard;



public class MoveListener extends Threaded {
    private Player p;
    public static boolean oopen = false;
    public static boolean hopen = false;

    /**
     * @param p Player to affect when keys pressed
     * adds itself to the list of threads the players display exists in,
     */
    MoveListener(Player p) {
        this.p = p;
        p.getShow().getMasterThread().addThread(this, 1);
    }


    /**
     * Run method, while not paused or stopped and while the main window is open checks for keypresses and moves the player accordingly,
     */
    public void run() {
        while (!Stop && p.getContext().Playing && p.getContext() != null) {
            while (!Pause) {

                if (Keyboard.isKeyPressed(Keyboard.Key.UP)) {
                    // left key is pressed: move our character
                    p.move(0);
                    hold();
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) {
                    // left key is pressed: move our character
                    p.move(1);
                    hold();
                }

                if (Keyboard.isKeyPressed(Keyboard.Key.DOWN)) {
                    // left key is pressed: move our character
                    p.move(2);
                    hold();

                }
                if (Keyboard.isKeyPressed(Keyboard.Key.LEFT)) {
                    // left key is pressed: move our character
                    p.move(3);
                    hold();


                }
                if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && !oopen) {
                    if (Settings.getDebug()) {
                        System.out.println("MOVE LISTENER: Escape Pressed");
                    }
                    synchronized (this) {
                        if (Settings.getDebug()) {
                            System.out.println("MOVE LISTENER: Lock Entered");
                        }
                        if (!oopen) {
                            oopen = true;
                            if (Settings.getDebug()) {
                                System.out.println("MOVE LISTENER: Open = true & Open Entered PAUSE = " + Pause);
                            }
                            if(Display.timerOn) {
                                Display.clockTimer.cancel();
                                Display.clockTimer.purge();
                            }
                            OptionsMenu.createOptionsmenu(p.getShow());
                        }
                    }
                }
            }
        }
    }


    /**
     * Used to pause this thread for a small amount of time to add slight delay to the keypresses to make movement smooth as opposed to instant
     */
    private void hold(){
        try {

            Thread.sleep(20);
            //System.out.println("sleep");
        } catch (Exception e) {
                    System.out.println(e);

        }
    }
}

