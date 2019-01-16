package lession_08.serial;

public class SerialNumberGenerator {
    private static volatile int serialNumber=0;
    public static int nextSerialNumber(){
        return serialNumber++; //volatile能保证可见性和编译器不优化，但是不能保证原子性
    }
}
