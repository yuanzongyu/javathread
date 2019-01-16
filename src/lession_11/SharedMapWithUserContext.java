package lession_11;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedMapWithUserContext implements Runnable{
    public static Map<Integer, String> userContextPerUserId = new
            ConcurrentHashMap<>();
    private Integer userId;
    public SharedMapWithUserContext(Integer uid){
        this.userId= uid;
    }

    public void run(){
        //为每个线程创建线程安全的变量
        userContextPerUserId.put(userId,"name:"+userId);
    }

    public static void main(String[] args) {
        SharedMapWithUserContext firstUser = new SharedMapWithUserContext(1);
        SharedMapWithUserContext secondUser = new SharedMapWithUserContext(2);
        new Thread(firstUser).start();
        new Thread(secondUser).start();
        System.out.println(userContextPerUserId.get(1));
        System.out.println(userContextPerUserId.get(2));
    }
}
