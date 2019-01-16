package lession_28;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ExchangerDemo implements Runnable {
    Exchanger exchanger = null;  //创建Exchanger ，被两个线程使用
    Object object =null;

    public  ExchangerDemo(Exchanger e, Object ox){
        this.exchanger = e ;
        this.object=ox;
    }


    public void run(){
        Object previous  = this.object;
        try{
            this.object = this.exchanger.exchange(this.object);
            TimeUnit.SECONDS.sleep(3);
            System.out.println(Thread.currentThread().getName()+"exchangerd"+
                    previous+" for "+this.object);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Exchanger exchanger = new Exchanger();
        ExchangerDemo  e1 = new ExchangerDemo(exchanger,"A");
        ExchangerDemo  e2 = new ExchangerDemo(exchanger,"B");
        new Thread(e1).start();
        new Thread(e2).start();
    }
}
