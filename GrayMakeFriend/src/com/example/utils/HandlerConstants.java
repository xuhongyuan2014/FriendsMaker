package com.example.utils;

public class HandlerConstants {

	
	/**
	 * 去上传文件
	 */
	public static final int TO_UPLOAD_FILE = 1;  
	/**
	 * 上传文件响应
	 */
	public static final int UPLOAD_FILE_DONE = 6;  //
	/**
	 * 选择文件
	 */
	public static final int TO_SELECT_PHOTO = 3;
	/**
	 * 上传初始化
	 */
	public static final int UPLOAD_INIT_PROCESS = 4;
	/**
	 * 上传中
	 */
	public static final int UPLOAD_IN_PROCESS = 5;
	/**
	 * 获取用户信息成功
	 */
	public static final int GET_USER_INFO_OK=101;
	/**
	 * 获取用户信息失败
	 */
	public static final int GET_USER_INFO_ERROR=102;
	
	
	public static final int GET_USER_LIST_OK = 201;
	public static final int GET_USER_LIST_ERROR = 202;
	/**
	 * 获取私信列表成功
	 */
	public static final int GET_MESSAGES_OK=203;
	/**
	 * 获取私信列表失败
	 */
	public static final int GET_MESSAGES_ERROR=204;
	/**
	 * 关注好友成功
	 */
	public static final int ADDFRIENDOK = 205;
	/**
	 * 关注好友失败  
	 */
	public static final int ADDFRIENDERROR = 206;
	/**
	 * 取消关注好友成功
	 */
	public static final int REMOVEFRIENDOK = 207;
	/**
	 * 取消关注好友失败
	 */
	public static final int REMOVEFRIENDERROR = 208;
}
