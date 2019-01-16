package lession_28.copyonwriteArrayList;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 *  CopyOnWriteArrayList 多线程并发删除不会报报错Concurrent-ModificationException 异常
 */
public class ExchangerDemo {
    static int size = 10;
    private static  int delay = 5;

    public static void main(String[] args) throws InterruptedException{
        ExecutorService exec = Executors.newCachedThreadPool();
        Exchanger<List> exchanger = new Exchanger<>();  //交换List
        List producerList = new CopyOnWriteArrayList<>(),
                consumerList = new CopyOnWriteArrayList<>();
        exec.execute(new ExchangerProducter(exchanger, producerList));
        ExchangerConsumer e =  new ExchangerConsumer(exchanger, consumerList);
        exec.execute(e);
        TimeUnit.SECONDS.sleep(delay);
        exec.shutdownNow();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(e.getHolderSize());

    }
}

class ExchangerProducter implements  Runnable{

    private Exchanger<List> exchanger;
    private Random random = new Random(47);
    private List holder;

    ExchangerProducter(Exchanger<List> exchanger, List holder) {
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                for (int i=0;i<ExchangerDemo.size;i++) {
                 holder.add(random.nextInt(10));
                }
                holder = exchanger.exchange(holder); //生产好了
            }

        }catch (InterruptedException e){
           // e.printStackTrace();
        }
    }
}
class ExchangerConsumer implements Runnable{
    private Exchanger<List> exchanger;
    private List holder;
    private volatile String value;  //禁止jvm对其优化

    public  int getHolderSize(){
        return holder.size();
    }

    ExchangerConsumer(Exchanger<List> exchanger, List holder) {
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                holder = exchanger.exchange(holder); //消费
                for(Object t:holder){
                    value = t.toString();
                    holder.remove(t);
                    System.out.println(value+"被移除!");
                }
            }
        }catch (InterruptedException e){
            //e.printStackTrace();
        }
        System.out.println("final value :" +value);
    }
}