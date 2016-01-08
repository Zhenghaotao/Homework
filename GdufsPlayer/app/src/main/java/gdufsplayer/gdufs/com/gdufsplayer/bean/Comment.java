package gdufsplayer.gdufs.com.gdufsplayer.bean;

import java.io.Serializable;
/**
 * 评论表
 * @author taotao
 *
 */
public class Comment implements Serializable{

	public static final int ALIAS = 1;
	public static final int NOT_ALIAS = 0;
	
	
	private static final long serialVersionUID = 6184811384793130049L;
	
	private int id;
	private int isAlias;//是否匿名(1:是，0:否)
	private String username;//
	private String account;//学号
	private int video_id;//评论的视频id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIsAlias() {
		return isAlias;
	}
	public void setIsAlias(int isAlias) {
		this.isAlias = isAlias;
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
	public int getVideo_id() {
		return video_id;
	}
	public void setVideo_id(int video_id) {
		this.video_id = video_id;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", isAlias=" + isAlias + ", username="
				+ username + ", account=" + account + ", video_id=" + video_id
				+ "]";
	}
}
