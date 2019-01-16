package lession_08.serial;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CircularSet{
    private int[] array;
    private int len;
    private int index=0;

    public CircularSet(int size){
        array = new int[size];
        len=size;
        //对数组进行初始化
        for (int i=0;i<size;i++){
            array[i]=-1;
        }
    }
    public synchronized void add(int i){
        array[index] = i;
        index = ++index%len; // index到了len的长度就从0开始
    }
    public  synchronized boolean contains(int val){
        for (int i=0;i<len;i++){
            if(array[i] == val){
                return true;
            }
        }
        return false;
    }
}
public class SerialNumberChecker {
    private static CircularSet serials = new CircularSet(1000);
    private static ExecutorService exec = Executors.newCachedThreadPool();

    static class SeriChecker implements  Runnable{
        @Override
        public void run() {
            while(true){
                int serial = SerialNumberGenerator.nextSerialNumber();
                if(serials.contains(serial)){
                    System.out.println("重复："+serial);
                    System.exit(0);
                }
                serials.add(serial);
            }
        }
    }

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            exec.execute(new SeriChecker());
        }
    }
}
