package lession_19;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NumbersConsumer implements Runnable {
    private BlockingQueue<Integer> queue;
    private final int poisonPill;
    public NumbersConsumer(BlockingQueue<Integer> queue,int poisonPill){
        this.queue=queue; this.poisonPill = poisonPill;
    }
    @Override
    public void run() {
        try{
            while (true){
                Integer  number = queue.take(); // 阻塞直到队列有数据
                if(number.equals(poisonPill)){
                    System.out.println("number =  "+ number);
                    return;
                }
                System.out.println(Thread.currentThread().getName()+" result:"+number);
            }
        }catch (InterruptedException e){ Thread.currentThread().interrupt(); }
    }
    public static void main(String[] args) {
        int BOUND = 10;
        int N_PRODUCERS = 4;
        int N_CONSUMERS = Runtime.getRuntime().availableProcessors(); //4
        int poisonPill = Integer.MAX_VALUE;  //2147483647
        int poisonPillPerProducer = N_CONSUMERS / N_PRODUCERS; //1
        int mod = N_CONSUMERS % N_PRODUCERS;  //0
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(BOUND);
        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new NumbersProducer(queue, poisonPill, poisonPillPerProducer)).start();
        }
        for (int j = 0; j < N_CONSUMERS; j++) {
            new Thread(new NumbersConsumer(queue, poisonPill)).start();
        }
        new Thread(new NumbersProducer(queue, poisonPill, poisonPillPerProducer + mod)).start();
    }
}
