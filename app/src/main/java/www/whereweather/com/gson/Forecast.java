package www.whereweather.com.gson;
/**
 * @date on 11:43 2018/8/3
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe daily_forecast实体类----单个天气的实体类      未来的天气信息
 */

import com.google.gson.annotations.SerializedName;

/**
 * daily_forecast中的具体内容如下
 * "daily_forecast":[
 * {
 *     "date":"2016-08-08",
 *     "cond":{
 *         "txt_d":"阵雨"
 *     },
 *     "tep":{
 *         "max":"34",
 *         "min":"27"
 *     }
 * },
 * {
 *     "date":"2016-08-09",
 *     "cond":{
 *         "txt_d":"多云"
 *     },
 *     "tep":{
 *         "max":"35",
 *         "min":"27"
 *     }
 * },
 * ...
 * ]
 *
 */
public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
