package lession_07;

public class SynchronizedEvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public synchronized int next() {
        ++currentEvenValue;
        ++currentEvenValue;  // ++ 操作是 非原子的
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenCheck.test(new SynchronizedEvenGenerator(), 10);
    }
}
