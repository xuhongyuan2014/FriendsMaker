package com.example.dialog;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.example.mfriends.ConfigActivity;
import com.example.mfriends.LoginActivity;
import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.R;
import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.example.utils.ProgressDialogUtils;
import com.example.widget.NumericWheelAdapter;
import com.example.widget.OnWheelChangedListener;
import com.example.widget.WheelView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SelectChangePassword extends PopupWindow implements OnClickListener {

	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private Button btn_submit, btn_cancel;
	private TextView titleview;
	private TextView  TV_originpass,TV_newpass,TV_newpassconfirm;
	private int mCurHeight = 80;


	public SelectChangePassword(Activity context,final View Fieldview,String title) {
		super(context);
		mContext = context;
		//this.textValue = ((TextView)Fieldview).getText().toString();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.changepassword_dialog, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));


		TV_originpass = (TextView) mMenuView.findViewById(R.id.TV_originpass);	
		TV_newpass = (TextView) mMenuView.findViewById(R.id.TV_newpass);	
		TV_newpassconfirm = (TextView) mMenuView.findViewById(R.id.TV_newpassconfirm);	
		 titleview = (TextView) mMenuView.findViewById(R.id.dialog_text_title);	
		btn_submit = (Button) mMenuView.findViewById(R.id.submit);
		btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);
		//text.setText( ((TextView)Fieldview).getText());
		titleview.setText(title);
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//((TextView)Fieldview).setText(text.getText());	
				if(validForm()){
					changePassword(String.valueOf(TV_originpass.getText()), String.valueOf(TV_newpass.getText()));
			     	SelectChangePassword.this.dismiss();
	                Intent intent = new Intent(mContext, LoginActivity.class);  
	                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置,设置登陆界面为最前
	                mContext.startActivity(intent); 
				}
			}
		});
		btn_cancel.setOnClickListener(this);





		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();

	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}





	public void onClick(View v) {
		this.dismiss();
	}
	public void changePassword(String password,String newpassword){//修改密码

		RequestParams params= new RequestParams();
		params.add("id",Constant.getUserId()); 
		params.add("password",password);  
		params.add("newpassword",newpassword);  
		HttpEngine.getHttpEngine().get(Constant.changePassword, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// 关闭进度条
				//ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {					
					 String infoText = (String) JSON.parseObject(result).get("infoText");
					Toast.makeText(mContext, infoText,Toast.LENGTH_SHORT).show();
				 } else {

						Toast.makeText(mContext, "服务器数据错误",Toast.LENGTH_SHORT).show();
					}
		    }						
		     @Override
             public void onFailure(Throwable error) {

                     Toast.makeText(mContext, "请求超时,请检查网络!",Toast.LENGTH_SHORT).show();
             }
		});	
	}

	
	
	public boolean validForm(){
		if(TV_originpass.getText()==""){
			Toast.makeText(mContext, "请输入旧密码!",Toast.LENGTH_SHORT).show();
			return false;
		}
		else	if(TV_newpass.getText()==""){
			Toast.makeText(mContext, "请输入新密码!",Toast.LENGTH_SHORT).show();
			return false;
		}
		else	if(TV_newpassconfirm.getText()==""){
			Toast.makeText(mContext, "请确认新密码!",Toast.LENGTH_SHORT).show();
			return false;
		}
		else if(!String.valueOf(TV_newpassconfirm.getText()).equals(String.valueOf(TV_newpass.getText()))){
			Toast.makeText(mContext, "两次密码不一致!",Toast.LENGTH_SHORT).show();
			return false;
		}
		else return true;
	}
}
