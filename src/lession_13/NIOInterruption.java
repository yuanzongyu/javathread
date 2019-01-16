package lession_13;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class NIOBlocked implements  Runnable{
    private final SocketChannel sc;
    public NIOBlocked(SocketChannel sc ){
        this.sc = sc;
    }
    public void run(){
        try {
            System.out.println("waiting for read() in " + this);
            sc.read(ByteBuffer.allocate(1));
        }catch(ClosedByInterruptException ex){
            System.out.println("closedByInterruptException");
        }catch(AsynchronousCloseException ex){
            System.out.println("asynchronousCloseException");
        }
        catch (IOException ex){
            throw  new RuntimeException(ex);
        }
        System.out.println("exiting NIOBlocked.run() "+this);
    }
}
public class NIOInterruption {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InetSocketAddress isa = new InetSocketAddress("localhost",8080);
        SocketChannel sc1 = SocketChannel.open(isa);
        SocketChannel sc2 =SocketChannel.open(isa);
        Future<?> f = exec.submit(new NIOBlocked(sc1));
        exec.execute(new NIOBlocked(sc2));
        exec.shutdown();
        TimeUnit.SECONDS.sleep(1);
        f.cancel(true);
        TimeUnit.SECONDS.sleep(1);
        sc2.close(); // 管理channel 必须close()后释放block
    }
}

