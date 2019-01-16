package lession_07.simple.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();
    public void untimed(){
        boolean captured =lock.tryLock();
        try{
            System.out.println("tryLock():"+captured);
        }finally { if(captured){ lock.unlock(); } }
    }
    public  void timed(){
        boolean captured = false;
        try{
            captured=lock.tryLock(2, TimeUnit.SECONDS);
        }catch(InterruptedException ex){
            throw  new RuntimeException(ex);
        }
        try{
            System.out.println("tryLock(2,TimeUnit.SECONDS):"+captured);
        }finally {if(captured){ lock.unlock(); } }
    }
    public static void main(String[] args) throws Exception{
        final AttemptLocking al = new AttemptLocking();
        al.untimed();
        al.timed();
        new Thread(){{ setDaemon(true); }
            public void run(){
                al.lock.lock();
                System.out.println("占用锁，不释放! ");
            }
        }.start();
        TimeUnit.SECONDS.sleep(2); // 确保让后台线程运行
        Thread.yield();
        System.out.println("----------------------------------------------");
        al.untimed();
        al.timed();
    }
}