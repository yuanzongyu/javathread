package lession_02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exectors管理线程
 * 教程地址：http://www.clips.xin/article/92
 */
public class Task_Executors implements  Runnable{

    //类变量，所有的实例共享
    private static int taskCount=0;
    private final int id=taskCount++;

    public String Status() {
        return "id="+id;
    }
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0;i<10;i++) {
            exec.execute(new Task_Executors());
        }
        exec.shutdown();
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println(Status());
        Thread.yield();  //Thread yield不会显示声明异常
    }
}
