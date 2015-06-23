package com.example.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.mfriends.ListUserDetailsActivity;
import com.example.mfriends.MainActivity;
import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.R;
import com.example.mfriends.UsersHuihuaAdapter;
import com.example.model.UsersHuiHua;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.DownImage.ImageCallBack;
import com.example.utils.ProgressDialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class AActivity extends Activity {

	private ListView listview1;
	private UsersHuihuaAdapter haAdapter;
	private int pageNumUsers = 1;
	private int pageSizeUsers = 10; // 默认
	private boolean isLastPageUsers = true;// 判断是否为最后一页
	private String searchname = "";

	/*
	 * private Handler mhandler=new Handler(){//未使用
	 * 
	 * @Override public void handleMessage(Message msg) { switch (msg.what) {
	 * case 301: hcAdapter.addList(cfDataList);
	 * listViewMyCare.setAdapter(hcAdapter); break; default: break; }
	 * super.handleMessage(msg); }
	 * 
	 * };
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * TextView tv = new TextView(this); tv.setText("This is A Activity!");
		 * tv.setGravity(Gravity.CENTER);
		 */
		setContentView(R.layout.menu_list);

		// 新页面接收数据
		Bundle bundle = this.getIntent().getExtras();
		// 接收name值
		if (bundle != null && bundle.getString("username") != null)
			searchname = bundle.getString("username");

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.refresh");
		intentFilter.addAction("action.refreshUsers");
		intentFilter.addAction("action.search");
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);

		listview1 = (ListView) findViewById(R.id.listView1);
		ArrayList<UsersHuiHua> st = new ArrayList<UsersHuiHua>();
		haAdapter = new UsersHuihuaAdapter(this, st);// 这里获取用户所有信息列表的数据（薯片，用户名等）
		listview1.setAdapter(haAdapter);// 填充数据
		listview1.setCacheColorHint(0);
		// 初始化先第一次加载一条数据
		// ReloadrunThreadUsers();
		if (isSearch()) {
			searchUserList();
		} else {
			startUserListPart();
		}

		// listViewUsers.set
		// 添加滚动加载数据
		listview1.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItemid = listview1.getLastVisiblePosition();
				if ((lastItemid + 1) == totalItemCount && !isLastPageUsers) {

					// ReloadrunThreadUsers();
					if (isSearch()) {
						searchUserList();
					} else {
						startUserListPart();
					}

				}

			}
		});
		// listview 每个item点击监听事件
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) { // (AdapterView<?> parent, View
												// view,int position, long id
				// 第一个是适配器，第二个是view ，第三个是position 第四个是id
				// TODO Auto-generated method stub

				UsersHuiHua uhh = (UsersHuiHua) listview1
						.getItemAtPosition(position);

				Intent intent = new Intent(AActivity.this,
						ListUserDetailsActivity.class);

				intent.putExtra("id", String.valueOf(Constant.getUserId()));
				intent.putExtra("friendid", String.valueOf(uhh.getId()));
				intent.putExtra("username", String.valueOf(uhh.getUsername()));
				startActivity(intent);
			}
		});
	}

	public void searchUserList() {

		RequestParams para = new RequestParams();
		para.add("pageSize", pageSizeUsers + "");
		para.add("pageNow", (pageNumUsers++) + "");// 每次加载，pageNum+1=pageNow
		para.add("username", searchname);
		// 访问服务器
		ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		HttpEngine.getHttpEngine().post(Constant.selectUsersList, para,
				new AsyncHttpResponseHandler() {

					String[] localar = { "id", "username", "gender",
							"education", "salary", "age", "image",
							"lastlogintime", "selfintro" };
					String[] serar = { "id", "username", "gender", "education",
							"salary", "age", "image", "lastlogintime",
							"selfintro" };

					@SuppressWarnings("deprecation")
					@Override
					public void onSuccess(String result) {
						// 关闭进度条
						ProgressDialogUtils.dismissProgressDialog();
						if (result != null) {
							// 成功之后则开始加载用户界面的组件
							List<Map<String, Object>> relist = JSONUtils
									.getDataList(localar, serar, result);

							if (relist != null) {
								if (relist.size() < pageSizeUsers)
									isLastPageUsers = true;
								else
									isLastPageUsers = false;

								if (relist.size() > 0) {

									/*
									 * userDataList=getHuahui(relist); Message
									 * msg = new Message(); msg.what = 302;
									 * handler.sendMessage(msg);
									 */

									haAdapter.addDataList(getHuahui(relist));
									if (haAdapter != null) {
										haAdapter.notifyDataSetChanged();
									}

								}

							}
							// 成功之后，回调信息
							// 如果正常启动子线程，之后，通知线程已经结束
							/*
							 * Message msg =
							 * handler.obtainMessage(HandlerConstants
							 * .GET_USER_LIST_OK,"");//两个参数，what and obj
							 * 
							 * handler.sendMessage(msg);
							 */

						} else {
							// Toast.makeText(MainActivity.this,
							// "获取数据错误",Toast.LENGTH_SHORT).show();
							/*
							 * Message msg =
							 * handler.obtainMessage(HandlerConstants
							 * .GET_USER_LIST_ERROR,"加载数据失败");//两个参数，what and
							 * obj
							 * 
							 * handler.sendMessage(msg);
							 */
						}

						// 每次执行之后强行关闭线程
						// Thread.currentThread().stop();
					}

					@Override
					public void onFailure(Throwable error) {
						if (Constant.Debug) {

							haAdapter.addDataList(getHuahuiLocal());
							if (haAdapter != null) {
								haAdapter.notifyDataSetChanged();
							}

						}

						// 关闭进度条
						ProgressDialogUtils.dismissProgressDialog();
						error.printStackTrace();
					}

				});
	}

	public boolean isSearch() {
		if (searchname.equals(""))
			return false;
		else
			return true;

	}

	public void startUserListPart() {
		RequestParams para = new RequestParams();

		para.add("pageSize", pageSizeUsers + "");
		para.add("pageNow", (pageNumUsers++) + "");// 每次加载，pageNum+1=pageNow
		// 访问服务器
		ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		HttpEngine.getHttpEngine().post(Constant.getUsersList, para,
				new AsyncHttpResponseHandler() {

					String[] localar = { "id", "username", "gender",
							"education", "salary", "age", "image",
							"lastlogintime", "selfintro" };
					String[] serar = { "id", "username", "gender", "education",
							"salary", "age", "image", "lastlogintime",
							"selfintro" };

					@SuppressWarnings("deprecation")
					@Override
					public void onSuccess(String result) {
						// 关闭进度条
						ProgressDialogUtils.dismissProgressDialog();
						if (result != null) {
							// 成功之后则开始加载用户界面的组件
							List<Map<String, Object>> relist = JSONUtils
									.getDataList(localar, serar, result);

							if (relist != null) {
								if (relist.size() < pageSizeUsers)
									isLastPageUsers = true;
								else
									isLastPageUsers = false;

								if (relist.size() > 0) {

									/*
									 * userDataList=getHuahui(relist); Message
									 * msg = new Message(); msg.what = 302;
									 * handler.sendMessage(msg);
									 */

									haAdapter.addDataList(getHuahui(relist));
									if (haAdapter != null) {
										haAdapter.notifyDataSetChanged();
									}

								}

							}
							// 成功之后，回调信息
							// 如果正常启动子线程，之后，通知线程已经结束
							/*
							 * Message msg =
							 * handler.obtainMessage(HandlerConstants
							 * .GET_USER_LIST_OK,"");//两个参数，what and obj
							 * 
							 * handler.sendMessage(msg);
							 */

						} else {
							// Toast.makeText(MainActivity.this,
							// "获取数据错误",Toast.LENGTH_SHORT).show();
							/*
							 * Message msg =
							 * handler.obtainMessage(HandlerConstants
							 * .GET_USER_LIST_ERROR,"加载数据失败");//两个参数，what and
							 * obj
							 * 
							 * handler.sendMessage(msg);
							 */
						}

						// 每次执行之后强行关闭线程
						// Thread.currentThread().stop();
					}

					@Override
					public void onFailure(Throwable error) {
						if (Constant.Debug) {
							haAdapter.addDataList(getHuahuiLocal());
							if (haAdapter != null) {
								haAdapter.notifyDataSetChanged();
							}

						}

						// 关闭进度条
						ProgressDialogUtils.dismissProgressDialog();
						error.printStackTrace();
					}

				});
	}

	public ArrayList<UsersHuiHua> userDataList = new ArrayList();

	private ArrayList<UsersHuiHua> getHuahui(List<Map<String, Object>> huilist) {
		// 显示加载数据
		ArrayList<UsersHuiHua> hhList = new ArrayList<UsersHuiHua>();

		UsersHuiHua huihua;
		for (int i = 0; i < huilist.size(); i++) {
			huihua = new UsersHuiHua();
			huihua.setId(String.valueOf(huilist.get(i).get("id")));
			// huihua.setTxPath(String.valueOf(huilist.get(i).get("image")));
			huihua.setImage(String.valueOf(huilist.get(i).get("image")));

			huihua.setUsername(String.valueOf(huilist.get(i).get("username")));
			huihua.setselfintro(String.valueOf(huilist.get(i).get("selfintro")));
			huihua.setLastlogintime(String.valueOf(huilist.get(i).get(
					"lastlogintime")));
			huihua.setGender(String.valueOf(huilist.get(i).get("gender")));
			huihua.setAge(String.valueOf(huilist.get(i).get("age")));
			huihua.setSalary(String.valueOf(huilist.get(i).get("salary")));
			huihua.setEducation(String.valueOf(huilist.get(i).get("education")));
			hhList.add(huihua);
		}

		return hhList;
	}

	private ArrayList<UsersHuiHua> getHuahuiLocal() {
		// 显示加载数据
		ArrayList<UsersHuiHua> hhList = new ArrayList<UsersHuiHua>();

		UsersHuiHua huihua;
		for (int i = 0; i < 10; i++) {
			huihua = new UsersHuiHua();
			huihua.setId(String.valueOf(007));
			// huihua.setTxPath(String.valueOf(huilist.get(i).get("image")));
			huihua.setImage(String.valueOf(""));

			huihua.setUsername(String.valueOf("Mrxu"));
			huihua.setselfintro(String.valueOf("个人简介"));
			huihua.setLastlogintime("");
			huihua.setGender(String.valueOf(true));
			huihua.setAge(String.valueOf(26));
			huihua.setSalary(String.valueOf(666666));
			huihua.setEducation(String.valueOf("博士"));
			hhList.add(huihua);
		}

		return hhList;
	}

	// broadcast receiver
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("action.refreshUsers")
					|| action.equals("action.refresh")) {
				searchname = "";
				reFreshList();
			}

			if (action.equals("action.search")) {
				searchname = intent.getStringExtra("username");
				reFreshList();

			}

		}
	};

	public void reFreshList() {
		pageNumUsers = 1;
		pageSizeUsers = 10; // 默认
		isLastPageUsers = true;// 判断是否为最后一页
		haAdapter.clearList();
		if (isSearch()) {
			searchUserList();
		} else {
			startUserListPart();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
