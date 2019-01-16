package lession_03.exercise_01_fibonacci;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Fibonacci implements Callable<Integer> {
    private int num;

    public Fibonacci(int num) {
        this.num = num;
    }

    //计算斐波拉契数列的和
    public Integer sum(int num) {
        if (num <= 1) {
            return 1;
        }
        return sum(num - 2) + sum(num - 1);
    }

    @Override
    public Integer call() throws Exception {
        return  sum(num);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<Integer>> list = new ArrayList<Future<Integer>>();
        for(int i=0;i<10;i++){
            list.add(executorService.submit(new Fibonacci(i)));
        }
        for(Future<Integer> f: list){
            try {
                System.out.println(f.get());
            }catch(InterruptedException e){
                e.printStackTrace();
            }catch(ExecutionException ex ){
                ex.printStackTrace();
            }
        }
    }
}
