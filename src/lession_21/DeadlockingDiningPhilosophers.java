package lession_21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeadlockingDiningPhilosophers {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[5]; //5个筷子
        for (int i = 0; i < 5; i++) {
            sticks[i] = new Chopstick(); //初始化5个筷子
        }
        for (int i = 0; i < 5; i++) {
            exec.execute(new Philosopher(sticks[i], sticks[(i + 1) % 5], i));
        }
      //  TimeUnit.SECONDS.sleep(6);
        //exec.shutdownNow();
    }
}
