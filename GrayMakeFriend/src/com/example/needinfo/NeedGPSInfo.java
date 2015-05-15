package com.example.needinfo;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class NeedGPSInfo {

	private Context gContext;
    // 获取位置管理服务
	private LocationListener locationListener;
    private LocationManager locationManager;
	public NeedGPSInfo(Context context,LocationListener localListener)
	{
		gContext = context;
		locationListener= localListener;
	}
	
	//判断是否开通gps功能
	public  boolean openGPSSettings() {
        LocationManager alm = (LocationManager) gContext.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        }
        
        //未开通
        //Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        //startActivityForResult(intent,0); //此为设置完成后返回到获取界面
        return false;
    }

	
	//获取当前位置
	public  Location getGPSLocation()
    {
        
        // 查找到服务信息
        Criteria criteria = new Criteria();//找到gps服务
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

        //String serviceName = gContext.LOCATION_SERVICE;
        
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) gContext.getSystemService(serviceName);
        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        
        //实现监听器   
        locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			//坐标发生改变时候，促发此函数
			@Override
			public void onLocationChanged(Location arg0) {
				// TODO Auto-generated method stub
				
			}
		};
        
        
      
        //z这里可以自动打开gps服务
        //locationManager.setTestProviderEnabled("gps", true);//打开gps
        
       
      
        //注册监听器  必须需要注册监听器    设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 1000, 500, locationListener);
        
        

        
        //关闭监听，关闭gps
        //locationManager.removeUpdates(locationListener);
        //locationManager.setTestProviderEnabled("gps", false);//打开gps
        // Location location = locationManager.getLastKnownLocation(provider)
       
       // Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); // 通过GPS获取位置
        
    
 		 
       return locationManager.getLastKnownLocation(provider);
    }
	
	
	//显示gps定位位置
	private String updateToNewLocation(LocationManager manager,String provider) {

       // TextView tv1;
       // tv1 = (TextView) this.findViewById(R.id.tv1);
		String gpslocal =null;
		Location location = manager.getLastKnownLocation(provider);
        if (location != null) {
            double  latitude = location.getLatitude();
            double longitude= location.getLongitude();
            
             gpslocal = String.valueOf(latitude)+String.valueOf(longitude);
            
           System.out.println("维度：" +  latitude+ "\n经度" + longitude);
        } else {
        	System.out.println("无法获取地理信息11");
        }
        
        return gpslocal;

    }
	
	public String getLocation(){
		if(openGPSSettings()){
			Location location = this.getGPSLocation();
			if(location != null)
				return String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
			else 
				return "无法获取地理信息" ;
		}else{
			return "未开通GPS";
		}
	}
	
}
