import java.util.ArrayList;

public class CleanUp implements Runnable{
    private ThreadMaster t;

    public CleanUp(ThreadMaster t){
        this.t=t;
    }


    /**
     * Thread that constantly cleans up stopped threads
     */
    public void run() {
        while(t!=null && !t.getThreads().isEmpty()){
        ArrayList<ThreadRunnable> RemoveList = new ArrayList<>();
        ArrayList<ThreadRunnable> Threads=t.getThreads();
        synchronized(Threads) {
            Threads.forEach((T) -> {if(T.getRunnable().Stop) RemoveList.add(T);});
            RemoveList.forEach((T) -> ((Threaded) T.getRunnable()).Stop());
            t.getThreads().removeAll(RemoveList);
        }
        }
    }
}
