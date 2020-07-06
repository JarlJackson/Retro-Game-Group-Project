import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundSource;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*this class is to create and draw to console the maze,
it instantiates player icons and makes the maze whilst calling the player move function upon a keypress.
it is important that the player and pickups are made with the constructors in Maze ( AddUser() and addPickup() etc....)  rather than their own constructor, as that will take many extra steps
 */
public class Display extends Threaded {
	// get window properties
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double height = screenSize.getHeight();
	private double width = screenSize.getWidth();
	//Calculate Arena Properties

	private int Screenprop = 2;
	private int windowHeight = (int) height / Screenprop;
	private int windowWidth = (int) width / Screenprop;
	private RenderWindow window;

	private Vector2i cellSize;


	private ArrayList<Drawable> Spaces = new ArrayList<>();
	private ArrayList<Drawable> Lines = new ArrayList<>();


	//make RenderWindows


	// initiate Maze, player and end point,  by default every display should have these,
	private Maze Demo;
	private Player User;


	private String MazeState = "SHOWN";
	private int MazeW;
	private int MazeH;
	private int viewRange;
	private long time;
	private ThreadMaster t;
	private Text Instruction;
	private Text Timer;
	private int Size;
	private int Style;
	public static Timer clockTimer;
	private static TimerTask clockTask;
	public static int clockTime =0;
	public static int totaltime = 0;
	public static boolean timerOn = false;
	public static boolean endGame;

	/**
	 * @return The State of the maze at current time , always returns "SHOWN" or "HIDDEN"
	 */
	String getMazeState() {
		return MazeState;
	}

	/**
	 * @return Size as Vector2i of each space
	 */
	Vector2i getCellSize() {
		return cellSize;
	}

	/**
	 * @return Height in pixels of the windows
	 */
	public int getWindowHeight() {
		return windowHeight;
	}

	/**
	 * @return the Width in pixels of the window
	 */
	public int getWindowWidth() {
		return windowWidth;
	}

	/**
	 * Constructor which upon making and adding items into the maze awaits key presses and is the main loop of the code
	 **/

	Display(ThreadMaster t) {
		this(t, 8, 1);
	}

	/**
	 * @param t     the ThreadMaster to add threads to
	 * @param Size  The size of the new maze
	 * @param Style The Style of it  as Int, 1 or 0
	 */
	private Display(ThreadMaster t, int Size, int Style) {
		this.t = t;
		this.Size = Size;
		this.Style = Style;
		window = new RenderWindow(new VideoMode(windowWidth, windowHeight), "Aion Mazes of Time", 1);
		new AssetPack();


			Instruction = new Text(" Use Arrow Keys to Move \n Esc to access Options \n Good Luck! ", AssetPack.getFont(), 24);
			Instruction.setColor(Color.WHITE);

		Instruction.setPosition(windowWidth/2,windowHeight/2);

		try {
			Node.setTextures();

		} catch (NullPointerException e) {
			System.out.println("DISPLAY: Relevant files not in directory //Assets");
		}

		if(Settings.debug && Settings.loop){
			System.out.println("DISPLAY; Window Created + Music Playing");
		}
	}
	//public void updateMainWindow(){ this.window=t.getMainWindow(); }

	/**
	 * Run() method, PopulatesMaze should this be initialized as a ThreadedRunnabe
	 */
	public void run() {
		populateMaze(Size, Style);
	}


	/**
	 * @param Size  The Size of the maze to be populated
	 * @param Style the Style an int for Shown/ hidden  i.e if menu or not
	 */
	//calls draw method for varying out puts so can be quickly toggled by flag
	private void populateMaze(int Size, int Style) {
		if (!(Size > 4 && Size < 120)) {
			Size = 8;
		}
		//make maze
		if (Style == 0) MazeState = "HIDDEN";
		int cellWidth = windowWidth / Size;
		int cellHeight = windowHeight / Size;
		cellSize=new Vector2i(cellWidth, cellHeight);
		Demo = new Maze(Size,Style,this);
		MazeW=Demo.getWidth();
		MazeH=Demo.getHeight();
		if(Settings.debug) {
			System.out.println("DISPLAY: Maze Size before user = " + Demo.getGrid().size());
		}
		User = Demo.addUser();
		User.setScale(Size);
		if(Settings.debug) {
			System.out.println("DISPLAY: Maze Size after user = " + Demo.getGrid().size());
		}
		Demo.getEndGame();


		viewRange=2;
		if(Settings.debug) {
			System.out.println("DISPLAY: Maze Created STYLE = " + Style);
		}
		DrawMaze();
	}
	//calls suitable draw method for maze based on the flag at top being "SHOW" or "HIDDEN"

	/**
	 * While thread is open and hasnt been paused or stopped draws the maze game to the string
	 */
	private  void DrawMaze(){
		while(window.isOpen() && Demo!=null && Demo.Playing && !Stop) {
			while (!Pause) {
				GUIDrawMaze();
				if(Settings.debug && Settings.loop){
					System.out.println("DISPLAY: Polling Events + Drawing Maze GUI " + "Maze Size: " + Demo.getNodesInRange(User.getIntLocation(), Demo.getViewRange()).size());
				}
				for(Event event: window.pollEvents()){
					switch (event.type){
						case MOUSE_BUTTON_PRESSED:
							break;
					}
				}
			}
		}
	}


