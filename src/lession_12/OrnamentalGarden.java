package lession_12;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Count{
    private int count=0;
    private Random random = new Random(47);
    //同步的自增
    public synchronized  int increment(){
        int temp = count;
        if(random.nextBoolean()){ Thread.yield(); }
        return (count = ++temp);
    }
    //同步的获取值
    public synchronized int value(){
        return count;
    }
}

class Entrance implements  Runnable{
    private static Count count = new Count();
    private static List<Entrance> entrances = new ArrayList<Entrance>();
    private final int id; //final 不会有并发问题
    private int  number;
    public static volatile  boolean canceled = false;
    public static void canceled(){
        canceled = true;
    }
    public Entrance(int id){
        this.id=id;
        entrances.add(this);
    }
    public void run(){
        while(!canceled){
            synchronized (this){  //锁住
                ++number;
            }
            System.out.println(this + " total :  "+count.increment()); //
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            }catch (Exception ex){
                System.out.println("sleep interrupted !");
            }
        }
        System.out.println("stoping    " +  this );
    }
    public  synchronized  int getValue(){
        return  number;
    }
    public String toString(){
        return "entrance "+id+":"+getValue();
    }
    public static int getTotalCount(){
        return count.value();
    }
    public static int sumEntrances(){
        int sum=0;
        for(Entrance e:entrances){
            sum+=e.getValue();
        }
        return sum;
    }
}

public class OrnamentalGarden {
    public static void main(String[] args) throws  Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0;i<5;i++){
            exec.execute(new Entrance(i));
        }
        TimeUnit.SECONDS.sleep(3);
        Entrance.canceled();
        exec.shutdown();

        if(!exec.awaitTermination(100, TimeUnit.MILLISECONDS)){
            System.out.println("some task were not terminated !");
        }
        System.out.println("total :"+ Entrance.getTotalCount());
        System.out.println("sum of entrances : "+Entrance.sumEntrances());
    }
}
