import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Sprite;

public abstract class Entity implements Item {

    // each node has a space for an Item,  create Item interface that entity implements
    Node Location;
    Maze Context;
    Entity(Maze m, Node location){
        this.setLocation(location);
        this.Context=m;
    }
    public Maze getContext(){return Context;}
    public Node getLocation() {
        return Location;
    }
    int getIntLocation(){return Context.NodetoInt(getLocation());}
    public void setLocation(Node location) {
        Location = location;
    }
    void checksLocation(Player p, Node location){
        if(location.getHas()!=null){
            if (Settings.getDebug()) {
                System.out.println("PLAYER: FOUND PICKUP");
            }
            p.get((Pickup)location.getHas());
            if(Context.getStyle() == 0){
                location.empty();
                //location.updateSprite();
            }
        }
    }

    public void setLocation(int location) {
        setLocation(Context.intToNode(location));
    }
    public boolean isPlayer(){return false;}
    public boolean isPickup(){return false;}

    public void get(Pickup has){}
    public abstract Sprite getSprite();
    public abstract Drawable getDrawable();
}
