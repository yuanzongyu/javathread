package lession_14;
import java.util.concurrent.TimeUnit;

class NeedsCleanup{
    private final int id;
    public NeedsCleanup(int ident){ this.id=ident;System.out.println("needscleanup :"+id); }
    public  void cleanup(){ System.out.println("cleaning up "+id); }
}
class Blocked3 implements  Runnable{
    private volatile  double d=0.0;
    public void run() {
        try {
            while (!Thread.interrupted()) {
                NeedsCleanup n1 = new NeedsCleanup(1);
                try {
                    System.out.println("sleeping");
                    TimeUnit.SECONDS.sleep(1);
                    NeedsCleanup n2 = new NeedsCleanup(2);
                    try {
                        System.out.println("calculating");
                        for (int i = 1; i < 2500000; i++) { d = d + (Math.PI + Math.E) / d; }
                        System.out.println("finished time-consuming operation，d:"+d);
                    }finally { n2.cleanup(); }
                }finally { n1.cleanup(); }
            }
            System.out.println("exiting via while() test");
        } catch (InterruptedException e) {
            System.out.println("isInterrupted: "+Thread.currentThread().isInterrupted());
            System.out.println("exiting via InterruptedException");
        }
    }
}
public class InterruptingIdiom {
    public static void main(String[] args) throws  Exception{
        Thread t =new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(1000);
        t.interrupt();  // 对线程设置打断状态
    }
}