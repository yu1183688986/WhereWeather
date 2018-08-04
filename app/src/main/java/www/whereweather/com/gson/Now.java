package www.whereweather.com.gson;
/**
 * @date on 11:31 2018/8/3
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe now的实体类          当前的天气信息
 */

import com.google.gson.annotations.SerializedName;

/**
 * now 中的具体内容
 * "now":{
 *     "tep":"29",
 *     "cond":{
 *         "tex":"阵雨"
 *     }
 * }
 */

public class Now {

    @SerializedName("tem")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
