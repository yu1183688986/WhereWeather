package www.whereweather.com.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @date on 9:46 2018/8/2
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe 用于和服务器进行交互
 */
public class HttpUtil {
    //可以回调
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
