package lession_13;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BlockedMutex{
    private Lock lock = new ReentrantLock();
    public BlockedMutex(){
        lock.lock(); //先进行锁
    }
    public void f(){
        try{
            lock.lockInterruptibly();  //等待获取锁，除非被打断
            System.out.println("lock acquired in f(); ");
        }catch (InterruptedException ex){
            System.out.println("interrupted from lock acquisition in f() ; ");
        }
    }
}
class Blocked2 implements  Runnable{
    BlockedMutex  blocked = new BlockedMutex();
    public void run(){
        System.out.println("waiting for f()  in  BlockedMutex ...");
        blocked.f();
        System.out.println("broken out of blocked call ...");
    }
}
public class Interrupting2 {
    public static void main(String[] args) {
        Thread t = new Thread(new Blocked2());
        t.start();
        System.out.println("issuing t.interrupt()");
        t.interrupt();
    }
}
