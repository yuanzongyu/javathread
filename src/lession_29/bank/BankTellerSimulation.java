package lession_29.bank;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//只读对象不需要synchronized
class Customer{
    private final int serviceTime;  //服务时间  ，final 是现成安全的
    public Customer(int tm){
        serviceTime = tm;
    }
    public  int getServiceTime(){
        return serviceTime;
    }
    public String toString(){
        return "[" + serviceTime + "]";
    }
}

class CustomerLine extends ArrayBlockingQueue<Customer>{
    public  CustomerLine(int maxLineSize){
        super(maxLineSize);  //ArrayBlockingQueue 必须给定数量
    }
    public String toString(){
        if(this.size() == 0 ){
            return "队列为空";
        }
        StringBuilder result = new StringBuilder();  //非线程安全
        for(Customer customer : this){
            result.append(customer);
        }
        return result.toString();
    }
}

class CustomerGenerator implements Runnable{  //随机将customer放到queue
    private CustomerLine customers;
    private static Random rand = new Random(47);
    public CustomerGenerator(CustomerLine cq){
        this.customers = cq;
    }
    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(rand.nextInt(300));
                customers.put(new Customer(rand.nextInt(1000)));
            }
        }catch (InterruptedException e ){
            System.out.println("CustomerGenerator 被打断！");
        }
        System.out.println("CustomerGenerator 终止任务!");
    }
}

class Teller implements  Runnable ,Comparable<Teller>{  // Teller 出纳员
    private static int counter =0;
    private final int id = counter++;

    private int customerServed = 0 ;// 服务期间服务人数
    private CustomerLine customers;
    private boolean servingCustomerLine = true;
    public  Teller(CustomerLine cq){
        customers = cq;
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                Customer customer = customers.take();
                TimeUnit.MILLISECONDS.sleep(customer.getServiceTime());
                synchronized (this){
                    customerServed++;
                    while(!servingCustomerLine){
                        wait();
                    }
                }
            }
        }catch (InterruptedException e){
            System.out.println(this + " 被打断! ");
        }
        System.out.println(this +"终止");
    }

    public synchronized void doSomethingElse(){
        customerServed = 0;
        servingCustomerLine = false; //做其他的事情
    }

    public synchronized void serveCustomerLine(){
        servingCustomerLine =true;
        notifyAll();
    }

    public String toString(){
        return "teller "+id+" ";
    }
    public String shortString(){
        return "T"+id;
    }

    @Override
    public  synchronized  int compareTo(Teller o) {  //使用优先级队列
        return customerServed< o.customerServed ? -1: (customerServed ==o.customerServed ? 0:1);
    }
}

class TellerManager implements  Runnable{
    private ExecutorService exec;
    private CustomerLine customers;
    private PriorityQueue<Teller> workingTellers = new PriorityQueue<>();
    private Queue<Teller> tellersDoingOtherThings = new LinkedList<Teller>();
    private int adjustmentPeriod;
    private static Random rand = new Random(47);
    public TellerManager(ExecutorService e,CustomerLine customers,int adjustmentPeriod){
        exec = e;
        this.customers = customers;
        this.adjustmentPeriod = adjustmentPeriod;
        Teller teller  =  new Teller(customers);
        exec.execute(teller);
        workingTellers.add(teller);
    }
    public void adjustTellerNumber(){
       if(customers.size() / workingTellers.size() > 2){
           if(tellersDoingOtherThings.size() >0 ){
               Teller teller = tellersDoingOtherThings.remove();
               teller.serveCustomerLine();
               workingTellers.offer(teller);
               return;
           }
           //如果没有正在干活的人，就在创建一个
           Teller teller  =  new Teller(customers);
           exec.execute(teller);
           workingTellers.add(teller);
           return;
       }
       //如果排队的人短，则删除一个teller.
        if(workingTellers.size() >1 && customers.size() / workingTellers.size() <2){
            reassignOneTeller();
            if(customers.size() ==0){
                while(workingTellers.size() >1){
                    reassignOneTeller();
                }
            }
        }
    }
    private void reassignOneTeller(){
        Teller teller  =  workingTellers.poll();
        teller.doSomethingElse();
        tellersDoingOtherThings.offer(teller);
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
                adjustTellerNumber(); //优化人数
                System.out.println(customers+"{");
                for (Teller teller: workingTellers){
                    System.out.println(teller.shortString() +" ");
                }
                System.out.println("}");
            }
        }catch (InterruptedException  e ){
            System.out.println(this + " 被打断 !");
        }
        System.out.println(this + "  终止!");
    }
    public String toString() { return "TellerManager "; }
}
public class BankTellerSimulation {
    static final int MAX_LINE_SIZE = 50;
    static final int ADJUSTMENT_PERIOD =1000;

    public static void main(String[] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        CustomerLine customers = new CustomerLine(MAX_LINE_SIZE);
        exec.execute(new CustomerGenerator(customers));
        exec.execute(new TellerManager(exec,customers,ADJUSTMENT_PERIOD));
        System.in.read();
        exec.shutdownNow();
    }
}
