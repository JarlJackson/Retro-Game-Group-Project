public class ThreadRunnable extends Thread {
    public final Threaded r;
    public ThreadRunnable(Threaded r){
        super(r);
        this.r=r;
    }
    public Threaded getRunnable(){
        return r;
    }
}
