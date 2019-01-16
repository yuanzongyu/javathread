package lession_07;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenCheck implements Runnable {
    private IntGenerator generator; //多个线程争夺一个资源，可能会出现并发问题
    private final int id;

    public EvenCheck(IntGenerator g, int ident) {
        generator = g;
        id = ident;
    }

    @Override
    public void run() {
        while (!generator.isCanceled()) {
            int val = generator.next();
            if (val % 2 != 0) {
                System.out.println(val + " not even");
                generator.cancel();
            } else {
                System.out.println(val + " is even");
            }
        }
    }

    public static void test(IntGenerator gp, int count) {
        System.out.println(" press control-c to exit ");
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < count; i++) {
            exec.execute(new EvenCheck(gp, i));
        }
        exec.shutdown();
    }
}
