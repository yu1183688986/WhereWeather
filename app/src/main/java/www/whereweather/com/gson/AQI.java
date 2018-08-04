package www.whereweather.com.gson;
/**
 * @date on 11:27 2018/8/3
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe AQI的实体类        当前的空气质量状况
 */

/**
 * aqi中的具体内容如下所示
 * "aqi":{
 *     "city":{
 *         "aqi":"44",
 *         "pm25":"13"
 *     }
 * }
 */
public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
