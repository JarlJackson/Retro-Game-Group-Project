import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundSource;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import java.util.ArrayList;


public class Player extends Entity implements Playable {
    private ArrayList<Item> HasFound=new ArrayList<>();
   // private MoveListener move;
   // private Thread t;
    boolean WhichSprite=true;
    String[] directions={"up","right","down","left"};

    private boolean playing = false;

    Sound walkingSound = new Sound();
    MoveListener mover;
    int Facing=0;
    int speed = 4;
    Vector2f Location;
    Sprite Me=new Sprite();
    Display Show;
    float SCALE=(float)1.5;
    public int getFacing() {
        return Facing;
    }


    /**
     * The Constructor for player recieves the Maze to add the player into and a start Location, it then generates sprites and sounds and creates a new movement listener.
     *
     * @param m
     * @param Location
     */
    public Player(Maze m, Node Location){
        super(m, Location);
        //monitors key presses
        this.Location=Location.getCentre();
        Show=m.getParent();
        mover=new MoveListener(this);

        Me.setScale(SCALE , SCALE);
        walkingSound=AssetPack.getSound("soundWalking.wav");

        if(Settings.getDebug()){
            System.out.println("PLAYER: Player Added");
        }

    }


    public void setScale(int I){
        SCALE= (float)2.8-((float)I*(float)0.1);
        Me.setScale(SCALE,SCALE);

    }
    /**
     * increaseSpacer is the method that chiefly runs the main movement of the player sprite, this works by moving the exact vector of the sprite location and then checking for collisions against walls and whether the player enters a new tile.
     * @param incr
     */

    public void increaseSpacer(int incr){
       // Me.move(((Facing+2)%4 )-2 * incr *(( Facing ) %2) ,((Facing+3)%4 )-2 * -incr *(( Facing+1 ) %2) );
        int xmod=0;
        int ymod=0;

        switch(Facing){
            case 0:
                ymod-=incr;
                break;
            case 1:
                xmod+=incr;
                break;
            case 2:
                ymod+=incr;
                break;
            case 3:
                xmod-=incr;
                break;
        }
        nextSprite();
        Vector2f old=Location;
        Location=new Vector2f((Location.x+Show.getWindowWidth() +xmod )%Show.getWindowWidth(),(Location.y+ymod)%Show.getWindowHeight());//new Vector2i(Location.x + xmod , Location.y +ymod);
        if (Location.y<0 & Context.getStyle()!=1) Location=new Vector2f(Location.x,0);
        Neighbour nextSpace=this.getLocation().getNeighbours()[Facing];
        Context.getNodesInRange(Context.NodetoInt(this.getLocation()),Context.getViewRange()).forEach((T)->{
           T.getWallColls().forEach((N)-> {
              if(N.intersection(new FloatRect(Location,getSpriteSize()))!=null) Location=old;
           });
           if(T.isInside(this) && T!=getLocation()&&T.equals(nextSpace)){//if buggy remove .nextspace

              this.Step(T);
              if (Settings.getDebug() & Settings.loop) {
                  System.out.println("PLAYER: STEPPED");
              }
           }


        });

    }






      /*  */



    /**
     * Method returns the exact x,y vector of the top left corner of the player
     * @return Location
     */
    public Vector2f getSpriteLocation(){
        return Location;
    }

    /**
     * Returns the Display Object that the player exists in by extension of being in a maze
     * @return Display
     */
    public Display getShow(){
        return Show;
    }
    /*
    * the move method takes in an int from an action listener 0-3, or 4 = random
    *
    *
     */


    /**
     * @param Direction
     * Recieves a Direction integer (0-up, 1-right, 2-down, 3-left) and returns if its a valid move to jump to that tile
     * @return
     */
    public boolean move(int Direction){
        setFacing(Direction);
        increaseSpacer(speed);
        if(!playing & !Display.endGame) {
            playing = true;
            if(Settings.getDebug() & Settings.getLoop()){
                System.out.println("STATUS = "+walkingSound.getStatus());
            }
            walkingSound.play();
            walkingSound.setVolume(60);
        }
        if(walkingSound.getStatus() == SoundSource.Status.STOPPED){
            playing = false;
        }
        if(Direction<4 && Direction>=0 && !this.getLocation().returnNeighbours()[Direction]){
            return true;
        }
        return false;
    }

    /**
     * takes in a Node location and then sets the location of the player to that node, the assumption is that this is used to jump between neighbouring nodes.
     * @param newLocation
     */
    private void Step(Node newLocation){

        //this.checksLocation(this,newLocation);
        newLocation.doFunction();
        this.get((Pickup)newLocation.getHas());
        if(Context.getStyle()==0)this.getLocation().empty();
        this.setLocation(newLocation);   //checks if move valid then sets the player location to the new nodes place.
    }

    /**
     * Recieves a direction integer (0-up, 1-right, 2-down, 3-left), which then sets the players facing direction to that integer which inturn will affect sprite generation
     * @param Direction
     */
    private void setFacing(int Direction){
        Facing=Direction %4;
    }

    /**
     * This method returns the name of the sprite Texture file currently being used.
     * @return String Filename
     */
    public String getSpriteName(){
        String name;
        String prefix="NEW";
        if(WhichSprite) name= prefix+directions[Facing]+"1";
        else name= prefix+directions[Facing]+"2";
        //System.out.println(name);
        return name+".png";
    }

    /**
     * this is used to toggle the sprite for the walking animation
     */
    public void nextSprite(){
        WhichSprite=!WhichSprite;
    }

    /**
     * @param Direction this is used to turn the character around
     */
    private void turn(int Direction){
        Facing= (Facing-(Direction-2))%4;
    }

    /**
     * @param found
     * gives the player the @Pickup that is passed to the function, calling any attached method too.
     */
    public void get(Pickup found){
        if(found!=null){
            HasFound.add(found);
        }//found.doFunction();

    }

    /**
     * @return is used for when given an entity, this overwrites the method to show that this @Entity is in fact a @Player
     */
    public boolean isPlayer(){return true;}


    /**
     * @return returns the current sprite, which encapsulates this inside the @Sprite Object.
     */
    public Sprite getSprite() {
        Me.setTexture(AssetPack.getTexture(this.getSpriteName()));

        Me.setPosition(Location);

        return Me;
    }

    /**
     * @return returns the @Vector2i of the Sprite size.
     */
    public Vector2f getSpriteSize(){
        return Vector2f.mul(new Vector2f(getSprite().getTexture().getSize()),SCALE);
    }

    /**
     * returns the Drawable of this class
     * @return
     */
    public Drawable getDrawable(){

        return getSprite();

    }
    public Drawable getSpot(){
        Vector2f buffer=Vector2f.mul(new Vector2f(Context.getCellsize()),(float)2);
        int viewDiameter=Context.getViewRange()*2+1;
        Vector2f size=Vector2f.add(buffer,new Vector2f(Vector2i.mul(Context.getCellsize(),viewDiameter)));
        RectangleShape a=new RectangleShape(size);
        a.setPosition(Vector2f.sub(getSpriteLocation() , Vector2f.div(size,2)));
        a.setTexture(AssetPack.getTexture("test.png"));
        return a;
    }
    /**
     * returns the default value of incr used by increaseSpacer, how far the player moves per call
     * @return
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed
     * Sets the default value of incr used by increaseSpacer, how far the player moves per call
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
