package lession_11;


import java.util.concurrent.TimeUnit;

public class ThreadLocalWithUserContext implements Runnable {
    private static ThreadLocal<String> userContext = new ThreadLocal<>();
    private Integer userId;
    public ThreadLocalWithUserContext(Integer uid){
        userId =uid;
    }
    public void run(){
        userContext.set("name: "+userId);
        System.out.println("Thread 上下文 userid:"+userId+ "，设置的值为:"+userContext.get());
    }


    public static void main(String[] args) throws  InterruptedException{
        ThreadLocalWithUserContext t1= new ThreadLocalWithUserContext(1);
        ThreadLocalWithUserContext t2= new ThreadLocalWithUserContext(2);
        new Thread(t1).start();
        TimeUnit.MILLISECONDS.sleep(300);
        new Thread(t2).start();
    }
}
