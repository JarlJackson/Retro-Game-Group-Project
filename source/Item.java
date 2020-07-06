import org.jsfml.graphics.Sprite;

public interface Item {
    boolean debug = true;
    boolean loop = false;
    Node getLocation();
    void setLocation(int location);
    void setLocation(Node node);
    boolean isPlayer();
    boolean isPickup();
    Sprite getSprite();
    void get(Pickup found);

}