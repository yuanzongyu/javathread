package lession_33;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ConcurrentModifyExcepitonTest {
    public static void main(String[] args)   {
        List<Integer>  list =new  ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
//
//        Iterator iter=  list.iterator();
//        while (iter.hasNext()){
//            Integer i = (Integer) iter.next();
//            iter.remove();
//
//        }

        for (int i=0;i<list.size();i++){
            list.remove(i);
        }

//
//        for(Integer i : list){
//            list.remove(i);
//        }
        System.out.println(list);
    }
}
