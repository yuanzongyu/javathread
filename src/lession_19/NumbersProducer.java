package lession_19;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class NumbersProducer implements Runnable {
    private BlockingQueue<Integer> numbersQueue;
    private final int poisonPill;
    private final int poisionPillPerProducer;
    public NumbersProducer(BlockingQueue<Integer> numbersQueue,int poisonPill,int poisionPillPerProducer){
        this.numbersQueue=numbersQueue;
        this.poisonPill=poisonPill;
        this.poisionPillPerProducer=poisionPillPerProducer;
    }
    @Override
    public void run() {
        try{
            generateNumbers();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
    private void generateNumbers() throws InterruptedException{
        for(int i=0;i<100;i++){
            numbersQueue.put(ThreadLocalRandom.current().nextInt(100));
        }
        for(int j=0;j<poisionPillPerProducer;j++){
            numbersQueue.put(poisonPill);
        }
    }
}
