import org.jsfml.audio.Sound;
import org.jsfml.graphics.*;

import java.util.Timer;
import java.util.TimerTask;

public class Pickup extends Entity{
    public static Sound speedUp = new Sound();
    public static Sound slowDown = new Sound();
    Sound portalSound = new Sound();
    Sound increaseTime = new Sound();
    Sound reduceTime = new Sound();
    public static Sound lightUp = new Sound();
    public static Sound blowOut = new Sound();
    private int function;
    private Sprite logo;
    private static long endtime;
    private Display parent;
    public static Timer torchTimer;
    public TimerTask torchTask;
    public static Timer blindTimer;
    public TimerTask blindTask;
    public static Timer speedUpTimer;
    public static TimerTask speedUpTask;
    public static Timer slowDownTimer;
    public static TimerTask slowDownTask;
    public static int lightUpTimer =0;
    public static int blindValueTimer =0;
    public static int speedUpValue =0;
    public static int slowDownValue =0;
    public static int pickupID;

    private boolean torchtimerOn = false;
    private boolean blindtimerOn = false;
    private boolean speedUpTimerOn = false;
    private boolean slowDownTimerOn = false;

    /**
     * @param m     MAze to be added to
     * @param Location Space to be added ontop of
     * @param function  What this pickup will do
     *  Constructor to create entity pickup and sets its sprite according to function code
     */
    Pickup(Maze m, Node Location, int function){
        super(m,Location);
        parent = m.getParent();
        logo=new Sprite();
        try {
            speedUp=AssetPack.getSound("speedUp.wav");
            slowDown=AssetPack.getSound("slowDown.wav");
            portalSound=AssetPack.getSound("soundPortal.wav");
            blowOut=AssetPack.getSound("Blow Sound Effect.wav");
            lightUp=AssetPack.getSound("Match Light.wav");
            increaseTime=AssetPack.getSound("increaseTime.wav");
            reduceTime=AssetPack.getSound("reduceTime.wav");

        }catch (NullPointerException e) {
            System.out.println("Sounds not yet instantiated through AssetPack.");
        }
        this.function=function;
    }

