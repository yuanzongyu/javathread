package lession_03.simulate_future;

public class FutureClient {
    private FutureData futureData = new FutureData();
    public Data getRequest(String queryStr){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //new RealData去处理耗时的逻辑
                RealData realData = new RealData(queryStr); //耗时5秒
                futureData.setRealData(realData);
            }
        }).start();
        return futureData;
    }
}
