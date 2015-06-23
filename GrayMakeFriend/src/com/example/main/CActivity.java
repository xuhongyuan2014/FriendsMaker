package com.example.main;

import java.util.ArrayList;
import java.util.List;

import com.example.mfriends.CareFriendsAdapter;
import com.example.mfriends.ListUserDetailsActivity;
import com.example.mfriends.MainActivity;
import com.example.mfriends.R;
import com.example.model.CareFriendsP;
import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class CActivity extends Activity {

	private ListView listview1;
	/*
	 * �ҹ�ע�ĺ���
	 */
	private int pageNumCF = 1;
	private int pageSizeCF = 10;
	private boolean isLastPageCF = true;
	private CareFriendsAdapter hcAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * TextView tv = new TextView(this); tv.setText("This is C Activity!");
		 * tv.setGravity(Gravity.CENTER);
		 */
		setContentView(R.layout.menu_list);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.refresh");
		intentFilter.addAction("action.refreshFriends");
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);

		listview1 = (ListView) findViewById(R.id.listView1);

		ArrayList<CareFriendsP> cfli = new ArrayList<CareFriendsP>();
		hcAdapter = new CareFriendsAdapter(this, cfli);// ��ʼ���������������ʼ��������ݸ����������������������䣬��ʾ���

		listview1.setAdapter(hcAdapter);
		listview1.setCacheColorHint(0);

		// reloadThreadCareFriends();
		startMyCarePart();

		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				// ��ת������һ�����棨�����û�����ϸ��Ϣ���棩
				CareFriendsP cfp = (CareFriendsP) listview1
						.getItemAtPosition(position);

				Intent intent = new Intent(CActivity.this,
						ListUserDetailsActivity.class);

				intent.putExtra("id", String.valueOf(Constant.getUserId()));
				intent.putExtra("friendid", String.valueOf(cfp.getId()));
				intent.putExtra("username", String.valueOf(cfp.getUsername()));
				startActivity(intent);

			}
		});

		listview1.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItemid = listview1.getLastVisiblePosition();
				// pageNowValue=pageNowValue+1;
				// ���жϵ����һ����ݵ�ʱ�򣬲��Ҳ������һҳ�������������
				if ((lastItemid + 1) == totalItemCount && !isLastPageCF) {
					startMyCarePart();
				}

			}
		});
	}

	private void startMyCarePart() {

		// ������һ����Ҫ��ȡ���������

		final RequestParams para = new RequestParams();
		para.add("id", Constant.getUserId());
		para.add("pageNow", (pageNumCF++) + "");
		para.add("pageSize", pageSizeCF + "");
		// ���ʷ�����
		HttpEngine.getHttpEngine().post(Constant.getUserCareList, para,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String result) {
						if (result != null) {

							List<CareFriendsP> cfList = JSONUtils
									.jsonToEntityArray(result,
											CareFriendsP.class);

							if (cfList != null) {
								if (cfList.size() < pageSizeCF)
									isLastPageCF = true;
								else
									isLastPageCF = false;

								if (cfList.size() > 0) {
									/*
									 * cfDataList=getCFList(cfList); Message msg
									 * = new Message(); msg.what = 301;
									 * handler.sendMessage(msg);
									 */

									hcAdapter.addList(getCFList(cfList));
									if (hcAdapter != null) {
										hcAdapter.notifyDataSetChanged();
									}

								}

							}
						} else {
							// Toast.makeText(MainActivity.this,
							// "��ȡ��ݴ���",Toast.LENGTH_SHORT).show();
						}

					}

					@Override
					public void onFailure(Throwable error) {
						if (Constant.Debug) {
							hcAdapter.addList(getCFListLocal());
							if (hcAdapter != null) {
								hcAdapter.notifyDataSetChanged();
							}
						}
					}
				});

	}

	public ArrayList<CareFriendsP> cfDataList = new ArrayList();

	@SuppressWarnings("unused")
	private ArrayList<CareFriendsP> getCFList(List<CareFriendsP> cfList) {
		ArrayList<CareFriendsP> hcList = new ArrayList<CareFriendsP>();

		CareFriendsP ctp = null;
		for (int i = 0; i < cfList.size(); i++) {
			ctp = new CareFriendsP();
			ctp.setId(cfList.get(i).getId());
			if (cfList.get(i).getImage() != null) {
				ctp.setImage(cfList.get(i).getImage());
			} else {
				ctp.setImage(R.drawable.headshow2 + "");
			}
			ctp.setUsername(cfList.get(i).getUsername());
			hcList.add(ctp);

		}

		return hcList;
	}

	private ArrayList<CareFriendsP> getCFListLocal() {
		ArrayList<CareFriendsP> hcList = new ArrayList<CareFriendsP>();

		CareFriendsP ctp = null;
		for (int i = 0; i < 10; i++) {
			ctp = new CareFriendsP();
			ctp.setId("007");
			ctp.setImage(R.drawable.headshow2 + "");
			ctp.setUsername("Mrxu");
			hcList.add(ctp);

		}

		return hcList;
	}

	// broadcast receiver
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("action.refreshFriends")
					|| action.equals("action.refresh")) {
				reFreshList();
			}
		}
	};

	public void reFreshList() {
		pageNumCF = 1;
		pageSizeCF = 10;
		isLastPageCF = true;
		hcAdapter.clearList();
		startMyCarePart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
