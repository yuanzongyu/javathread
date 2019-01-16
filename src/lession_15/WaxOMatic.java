package lession_15;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Car{
    private boolean waxOn = false;  //涂蜡状态
    public synchronized  void waxed(){   waxOn = true;notifyAll(); }
    public synchronized  void buffed(){  waxOn=false;notifyAll(); }
    public synchronized  void waitForWaxing() throws  InterruptedException{
         while (waxOn ==false){ wait();}
          }
    public synchronized  void waitForBuffing() throws  InterruptedException{ while(waxOn){ wait(); } }
}
class  WaxOn implements  Runnable{
    private  Car car ; //同一个资源
    public  WaxOn(Car car){ this.car = car;}
    public void run(){
        try{while (!Thread.interrupted()){
         System.out.println("涂蜡");TimeUnit.MILLISECONDS.sleep(200);car.waxed();car.waitForBuffing(); }
        }catch (InterruptedException e){ System.out.println("通过interrupt退出run()方法"); }
        System.out.println("涂蜡作业结束");
    }
}
class WaxOff implements  Runnable{
    private Car car ;
    public WaxOff(Car car){this.car = car;}
    public void run(){
        try{
            while(!Thread.interrupted()){
                car.waitForWaxing(); //等待涂蜡完成
                TimeUnit.MILLISECONDS.sleep(200);
                System.out.println(" 抛光完成");
                car.buffed(); //抛光完成
            }
        }catch (InterruptedException  ex){ System.out.println(" 通过Interrupt 退出run()方法"); }
        System.out.println("抛光作业完成!");
    }
}
public class WaxOMatic{

    public static void main(String[] args) throws  InterruptedException{
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute( new WaxOn(car));
        exec.execute(new WaxOff(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}