package com.irving.baidudemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MapActivity
        extends AppCompatActivity
{

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private boolean isFirstIn = true;
    public LocationClient     mLocationClient;
    public BDLocationListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_map);

        initView();

        initLocation();



    }

    private void initView() {

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);

        mBaiduMap.setMapStatus(msu);
    }

    boolean isNormal=true;
    public void weixing(View view){

        if (!isNormal){
            //普通地图
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            isNormal=true;

        }else {
            //卫星地图
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            isNormal=false;
        }
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        myListener      = new MyLocationListenner();
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setOpenGps(true);

        mLocationClient.setLocOption(option);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public class MyLocationListenner
            implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            MyLocationConfiguration config =new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,descriptor);
            mBaiduMap.setMyLocationConfigeration(config);
            mBaiduMap.setMyLocationData(locData);

            if (isFirstIn){
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);

                Toast.makeText(MapActivity.this,"位置："+location.getAddrStr(),Toast.LENGTH_SHORT).show();

                isFirstIn = false;
            }


        }

    }
}

