package lession_10;

class DualSynch{
    private Object sycObject = new Object();

    // synchronized整个方法(当前对象)
    public synchronized  void f(){
        for (int i=0;i<5;i++){
            System.out.println("f()");
            Thread.yield();
        }
    }

    // synchronized代码块，给到对象的类
    public void g(){
        synchronized (sycObject){
            for(int i=0;i<5;i++) {
                System.out.println("g()");
                Thread.yield();
            }
        }
    }
}
public class SyncObject {
    public static void main(String[] args) {
        final DualSynch ds = new DualSynch();
        new Thread(){
            public  void run(){
                ds.f();
            }
        }.start();
        ds.g(); //   和f()同时运行
    }
}
