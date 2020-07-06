import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import java.util.ArrayList;

public class Node implements Neighbour {

	private RectangleShape Graphic;
	private ArrayList<RectangleShape> walls;
	private ArrayList<FloatRect> WallColls;
	private Vector2i size;
	private Vector2i position;
	private Pickup Has;
	private IntRect Collisions;
	boolean visited;  //This flag used for creation, all should be true by the end
	//private boolean[] CanGo=[false,false,false,false];
	private Neighbour[] Neighbours;
	private static Texture backwall;
	private static Texture sideTexture;

	/**
	 * Imports textures to be used for this class from AssetPack
	 */
	// Neighbours defines the neighbouring cells whether they are other nodes or simply null values representing a wall in that direction.
	// this allows the possible expansion to have some areas of 1-way systems
	static void setTextures() {
		try {
			sideTexture = AssetPack.getTexture("walltexture.png");
			backwall = AssetPack.getTexture("backwall.png");

		} catch (NullPointerException e) {
			System.out.println("Textures Not fonund in correct folder");
		}
	}


	/**
	 * @param x  its x space in the maze
	 * @param y  its y space in the maze
	 * @param Dimension the Vector of the Dimensions this space takes up
	 */
	Node(int x, int y, Vector2i Dimension) {
		Has=null;
		try {
			sideTexture = AssetPack.getTexture("walltexture.png");
			backwall = AssetPack.getTexture("backwall.png");

		} catch (NullPointerException e) {
			System.out.println("Textures Not fonund in correct folder");
		}
		walls = new ArrayList<>();
		WallColls=new ArrayList<>();
		this.size = Dimension;
		this.position = new Vector2i(size.x * x, size.y * y);
		Graphic=new RectangleShape();
		Graphic.setSize(new Vector2f(size));
		Graphic.setPosition(new Vector2f(position));

		Graphic.setTexture(backwall);

		Collisions = new IntRect(position, Dimension);
		//	System.out.println("Top : "+Collisions.top + " Left : " + Collisions.left);
		//	System.out.println("Height : " + Collisions.height+ " Width : "+Collisions.width);
		Neighbours = new Neighbour[4];
		for (int j = 0; j < 4; j++) {
			Neighbours[j] = new Wall();
		}            // representing the 4 possible NESW adjacent cells/
		visited = false;


	}

	/**
	 * @return Any Entities currently on this space
	 */
	Item getHas() {
		return (Item)Has;
	}

	/**
	 * sets pickup of this space and suitable updates sprite
	 * @param newItem gives this space a new item Pickup
	 */
	void setHas(Pickup newItem) {

		Has = newItem;
		if (Has != null) {
			if(Settings.debug){
				System.out.println("NODE: NEW PICKUP ADDED");
			}
			updateSprite();
		}
	}

	/**
	 * Sets texture of this space to whatever is on it or just to empty if thats null.
	 */
	void updateSprite(){
		if(Has!=null && Has.getText()!=null){
			if(Settings.debug){
				System.out.println("NODE: UPDATING SPRITE");
			}
			Graphic.setTexture(Has.getText());
			//System.out.println(Has.getSprite().getTexture());
		}
		else{
			Graphic.setSize(new Vector2f(size));
			Graphic.setPosition(new Vector2f(position));
			Graphic.setTexture(backwall);
		}
	}

