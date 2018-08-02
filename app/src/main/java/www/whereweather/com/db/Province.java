package www.whereweather.com.db;
/**
 * @date on 8:58 2018/8/2
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe Province 实体类
 */
import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    private int id;

    private String provinceName;

    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
