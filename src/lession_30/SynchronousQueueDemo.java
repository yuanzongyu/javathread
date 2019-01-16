package lession_30;

import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue :线程安全，使用在生产者，消费者，可以在两个线程内安全的交换数据
 */
public class SynchronousQueueDemo {
    public static void main(String[] args)
            throws InterruptedException{
        SynchronousQueue<String> q= new SynchronousQueue();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    q.put("hell world");
                }catch (Exception e){}
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(q.take());
                }catch (Exception e){}
            }
        }).start();
    }
}
