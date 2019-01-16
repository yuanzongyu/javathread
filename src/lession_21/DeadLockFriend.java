package lession_21;

public class DeadLockFriend {
    public synchronized void bow(DeadLockFriend friend){
        System.out.println("bow");
        friend.backBow(this);
    }
    public synchronized  void backBow(DeadLockFriend friend){
        System.out.println("backbow");
    }

    public static void main(String[] args) {
        DeadLockFriend d1 = new DeadLockFriend();
        DeadLockFriend d2 = new DeadLockFriend();

        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                d1.bow(d2);
            }
        });

        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                d2.bow(d1);
            }
        });
        t1.start();
        t2.start();
    }
}
