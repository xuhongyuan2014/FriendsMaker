package com.example.model;


import java.util.Date;

public class UserInfo {

	
	private Integer id;
	private java.lang.String username;
	private java.lang.String email;
	private java.util.Date registerTime;
	private java.lang.String registerIp;
	private java.util.Date lastLoginTime;
	private java.lang.String lastLoginIp;
	private java.lang.Integer loginCount;
	private java.lang.Integer rank;
	private java.lang.Long uploadTotal;
	private java.lang.Integer uploadSize;
	private java.sql.Date uploadDate;
	private java.lang.Boolean admin;
	private java.lang.Boolean viewonlyAdmin;
	private java.lang.Boolean selfAdmin;
	private java.lang.Boolean disabled;
	private java.lang.Integer mfdisabled;
	
	
	
	private java.lang.String realname;
	private java.lang.Boolean gender;
	private java.util.Date birthday;
	private java.lang.String intro;
	private java.lang.String comefrom;
	private java.lang.String qq;
	private java.lang.String msn;
	private java.lang.String phone;
	private java.lang.String mobile;
	private java.lang.String userImg;
	private java.lang.String userSignature;
	
	
	
	
	//鐢ㄦ埛鎵╁睍琛ㄦ柊澧炵殑瀛楁
	private java.lang.String userwork;
	private java.lang.Integer userage;
	private java.lang.Integer mfyes;
	private java.lang.String mfname;
	private java.lang.Integer user_level;
	private java.lang.Integer user_xunzhang;
	private java.lang.String user_lovestyle;
	private java.lang.String user_height;
	private java.lang.String user_maritalstatus;
	private java.lang.String user_xueli ;//瀛﹀巻
	private java.lang.String user_salary ;//宸ヨ祫
	private java.lang.String user_house ;//浣忔埧鏉′欢
	private java.lang.String graduatedschool ;//姣曚笟瀛︽牎
	private java.lang.String bodysize ;//韬潗
	private java.lang.String studypro ;//瀛︿範涓撲笟
	private java.lang.String shuxiang ;//灞炵浉
	private java.lang.String buycar ;//鏄惁璐溅
	private java.lang.String xingzuo;// 鏄熷骇
	private java.lang.String havebaby ;//鏄惁鏈夊瀛�
	private java.lang.String weight;// 浣撻噸
	private java.lang.String studylanguage ;// 鎺屾彙璇█
	private java.lang.String bloodstyle ;//琛�瀷
	private java.lang.String companystyle ;//鍏徃鎬ц川
	private java.lang.Integer looks  ;//鐩歌矊璇勪环
	private java.lang.String companyhangye;
	private java.lang.String race;
	private java.lang.String religiousbeliefs ;
	private java.lang.String select_age ;
	private java.lang.String select_place ;
	private java.lang.String select_height ;
	private java.lang.String select_marritalstatus ;
	private java.lang.String select_xueli;
	private java.lang.String select_house;
	private java.lang.String select_salary;
	private java.lang.String select_havebaby ;
	private java.lang.String parentssituation; 
	private java.lang.String lifezuoxi;
	private java.lang.String homerank;
	private java.lang.String smoking;
	private java.lang.String housework;
	private java.lang.String drinking;
	private java.lang.String cooking;
	private java.util.Date kaitongtime;
	private java.lang.Integer points;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public java.lang.String getUsername() {
		return username;
	}
	public void setUsername(java.lang.String username) {
		this.username = username;
	}
	public java.lang.String getEmail() {
		return email;
	}
	public void setEmail(java.lang.String email) {
		this.email = email;
	}
	public java.util.Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(java.util.Date registerTime) {
		this.registerTime = registerTime;
	}
	public java.lang.String getRegisterIp() {
		return registerIp;
	}
	public void setRegisterIp(java.lang.String registerIp) {
		this.registerIp = registerIp;
	}
	public java.util.Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(java.util.Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public java.lang.String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(java.lang.String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public java.lang.Integer getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(java.lang.Integer loginCount) {
		this.loginCount = loginCount;
	}
	public java.lang.Integer getRank() {
		return rank;
	}
	public void setRank(java.lang.Integer rank) {
		this.rank = rank;
	}
	public java.lang.Long getUploadTotal() {
		return uploadTotal;
	}
	public void setUploadTotal(java.lang.Long uploadTotal) {
		this.uploadTotal = uploadTotal;
	}
	public java.lang.Integer getUploadSize() {
		return uploadSize;
	}
	public void setUploadSize(java.lang.Integer uploadSize) {
		this.uploadSize = uploadSize;
	}
	public java.sql.Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(java.sql.Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public java.lang.Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(java.lang.Boolean admin) {
		this.admin = admin;
	}
	public java.lang.Boolean getViewonlyAdmin() {
		return viewonlyAdmin;
	}
	public void setViewonlyAdmin(java.lang.Boolean viewonlyAdmin) {
		this.viewonlyAdmin = viewonlyAdmin;
	}
	public java.lang.Boolean getSelfAdmin() {
		return selfAdmin;
	}
	public void setSelfAdmin(java.lang.Boolean selfAdmin) {
		this.selfAdmin = selfAdmin;
	}
	public java.lang.Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(java.lang.Boolean disabled) {
		this.disabled = disabled;
	}
	public java.lang.Integer getMfdisabled() {
		return mfdisabled;
	}
	public void setMfdisabled(java.lang.Integer mfdisabled) {
		this.mfdisabled = mfdisabled;
	}
	public java.lang.String getRealname() {
		return realname;
	}
	public void setRealname(java.lang.String realname) {
		this.realname = realname;
	}
	public java.lang.Boolean getGender() {
		return gender;
	}
	public void setGender(java.lang.Boolean gender) {
		this.gender = gender;
	}
	public java.util.Date getBirthday() {
		return birthday;
	}
	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}
	public java.lang.String getIntro() {
		return intro;
	}
	public void setIntro(java.lang.String intro) {
		this.intro = intro;
	}
	public java.lang.String getComefrom() {
		return comefrom;
	}
	public void setComefrom(java.lang.String comefrom) {
		this.comefrom = comefrom;
	}
	public java.lang.String getQq() {
		return qq;
	}
	public void setQq(java.lang.String qq) {
		this.qq = qq;
	}
	public java.lang.String getMsn() {
		return msn;
	}
	public void setMsn(java.lang.String msn) {
		this.msn = msn;
	}
	public java.lang.String getPhone() {
		return phone;
	}
	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}
	public java.lang.String getMobile() {
		return mobile;
	}
	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}
	public java.lang.String getUserImg() {
		return userImg;
	}
	public void setUserImg(java.lang.String userImg) {
		this.userImg = userImg;
	}
	public java.lang.String getUserSignature() {
		return userSignature;
	}
	public void setUserSignature(java.lang.String userSignature) {
		this.userSignature = userSignature;
	}
	public java.lang.String getUserwork() {
		return userwork;
	}
	public void setUserwork(java.lang.String userwork) {
		this.userwork = userwork;
	}
	public java.lang.Integer getUserage() {
		return userage;
	}
	public void setUserage(java.lang.Integer userage) {
		this.userage = userage;
	}
	public java.lang.Integer getMfyes() {
		return mfyes;
	}
	public void setMfyes(java.lang.Integer mfyes) {
		this.mfyes = mfyes;
	}
	public java.lang.String getMfname() {
		return mfname;
	}
	public void setMfname(java.lang.String mfname) {
		this.mfname = mfname;
	}
	public java.lang.Integer getUser_level() {
		return user_level;
	}
	public void setUser_level(java.lang.Integer user_level) {
		this.user_level = user_level;
	}
	public java.lang.Integer getUser_xunzhang() {
		return user_xunzhang;
	}
	public void setUser_xunzhang(java.lang.Integer user_xunzhang) {
		this.user_xunzhang = user_xunzhang;
	}
	public java.lang.String getUser_lovestyle() {
		return user_lovestyle;
	}
	public void setUser_lovestyle(java.lang.String user_lovestyle) {
		this.user_lovestyle = user_lovestyle;
	}
	public java.lang.String getUser_height() {
		return user_height;
	}
	public void setUser_height(java.lang.String user_height) {
		this.user_height = user_height;
	}
	public java.lang.String getUser_maritalstatus() {
		return user_maritalstatus;
	}
	public void setUser_maritalstatus(java.lang.String user_maritalstatus) {
		this.user_maritalstatus = user_maritalstatus;
	}
	public java.lang.String getUser_xueli() {
		return user_xueli;
	}
	public void setUser_xueli(java.lang.String user_xueli) {
		this.user_xueli = user_xueli;
	}
	public java.lang.String getUser_salary() {
		return user_salary;
	}
	public void setUser_salary(java.lang.String user_salary) {
		this.user_salary = user_salary;
	}
	public java.lang.String getUser_house() {
		return user_house;
	}
	public void setUser_house(java.lang.String user_house) {
		this.user_house = user_house;
	}
	public java.lang.String getGraduatedschool() {
		return graduatedschool;
	}
	public void setGraduatedschool(java.lang.String graduatedschool) {
		this.graduatedschool = graduatedschool;
	}
	public java.lang.String getBodysize() {
		return bodysize;
	}
	public void setBodysize(java.lang.String bodysize) {
		this.bodysize = bodysize;
	}
	public java.lang.String getStudypro() {
		return studypro;
	}
	public void setStudypro(java.lang.String studypro) {
		this.studypro = studypro;
	}
	public java.lang.String getShuxiang() {
		return shuxiang;
	}
	public void setShuxiang(java.lang.String shuxiang) {
		this.shuxiang = shuxiang;
	}
	public java.lang.String getBuycar() {
		return buycar;
	}
	public void setBuycar(java.lang.String buycar) {
		this.buycar = buycar;
	}
	public java.lang.String getXingzuo() {
		return xingzuo;
	}
	public void setXingzuo(java.lang.String xingzuo) {
		this.xingzuo = xingzuo;
	}
	public java.lang.String getHavebaby() {
		return havebaby;
	}
	public void setHavebaby(java.lang.String havebaby) {
		this.havebaby = havebaby;
	}
	public java.lang.String getWeight() {
		return weight;
	}
	public void setWeight(java.lang.String weight) {
		this.weight = weight;
	}
	public java.lang.String getStudylanguage() {
		return studylanguage;
	}
	public void setStudylanguage(java.lang.String studylanguage) {
		this.studylanguage = studylanguage;
	}
	public java.lang.String getBloodstyle() {
		return bloodstyle;
	}
	public void setBloodstyle(java.lang.String bloodstyle) {
		this.bloodstyle = bloodstyle;
	}
	public java.lang.String getCompanystyle() {
		return companystyle;
	}
	public void setCompanystyle(java.lang.String companystyle) {
		this.companystyle = companystyle;
	}
	public java.lang.Integer getLooks() {
		return looks;
	}
	public void setLooks(java.lang.Integer looks) {
		this.looks = looks;
	}
	public java.lang.String getCompanyhangye() {
		return companyhangye;
	}
	public void setCompanyhangye(java.lang.String companyhangye) {
		this.companyhangye = companyhangye;
	}
	public java.lang.String getRace() {
		return race;
	}
	public void setRace(java.lang.String race) {
		this.race = race;
	}
	public java.lang.String getReligiousbeliefs() {
		return religiousbeliefs;
	}
	public void setReligiousbeliefs(java.lang.String religiousbeliefs) {
		this.religiousbeliefs = religiousbeliefs;
	}
	public java.lang.String getSelect_age() {
		return select_age;
	}
	public void setSelect_age(java.lang.String select_age) {
		this.select_age = select_age;
	}
	public java.lang.String getSelect_place() {
		return select_place;
	}
	public void setSelect_place(java.lang.String select_place) {
		this.select_place = select_place;
	}
	public java.lang.String getSelect_height() {
		return select_height;
	}
	public void setSelect_height(java.lang.String select_height) {
		this.select_height = select_height;
	}
	public java.lang.String getSelect_marritalstatus() {
		return select_marritalstatus;
	}
	public void setSelect_marritalstatus(java.lang.String select_marritalstatus) {
		this.select_marritalstatus = select_marritalstatus;
	}
	public java.lang.String getSelect_xueli() {
		return select_xueli;
	}
	public void setSelect_xueli(java.lang.String select_xueli) {
		this.select_xueli = select_xueli;
	}
	public java.lang.String getSelect_house() {
		return select_house;
	}
	public void setSelect_house(java.lang.String select_house) {
		this.select_house = select_house;
	}
	public java.lang.String getSelect_salary() {
		return select_salary;
	}
	public void setSelect_salary(java.lang.String select_salary) {
		this.select_salary = select_salary;
	}
	public java.lang.String getSelect_havebaby() {
		return select_havebaby;
	}
	public void setSelect_havebaby(java.lang.String select_havebaby) {
		this.select_havebaby = select_havebaby;
	}
	public java.lang.String getParentssituation() {
		return parentssituation;
	}
	public void setParentssituation(java.lang.String parentssituation) {
		this.parentssituation = parentssituation;
	}
	public java.lang.String getLifezuoxi() {
		return lifezuoxi;
	}
	public void setLifezuoxi(java.lang.String lifezuoxi) {
		this.lifezuoxi = lifezuoxi;
	}
	public java.lang.String getHomerank() {
		return homerank;
	}
	public void setHomerank(java.lang.String homerank) {
		this.homerank = homerank;
	}
	public java.lang.String getSmoking() {
		return smoking;
	}
	public void setSmoking(java.lang.String smoking) {
		this.smoking = smoking;
	}
	public java.lang.String getHousework() {
		return housework;
	}
	public void setHousework(java.lang.String housework) {
		this.housework = housework;
	}
	public java.lang.String getDrinking() {
		return drinking;
	}
	public void setDrinking(java.lang.String drinking) {
		this.drinking = drinking;
	}
	public java.lang.String getCooking() {
		return cooking;
	}
	public void setCooking(java.lang.String cooking) {
		this.cooking = cooking;
	}
	public java.util.Date getKaitongtime() {
		return kaitongtime;
	}
	public void setKaitongtime(java.util.Date kaitongtime) {
		this.kaitongtime = kaitongtime;
	}
	public java.lang.Integer getPoints() {
		return points;
	}
	public void setPoints(java.lang.Integer points) {
		this.points = points;
	}


}
