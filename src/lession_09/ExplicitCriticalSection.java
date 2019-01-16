package lession_09;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Pair1 {
    private int x, y;
    public Pair1(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Pair1() {
        this(0, 0);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void incrementX() {
        x++;
    }
    public void incrementY() {
        y++;
    }
    public String toString() {
        return "x：" + x + ",y：" + y;
    }
    public void checkState() {

        if (x != y) {
            System.out.println("x:"+x+"y:"+y);
            throw new RuntimeException();
        }
    }
}
class Pair1Manager {
    AtomicInteger check = new AtomicInteger(0);
    private Pair1  p = new Pair1();
    private List<Pair1> store  = Collections.synchronizedList(new ArrayList<>());
    private Lock lock = new ReentrantLock();
    public synchronized Pair1 getPair1(){
        return new Pair1(p.getX(),p.getY());
    }
    public void increment(){
        Pair1 pair1 ;
       try {
            lock.lock();
            p.incrementX();
            p.incrementY();
            pair1 = getPair1();
        }finally {
           lock.unlock();
        }
        store.add(pair1);


    }
}

class Pair1Manipulator implements Runnable{
    private Pair1Manager pair1Manager ;
    Pair1Manipulator(Pair1Manager pm){
        pair1Manager =pm;
    }
    public void run(){
        while (true){
            pair1Manager.increment();
        }
    }
    public String toString(){
        return "getPari():"+pair1Manager.getPair1()+
                ",checkcount:"+pair1Manager.check.get();
    }
}
class Pair1Checker implements Runnable{

    private Pair1Manager pair1Manager ;
    Pair1Checker(Pair1Manager pm){
        pair1Manager =pm;
    }
    public void run(){
        while (true){
            pair1Manager.check.incrementAndGet();
            pair1Manager.getPair1().checkState();
        }
    }
}
public class ExplicitCriticalSection {
    public static void main(String[] args)  throws  Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        Pair1Manager p1 = new Pair1Manager();
        Pair1Manipulator pm1 = new Pair1Manipulator(p1);
        Pair1Checker pc1 = new Pair1Checker(p1);
        exec.execute(pm1);
        //TimeUnit.MICROSECONDS.sleep(500);
        exec.execute(pc1);

        TimeUnit.MILLISECONDS.sleep(400);
        System.out.println(pm1);
        System.exit(0);
    }
}
