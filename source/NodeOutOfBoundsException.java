/*
* Charlie Moore 23-01-2019
* An exception that is thrown when x or y are out of bounds.
* I.e when x < 0, y < 0, x > size (width), or y > size (height)
*/

class NodeOutOfBoundsException extends Exception {

    public NodeOutOfBoundsException() {
        super("x or y out of bounds");
    }
}