    /**
     * calls the suitable method to its function code.  This is called by a Player when they find or get this Pickup
     */
    public void doFunction(){
        //System.out.println("SWITcHING FUNKINESS");
        switch(function) {
            case 0:  // ends maze and calls next level
                long size = parent.getSize();
                if(size==12){
                    Display.clockTimer.cancel();
                    Display.clockTimer.purge();
                    endtime = Display.totaltime + Display.clockTime;
                    //long time = parent.getTime();
                    //endtime = (System.currentTimeMillis() -time)/1000;
                    System.out.println("TIMER: "+endtime);
                    parent.stopGame();
                    HighscoreMenu.createHighscpre(parent, 0);
                }else{
                    if(torchtimerOn) {
                        torchTimer.cancel();
                        torchTimer.purge();
                    }
                    if(blindtimerOn) {
                        blindTimer.cancel();
                        blindTimer.purge();
                    }
                    if(speedUpTimerOn){
                        speedUpTimer.cancel();
                        speedUpTimer.purge();
                    }
                    if(slowDownTimerOn){
                        slowDownTimer.cancel();
                        slowDownTimer.purge();
                    }
                    Display.clockTimer.cancel();
                    Display.clockTimer.purge();
                    Display.totaltime = Display.totaltime + Display.clockTime;
                    Display.clockTime = 0;
                    portalSound.play();
                    Context.getParent().nextLevel();
                }
                break;      //finishes level and calls new on

            case 1:
                portalSound.play();
                Context.getParent().newGame();      // new game  -starts new level
                break;

            case 2:                       // options menu         opens options menu
                if (Context.getParent().getMazeState().equals("SHOWN")) {

                    Context.getParent().OptionsMenu();
                }
                else{
                    System.out.println("ERROR : BAD PICKUP FOUND ");
                }
                break;

            case 3:                       //quit game
                //quit game
                Context.getParent().close();        //
                break;

            case 4:
                //powerup that increases view range
                try {
                    lightUp.play();
                }
                catch(NullPointerException e ){
                    if(Settings.debug){
                        System.out.println("PICKUP : NOT FOUND LIGHT UP SOUND ");
                    }
                }
                Context.setViewRange(Context.getViewRange() + 1);
                pickupID = 4;

                torchTimer = new Timer();
                torchtimerOn = true;
                torchTask = new TimerTask() {
                    public void run() {
                        lightUpTimer ++;
                    }
                };
                torchTimer.scheduleAtFixedRate(torchTask,0,1000);
                break;

            case 5:
                //buff increases speed
                speedUp.play();
                Context.getUser().setSpeed(6);
                pickupID = 5;

                speedUpTimer = new Timer();
                speedUpTimerOn = true;
                speedUpTask = new TimerTask() {
                    public void run() {
                        speedUpValue ++;
                    }
                };
                speedUpTimer.scheduleAtFixedRate(speedUpTask,0,1000);

                break;

            case 6:
                //debuff that decreases speed
                slowDown.play();
                Context.getUser().setSpeed(2);
                pickupID = 6;

                slowDownTimer = new Timer();
                slowDownTimerOn = true;
                slowDownTask = new TimerTask() {
                    public void run() {
                        slowDownValue ++;
                    }
                };
                slowDownTimer.scheduleAtFixedRate(slowDownTask,0,1000);
                break;

            case 7:
                //debuff that decreases viewrange
                blowOut.play();
                if(Context.getViewRange() > 2) {
                    Context.setViewRange(Context.getViewRange() - 2);
                } else if (Context.getViewRange()==2){
                    Context.setViewRange(Context.getViewRange() - 1);
                }
                pickupID = 7;

                blindTimer = new Timer();
                blindtimerOn = true;
                blindTask = new TimerTask() {
                    public void run() {
                        blindValueTimer ++;
                    }
                };
                blindTimer.scheduleAtFixedRate(blindTask,0,1000);
                break;

            case 8:
                //decreases time
                reduceTime.play();
                if(Display.clockTime<10){
                    Display.clockTime = 0;
                }else {
                    Display.clockTime = Display.clockTime - 10;
                }
                break;

            case 9:
                //increases time
                increaseTime.play();
                Display.clockTime = Display.clockTime +10;
        }
    }

    public static long getEndtime(){
        return endtime;
    }

    /**
     * @param location the new Location of this pickup
     *                 if theres an old location it empties that and sets the new place as having this pickup
     */
    public void setLocation(Node location) {
        if(Location!=null && Location.getHas()==this){Location.empty();}
        Location = location;
        Location.setHas(this);
    }


    /**
     * @return the function code of this pickup
     */
    int getFunction() {
        return function;
    }

    /**
     * @return Overrides method in entity for determining subclass
     */
    public boolean isPickup(){return true;}

    /**
     * @return Sprite for this Pickup, typically used for setting as the texture of the tile its sat on.
     */
    public Sprite getSprite(){


        return logo;
    }
    public Texture getText(){
        switch(this.function) {
            case 0:         //next level
                return AssetPack.getTexture("portal.png");
                //System.out.println("DRAWN");

            case 1:
                return AssetPack.getTexture("StartLogo.png");
                //System.out.println("DRAWN");

            case 2:
                return AssetPack.getTexture("SettingsIcon.png");
                //System.out.println("DRAWN");
            case 3:
                return AssetPack.getTexture("EXIT.png");
                //System.out.println("DRAWN");
            case 4:
                return AssetPack.getTexture("Torch.png");
            case 5:
                return AssetPack.getTexture("speedup.png");
            case 6:
                return AssetPack.getTexture("speeddown.png");
            case 7:
                return AssetPack.getTexture("blindpick.png");
            case 8:
                return AssetPack.getTexture("clockPickup.png");
            case 9:
                return AssetPack.getTexture("sandtimer.png");
            default:
                return AssetPack.getTexture("backwall.png");

        }
    }

    /**
     * @return the Drawable to be added to the screen, in this instance it grabs the drawable of the sprite location, not directly the sprite of the pickup
     */
    public Drawable getDrawable(){
        return Location.getDrawable();
    }
}
