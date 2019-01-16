package lession_05;

import java.util.concurrent.ThreadFactory;

/**
 * 通过线程工厂创建后台线程
 */
public class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);  //将线程设置为后台线程
        return t;
    }
}
