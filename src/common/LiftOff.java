package common;

public class LiftOff implements Runnable {
	protected int countDown = 10; // Default
	private static int taskCount = 0;
	private final int id = taskCount++; // 声明final ，表示不可变 ，id=0

	public LiftOff() {
	}

	public LiftOff(int countDown) {
		this.countDown = countDown;
	}

	public String status() {
		return "#" + id + "(" + (countDown > 0 ? countDown : "Liftoff!") + "), ";
	}

	public void run() {
		while (countDown-- > 0) {
			System.out.println(status());
		    Thread.yield(); // 放弃自己的CPU 时间片，给其他的任务
		}
		System.out.println("\n");

	}

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new LiftOff()); //线程构造方法需要 Runnable对象
			t.start(); // 起一个新的分支
		}
		System.out.println("Lift OFF Lift OFF Lift OFF"); // 继续执行，liftOff 发射
	}

}
