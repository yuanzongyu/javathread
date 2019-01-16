package lession_03.callable;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Future模式的例子
 * 教程地址：http://www.clips.xin/article/94
 */
class TaskWithResult implements Callable<String> {
    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

    public String call() {
        return "result of TaskWithResult " + id;
    }
}

public class CallableDemo {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        for (int i = 0; i < 1; i++) {
            results.add(exec.submit(new TaskWithResult(i))); // exec.submit()会返回Future对象
        }
        for (Future<String> fs : results)
            try {
                // get()方法进行blocks until completion（完成）
                System.out.println(fs.get()); // Future 的get()方法来获取结果
            } catch (InterruptedException e) {
                System.out.println(e);
                return;
            } catch (ExecutionException e) {
                System.out.println(e);
            } finally {
                exec.shutdown();
            }
    }
}
