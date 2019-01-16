package lession_18;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class WaxOn implements Runnable {
    private Car car;
    public WaxOn(Car c) {
        car = c;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println("wax on! ");
                TimeUnit.MICROSECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();

            }
        } catch (Exception ex) {
            System.out.println(" exiting via interrupt");
        }
        System.out.println("ending wax on  task !");
    }
}
class WaxOff implements Runnable {
    private Car car;
    public WaxOff(Car car) {
        this.car = car;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                car.waitForWaxing();
                System.out.println(" wax off ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        } catch (Exception ex) {
            System.out.println("exiting via interrupted");
        }
        System.out.println("ending wax off task");
    }

}
class  Car{
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition(); // 实例化 Condition
    private boolean waxOn = false;
    public void waxed(){
        lock.lock();
        try{
            waxOn =true;
            condition.signalAll(); //进行通知
        }finally {
            lock.unlock(); //解锁
        }
    }

    public void buffed(){
        lock.lock();
        try{
            waxOn =false;
            condition.signalAll(); //signalAll
        }finally {
            lock.unlock();
        }
    }
    public void waitForWaxing() throws InterruptedException{
        lock.lock();
        try{
            while (waxOn == false){
                condition.await(); // 等
            }
        }finally {
            lock.unlock();
        }
    }
    public  void waitForBuffing() throws InterruptedException{
        lock.lock();
        try{
            while (waxOn){
                condition.await();
            }
        }finally {
            lock.unlock();
        }
    }
}

public class WaxOMatic {
    public static void main(String[] args) throws Exception {
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOn(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
