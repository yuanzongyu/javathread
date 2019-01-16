package lession_03.simulate_future;

public class Main {
    public static void main(String[] args) {
        FutureClient futureClient = new FutureClient();
        System.out.println("main()请求FutureClient,传递参数name=zhangsan");
        Data data = futureClient.getRequest("name=zhangsan");
        System.out.println("main(）方法继续做其他的事情 ......... working....working....");
        System.out.println(data.getRequest());
    }
}
