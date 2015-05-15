package com.example.utils;



import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpEngine {
	public HttpEngine() {

	}
	
	public static HttpEngine getHttpEngine(){
		return new HttpEngine();
	}

	private static final String TAG = "HttpEngine";
	private int mTimeOut = 25000;
	private String mUserAgent = "";

	public void setUserAgent(String ua) {
		mUserAgent = ua;
	}

	public void setTimeOUT(int to) {
		mTimeOut = to;
	}

	private AsyncHttpClient mHttpClient = getHttpsClientInstance();

	public void get(String url, RequestParams params,AsyncHttpResponseHandler responseHandler) {
		mHttpClient.get(url, params, responseHandler);
	}

	public void post(String url, RequestParams params,AsyncHttpResponseHandler responseHandler) {
		mHttpClient.post(url, params, responseHandler);
	}

	

	private AsyncHttpClient getHttpsClientInstance() {
		AsyncHttpClient client = new AsyncHttpClient();
		//client.setTimeout(999999);
		client.setTimeout(mTimeOut);
		client.setUserAgent(mUserAgent);
		return client;
	}

	/**
	 * HTTP GET FOR STRING
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @param responseHandler
	 */
	public void httpGet(String url, RequestParams params, Header[] headers,
			AsyncHttpResponseHandler responseHandler) {
		if (params == null) {
			params = new RequestParams();
		}
		// Logger.i(TAG, "GO AHEAD TO GET: " + url);
		try {
			mHttpClient.get(null, url, headers, params, responseHandler);
		} catch (Exception e) {
			// Logger.i(TAG, "FAIL TO GET: " + url);
		}
	}

	/**
	 * HTTP POST FOR STRING
	 * 
	 * @param url
	 * @param headers
	 * @param entity
	 * @param contentType
	 * @param responseHandler
	 */
	public void httpPost(String url, Header[] headers, HttpEntity entity,
			String contentType, final AsyncHttpResponseHandler responseHandler) {
		try {
			// Logger.i(TAG, "GO AHEAD TO POST: " + url);
			mHttpClient.post(null, url, headers, entity, contentType,
					responseHandler);
		} catch (Exception e) {
			// Logger.i(TAG, "FAIL TO POST: " + url);
		}
	}

	/**
	 * HTTP GET FOR BINARY
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @param responseHandler
	 */
	public void httpGet(String url, RequestParams params, Header[] headers,
			BinaryHttpResponseHandler responseHandler) {
		if (params == null) {
			params = new RequestParams();
		}
		// Logger.i(TAG, "GO AHEAD TO GET: " + url);
		try {
			mHttpClient.get(null, url, headers, params, responseHandler);
		} catch (Exception e) {
			// Logger.i(TAG, "FAIL TO GET: " + url);
		}
	}

	/**
	 * HTTP POST FOR BINARY
	 * 
	 * @param url
	 * @param headers
	 * @param entity
	 * @param contentType
	 * @param responseHandler
	 */
	public void httpPost(String url, Header[] headers, HttpEntity entity,
			String contentType, final BinaryHttpResponseHandler responseHandler) {
		try {
			// Logger.i(TAG, "GO AHEAD TO POST: " + url);
			mHttpClient.post(null, url, headers, entity, contentType,
					responseHandler);
		} catch (Exception e) {
			// Logger.i(TAG, "FAIL TO POST: " + url);
		}
	}
}
