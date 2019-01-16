package lession_03.simulate_future;

public class FutureData implements  Data{

    private RealData realData;
    private static boolean isReady=false;

    public  synchronized  void setRealData(RealData realData){
        if(isReady){
            return;
        }
        this.realData = realData;
        isReady =true;
        notify(); //realData 任务完成，进行通知
    }

    @Override
    public synchronized   String getRequest() {
        while(!isReady){
           try{
               wait(); //等待readData处理完成
           }catch (Exception ex){ex.printStackTrace();}
        }
        return realData.getRequest();
    }
}
