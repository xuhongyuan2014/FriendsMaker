package com.example.mfriends;
//Download by http://www.codefans.net
import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.UsersHuiHua;
import com.example.utils.DownImage;
import com.example.utils.DownImage;
import com.example.utils.DownImage.ImageCallBack;
import com.example.utils.ImageLoader;

public class UsersHuihuaAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<UsersHuiHua> list = new ArrayList<UsersHuiHua>();
	
	public UsersHuihuaAdapter(Context context,ArrayList<UsersHuiHua> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ImageLoader imageLoader = new ImageLoader(context);
		UsersHuiHua hh = list.get(position);
		final H h ;
	
		if(view==null){
			h = new H();
			view = LayoutInflater.from(context).inflate(R.layout.allusers, parent, false);
			h.userimage = (ImageView)view.findViewById(R.id.headimage);
			h.username = (TextView)view.findViewById(R.id.username);
			h.lastlogintime = (TextView)view.findViewById(R.id.lastlogintime);
			h.personinfo = (TextView)view.findViewById(R.id.personinfo);  
			h.basemsg = (TextView)view.findViewById(R.id.basemsg); 
			h.carebut = (Button) view.findViewById(R.id.butsayhello);
			view.setTag(h);
		}else{
			h = (H)view.getTag();
		}
		String url = hh.getImage();

		imageLoader.DisplayImage(url,h.userimage);
		//if(hh.getImage()!=null&&!hh.getImage().equals("")){	Toast.makeText(context,"图片地址"+url,Toast.LENGTH_SHORT).show();}
		
		/*if(hh.getImage()!=null&&!hh.getImage().equals(""))
		{
			DownImage downImage = new DownImage(hh.getImage());
			downImage.loadImage(new ImageCallBack() {
			

			
			
				@Override
				public void getDrawable(Drawable drawable) {
					// TODO Auto-generated method stub
					if(drawable!=null&&url==String.valueOf(h.userimage.getTag()))
						h.userimage.setImageDrawable(drawable);
				}
			});
		}
		*/
		
		h.username.setText(hh.getUsername());
		
		if(hh.getLastlogintime()!=null)
		h.lastlogintime.setText(checkNull(String.valueOf(hh.getLastlogintime())));
		else h.lastlogintime.setText(context.getString(R.string.longtimeago));
		
		h.personinfo.setText(checkNull(hh.getselfintro()));
		h.basemsg.setText(getGender(hh.getGender())+"|"
				+checkNull(hh.getAge())+"|"
				+checkNull(hh.getEducation())+"|"
				+checkNull(hh.getSalary()));
		
		h.carebut.setTag(position);
		h.carebut.setOnClickListener(new View.OnClickListener() {  
            //获取当前的userid和username就可以
          @Override  
          public void onClick(View v) {  
        	  
        	  //跳转到发私信的界面
        	  Intent intosx = new Intent(context,ReplyMessActivity.class);
				//intentsx.putExtra("id", userId);
        	  	intosx.putExtra("userid", String.valueOf(list.get(position).getId()));
        	  	intosx.putExtra("username", String.valueOf(list.get(position).getUsername()));
				
				context.startActivity(intosx);
        	  
        	 /* new AlertDialog.Builder(context).setTitle("详情").setMessage("111="+list.get(position).getId())
              .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
                  @Override  
                  public void onClick(DialogInterface dialog, int which) {  
                	  
                	  
                  }  
              }).show();      */             
          }  
       });  
		
		return view;
	}

	
	public String checkNull(Integer te)
	{
		if(te!=null)
			return String.valueOf(te);
		else
			return "";
	}
	
	public String checkNull(String str)
	{
		if(str!=null&&str!="null")
			return str;		
		else
			return "";
	}
	public String getGender(String str){
		String backstr="";
		if(str!=null&&str.equals("true"))
		{backstr=context.getString(R.string.man);}
	else 
		{
		if(str!=null&&str.equals("false"))
		{backstr=context.getString(R.string.female);}
		else 
			backstr="";
		}
		return backstr;
		
	}
	
	public void addDataList(ArrayList<UsersHuiHua> ulist){
		Log.i("419", "add 之前："+list.size());
		if(ulist != null) {
	
			this.list.addAll(ulist);
			
		}
	}
	
	public void clearList(){
		
		this.list.clear();
	}
	class H{
		ImageView userimage; //用户头像
		TextView username;  //userusername
		TextView lastlogintime; //last login lastlogintime
		TextView personinfo;   //用户个人简介
		TextView basemsg;  //用户基本信息
		Button carebut;
	}
}
