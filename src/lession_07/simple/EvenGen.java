package lession_07.simple;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class EvenCheck implements  Runnable{
     //boolean 是原子的，volatile 让其在线程间立即可见
    EvenGen evenGen;
    private final int id; //线程ID

    public EvenCheck(EvenGen evenGen,int ident){
        this.evenGen = evenGen;
        id=ident;
    }
    @Override
    public void run() {
        while(!evenGen.getFlag()){
         int   num =evenGen.getEven();
            if(num%2 !=0){
                System.out.println("num="+num+"线程id="+id+"产生了奇数");
                evenGen.cancel();
            }
        }
    }
}
public class EvenGen  {
    private int num=0;
    private volatile  boolean flag= false;
    //使用synchronized 同步
    public  synchronized int getEven(){
        ++num;
        Thread.yield(); //
        ++num;
        return num;
    }

    public void cancel( ){
        flag=true;
    }
    public boolean getFlag(){
        return flag;
    }

    public static void main(String[] args){
        EvenGen  evenGen = new EvenGen(); //创建同一个共享资源，为了并发模拟
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0;i<10;i++){
            exec.execute(new EvenCheck(evenGen,i));
        }
        exec.shutdown();
    }
}
