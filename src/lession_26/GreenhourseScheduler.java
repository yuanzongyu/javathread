package lession_26;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务：ScheduledThreadPoolExecutor
 *
 */
public class GreenhourseScheduler {
    ScheduledThreadPoolExecutor scheduler =
            new ScheduledThreadPoolExecutor(10);  //调度线程池
    List<DataPoint> data = Collections.
            synchronizedList(new ArrayList<DataPoint>());
    public void schedule(Runnable event,long delay){
        scheduler.schedule(event,delay, TimeUnit.MILLISECONDS); //延迟多少时间执行，只执行一次
    }
    public void repeat(Runnable event,long initalDelay,long period){
        scheduler.scheduleAtFixedRate
                (event,initalDelay,period,TimeUnit.MILLISECONDS); //重复延迟进行
    }
    class Terminate implements  Runnable{
        public  void run(){
            System.out.println("终止");
            scheduler.shutdownNow();
            new Thread(){
                public  void run(){
                    for(DataPoint d: data){
                        System.out.println(d);
                    }
                }
            }.start();
        }
    }
    class Bell implements Runnable{
        public  void run(){
            System.out.println("bing");
        }
    }
    static class DataPoint{
        private String name;
        public DataPoint(String name) {
            this.name = name;
        }
        public String toString() {
            return "Data: "+name;
        }
    }
    public static void main(String[] args) {
        GreenhourseScheduler g = new GreenhourseScheduler();
        g.data.add(new DataPoint("10"));
        g.data.add(new DataPoint("20"));
        g.repeat(g.new Bell(),0,1000);
        g.schedule(g.new Terminate(),5000);
    }
}
