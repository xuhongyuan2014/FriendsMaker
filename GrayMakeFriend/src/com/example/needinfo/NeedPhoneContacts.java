package com.example.needinfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NeedPhoneContacts {

	Context mContext;
	
	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER };
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;	/** 联系人显示名称 **/
	private static final int PHONES_NUMBER_INDEX = 1;	/** 电话号码 **/




	private List<ContractsEntity> listcon = new ArrayList<ContractsEntity>();
	
	public NeedPhoneContacts(Context context) {
		mContext = context;
	}

	
	//获得电话上与sim卡上的所有联系方式
	public List<ContractsEntity> getPhoneAndSIMContracts()
	{
		//获取电话上的联系方式
		this.getPhoneContacts();
		//获取sim卡上的联系方式
		this.getSIMContacts();
		
		
		return listcon; 
	}
	
	
	
	/** 得到手机通讯录联系人信息 **/
	private  void getPhoneContacts() {
		ContentResolver resolver = mContext.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

				

				ContractsEntity ce = new ContractsEntity();
				ce.setName(contactName);
				ce.setPhonenum(phoneNumber);
				listcon.add(ce);
				
			}

			phoneCursor.close();
		}
	}

	/** 得到手机SIM卡联系人人信息 **/
	private void getSIMContacts() {
		ContentResolver resolver = mContext.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

				// Sim卡中没有联系人头像
				ContractsEntity ce = new ContractsEntity();
				ce.setName(contactName);
				ce.setPhonenum(phoneNumber);
				listcon.add(ce);
			}

			phoneCursor.close();
		}
	}

	

}
