package lession_06;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ExceptionThread2 implements Runnable {
    public void run() {
        Thread t = Thread.currentThread();
        System.out.println("by" + t);
        System.out.println("eh1" + t.getUncaughtExceptionHandler());
        throw new RuntimeException(); //new异常抛出
    }
}
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("catch" + t.getName() + ",e: " + e);
    }
}
class HandlerThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        System.out.println("new Thread");
        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        System.out.println("eh2 :" + t.getUncaughtExceptionHandler());
        return t;
    }
}
public class CaptureUncaughtDefaultException {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool(new HandlerThreadFactory());
        exec.execute(new ExceptionThread2());
        exec.shutdown();
    }
}
