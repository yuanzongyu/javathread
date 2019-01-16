package lession_17;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


class Meal {
    private final int orderNum;
    public Meal(int orderNum) { this.orderNum = orderNum; }
    public String toString() { return "meal " + orderNum; }
}

class WaitPerson implements Runnable {
    private Restaurant restaurant;

    public WaitPerson(Restaurant r) {
        restaurant = r;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal == null) {
                        wait();
                    }
                }
                System.out.println("服务员拿到" + restaurant.meal);
                synchronized (restaurant.chef) {
                    restaurant.meal = null;
                    restaurant.chef.notify();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("服务员被打断");
        }
    }
}

class Chef implements Runnable {
    private int count = 0;
    private Restaurant restaurant;

    public Chef(Restaurant r) {
        this.restaurant = r;
    }

    public void run() {
        try {
            while (!Thread.interrupted()){
                synchronized (this){
                    while (restaurant.meal!=null){
                        wait();
                    }
                }
                if(++count ==10){
                    System.out.println("没食物可以做了，程序退出");
                    restaurant.exec.shutdownNow();
                }
                System.out.println("上菜！");
                synchronized (restaurant.waitPerson){
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);  //被打断，被下面的异常接收，如果不加，由while循环退出
            }
        } catch (InterruptedException e) {
            System.out.println("厨师被打断!");
        }
    }
}

public class Restaurant {
    Meal meal;
    Chef chef = new Chef(this);
    WaitPerson waitPerson  =new  WaitPerson(this);
    ExecutorService exec = Executors.newCachedThreadPool();
    public Restaurant(){
        exec.execute(chef);
        exec.execute(waitPerson);
    }
    public static void main(String[] args) {
        new Restaurant();
    }
}
