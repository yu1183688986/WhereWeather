package www.whereweather.com;
/**
 * @date on 10:22 2018/8/2
 * @author yuyong
 * @Email yu1183688986@163.com
 * @describe 用于遍历省市县数据的碎片
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import www.whereweather.com.db.City;
import www.whereweather.com.db.County;
import www.whereweather.com.db.Province;
import www.whereweather.com.util.HttpUtil;
import www.whereweather.com.util.Utility;

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressBar progressBar; //ProgressDialog过时，用它替换
    private ProgressDialog progressDialog;


    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    //listview中需要的数据
    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener((parent,view,position,id) -> {
            if (currentLevel == LEVEL_PROVINCE){
                selectedProvince = provinceList.get(position);
                queryCities();
            }else if (currentLevel == LEVEL_CITY){
                selectedCity = cityList.get(position);
                queryCounties();
            }else if (currentLevel == LEVEL_COUNTY){
                String weatherId = countyList.get(position).getWeatherId();
                if (getActivity() instanceof MainActivity){
                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }else if (getActivity() instanceof WeatherActivity){
                    WeatherActivity activity = (WeatherActivity) getActivity();
                    activity.drawerLayout.closeDrawers();
                    activity.swipeRefresh.setRefreshing(true);
                    activity.requestWeather(weatherId);
                }
            }
        });
        backButton.setOnClickListener(v ->{
            if (currentLevel == LEVEL_COUNTY){
                queryCities();
            }else if (currentLevel == LEVEL_CITY){
                queryProvinces();
            }
        });
        queryProvinces();//开始加载省级数据
    }

    /**
     * 查询全国所有的省份，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {//去服务器查询
            String address = "http://guolin.tech/api/china";
            qerryFromServer(address,"province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int proviceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+proviceCode;
            qerryFromServer(address,"city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = LitePal.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int proviceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+proviceCode+"/"+cityCode;
            qerryFromServer(address,"county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private void qerryFromServer(String address,final String type){

        showProgress();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() ->{//通过runOnUiThread方法回到主线程处理逻辑
                    closeProgress();
                    Toast.makeText(MyApplication.getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                //重新加载数据库中的数据
                if (result){
                    getActivity().runOnUiThread(() ->{
                        closeProgress();
                        if ("province".equals(type)){
                            queryProvinces();
                        }else if ("city".equals(type)){
                            queryCities();
                        }else if ("county".equals(type)){
                            queryCounties();
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示进度条
     */
    private void showProgress(){
        if (getActivity() instanceof MainActivity){
            View view = getView();
            if (view != null){
                progressBar = view.findViewById(R.id.progressBar);
                if (view.getRootView() != null){
                    progressBar = view.getRootView().findViewById(R.id.progressBar);
                }
            }
            progressBar.setVisibility(View.VISIBLE);
        }else {
             if (progressDialog == null){
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
            }
            progressDialog.show();
        }
        /*if (view != null && view.getRootView() != null){
            if (progressBar == null){
                progressBar = view.getRootView().findViewById(R.id.progressBar);
                *//*if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                    final Drawable drawable =  MyApplication.getContext().getResources().getDrawable(R.drawable.progress_indeterminate_running);
                    progressBar.setIndeterminateDrawable(drawable);
                }*//*
                progressBar.setVisibility(View.VISIBLE);
            }
        }*/
       // progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭进度条
     */
    private void closeProgress(){
        if (getActivity() instanceof MainActivity){
            if (progressBar != null){
                progressBar.setVisibility(View.GONE);
            }
        }else {
            if (progressDialog != null){
                progressDialog.dismiss();
            }
        }

    }
}
