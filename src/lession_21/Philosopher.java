package lession_21;

public class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;
    private final int id;

    private void pause() throws  InterruptedException{
        //暂定的时间用于思考
       // TimeUnit.MILLISECONDS.sleep(1);
    }

    public Philosopher(Chopstick left,Chopstick right,int ident){
        this.left =left;
        this.right =right;
        this.id=ident;
    }
    public void run(){
        try{
            while (!Thread.interrupted()){
                System.out.println(this+"思考");
                pause();
                System.out.println(this+"准备拿右边人的筷子");
                right.take();
                System.out.println(this+"已经拿到右边人的筷子");
                System.out.println(this+"准备拿左边的筷子");
                left.take();
                System.out.println(this+"已经拿到左边人的筷子");
                System.out.println(this+"吃吃吃");
                pause();
                right.drop();
                left.drop();
            }
        }catch (InterruptedException e){
            System.out.println(this +"被打断");
        }

    }
    public String toString(){
        return "哲学家："+id;
    }
}
