package com.example.dialog;


import java.text.MessageFormat;

import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.R;
import com.example.mfriends.SaveDate;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class SelectPicture extends PopupWindow {

	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * 使用相册中的图片
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	
	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";
	/**
	 * 获取相片路径
	 */
	private static final int PHOTO_URL=103;
	/**获取到的图片路径*/
	private String picPath;
	
	private Intent lastIntent ;
	
	private Uri photoUri;
	
	
	private Button  btn_cancel;
	private View mMenuView;
	private LinearLayout my_photo,my_album,my_file;
	private Handler mHandler;

	public SelectPicture(final Activity context,Handler handler) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.selectpic_dialog, null);
		mHandler=handler;
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
/*		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		//取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				SaveDate.saveDate(context, new OAuthV2()); 
				context.finish();
			}
		});*/
		my_photo = (LinearLayout) mMenuView.findViewById(R.id.my_photo);
		my_photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "拍照");
				takePhoto(context);
				dismiss();
			}
		});
		my_album = (LinearLayout) mMenuView.findViewById(R.id.my_album);
		my_album.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "个人相册");
				pickPhoto(context);
/*				Handler handler=(Handler) mMenuView.getTag();
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg); */
				dismiss();
			}
		});
		
/*		my_file = (LinearLayout) mMenuView.findViewById(R.id.my_file);
		my_file.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "本地文件");
				dismiss();
			}
		});*/
		//设置按钮监听
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w/2+50);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.mystyle);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}
	
	/**
	 * 拍照获取图片
	 */
	private void takePhoto(Activity context) {
		//执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if(SDState.equals(Environment.MEDIA_MOUNTED))
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
			 * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();  
			photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
			Message msg=new Message();
			msg.what=PHOTO_URL;
			msg.obj=photoUri;
			mHandler.sendMessage(msg);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/**-----------------*/
			context.startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		}else{
			Toast.makeText(context,"内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * 从相册中取图片
	 */
	private void pickPhoto(Activity context) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		context.startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}
	
}
