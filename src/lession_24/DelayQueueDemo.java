package lession_24;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * NANOSECONDS.convert() 将指定的参数转换位纳秒
 *
 * Delayed接口继承Compare接口，要重写getDelay和compareTo方法
 *
 *
 */

class DelayedTask implements Runnable, Delayed {
    private static int counter = 0;
    private final  int id = counter++;
    private final  int delta;
    private final  long trigger;
    protected static List<DelayedTask> sequence = new ArrayList<DelayedTask>();

    public DelayedTask(int delayMillseconds) {
        delta = delayMillseconds;
        //给元素在队列中设置过期时间
        trigger = System.nanoTime() + NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);
        sequence.add(this);  //将任务放到集合中
    }

    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask) o;
        if (trigger < that.trigger) { return -1; }
        if (trigger > that.trigger) { return 1; }
        return 0;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        //返回关联对象的 剩余延时时间
        return unit.convert(trigger - System.nanoTime(), NANOSECONDS);
    }

    @Override
    public void run() {
        System.out.println(this + " ");
    }

    public String toString() {
        return String.format("[%1$-4d]", delta) + " Task " + id;
    }

    public String summary() {
        return "(" + id + ":" + delta + ")";
    }

    public static class EndSentinel extends DelayedTask {  //sentinel ：哨兵
        private ExecutorService exec;
        public EndSentinel(int delay, ExecutorService e) {
            super(delay);
            exec = e;
        }
        public void run() {
            for (DelayedTask pt : sequence) {
                System.out.println(pt.summary() + " ");
            }
            System.out.println(this + "calling shutdownNow()");
            exec.shutdownNow();  //通过EndSentinel 关闭所有的线程
        }
    }
}

class DelayedTaskConsumer implements Runnable {
    private DelayQueue<DelayedTask> q;
    public DelayedTaskConsumer(DelayQueue q) {
        this.q = q;
    }
    public void run() {
        try {
            while (!Thread.interrupted()) {
                q.take().run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("完成 DealyTask 消费 !");
    }
}

public class DelayQueueDemo {
    public static void main(String[] args) {
        Random random = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<DelayedTask>();
        for(int i=0;i<20;i++){
            queue.put(new DelayedTask(random.nextInt(5000)));  // delayTask 实现了Delayed接口
        }
        queue.add(new DelayedTask.EndSentinel(5000,exec ));
        exec.execute(new DelayedTaskConsumer(queue));
    }
}
