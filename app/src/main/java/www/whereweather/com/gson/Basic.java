package www.whereweather.com.gson;
/**
 * @date on 11:16 2018/8/3
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe basic实体类
 */

import com.google.gson.annotations.SerializedName;

/**
 * basic中具体内容是
 * "basic":{
 *     "city":"苏州",   城市名
 *     "id":"CN101190401",  天气id
 *     "update":{
 *         "loc":"2016-08-08 21:58"  天气更新时间
 *     }
 * }
 * @SerializedName 注解的方式让JSON字段和Java字段之间建立映射关系
 */
public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
