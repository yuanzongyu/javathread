package lession_32;

public abstract class Tester<C> {
    static int testReps = 10;
    static int testCycles = 1000;
    static int containerSize =1000;
    abstract  C containerInitializer();
}
