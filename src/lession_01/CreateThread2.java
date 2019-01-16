package lession_01;
/**
 * 线程的方式2 (实现Runnable接口）
 * 教程地址：http://www.clips.xin/article/91
 */
public class CreateThread2 implements  Runnable{
    public static void main(String[] args) {
        CreateThread2 t1= new CreateThread2();
        Thread thread1= new Thread(t1);
        thread1.start(); //开启另一个线程分支，main线程继续向下执行
        for (int i=0;i<100;i++){
            System.out.println("main thread : "+i);
        }
    }

    @Override
    public void run() {
        for (int i=0;i<100;i++){
            System.out.println("Runnable1 :"+i );
        }
    }
}
