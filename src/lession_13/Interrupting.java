package lession_13;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class SleepBlocked implements Runnable{
    public void run(){
        try{
            TimeUnit.SECONDS.sleep(100);
        }catch (InterruptedException ex){
            System.out.println("interruptedException !");
            // interrupt state 被重置为false
            System.out.println("sleepBlocked : "+
                    Thread.currentThread().isInterrupted());
        }
        System.out.println("exiting sleepblock.run()");
    }
}


//InputStream 阻塞不能被打断
class IOBlocked implements Runnable{
    private InputStream in;
    public IOBlocked(InputStream is) { in = is; }
    public IOBlocked(){ }
    public void run(){
        try {
            System.out.println("waiting for read() ...");
            in.read();
        }catch(IOException ex){
            if(Thread.currentThread().isInterrupted()){        //不会执行，IO 中断不可达
                System.out.println("interrupted form blocked I/O");
            }else{
                throw new RuntimeException(ex);
            }
        }
        System.out.println("exiting IOBlocked.run()");
    }
}

class SynchronizedBlocked implements Runnable{
    public synchronized void f(){
        while (true){   // 不释放锁,理论上让其构造方法的线程先执行，获取锁
            Thread.yield();
        }
    }
    public SynchronizedBlocked(){
        new Thread(){
            public void run(){
                f();  //通过线程进行Lock
            }
        }.start();
    }
    public void run(){
        System.out.println("trying to call f()  ");
        f();
        System.out.println("exiting synchronizedBlcoked.run()  ");
    }
}

public class Interrupting {
    static  ExecutorService exec = Executors.newCachedThreadPool();
    static void test(Runnable r ) throws InterruptedException{
        Future<?> f = exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("interrupting"+r.getClass().getName());
        f.cancel(true);  //  call thread interrupt
        System.out.println("interrupt sent to "+r.getClass().getName());
    }
    public static void main(String[] args)throws InterruptedException {
        //test(new IOBlocked());
          test(new SynchronizedBlocked());
        //test(new SleepBlocked());
    }
}
