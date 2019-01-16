package lession_22;

import java.util.Random;
import java.util.concurrent.*;

/**
 * CountDownLatch
 * 1、不会被重置，CycliBarrier可以重置
 */
class TaskPorition implements Runnable{
    private static int counter =0 ;
    private final int id = counter++;
    private static Random rand = new Random(47);
    private final CountDownLatch latch; // the count cannot be reset. if you need a version that resets the count,you can use cycliBarrier .
    TaskPorition(CountDownLatch latch) {
        this.latch = latch;
    }
    public void run(){
            try{
                doWork();
                latch.countDown();
            }catch (InterruptedException e){}
    }
    public void doWork() throws  InterruptedException{

        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(200));//Random.nextInt 是线程安全的
        System.out.println(this+"complete");
    }
    public String toString(){
        return "TaskProition id:"+id;
    }
}
class WaitingTask implements Runnable{
    private static int counter = 0;
    private final int id=counter++;
    private final CountDownLatch latch;
    WaitingTask(CountDownLatch latch){
        this.latch =latch;
    }
    public  void  run(){
        try{
            latch.await(); // 阻塞，直到 count为0
            System.out.println("pass !");
        }catch (InterruptedException e){
            System.out.println(this+"interrupted");
        }
    }
    public String toString(){
        return "watingTask id"+id;
    }
}
public class CountDownLatchDemo {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch latch=  new CountDownLatch(100);
        for(int i=0;i<10;i++ ){
            exec.execute(new WaitingTask(latch));
        }
        for (int i=0;i<100;i++){
            exec.execute(new TaskPorition(latch));
        }
        System.out.println("启动完所有的任务");
        exec.shutdown();
    }
}
