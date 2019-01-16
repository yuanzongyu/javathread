package lession_13;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CloseResource {
    public static void main(String[] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InputStream socketInput = new Socket("localhost",8080).getInputStream();
        exec.execute(new IOBlocked(socketInput));
        exec.execute(new IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("shutting down all threads ");
        exec.shutdownNow();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("close："+socketInput.getClass().getName());
        socketInput.close();  // 底层资源 关闭，释放所有的阻塞资源
        TimeUnit.SECONDS.sleep(1);
        System.out.println("close："+System.in.getClass().getName());
        System.in.close(); // 底层资源关闭，释放所有的阻塞资源

    }
}
