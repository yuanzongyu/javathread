package lession_06;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExceptionThread implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException(" 异常! "); //向外传播到控制台
    }

    public static void main(String[] args) {
        try {
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(new ExceptionThread());
        }catch (Exception e){
            System.out.println("已捕获异常!");
        }
    }
}
