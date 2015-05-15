package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class MessageEntity implements Serializable{

	// primary key
	private java.lang.Integer id;
	private String TxPath;
	// fields
	private java.lang.String msgTitle;
	private java.lang.String msgContent;
	private java.util.Date sendTime;
	private java.lang.Boolean msgStatus;
	private java.lang.Integer msgBox;
	private Integer userid;
	private java.lang.String username;
	private java.lang.String useremail;
	private java.lang.String userintro;
	private java.lang.String userImg;
	private boolean status;

	
	
	public String getTxPath() {
		return TxPath;
	}
	public void setTxPath(String txPath) {
		TxPath = txPath;
	}
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(java.lang.String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public java.lang.String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(java.lang.String msgContent) {
		this.msgContent = msgContent;
	}
	public java.util.Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(java.util.Date sendTime) {
		this.sendTime = sendTime;
	}
	public java.lang.Boolean getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(java.lang.Boolean msgStatus) {
		this.msgStatus = msgStatus;
	}
	public java.lang.Integer getMsgBox() {
		return msgBox;
	}
	public void setMsgBox(java.lang.Integer msgBox) {
		this.msgBox = msgBox;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public java.lang.String getUsername() {
		return username;
	}
	public void setUsername(java.lang.String username) {
		this.username = username;
	}
	public java.lang.String getUseremail() {
		return useremail;
	}
	public void setUseremail(java.lang.String useremail) {
		this.useremail = useremail;
	}
	public java.lang.String getUserintro() {
		return userintro;
	}
	public void setUserintro(java.lang.String userintro) {
		this.userintro = userintro;
	}
	public java.lang.String getUserImg() {
		return userImg;
	}
	public void setUserImg(java.lang.String userImg) {
		this.userImg = userImg;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}


	

}
