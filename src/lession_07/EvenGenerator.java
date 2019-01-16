package lession_07;

public class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public int next() {
        ++currentEvenValue;
        Thread.yield();
        ++currentEvenValue;  // ++ 操作是 非原子的
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenCheck.test(new EvenGenerator(), 10);
    }
}
