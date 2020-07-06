/*
This class implements MazeInterface to generate a solvable maze (with random start and end point) of a size determinged by the user
this was coded by Stephen Mander as is. 
*
*

*/


import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import org.jsfml.system.Vector2i;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Maze implements MazeInterface{
	private int size;
	private int height;
	private int width;
	private int tilecount;
	private Random a=new Random();
	private Player User;
	private int StartLocation=-1;
	private int EndLocation=-1;
	private int Style;
	private ArrayList<Node> Grid=new ArrayList<>();
	private ArrayList<Integer> Directions=new ArrayList<>(Arrays.asList(1,2,3,0));
	private ArrayList<Pickup> Picks=new ArrayList<>();
	boolean Playing;
	private int viewRange=2;
	private Display parent;

	public Vector2i getCellsize() {
		return Cellsize;
	}

	public int getStyle() {
		return Style;
	}

	private Vector2i Cellsize;

	Display getParent() {
		return parent;
	}
	//void setParent(Display Parent)parent=Parent;}

	/**
	 * @param size  the dimension of the maze
	 * @param Style  1 or 0 depending on whether the maze is to be partially hidden or not
	 * @param parent the display object the maze is drawing to
	 */
	Maze(int size, int Style, Display parent){
		this.parent=parent;
		Cellsize=parent.getCellSize();
		if(size < 5 || size > 100 ) this.size=6;
		else this.size=size;
		height=size;
		width=size;
		tilecount=height*width;
		this.Style=Style %2;
		if(Settings.getDebug()) {
			System.out.println("MAZE: Maze being made with size " + height + " by " + width);
		}
		Grid.ensureCapacity(tilecount); // this could be a 2d array however as each Item has its own x, y anyway this seems entirely superfluous

		populate(width); //setup maze

		if(Settings.getDebug()){
			System.out.println("MAZE: Maze Populating");
		}

	}
/**
*  This method takes in a Location and a Range, and returns an Arraylist of Ints corresonding to the grid index of nodes within range.
*
*
*
 **/
	public ArrayList<Integer> getInRange(int Location, int Range){
		ArrayList<Integer> Out=new ArrayList<>();
		int start=Location - Range*(width+Range);
		for(int i=start;i<(Location+Range*(width+Range));i++){
			int verticalDisplace=(i/width)-(Location/width);
			int horizontalDisplace=(i%width)-(Location%width);
			if(verticalDisplace<=Range && verticalDisplace>=-Range ){
				if(horizontalDisplace<=Range && horizontalDisplace>=-Range){
					if(i>=0 && i<tilecount)Out.add(i);
				}
			}
		}
		return Out;
	}

	/**
	 * @param Location the Integer of which tile to start from
	 * @param Range the Radius to return the squares in
	 * @return an arraylist of squares to be drawn
	 */
	ArrayList<Node> getNodesInRange(int Location, int Range){
	    //retrieves the nodes that can be seen horizontally and vertically so that they may be drawn(?)
		if(parent.getMazeState().equals("SHOWN")){
			if (Settings.getDebug() & Settings.loop) {
				System.out.println("MAZE:  Polled for Size : " + Grid.size());}
			return getGrid();
		}

		else {
			ArrayList<Node> Out = new ArrayList<>();

				int start = Location - Range * (width + Range);
				if (start < 0) start = 0;
				int end = Location + Range * (width + Range);
				if (end >= tilecount) end = tilecount-1;
				for (int i = start; i <= end; i++) {
					int verticalDisplace = (i / width)%height - (Location / width)%height;
					int horizontalDisplace = (i % width)%width - (Location % width)%width;
					if (verticalDisplace <= Range && verticalDisplace >= -Range && horizontalDisplace <= Range && horizontalDisplace >= -Range && intToNode(i)!=null) {
						Out.add(intToNode(i));
					}
				}
			// IF SETTINGS DIFFICULTY == INSANE
			/*	try{
					return getVisible(Location);
				}
				catch(NullPointerException e ){
					return Out;
				}
			*/
			//Out.addAll(getVisible(Location));
			return Out;
			//return getVisible(Location);
		}
	}

	/**
	 * @param Location The current player node.
	 * @return and ArrayList of nodes immediately infront of the player in all Directions
	 */
	public ArrayList<Node> getAllVisible(Node Location){
		ArrayList<Node> out = new ArrayList<>();
		for(int i=0;i<4; i++){
			out.addAll(getVisible(Location,i));

		}
		return out;
	}
	/**
	 * @param Location The current player node.
	 * @param Facing the integer describing which way the players facing,
	 * @return and ArrayList of nodes immediately infront of the player
	 */
	public ArrayList<Node> getVisible(Node Location, int Facing) {
		ArrayList<Node> out = new ArrayList<>();

		Node here=Location;//=intToNode(Location % Grid.size());
		if (getUser() != null)
			Facing = getUser().getFacing();
		here = User.getLocation();

		out.add(here);
		while (!here.getNeighbours()[Facing].isWall()) {
			out.add((Node) here.getNeighbours()[Facing]);
			here = (Node) here.getNeighbours()[Facing];
		}
		return out;
	}

	/**
	 * @param Location current Tile to check
	 * @param Range	 the range to search
	 * @return	True if theres a pickup within the range given
	 */
		private boolean PickupInRange(int Location,int Range){
		if(Settings.getDebug()){
			System.out.println("MAZE: Checking View Range");
		}
		if(Style!=1) {
			ArrayList<Node> copy = (ArrayList<Node>) getNodesInRange(Location, Range).clone();
			return copy.removeIf((n) -> n.getHas() != null);    //returns if any elements are removed. i.e theres a pickup in range
		}
		else return false;
	}

	/**Supports the conversion of an arraylist of ints to the nodes they reference **/
	public ArrayList<Node> convertIntsToNodes(ArrayList<Integer> refs){
		ArrayList<Node> Out=new ArrayList<>();
		refs.forEach((n) -> Out.add(intToNode(n)));
		return Out;
	}

	/**
	 * @param In the Integer reference of a node
	 * @return the Node the integer corresponds to
	 */
	Node intToNode(Integer In){
		try {return Grid.get(In);}
		catch(IndexOutOfBoundsException e){
			return null;
		}
	}


	/**
	 * @param Location The Node in the maze to be given an Integer Reference
	 * @return The Integer reference of the Node
	 */
	int NodetoInt(Node Location){return Grid.indexOf(Location);}


	/**
	 * @return The Range the player can see
	 */
	int getViewRange() {
		return viewRange;
	}


	/**
	 * @param viewRange the new viewrange for the player
	 */
	public void setViewRange(int viewRange) {
		this.viewRange = viewRange;
	}
	/**
	*Returns the size of the side of the maze 
	* can be edited to be each side. 
	**/

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	/**returns the start location in the maze (be careful of numbering, works like a number grid- counting left-right top-bottom) if its undefined it picks a random one**/
	public int getStart(){
		if(StartLocation<0){
			StartLocation=a.nextInt((int)Math.pow(size,2));
			//System.out.println("Start Position : " + StartLocation);
		}
		return StartLocation;
	}


	/**returns end location ( note that this isnt defined until the maze is generated**/
	public int getEnd(){
		if(EndLocation<=0)EndLocation=a.nextInt(tilecount);
		return EndLocation;
	}

	/**
	 * returns the grid of cells for the maze
	 **/
	ArrayList<Node> getGrid(){
		return Grid;
	}

	/**
	 *  populate simple adds cells to the maze each with its own coordinates and then calls to make pathway
	 **/
	private void populate(int width){
		Grid.clear();
		for(int i=0;i<(tilecount);i++){
			//System.out.println("Working on Node : "+ i);
			Grid.add(new Node(i%width, i/ width, Cellsize));  // instantiates all the nodes with an initial x and y pattern
		}
		float modifier=(1/4);
		// makes the pathways in side the maze

		if (Style==0){
			addPathway(getStart(), (int) ((float)(tilecount)* modifier));
			randomPowerup();
			randomPowerup();
			randomPowerup();
		}
		if (Style==1){
			addMenu();
		}
		getGrid().forEach(Node::setVisited);
		Playing=true;
	}
	
	/**
	 *  method that recursively generates a solveable maze from a given start point and distance to the end point - not working for size >100
	 **/
	private void addPathway(int current,int distance) {
		//style : 1 == manu, 0 == game
		addPathway(current,distance,0);
		if(getEnd()<0)EndLocation=a.nextInt(tilecount);
		addEnd();

	}

	/**
	 * used to generate the menu
	 */
	private void addMenu(){
		//addPathway(0,tilecount,1);
		for(int J=0;J<tilecount;J++){
			Collections.shuffle(Directions);
			Grid.get(J).addNeighbour(Directions.get(0),Grid.get((J+getStep(0) + (tilecount)) % (tilecount)));
			Grid.get(J).addNeighbour(Directions.get(1),Grid.get((J+getStep(1) + (tilecount)) % (tilecount)));

		}
		// LOOP TO ADD IN MENU PICKUPS AND CHECK MAZE SIZE
		for(int i=0; i<3;i++) {
			int Location = (tilecount / 3) + ((i * 4) + 1) * width / 2;
			Grid.get(Location).setHas(addPickup(Location, i+1));//starts game
			if (Settings.getDebug() && Grid.get(Location).getHas() != null) {
				System.out.println("MAZE: Menu Pickup " + "Created. Maze of " + Grid.size() + " Nodes...NODE OF TYPE " + Grid.get(Location).getClass().getSimpleName());
			}
		}



		if(Settings.getDebug()){
			System.out.println("MAZE: Menu Pickups Created." +
					" MAZE : Grid Of Size "+Grid.size());
		}

	}


	/**
	 * @param count the direction of movement
	 * @return Integer for the actual change in Integer reference of the nodes.
	 */
	private int getStep(int count){
		int Direction=Directions.get(count);
		int step;
		switch ( Direction){
			case 0: step=-width;
				break;
			case 1: step=1;
				break;
			case 2: step=width;
				break;
			case 3: step=-1;
				break;
			default: step=0;
				break;
		}
		return step;
	}


	/**
	 * REcursive algorithm for populating the maze in a random, yet solveable way.
	 * @param current Start Location
	 *
	 * @param distance Count down for endLocation
	 * @param Style Whether or not the maze is to be shown or not
	 **/
	private void addPathway(int current,int distance, int Style){

		Collections.shuffle(Directions);
		//System.out.println(Directions);
		int flag=1;
		if(Style==1 && Grid.get(current).visited) flag=0;		//checks if change made and only if so it decrements distance
		Grid.get(current).visited=true;

		int step=0; //jump to next tile
		int count=0; //how many steps have been taken from this tile
		int routes=0;
		while(count<=3 && routes <=2 ){

			step=getStep(count);
			count++;
			if((step ==1 || step ==-1)&&((current+step)/width != current/width))step=0; //stepping around edge of map

			else if(((current+step) < (tilecount) && (current+step) >=0)){ // checks for a valid move to stay within grid
				int newplace=(current+step +(tilecount))%(tilecount);    // ensures its within bounds of grid
				if(distance <=0 && count==3 && routes==1){	// checks end location to be far into grid and
					EndLocation=newplace;
				}
				if(!Grid.get(newplace).visited){      //checks its an as yet unvisited square
					Grid.get(current).addNeighbour(Directions.get(count-1), Grid.get(newplace));
					routes++;
					addPathway(newplace,distance-flag,Style);
				}
			}
		}
	}

	/**
	 * @return the reference to the User to be added to the maze
	 */
	Player addUser(){
		//adds player into maze
		while(PickupInRange(getStart(),viewRange))StartLocation=(StartLocation+1)%(tilecount-1);
		Node Start=intToNode(getStart());
		User=new Player(this, Start);


		return getUser();
		}

	/**
	 * @return the reference to the pickup of the end location
	 */
	private Pickup addEnd(){
			return addPickup(EndLocation,0);
	}

	/**
	 * @param Location the location to add a pickup
	 * @param Function	the function code of the new pickup
	 * @return	the new Pickup object thats been added to the maze
	 */
	private Pickup addPickup(int Location, int Function){
		Pickup it=new Pickup(this, intToNode(Location),Function);

		intToNode(Location).setHas(it);
		//intToNode(Location).updateSprite();
		Picks.add(it);
		if(Settings.debug){
			System.out.println(Picks);
		}
		return it;
	}

	/**
	 * clears all pickups from the maze.
	 */
	public void ClearPickups(){
		Picks.forEach((P)->{
			P.getLocation().empty();

		});
		Picks.clear();
	}

	/**
	 * @return User in the maze
	 */
	Player getUser() {
		return User;
	}

	/**
	 * populates the End Game space
	 */
	void getEndGame() {
		if(Style==0 && intToNode(getEnd()).getHas()==null) {
			addEnd();
		} else if(!(Style==1)) intToNode(getEnd());

	}

	/**
	 * Used to call end -of -round for other threads running who are polling this for whether the game is running.
	 */
	void endGame(){
		getUser().mover.Stop=true;

		Playing=false;
		ClearPickups();
		//User=null;
		if(Settings.getDebug()){
			System.out.println("MAZE: Pickups Cleared");
		}
	}


	/**
	 * @return returns the random Pickup added to the maze in a random location
	 */
	private Pickup randomPowerup(){
		//bounds are the numbers in the case statements in the Pickup constructor that are valid
		int randomFunction = ThreadLocalRandom.current().nextInt(4,9);
		return addRandomPowerup(randomFunction);
	}

	/**
	 * @param function The integer of the pickup function of the pickup to be added to the maze
	 * @return	 the pickup added to the maze randomly -- may be null if no pickup added.
	 */
	private Pickup addRandomPowerup(int function){
		if(Settings.getDebug()){
			System.out.println("MAZE: Random Pickups added");
		}
		Random a=new Random();
		boolean give = a.nextBoolean();
		if(give){
			//add in check that its adding to empty tile
			int j=a.nextInt(tilecount);
			while(intToNode(j).getHas()!=null)j=a.nextInt(tilecount);
			return addPickup(j,function);
		}
		else return null;
	}

}