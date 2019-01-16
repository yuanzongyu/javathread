package lession_23;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class Horse implements Runnable{
    private static int counter=0;
    private final int id =counter++;
    private int strides = 0 ;
    private static Random random = new Random(47);
    private static CyclicBarrier barrier;
    public Horse(CyclicBarrier b){
        this.barrier = b;
    }
    public  int getStrides(){
        return strides;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                //synchronized (this){
                    strides += random.nextInt(3);
                //}
                barrier.await();
            }
        }catch (InterruptedException e){
        }catch (BrokenBarrierException e){}
    }
    public String toString(){
        return "horse: "+ id;
    }
    public String tracks(){
        StringBuilder s = new StringBuilder();
        for(int i=0;i< getStrides();i++){
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }
}
public class HorseRace {
    static final int FINISH_LINE=75;
    private List<Horse> horses = new ArrayList<Horse>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    public HorseRace(int nHorse,final int pause){
        barrier = new CyclicBarrier(nHorse, new Runnable() {
            @Override
            public void run() {
                StringBuilder s = new StringBuilder();
                for (int i=0;i<FINISH_LINE;i++){
                    s.append("=");
                }
                System.out.println(s);
                for(Horse horse:horses){
                    System.out.println(horse.tracks());
                }
                for(Horse horse:horses){
                    if(horse.getStrides() >=FINISH_LINE){
                        System.out.println(horse +" 赢了");
                        exec.shutdownNow();
                        return;
                    }
                }
                try{
                    TimeUnit.MILLISECONDS.sleep(pause);
                }catch (InterruptedException e){
                    System.out.println("barrier sleep 被打断 !");
                }
            }
        });
        for (int i=0;i<nHorse;i++){
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }
    public static void main(String[] args) {
        int nHorse =7;
        int pause=200;
        new HorseRace(nHorse,pause);
    }
}
