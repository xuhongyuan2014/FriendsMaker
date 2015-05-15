package com.example.dialog;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.R;
import com.example.widget.ArrayWheelAdapter;
import com.example.widget.NumericWheelAdapter;
import com.example.widget.OnWheelChangedListener;
import com.example.widget.WheelView;

public class SelectArray extends PopupWindow implements OnClickListener {

	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private Button btn_submit, btn_cancel;
	private TextView titleview;
	private TextView  text;
	private int mCurHeight = 80;
	
	private String arrayValue;
	private MyArrayAdapter arrayAdapter;
	private WheelView arrayWheel;


	public SelectArray(Activity context,final View Fieldview,String title,int arrayType) {
		super(context);
		mContext = context;
		//this.textValue = ((TextView)Fieldview).getText().toString();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.array_dialog, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		arrayWheel = (WheelView) mMenuView.findViewById(R.id.arrayWheel);
		
		 titleview = (TextView) mMenuView.findViewById(R.id.dialog_text_title);	
		btn_submit = (Button) mMenuView.findViewById(R.id.submit);
		btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);
		
		titleview.setText(title);
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((TextView)Fieldview).setText(arrayAdapter.getItemText(arrayWheel.getCurrentItem()));
				SelectArray.this.dismiss();
			}
		});
		btn_cancel.setOnClickListener(this);


		
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//updateValue(arrayWheel);
				 //((TextView)view).setText("111");
			}
		};
		String[] items=context.getResources().getStringArray(arrayType);
		arrayAdapter = new MyArrayAdapter(context,items);
		/*arrayAdapter.setTextType(dateType);*/
		arrayWheel.setViewAdapter(arrayAdapter);
		arrayWheel.addChangingListener(listener);
		



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

	private void updateValue(WheelView height) {

		//arrayValue=arrayWheel.getContentDescription();
	}
	
	
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}


	/**
	 * Adapter for  wheels. Highlights the current value.
	 */
	private class MyArrayAdapter extends ArrayWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		String values[];
		/**
		 * Constructor
		 */
		public MyArrayAdapter(Context context,  String items[]) {
			super(context, items);
			//this.values=values;
			
			//this.currentValue = current;
			//setTextSize(24);
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return super.getItemText(index);
		}

	}


	public void onClick(View v) {
		this.dismiss();
	}


}
