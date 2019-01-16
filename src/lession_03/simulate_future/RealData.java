package lession_03.simulate_future;

public class RealData implements Data{
    private String result;

    public RealData(String queryStr){
        System.out.println("进行查询，预计5秒返回数据");
        try {
            Thread.sleep(5000);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("查询结果成功!");
        result ="用户ID=1";
    }

    @Override
    public String getRequest() {
        return result;
    }
}
