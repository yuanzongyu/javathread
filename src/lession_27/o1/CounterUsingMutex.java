package lession_27.o1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

/**
 * binary semaphore to build a counter
 */
public class CounterUsingMutex {
    private Semaphore mutex;
    private int count;

    CounterUsingMutex(){
        mutex = new Semaphore(1);  // 信用量 ，初始为1
        count=0;  //计数器
    }

    void increase() throws  InterruptedException{
        mutex.acquire();
        this.count=this.count+1;
        Thread.sleep(100);
        mutex.release();
    }

    int getCount(){
        return this.count;
    }

    boolean hasQueuedThreads(){
        return mutex.hasQueuedThreads();
    }

    public static void main(String[] args) {
        int count=5;
        ExecutorService  exec = Executors.newFixedThreadPool(count);
        CounterUsingMutex counter = new CounterUsingMutex();
        IntStream.range(0,count).forEach(user-> exec.execute(()->{
                    try{
                        counter.increase();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
        ));
        exec.shutdown();
        System.out.println(counter.hasQueuedThreads());
    }
}
