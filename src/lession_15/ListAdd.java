package lession_15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListAdd {
    static volatile List list = new ArrayList<>();
    private static Object obj = new Object();
    static class Thread1 implements  Runnable{
        public void run(){
            synchronized (obj){
                for(int i=0;i<10;i++) {
                    list.add("hell world "+i);
                    System.out.println("新增  hello world"+i+" 成功");
                    if (list.size() == 5) {
                        System.out.println("obj 发出通知终止Thread2 任务");
                        obj.notify(); //notify()不释放锁，必须和synchronized 配合使用
                    }
                }
            }
        }
    }
    static class Thread2 implements Runnable{
        public void run(){
            synchronized (obj){
                if(list.size()!= 5){
                    try {
                        obj.wait(); //释放锁
                    }catch (InterruptedException e){ System.out.println("wait() 被打断!"); }
                }
            }
            System.out.println("list size = 5,thread2任务完成");
            throw new RuntimeException();
        }
    }
    public static void main(String[] args)throws  InterruptedException {
        ListAdd listAdd = new ListAdd();
        new Thread(new Thread2()).start();
        TimeUnit.MILLISECONDS.sleep(300);  //让Thread2 先起来
        new Thread(new Thread1()).start();
    }
}
