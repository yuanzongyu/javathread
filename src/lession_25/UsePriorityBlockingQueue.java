package lession_25;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

// PriorityBlockingQueue, 任务需要实现Comparable接口
class Task{
    int id;
    public  Task(int id){
        this.id=id;
    }

    public String toString(){
        return "Task : "+id;
    }

}
public class UsePriorityBlockingQueue {
    public static void main(String[] args) throws  InterruptedException {
        PriorityBlockingQueue<Task> q = new PriorityBlockingQueue<Task>(10, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.id>o2.id ? 1:(o1.id<o2.id? -1:0);
            }
        });
        Task  t1 = new Task(3);
        Task  t2 = new Task(4);
        Task  t3 = new Task(1);
        Task  t4 = new Task(9); // 完全二叉树，顶部是最小值
        q.add(t1);
        q.add(t2);
        q.add(t3);
        q.add(t4);
        System.out.println(q);    //插入的时候没有排序，取出来的时候才排序
        Task tt1=  q.take();     //take()的时候进行排序
        System.out.println("take Task : "+tt1.id);
        System.out.println(q);
    }
}
