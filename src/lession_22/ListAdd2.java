package lession_22;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ListAdd2 {
    static volatile List list = new ArrayList<>();
    private static CountDownLatch count = new CountDownLatch(1);  //使用CountDownLatch

    static class Thread1 implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                list.add("hell world " + i);
                System.out.println("新增  hello world" + i + " 成功");
                if (list.size() == 5) {
                    System.out.println("obj 发出通知终止Thread2 任务");
                    count.countDown();
                }
            }
        }
    }

    static class Thread2 implements Runnable {
        public void run() {
            if (list.size() != 5) {
                try {
                    count.await();
                } catch (InterruptedException e) {
                    System.out.println("wait() 被打断!");
                }
            }
            System.out.println("list size = 5,thread2任务完成");
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ListAdd2 listAdd = new ListAdd2();
        new Thread(new Thread2()).start();
        TimeUnit.MILLISECONDS.sleep(300);  //让Thread2 先起来
        new Thread(new Thread1()).start();
    }
}
