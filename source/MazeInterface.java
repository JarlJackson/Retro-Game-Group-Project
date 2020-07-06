import java.util.ArrayList;

public interface MazeInterface {
    int getHeight();
    int getWidth();
	int getStart();
	int getEnd();
	ArrayList<Integer> getInRange(int Location, int Range);
	ArrayList<Node> convertIntsToNodes(ArrayList<Integer> References);
	/*
	*When Generating the UI, you may find extra methods you wish to use, such as a "GetNeighbouringCells"(int location,int Range);
	* as an example.  please put those here in this file. I will try and be as prompt as i can in creating those, however .
	 * in the meantime, please make a branch prototype of Maze, and spoof the method using a hard coded return
	* so that you can merrily test your code works
	*
	 */
}