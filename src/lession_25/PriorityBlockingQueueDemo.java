package lession_25;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

class PrioritizedTask implements Runnable , Comparable<PrioritizedTask>{
    private Random random = new Random(47);
    private static int counter =0;
    private final int id =counter++;
    private final int priority;
    protected static List<PrioritizedTask> sequence =
            new ArrayList<PrioritizedTask>();

    public PrioritizedTask(int priority){
        this.priority = priority;
        sequence.add(this);
    }

    @Override
    public int compareTo(PrioritizedTask o) {
        return priority < o.priority? 1 : (priority> o.priority? -1:0);
    }

    @Override
    public void run() {
        try{
            TimeUnit.MILLISECONDS.sleep(random.nextInt());
        }catch (InterruptedException e){}
        System.out.println(this);
    }
    public String toString(){
        return String.format("[%1$-3d]", priority) + " Task " + id;
    }
    public String summary(){
        return "(" + id + ":" + priority + ")";
    }

    public static class Endsentinel extends PrioritizedTask{
        private ExecutorService exec ;
        public Endsentinel(ExecutorService e ){
            super(-1);  //设置其为低优先级任务
            exec=e;
        }
        public void run(){
            int count=0;
            for(PrioritizedTask pt: sequence){
                System.out.println(pt.summary());
                if(++count %5 ==0){
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println(this +"calling shutdownNow()");
            exec.shutdownNow();
        }
    }
}
class PrioritizedTaskProducer implements  Runnable{
    private Random rand = new Random(47);
    private Queue<Runnable> queue;
    private ExecutorService exec;

    public  PrioritizedTaskProducer(Queue<Runnable> q,ExecutorService e){
        queue= q;
        exec =e;
    }
    public void run(){
        for (int i=0;i<20;i++){
            //插入随机优先级任务
            queue.add(new PrioritizedTask(rand.nextInt(10)));
            Thread.yield();
        }
        try{
            for(int i=0;i<10;i++){
                TimeUnit.MILLISECONDS.sleep(250);
                queue.add(new PrioritizedTask(10));  //创建最高优先级任务
            }
            for (int i=0;i<10;i++){
                queue.add(new PrioritizedTask(i));//创建优先级由低到高的任务
            }
            queue.add(new PrioritizedTask.Endsentinel(exec));  //停止所有的任务
        }catch(InterruptedException e){

        }
        System.out.println("完成 优先级任务的生产!");
    }
}

class PrioritizedTaskConsumer implements Runnable{
    private PriorityBlockingQueue<Runnable> q;

    public PrioritizedTaskConsumer(PriorityBlockingQueue<Runnable> q){
        this.q=q;
    }

    public void run(){
        try{
            while(!Thread.interrupted()){
                q.take().run();  //阻塞的获取
            }
        }catch (InterruptedException e){

        }
        System.out.println("PrioritizedTaskConsumer 完成");
    }
}
public class PriorityBlockingQueueDemo {
    public static void main(String[] args) {
        Random rand = new Random(47);
        ExecutorService exec= Executors.newCachedThreadPool();
        PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();
        exec.execute(new PrioritizedTaskProducer(queue,exec));
        exec.execute(new PrioritizedTaskConsumer(queue));
    }
}



