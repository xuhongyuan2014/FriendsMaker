package com.example.needinfo;

import android.content.Context;
import android.telephony.TelephonyManager;

public class NeedSIMCH {

	
	TelephonyManager telephonemanage;
	
	public NeedSIMCH(Context context) {
		telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }
	
	
	
	public String getSIMCH()
	{
		          

		return  telephonemanage.getDeviceId();   

	
	}
	
}
