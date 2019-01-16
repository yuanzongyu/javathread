package lession_05;

import java.util.concurrent.TimeUnit;

class Daemon implements  Runnable{
    Thread [] t = new Thread[10];
    @Override
    public void run() {
        for(int i=0;i<t.length;i++){
            t[i] = new Thread(new DaemonSpaw());
            t[i].start();
            System.out.println("DaemonSpaw "+i +" started ...");
        }
        for(int i=0;i<t.length;i++){
            System.out.println("t["+i+"].isDaemon()"+t[i].isDaemon()+",");
        }
        while(true){
            Thread.yield();
        }
    }
}
class DaemonSpaw implements  Runnable{
    public void run(){
        while (true){
            Thread.yield();
        }
    }
}
public class Daemons {
    public static void main(String[] args) throws InterruptedException {
        Thread  d = new Thread(new Daemon());
        d.setDaemon(true);
        d.start();
        System.out.println("d.isDaemon() = "+d.isDaemon());
        TimeUnit.SECONDS.sleep(10);
    }
}
