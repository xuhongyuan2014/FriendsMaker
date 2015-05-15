package com.example.mfriends;


import com.example.main.AActivity;
import com.example.main.MainTabActivity;
import com.example.mfriends.R;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SelectSearchUserWindow extends PopupWindow {

	private View mMenuView;
	private Button search_btn;
	private TextView search_name;

	public SelectSearchUserWindow(final Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.search, null);
		search_btn=(Button)mMenuView.findViewById(R.id.search_btn);
		search_name=(TextView)mMenuView.findViewById(R.id.search_name);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		//设置按钮监听
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.mystyle);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout_search).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});
		
		search_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
/*				Log.i("SelectPicPopupWindow", "个人资料");
			 	Intent intent=new Intent();
            	intent.setClass(context,SearchActivity.class);
            	
            	//用Bundle携带数据
                Bundle bundle=new Bundle();
                //传递name参数为tinyphp
                bundle.putString("username", search_name.getText().toString());
                intent.putExtras(bundle);
            	
            	context.startActivity(intent);*/
				
				
				// 广播通知  
		         Intent intent = new Intent();  
		         intent.putExtra("username", search_name.getText().toString());
		         intent.setAction("action.search");  
		         context.sendBroadcast(intent); 
				
				
				dismiss();
			}
		});
	}
}
