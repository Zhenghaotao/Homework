package gdufsplayer.gdufs.com.gdufsplayer.bean;

import java.io.Serializable;
/**
 * 学生账号表
 */
public class Account implements Serializable{

	private static final long serialVersionUID = 151265695118667476L;
	
	private int id;//id
	private String username;//学生名字
	private String account;//学生学号
	private String password;//学生密码
	private String imei;//设备id号
	
	
	public Account() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + ", account="
				+ account + ", password=" + password + ", imei=" + imei + "]";
	}

}
