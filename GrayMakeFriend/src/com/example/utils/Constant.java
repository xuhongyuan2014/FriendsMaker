package com.example.utils;

import com.example.model.UserInfo;

public class Constant {
	public static boolean Debug=true;
	
	public static String UserId;
	
	public static String tokenId;
	
	public static UserInfo userinfo;
	

	public static String getUserId() {
		return UserId;
	}

	public static void setUserId(String id){
		
		Constant.UserId=id;
		
	}
	
	public static String getToken() {
		return tokenId;
	}

	public static void setToken(String tokenId) {
		Constant.tokenId = tokenId;
	}





	public static final String ContextUrl = "http://10.141.70.240:8080/spcms/mobile/";




	
	public static String buildUrl(String suffix){
		return ContextUrl +"news/"+ suffix + ".jspx";
	}
	
	public static String buildUrl2(String suffix){
		return ContextUrl +"forum/"+ suffix + ".jspx";
	}
	public static String buildFriendsUrl(String suffix){
		return ContextUrl +"friends/"+ suffix + ".jspx";
	}
	
	public static String buildUrl(String suffix, String namespace){
		return ContextUrl + namespace +"/"+ suffix + ".jspx";
	}
	
	
	/**
	 * 获取新闻栏目 如独家新闻 西藏新闻 等
	 * 
	 */
	public static String getCategoryList = buildUrl("getCategoryList");
	
	/**
	 * 根据栏目id获取对应内容
	 * 
	 * @param id
	 *            栏目id
	 * @param type 新闻类型 普通1 图片 2 焦点 3 可以设置为"1,2,3" 查询3种类型新闻
	 * @param order 排序后给出说明 目前设置为3 按点击量排序
	 * @param pageSize
	 * @param pageNow
	 */
	public static String getNewsListByCategoryId = buildUrl("getNewsListByCategoryId");
	
	/**
	 * 获取新闻对应id下的详细内容
	 * 
	 * @param id 新闻内容id
	 */
	public static String  getNewsById = buildUrl("getNewsById");
	
	/**
	 * 获取对应新闻id下的评论
	 * 
	 * @param id
	 * @param pageSize
	 * @param pageNow
	 */
	public static String getCommentListByNewsId = buildUrl("getCommentListByNewsId");
	
	/**
	 * 评论新闻
	 * 
	 * @param userId
	 *            用户id
	 * @param text
	 *            评论内容
	 * @param tokenId
	 *            目前未处理设置为空
	 * @param newsId
	 *            新闻id
	 */
	public static String commitNewsComment = buildUrl("commitNewsComment");
	
	/**
	 * 回复新闻 (评论)
	 * @param userId
	 * @param tokenId
	 * @param text
	 */
	public static String replyNews = buildUrl("replyNews");
	
	public static String getSectionList = buildUrl2("getSectionList"); 
	
	/*  上传图片*/
	public static String uploadImage = buildFriendsUrl("o_upload_image_mobile");
	/*  根据ID获取用户基本信息*/
	public static String getBaseInfoById = buildFriendsUrl("getBaseInfoById");
/*  根据ID获取用户详细信息*/
	public static String getUserInfoById = buildFriendsUrl("getDetailById");
	/*  根据ID获取征友条件*/
	public static String getConditionsById = buildFriendsUrl("getConditionsById");
	/*  根据ID获取个人生活信息*/
	public static String getLifeInfo = buildFriendsUrl("getLifeInfo");
	/*修改用户详细信息*/
	public static String updateUserInfo = buildFriendsUrl("updateUserInfo");
	/*修改征友条件*/
	public static String updateConditions = buildFriendsUrl("updateConditions");
	/*修改个人生活信息*/
	public static String updateLifeInfo = buildFriendsUrl("updateLifeInfo");
	/*修改头像*/
	public static String updateUserImage = buildFriendsUrl("updateUserImage");
	/*注册交友用户*/
	public static String register = buildFriendsUrl("register");
	/*登陆*/
	public static String login = buildFriendsUrl("login");
	/*修改密码*/
	public static String changePassword = buildFriendsUrl("changePassword");
	
	/**
	 *获取用户的列表信息
	 * @param 
	 * @author AGY
	 */
	public static String getUsersList = buildFriendsUrl("getAllUsersInfo");
	
	/**
	 * 获取当前用户的私信
	 * @author Administrator//agy
	 */
	public static String getUserSiXinList = buildFriendsUrl("getMessagesById");
	
	/**
	 * 发送私信
	 * @author Administrator//agy
	 */
	public static String sendMessageById = buildFriendsUrl("sendMessageById");
	/**
	 * 改变私信状态为已读
	 * @author Administrator//agy
	 */
	public static String readMessageById = buildFriendsUrl("readMessageById");
	
	/**
	 * 获取当前用户关注的用户
	 * @author agy
	 */
	public static String getUserCareList = buildFriendsUrl("getUserCareFriend");
	

	public static String saveInfo = buildUrl("saveInfo", "info");

	/**
	 * @author agy   获取用户详细信息，url
	 */
	public static String getUserInfoDetails = buildFriendsUrl("getUserInfoDetails");
	
	/**
	 * @author agy  点击关注好友
	 * 
	 */
	public static String addFriend = buildFriendsUrl("addCareFriend");
	
	/**
	 * @author agy 取消关注好友
	 */
	public static String removeFriend = buildFriendsUrl("removeCarefriend");
	/**
	 * @author agy  查询是否已经是好友
	 */
	public static String checkIsFriend = buildFriendsUrl("checkCareFriend");
	/**
	 * @author agy  查询是否已经是好友
	 */
	public static String selectUsersList = buildFriendsUrl("selectUsersList");
	
}
