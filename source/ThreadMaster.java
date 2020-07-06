

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class ThreadMaster {
    private final ArrayList<ThreadRunnable> Threads= new ArrayList<>();
    private Thread main;
    private Thread c;
    private static int value = 100;
    public static int highscore;
    public static String highscoreName;

    /**
     * @param args Passed from command line , though has no real consequence
     *
     *             breaks out from static context
     */
    public static void main(String[] args){
        //break out of static context
        //new SplashScreen()
        highscoreName = ReadingHighscore.getHighScore().split(":")[0];
        highscore = Integer.parseInt(ReadingHighscore.getHighScore().split(":")[1]);
        Settings.readFile();
        new SplashScreen();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SplashScreen.close();
        new ThreadMaster();

    }

    public static void startGame(){
        new ThreadMaster();
    }

    public static int getValue(){
        return value;
    }

    public static void setValue(int value){
        ThreadMaster.value = value;
    }

    /**
     * Creates ThreadMaster with a Display and Cleanup Thread running in parralel to this   (Thread count ought be <=6? cleanup, this, Display, movelistener, options Menu? Sound playing? )
     */
    public ThreadMaster() {
        if (Settings.getDebug()) {
            System.out.println("THREAD MASTER: SYSTEM THREAD CREATED");
        }
        main = Thread.currentThread();
        ThreadRunnable Display = new ThreadRunnable(new Display(this));
        //Display.start();
        Display.setName("Display");
        Threads.add(Display);
        if (Settings.getDebug()) {
            System.out.println("THREAD MASTER: Thread Added - " + Threads.get(Threads.size() - 1));
        }
        c = new Thread(new CleanUp(this));
        run();
    }
//    public RenderWindow getMainWindow(){return window;}

    /**
     * resumes or runs each Thread stored in the list of threads (no this or cleanup)
     */
    private void run(){
        try {
            Threads.forEach(ThreadRunnable::run);

    }catch (ConcurrentModificationException e) {
            e.printStackTrace();
            System.out.println("THREAD MASTER: Conflict in restarting Threads? "+e);
        }
    }

    /**
     * Makes new ThreadRunnable that extends Thread
     * @param r  Runnable Implementation to be added to the thread list
     * @return the ThreadRunnable Object thats been made
     */
    ThreadRunnable addThread(Threaded r, int id){
        ThreadRunnable t=new ThreadRunnable(r);
        Threads.add(t);
        if(id == 1){
            t.setName("Move Listener");
        }else if(id == 2){
            t.setName("Options Menu");
        }else if(id == 3){
            t.setName("High Score");
        }
        t.start();
        if(Settings.getDebug()){
            System.out.println("THREAD MASTER: Thread Added "+ Threads.get(Threads.size() -1));
        }
        return t;
    }
    void killRunning(Threaded r){
        Threads.removeIf((T)->{
             if(T.getRunnable().equals(r)){
                 r.Stop=true;
                 return true;

             }
             else{
                 return false;
             }

        });
    }
    /**
     * @param r Threaded to be ran above all others which resume upon its death.
     */
    void TopThread(Threaded r){
        if(Settings.getDebug()){
            System.out.println("THREAD MASTER: Top Thread Assigned");
        }
        ThreadRunnable t =new ThreadRunnable(r);
        Threads.forEach((T)->T.getRunnable().Pause()); // Pause
        t.start();
        while(t.isAlive()) {
            Threads.forEach((T) -> T.getRunnable().Pause()); // Pause
        }
        }

    /**
     * @param r Threaded to become the main thread and be run with max priority
     */
    void addMainThread(Threaded r){
        main=new ThreadRunnable(r);
        main.setPriority(Thread.MAX_PRIORITY);
        main.start();
    }


    /**
     * @param t threadmaster to tidy,
     *          checks all threads to see if theyre stopped and  kills them if so. waiting for the GC to come eat them
     */
    void Tidy(ThreadMaster t){
        ArrayList<ThreadRunnable> RemoveList=new ArrayList<>();
        synchronized(Threads) {
            t.Threads.forEach((T) -> {
                if (((Threaded) (T.getRunnable())).toRemove()) RemoveList.add(T);
            });
            RemoveList.forEach((T) -> ((Threaded) T.getRunnable()).Stop());
            Threads.removeAll(RemoveList);
        }
    }


    /**
     * gets rid of all threads
     * @param Except Threaded not to get rid of
     */
    void killAll(Threaded Except){
        killAll();
        //addThread(Except);
    }

    /**
     * removes all threads to allow GC to swoop in
     */
    void killAll(){
        ArrayList<ThreadRunnable> RemoveList = new ArrayList<>(Threads);
        if(Settings.getDebug()){
            System.out.println("THREAD MASTER: Thread List = "+RemoveList);
        }
        synchronized(Threads) {
            RemoveList.addAll(Threads);
            RemoveList.forEach((T) -> T.getRunnable().Stop());

            Threads.removeAll(RemoveList);
            //RemoveList.clear();
        }

        if(Settings.getDebug()){
            System.out.println("THREAD MASTER: All Threads Destroyed");
            System.out.println("THREAD MASTER: Thread List = "+Threads);
        }
    }


    /**
     * @return ArrayList of ThreadRunnables currently running/
     */
    public ArrayList<ThreadRunnable> getThreads() {
        return Threads;
    }
}
