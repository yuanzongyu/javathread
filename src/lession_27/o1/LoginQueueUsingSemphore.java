package lession_27.o1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class LoginQueueUsingSemphore {
    private Semaphore semaphore;

    public LoginQueueUsingSemphore(int slotLimit){
        semaphore = new Semaphore(slotLimit);  //设置Semaphore的数量
    }

    boolean tryLogin(){
        return semaphore.tryAcquire(); //阻塞试着去获取锁
    }

    void logout(){
        semaphore.release(); //释放锁
    }
    int  availableSlots(){
        return semaphore.availablePermits();  //返回可用的许可的数量
    }

    public static void main(String[] args) {
        int slots= 10;
        ExecutorService exec = Executors.newFixedThreadPool(slots);
        LoginQueueUsingSemphore loginQueue = new LoginQueueUsingSemphore(slots);
        IntStream.range(0,slots).forEach(user->exec.execute(loginQueue::tryLogin));
        exec.shutdown();
        System.out.println(loginQueue.availableSlots());
        System.out.println(loginQueue.tryLogin());
        loginQueue.logout();  //释放一个relase
        System.out.println(loginQueue.availableSlots());  //可用1
        System.out.println(loginQueue.tryLogin());  //拿到许可

    }
}
