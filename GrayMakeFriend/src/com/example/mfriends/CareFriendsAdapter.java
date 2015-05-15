package com.example.mfriends;
//Download by http://www.codefans.net
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mfriends.R;
import com.example.model.CareFriendsP;
import com.example.utils.DownImage;
import com.example.utils.ImageLoader;
import com.example.utils.DownImage.ImageCallBack;

/**
 * ¡™œµ»À  ≈‰∆˜
 * @author Administrator
 *
 */
public class CareFriendsAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<CareFriendsP> list = new ArrayList<CareFriendsP>();
	
	public CareFriendsAdapter(Context context,ArrayList<CareFriendsP> list){
		this.context = context;
		this.list = list;
	}

	
	public void addList(ArrayList<CareFriendsP> cfList)
	{
		this.list.addAll(cfList);
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

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ImageLoader imageLoader = new ImageLoader(context);
		CareFriendsP hh = list.get(position);
		final H h;
		if(view==null){
			h = new H();
			view = LayoutInflater.from(context).inflate(R.layout.mycare, parent, false);
			h.pic = (ImageView)view.findViewById(R.id.cfimage);
			h.name = (TextView)view.findViewById(R.id.cfname);
			
			view.setTag(h);
		}else{
			h = (H)view.getTag();
		}
		
		
		h.pic.setTag(hh.getImage());
		h.pic.setImageResource(R.drawable.icon);
		
		
		imageLoader.DisplayImage(hh.getImage(),h.pic);
	/*	if(hh.getImage()!=null)
		{
			DownImage downImage = new DownImage(hh.getImage());
			downImage.loadImage(new ImageCallBack() {
				public void getDrawable(Drawable drawable) {

					// TODO Auto-generated method stub

					h.pic.setImageDrawable(drawable);
				}
			});
		}else{
			//h.pic.setImageResource(R.drawable.icon);
		}*/
		
		//h.pic.setImageResource(Integer.parseInt(hh.getTxPath()));
		h.name.setText(hh.getUsername());
		
		return view;
	}

	class H{
		ImageView pic;
		TextView name;
	}
}
