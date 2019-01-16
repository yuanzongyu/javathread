package lession_21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FixedDiningPhilosophers {
    public static void main(String[] args) throws Exception{
        ExecutorService exec  = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[5];
        for (int i = 0; i < 5; i++)
            sticks[i] = new Chopstick();
        for (int i = 0; i < 5; i++)
            if (i < (5 - 1))
                exec.execute(new Philosopher(sticks[i], sticks[i + 1], i));
            else
                exec.execute(new Philosopher(sticks[0], sticks[i], i));
        if (args.length == 3 && args[2].equals("timeout"))
            TimeUnit.SECONDS.sleep(5);
        else {
            System.out.println("Press 'Enter' to quit");
            System.in.read();
        }
        exec.shutdownNow();
    }
}
