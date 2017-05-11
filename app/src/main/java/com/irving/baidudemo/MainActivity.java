package com.irving.baidudemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class MainActivity
        extends AppCompatActivity
{
    public LocationClient     mLocationClient = null;
    public BDLocationListener myListener      = new MyLocationListenner();
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.content);

        mLocationClient = new LocationClient(getApplicationContext()); //声明 LocationClient 类
        mLocationClient.registerLocationListener(myListener); //注册监听函数

        initLocation();

    }

    public void openmap(View view) {
        Intent intent = new Intent(this, MapActivity.class);

        startActivity(intent);
    }

    //发起定位请求。请求过程是异步的，定位结果在上面的监听函数 onReceiveLocation 中获取。
    public void location(View view) {
        mLocationClient.start();
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        } else { Log.d("LocSDK3", "locClient is null or not started ");}
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值 gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为 5000ms
        option.disableCache(true);//禁止启用缓存定位
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListenner
            implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (location == null) { return; }
            final  StringBuffer sb = new StringBuffer(256);
            sb.append(" time : ");
            sb.append(location.getTime());
            sb.append("\n error code : ");
            sb.append(location.getLocType());
            sb.append("\n latitude : ");
            sb.append(location.getLatitude());
            sb.append("\n lontitude : ");
            sb.append(location.getLongitude());
            sb.append("\n radius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\n speed : ");
                sb.append(location.getSpeed());
                sb.append("\n satellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\n addr : ");
                sb.append(location.getAddrStr());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(sb.toString());
                }
            });
            mLocationClient.stop();


            Log.d("MainActivity", sb.toString());
        }


    }
}
