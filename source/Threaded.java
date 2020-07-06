public abstract class Threaded implements Runnable {
    boolean Pause=false;
    boolean Stop=false;

    boolean toRemove(){return Stop;}
    public void Stop(){
        Stop=true;
    }
    public void Pause(){
        Pause=!Pause;
    }
    public abstract void run();
}