	/**
	 * Collects and dtaws all entities and walls to the screen
	 */
	//Draws maze in new GUI window
	private synchronized void GUIDrawMaze(){
		window.clear(Color.BLACK);
		Lines.clear();
		Spaces.clear();

		//makes render list
		Demo.getNodesInRange(User.getIntLocation(), Demo.getViewRange()).forEach(j ->  {     //goes through each space.
			//adds space to render list
			//if(Settings.debug && j.getHas()!=null){
			//	System.out.println("DISPLAY: Menu Item Added");
			//}
			Spaces.add(j.getDrawable());
			Lines.addAll(j.getWalls());

		});
		Spaces.add(User.getDrawable());



		Spaces.forEach((n)-> window.draw(n));
		Lines.forEach((n)-> window.draw(n));
		if(Demo.getStyle()==0){
			window.draw(User.getSpot());
			window.draw(Timer);
		}
		else window.draw(Instruction);
			//window.draw(SpotLight);
		if(Settings.debug && Settings.loop){
			System.out.println("DISPLAY: GUI ADDED DRAWING WINDOW");
		}
		if(getSound().getStatus() == SoundSource.Status.STOPPED && Settings.getMusic()){
			getSound().play();
			getSound().setVolume(Settings.getVolume());
		}else if(!Settings.getMusic()){
			getSound().stop();
		}
		if(ThreadMaster.getValue() != 101){
			getSound().setVolume(Settings.getVolume());
		}
		if(Pickup.lightUpTimer == 8 & Pickup.pickupID == 4){
			Pickup.lightUpTimer = 0;
			Demo.setViewRange(Demo.getViewRange()-1);
			Pickup.blowOut.play();
			Pickup.torchTimer.cancel();
			Pickup.torchTimer.purge();
			if(Settings.debug) {
				System.out.println("Timer Hit ID 4");
			}
		}
		if(Pickup.speedUpValue == 5 & Pickup.pickupID == 5) {
			Pickup.speedUpValue = 0;
			Demo.getUser().setSpeed(4);
			Pickup.slowDown.play();
			Pickup.speedUpTimer.cancel();
			Pickup.speedUpTimer.purge();
			if (Settings.debug) {
				System.out.println("Timer HIT ID 5");
			}
		}
		if(Pickup.slowDownValue == 5 & Pickup.pickupID == 6) {
			Pickup.slowDownValue = 0;
			Demo.getUser().setSpeed(4);
			Pickup.speedUp.play();
			Pickup.slowDownTimer.cancel();
			Pickup.slowDownTimer.purge();
			if (Settings.debug) {
				System.out.println("Timer HIT ID 6");
			}
		}
		if(Pickup.blindValueTimer == 5 & Pickup.pickupID == 7){
			Pickup.blindValueTimer = 0;
			Demo.setViewRange(2);
			Pickup.lightUp.play();
			Pickup.blindTimer.cancel();
			Pickup.blindTimer.purge();
			if(Settings.debug) {
				System.out.println("Timer HIT ID 7");
			}
		}
		Timer = new Text(String.valueOf(clockTime),AssetPack.getFont(),24);
		Timer.setPosition(10,10);
		Timer.setColor(Color.WHITE);
		window.display();
	}

	/**
	 * Called to close the current window
	 */
		// called to close the current window
	void close(){							// closes JSFML window though this simply freezes?
		endGame();
		if(window.isOpen()){
			window.close();
			if(Settings.getDebug()) {
				System.out.println("DISPLAY: Window Closed");
			}
			System.exit(0);
		}
	}

	/**
	 * Closes currentmap ahead of making a new one.
	 */
	//closes current map and makes new one.
	void endGame(){
		if(Settings.debug) {
			System.out.println("DISPLAY: EndGame Hit");
		}
		//Stop();

		Demo.endGame();
        //t.killRunning(User.mover);
        //User=null;
		//Demo=null;
		t.killAll();
		//Demo.getMoveListener().Stop();

	}

	void stopGame(){
		if(Settings.debug) {
			System.out.println("DISPLAY: EndGame Hit");
		}
		Stop();
		Demo.endGame();
		t.killRunning(User.mover);
		t.killAll();
		endGame = true;
		window.clear(Color.WHITE);
		window.setVisible(false);

	}

	/**
	 * calls the next level, 1 bigger than current
	 */
	void nextLevel(){
		if(Settings.debug) {
			System.out.println("DISPLAY: Next Level Hit");
		}
		Size=MazeW+1;
		Style=0;
		endGame();
		startTimer();
		run();
		//t.addThread(this);
	}


	/**
	 * Stops the game and deletes the user
	 */
	void reset(){
		Demo.Playing=false;
		User=null;

	}
	//creates new level

	/**
	 * creates a new game, sets the size to 8, then ends anything current and regenerates the maze
	 */
	void newGame(){

		if(Settings.debug) {
			System.out.println("DISPLAY: New Game hit");
		}
		Size=8;
		Style=0;
		endGame();
		startTimer();
		time = System.currentTimeMillis();
		System.out.println(time);
		run();
	}

	public long getTime(){
		System.out.println(time + "disp");
		return time;
	}

	public long getSize(){
		System.out.println(Size);
		return Size;
	}

	public static void startTimer(){
		clockTimer = new Timer();
		clockTask = new TimerTask() {
			public void run() {
				clockTime ++;
			}
		};
		clockTimer.scheduleAtFixedRate(clockTask,0,1000);
		timerOn = true;
	}

	/**
	 * Calls an optionsmenu
	 */
	//opens options menu
	void OptionsMenu(){
		OptionsMenu.createOptionsmenu(this);
	}

	/**
	 * @return Threadmaster running this
	 */
	ThreadMaster getMasterThread(){
		return t;
	}

	/**
	 * @return gets the gamesound being played
	 */
	public Sound getSound(){
		return AssetPack.getSound("musicGame.wav");
	}
}