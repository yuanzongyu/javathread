package lession_20;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 管道的输入和输出 PipedWriter, PipedReader
 * 1、可以被中断
 * 2、使用相同的通道
 */

class Sender implements Runnable{
    private PipedWriter out = new PipedWriter();
    public PipedWriter getPipedWriter(){
        return out;
    }
    public  void run(){
        try{
            while (true){
                for(char c='A';c<='Z';c++){
                    out.write(c);
                    TimeUnit.MILLISECONDS.sleep(400);
                }
            }
        }catch (InterruptedException e){
            System.out.println("sender 被打断");
        }catch (IOException e){
            System.out.println("sender 类 写入异常");
        }
    }
}
class Receiver implements  Runnable{
    private PipedReader in ;
    public Receiver(Sender sender) throws IOException{
        in = new PipedReader(sender.getPipedWriter());
    }

    public void run(){
        try{
            while (true){
                System.out.println("read"+ (char)in.read()+",");  //阻塞的去读
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
public class PipedIO {
    public static void main(String[] args)throws  Exception {
        Sender sender = new Sender();
        Receiver receiver = new Receiver(sender);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(sender);
        exec.execute(receiver);
        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();
    }
}