	/**
	 * creates Rectangles for the Walls of this node and puts them in place with correct textures too, then sets this space as "created"
	 */
	void setVisited(){
		RectangleShape vertical = new RectangleShape(new Vector2f(4, size.y +2) );
		RectangleShape horizontal=new RectangleShape(new Vector2f(size.x + 2,4));
		RectangleShape vertical1 = new RectangleShape(new Vector2f(4, size.y +2) );
		RectangleShape horizontal1=new RectangleShape(new Vector2f(size.x + 2,4));


		vertical.setTexture(sideTexture);
		horizontal.setTexture(sideTexture);
		vertical1.setTexture(sideTexture);
		horizontal1.setTexture(sideTexture);
		//r.setFillColor(Color.BLUE);

		horizontal.setPosition( new Vector2f(position.x, position.y -2));
		vertical.setPosition(new Vector2f((position.x+size.x)-2, (position.y)));
		horizontal1.setPosition(position.x, (position.y+size.y)-2);
		vertical1.setPosition(new Vector2f((position.x)-2, (position.y)));
		boolean[] Walls = returnNeighbours();
		if (Walls[0] | (position.y==0)) {
			//Vertex[] Line = {TLCorner, TRCorner};
			walls.add(horizontal);
			WallColls.add(new FloatRect(horizontal.getPosition(),horizontal.getSize()));
		}
		if (Walls[1]) {

			walls.add(vertical);
			WallColls.add(new FloatRect(vertical.getPosition(),vertical.getSize()));
		}
		if (Walls[2]) {

			walls.add(horizontal1);
			WallColls.add(new FloatRect(horizontal1.getPosition(),horizontal1.getSize()));
		}
		if (Walls[3]) {

			walls.add(vertical1);
			WallColls.add(new FloatRect(vertical1.getPosition(),vertical1.getSize()));

		}
		visited=true;
	}

	/**
	 * empties the contents of this space
	 */
	void empty(){
		Has=null;
		updateSprite();
	}

	/**
	 * checks if a player is completely inside this tile or not
	 * @param p player to check if inside the tile of
	 * @return returns True is player completely inside this tile
	 */
	boolean PlayerInside(Player p){    // checks if player is in square in entirity.
		 IntRect b=(Collisions.intersection(new IntRect(new Vector2i(p.getSpriteLocation()),new Vector2i( p.getSpriteSize()))));
		 if (b!=null && b.equals(new IntRect(new Vector2i(p.getSpriteLocation()), new Vector2i(p.getSpriteSize())))) return true;
		 else return false;
	}

	/**
	 * checks if a player is even partially inside the tile
	 * @param p Player to check
	 * @return True if the player is even partially inside the tile
	 */
	boolean isInside(Player p){
		//System.out.println(p.getSpriteSize().x +" : "+p.getSpriteSize().y);
		return (Collisions.contains(new Vector2i(p.Location)));

	}
	ArrayList<FloatRect> getWallColls(){
		return WallColls;
	}
	//	public boolean[] GetValidMoves(){
//		return CanGo;
	//}

	/**
	 * @return the centre of this tile as Vector2f
	 */
	Vector2f getCentre(){
		return (new Vector2f(position.x + (size.x/2), position.y +(size.y/2)));
	}

	/**
	 * method that replaces a wall in the given direction with a neighbouring cell
	 * @param Direction The direction of the neighbour (0-3)
	 * @param adj the Node to add as neighbour
	 */
	void addNeighbour(int Direction, Node adj){
		// throw exception if Neighbours[direction] taken
		//if(position.y!=0 && Direction!=0){
			Neighbours[Direction] = adj;
			//CanGo[Direction]=true;
			if (adj.getNeighbours()[(Direction + 2) % 4] != this) {        // makes sure that this Node is added to the new Neighbour as a pathway too.
				adj.addNeighbour((Direction + 2) % 4, this);
			}
		//}
	}

	/**
	 * @return an Arraylist of neighbours, some of which may be walls
	 */
	Neighbour[] getNeighbours(){
		return Neighbours;
	}

	/**
	 * overrides Neighbour to allow subclass querying when presented with Neightbour  this returning false is synonymous with being a "isNode==True"
	 */
	@Override
	public boolean isWall(){
		return false;
	}

	/**
	 * @return a List of Rectrangle Shapes to draw as walls around this space
	 */
	ArrayList<RectangleShape> getWalls(){
				//adding Walls to Render list,
		return walls;
	}

	/**
	 * @return An array of bool values according to if neighbours are walls or not
	 */
	boolean[] returnNeighbours(){
		boolean[] out=new boolean[4];
		for(int i=0; i<out.length;i++){
			out[i]=  (Neighbours[i].isWall()) ;
		}
		return out;
	}

	/**
	 * @return the drawable of this space
	 */
	Drawable getDrawable(){
	    //updateSprite();
		return Graphic;

	}
	void doFunction(){
		if(Has!=null){Has.doFunction();}
	}
}
