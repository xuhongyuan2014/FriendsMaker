package com.example.mfriends;
//Download by http://www.codefans.net
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mfriends.R;
import com.example.model.MessageEntity;
import com.example.model.MySiXin;
import com.example.utils.DownImage;
import com.example.utils.ImageLoader;
import com.example.utils.DownImage.ImageCallBack;

/**
 * 私信适配器
 * @author Administrator
 *
 */
public class MySiXinAdapter extends BaseAdapter {
	private Context context;
	//private ArrayList<MySiXin> list = new ArrayList<MySiXin>();
	private  List<MessageEntity> list;//私信列表
	public MySiXinAdapter(Context context,List<MessageEntity> list){
		this.context = context;
		this.list=list;
	}

	
	public void setList(List<MessageEntity> list2)
	{
		//this.list = list2;
		this.list.clear();
		this.list.addAll(list2);
	}
	
	public void clearData()
	{
		this.list.clear();
	}
	
	public void addDataList(List<MessageEntity> li)
	{
		if(li.size()>0)
		this.list.addAll(li);
	}
	public void clearList(){
		
		this.list.clear();
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

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ImageLoader imageLoader = new ImageLoader(context);
		MessageEntity hh = list.get(position);
		final H h;
		if(view==null){
			h = new H();
			view = LayoutInflater.from(context).inflate(R.layout.mysixin, parent, false);
			h.pic = (ImageView)view.findViewById(R.id.sxhead11);
			h.name = (TextView)view.findViewById(R.id.sxhead22);
			h.receivetime = (TextView) view.findViewById(R.id.receivetime);
			h.imagebox=(ImageView) view.findViewById(R.id.imagebox);
			view.setTag(h);

		}else{
			h = (H)view.getTag();
		}
		
		String url=hh.getUserImg();
		
		imageLoader.DisplayImage(url,h.pic);
		
				
		if(hh.getMsgStatus())h.imagebox.setImageResource(R.drawable.__mail_read);
		h.name.setText(hh.getUsername());
		if(hh.getSendTime()!=null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			h.receivetime.setText(String.valueOf(sdf.format(hh.getSendTime())));
		}
		else 
		{
			h.receivetime.setText("");
		}
		return view;
	}

	class H{
		ImageView pic;
		ImageView imagebox;
		TextView name;
		TextView receivetime;
	}
}
