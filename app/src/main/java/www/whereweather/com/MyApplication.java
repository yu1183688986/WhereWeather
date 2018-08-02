package www.whereweather.com;
/**
 * @date on 9:20 2018/8/2
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe 写自己的MyApplication类 方便全局获取Context
 */
import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);//使得LitePal能够在内部自动获取Context，可以让他正常工作
    }

    public static Context getContext(){
        return context;
    }
}
