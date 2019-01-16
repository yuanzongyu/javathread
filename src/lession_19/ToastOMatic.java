package lession_19;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Toast {
    public enum Status {
        DRY, BUFFTERED, JAMMED
    }

    private Status status = Status.DRY;
    private final int id;

    public int getId() {
        return id;
    }

    public Toast(int id) {
        this.id = id;
    }

    public void butter() {
        status = Status.BUFFTERED;
    }

    public void jam() {
        status = Status.JAMMED;
    }

    public Status getStatus() {
        return status;
    }

    public String toString() {
        return "吐司" + id + ":" + status;
    }
}

//生产者
class Toaster implements Runnable {
    private LinkedBlockingQueue<Toast> queue;

    public Toaster(LinkedBlockingQueue<Toast> q) {
        this.queue = q;
    }

    private int count = 0;

    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(400);
                Toast t = new Toast(count++);
                System.out.println(t);
                queue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster 被打断 !");
        }
        System.out.println("Toaster  退出! ");
    }
}

//生产者
class Butterer implements Runnable {
    private LinkedBlockingQueue<Toast> dryQueue, butterQueue;

    public Butterer(LinkedBlockingQueue<Toast> dry, LinkedBlockingQueue<Toast> buttered) {
        this.dryQueue = dry;
        this.butterQueue = buttered;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast t = dryQueue.take();
                t.butter();
                System.out.println(t);
                butterQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("butter 被打断");
        }
        System.out.println(" butter 退出");

    }
}

//生产者
class Jammer implements Runnable {
    private LinkedBlockingQueue<Toast> butterQueue, finishedQueue;

    public Jammer(LinkedBlockingQueue<Toast> butter, LinkedBlockingQueue<Toast> finished) {
        this.butterQueue = butter;
        this.finishedQueue = finished;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast t = butterQueue.take();
                t.jam();
                System.out.println(t);
                finishedQueue.put(t);
            }
        } catch (InterruptedException ex) {
            System.out.println("jammer 被打断 !");
        }
        System.out.println("jammer 退出");
    }
}

//消费者
class Eater implements Runnable {
    private LinkedBlockingQueue<Toast> finishedQueue;
    private int count = 0;

    public Eater(LinkedBlockingQueue<Toast> f) {
        this.finishedQueue = f;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast t = finishedQueue.take();  //阻塞，直到队列中有数据
                if (t.getId() != count++ || t.getStatus() != Toast.Status.JAMMED) {
                    System.out.println("error " + t);
                    System.exit(1);
                } else {
                    System.out.println("chomp !");
                }

            }
        } catch (InterruptedException e) {
            System.out.println("eater 被打断 ！");
        }
        System.out.println("eater 退出");
    }
}

public class ToastOMatic {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Toast> dryQueue = new LinkedBlockingQueue<Toast>();
        LinkedBlockingQueue<Toast> butteredQueue = new LinkedBlockingQueue<Toast>();
        LinkedBlockingQueue<Toast> finishedQueue = new LinkedBlockingQueue<Toast>();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Butterer(dryQueue, butteredQueue));
        exec.execute(new Jammer(butteredQueue, finishedQueue));
        exec.execute(new Eater(finishedQueue));

        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
