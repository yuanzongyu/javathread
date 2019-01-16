package lession_08.atomic;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest implements Runnable {

    private AtomicInteger i = new AtomicInteger(0);
    public int getValue(){
        return i.get();
    }

    public void evenIncrement(){
        i.addAndGet(2);
    }
    public void run(){
        while (true){
            evenIncrement();
        }
    }

    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("终止程序");
                System.exit(0);
            }
        },5000); //延迟5秒执行
        ExecutorService exec = Executors.newCachedThreadPool();
        AtomicIntegerTest atomicIntegerTest = new AtomicIntegerTest();
        exec.execute(atomicIntegerTest);
        while (true){
            int val =atomicIntegerTest.getValue(); //保证原子性操作
            if(val%2 !=0){
                System.out.println(val);
                System.exit(0);
            }
        }
    }
}
