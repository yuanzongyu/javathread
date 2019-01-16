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

class Pair {   //线程不安全的类
    int x, y;
    public Pair(int x, int y) { this.x = x;this.y = y;}
    public Pair() { this(0, 0); }
    public int getX() {return x; }
    public int getY() { return y; }
    public void incrementX() { x++; }
    public void incrementY() { y++; }
    public String toString() { return "x：" + x + ",y：" + y; }
    public void checkState() { if (x != y) { throw new RuntimeException(); } }
}

class PairManager {
    AtomicInteger checkCount = new AtomicInteger(0);  //原子类，线程安全
    List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());
    private Pair pair = new Pair();
    public synchronized Pair getPair() { return new Pair(pair.getX(), pair.getY()); }
    private Lock lock = new ReentrantLock();
    public void increment() {
        Pair temp;
        //synchronized (this) { //synchronized 代码块 ,锁住当前对象
        lock.lock();
        try {
            pair.incrementX();
            pair.incrementY();
            temp = getPair();
        }finally {
            lock.unlock();
        }
        //}
        storage.add(temp); //线程安全的，可以不放到synchronized
        try { TimeUnit.MICROSECONDS.sleep(50); } catch (Exception ex) { ex.printStackTrace(); }
    }
}
class PairManipulator implements Runnable {
    PairManager p;
    public PairManipulator(PairManager p) {
        this.p = p;
    }
    public void run() { while (true) { p.increment(); } }
    public String toString() {
        return "pair : " + p.getPair() + ",checkcount:" + p.checkCount.get();
    }
}

class PairChecker implements Runnable {
    PairManager p;

    public PairChecker(PairManager p) {
        this.p = p;
    }

    public void run() {
        while (true) {
            p.checkCount.incrementAndGet();
            p.getPair().checkState();
        }
    }
}

public class CriticalSection {
    public static void main(String[] args) {
        //Pair是非线程安全的，可以将其通过Collecitons.synchronizedList将其设置为线程安全的
        ExecutorService exec = Executors.newCachedThreadPool();
        PairManager p1  =new PairManager();
        PairManager p2  =new PairManager();

        PairManipulator pm1 =new PairManipulator(p1);
        PairManipulator pm2 =new PairManipulator(p2);

        PairChecker c1  =new PairChecker(p1);
        PairChecker c2  =new PairChecker(p2);

        exec.execute(pm1);
        exec.execute(pm2);
        exec.execute(c1);
        exec.execute(c2);
        try {
            TimeUnit.MILLISECONDS.sleep(400);
        } catch (InterruptedException ex) {
            System.out.println("sleep interrupted");
        }
        System.out.println("pm1" + pm1 + " \npm2:" + pm2);
        System.exit(0);
    }
}