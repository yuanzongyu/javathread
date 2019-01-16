package lession_06;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ExcepitonThread1 implements  Runnable{
    public void run(){
        System.out.println("by"+Thread.currentThread());
        System.out.println("抛出异常");
        throw new RuntimeException();
    }
}
public class SettingDefaultHandler {
    public static void main(String[] args) {
        //设置默认的未捕获的异常类
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ExcepitonThread1());
        exec.shutdown();
    }
}
