package lession_27;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Pool<T> {
    private int size;
    private List<T>  items = new ArrayList<T>();
    private volatile  boolean[] checkedOut;
    private Semaphore available;

    public Pool(Class<T> classObject,int size){
        this.size=size;
        checkedOut = new boolean[size];
        available = new Semaphore(size,true);
        for (int i=0;i<size;i++){
            try {
                items.add(classObject.newInstance());
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public T checkOut() throws  InterruptedException{
        available.acquire(); //获取semaphore的许可证，阻塞直到可用或被打断
        return getItem();
    }

    private synchronized T getItem(){
        for (int i=0;i<size;++i){
            if(!checkedOut[i]){
                //没有签出，进行签出
                checkedOut[i]= true;
                return items.get(i);
            }
        }
        return null;
    }

    public void checkIn(T x){
        if(relaseItem(x)){
            available.release(); //签出一个许可，释放一个可用的许可证
        }
    }
    private synchronized boolean relaseItem(T item){
        int index = items.indexOf(item);  //查找List是否有item元素
        if(index == -1){
            return false;
        }
        if(checkedOut [index]){
            checkedOut[index] = false; //设置未使用
            return true;
        }
        return false;
    }
}
