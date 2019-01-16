package lession_19;

import common.LiftOff;
import java.util.concurrent.*;

class LiftOffRunnable implements  Runnable{
    private BlockingQueue<LiftOff>  rockets ;
    public LiftOffRunnable(BlockingQueue<LiftOff> queue){
        this.rockets = queue;
    }
    public  void add(LiftOff lo){
        rockets.add(lo);
    }
    public void run(){
        try{
            while (!Thread.interrupted()){
                LiftOff rocket = rockets.take();
                rocket.run();
            }
        }catch (InterruptedException e){ e.printStackTrace(); }
    }
}
public class TestBlockingQueues {
    static void test(String msg, BlockingQueue<LiftOff> q) throws InterruptedException{
        System.out.println(msg);
        LiftOffRunnable runner  = new LiftOffRunnable(q);
        Thread t = new Thread(runner);
        t.start();

        for(int i=0;i<1;i++){
            runner.add(new LiftOff(5));
        }

        TimeUnit.SECONDS.sleep(5);
        t.interrupt();
        System.out.println("finished "+msg+" test!");
    }
    public static void main(String[] args) throws  InterruptedException {
        test("linkedBlockingQueue",new LinkedBlockingDeque<LiftOff>()); //无限
        test("arrayBlockingQueue",new ArrayBlockingQueue<LiftOff>(5));  //size 3
        //test("synchrnoizedQueue",new SynchronousQueue<LiftOff>());  //size 1
    }

}
