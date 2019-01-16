package lession_08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomictyTest implements  Runnable{
    private int i=0;

    public int getValue(){
        return i;
    }

    public synchronized  void increment(){
        i++;
        i++;
    }
    @Override
    public void run() {
        while (true) {
            increment();
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        AtomictyTest atomictyTest = new AtomictyTest();
        exec.execute(atomictyTest);
        while(true){
            int val=atomictyTest.getValue();
            if(val%2 !=0){
                System.out.println(val);
                System.exit(0);
            }
        }
    }
}
