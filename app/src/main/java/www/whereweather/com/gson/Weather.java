package www.whereweather.com.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @date on 11:52 2018/8/3
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe 天气总的实体类来引用其他的实体类
 */
public class Weather {
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
