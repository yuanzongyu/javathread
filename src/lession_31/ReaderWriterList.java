package lession_31;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock :
 * 1、适合读多写少场景
 * 2、其有两把锁，一个是writeLock，还有一个readLock。
 * 3、在没有写的情况下，多线程可以获取读锁获取数据
 * 4、如果有一个写锁，其他的线程阻塞
 * @param <T>
 */

public class ReaderWriterList<T> {
    private ArrayList<T> lockedList;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public ReaderWriterList(int size , T element){
        lockedList = new ArrayList<>(Collections.nCopies(size,element));
    }
    public T set(int index,T element){
        Lock writeLock  = lock.writeLock(); //写锁
        writeLock.lock();
        try{
          return   lockedList.set(index,element);
        }finally {
            writeLock.unlock();
        }
    }
    public  T get(int index){
        Lock rlock = lock.readLock();
        rlock.lock();
        try{
            //多个线程可以获取读锁
            if(lock.getReadLockCount() >1){
                System.out.println(lock.getReadLockCount());
            }
            return lockedList.get(index);
        }finally {
            rlock.unlock();
        }
    }

    public static void main(String[] args) {
        new ReadWriterListTest(30,1);
    }
}

class  ReadWriterListTest{
    ExecutorService exec = Executors.newCachedThreadPool();
    private final static int SIZE=100;
    private static Random rand = new Random(47);
    private ReaderWriterList<Integer> list =
            new ReaderWriterList<Integer>(SIZE,0);

    public ReadWriterListTest(int readers , int writers){
        for(int i=0;i<readers;i++){
            exec.execute(new Reader());
        }
        for (int i=0;i<writers;i++){
            exec.execute(new Writer());
        }
    }

    private class Writer implements Runnable{
        @Override
        public void run() {
            try {
                for (int i = 0; i < 20; i++) {
                    list.set(i, rand.nextInt());
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            }catch (InterruptedException e){}
            System.out.println("写入完成，shut down");
            exec.shutdownNow();
        }
    }
    private class Reader implements  Runnable{
        @Override
        public void run() {
            try{
                while(!Thread.interrupted()){
                    for (int i=0;i<SIZE;i++){
                        list.get(i);
                        TimeUnit.MILLISECONDS.sleep(1);
                    }
                }
            }catch (InterruptedException e){}
        }
    }
}