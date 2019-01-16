package lession_01;

/**
 * 创建线程的方式1（继承Thread类）
 * 教程地址： http://www.clips.xin/article/91
 */
public class CreateThread extends Thread {
    public static void main(String[] args) {

        CreateThread t1 = new CreateThread();
        CreateThread t2 = new CreateThread();
        t1.start();
        //t1.start();  将会抛出异常:IllegalThreadStateException
        t2.start();
    }

    @Override
    public void run() {
        //多线程要执行的代码
        for (int i = 0; i < 10; i++) {
            System.out.println("thread : " + i);
        }
    }
}
