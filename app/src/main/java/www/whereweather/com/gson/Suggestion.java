package www.whereweather.com.gson;
/**
 * @date on 11:35 2018/8/3
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe suggestion 实体类          对天气的建议
 */

import com.google.gson.annotations.SerializedName;

/**
 * suggestion中的具体内容如下
 * "suggestion":{
 *     "comf":{
 *         "txt":"白天......"
 *     },
 *     "cw":{
 *         "txt":"不宜洗车"
 *     },
 *     "sport":{
 *         "txt":"有降水......"
 *     }
 * }
 */
public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
