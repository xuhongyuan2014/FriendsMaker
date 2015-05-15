package com.example.utils;
//评论列表专用
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class DownImagelist {

	public String image_path;

	public DownImagelist(String image_path) {

		this.image_path = image_path;

	}

	public void loadImage(final ImageCallBack callBack) {

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				// TODO Auto-generated method stub

				super.handleMessage(msg);

				Bitmap bitmap = (Bitmap) msg.obj;

				callBack.getDrawable(bitmap);

			}

		};

		new Thread(new Runnable() {

			@Override
			public void run() {

				// TODO Auto-generated method stub

				try {

					BitmapFactory.Options opts = new BitmapFactory.Options();
					
					 opts.inSampleSize =2;
					 Bitmap bitmap = BitmapFactory.decodeStream(new URL(
								image_path).openStream(), null, opts);

					 System.out.println("-----------image_path="+image_path);
					Message message = Message.obtain();

					message.obj = bitmap;

					handler.sendMessage(message);
					 // 先判断是否已经回收

					

				} catch (MalformedURLException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				} catch (IOException e) {

					// TODO Auto-generated catch block

					
					e.printStackTrace();

				}

			}

		}).start();

	}

	
	
	
	
	public interface ImageCallBack {

		public void getDrawable(Bitmap bitmap);

	}

}