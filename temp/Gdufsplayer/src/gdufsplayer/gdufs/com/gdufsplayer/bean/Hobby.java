package gdufsplayer.gdufs.com.gdufsplayer.bean;

import java.io.Serializable;
/**
 * 收藏表
 * @author taotao
 *
 */
public class Hobby implements Serializable {

	private static final long serialVersionUID = -2708548586997964115L;
	
	private int id;
	private String video_name;
	private String video_url;
	private String img_url;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVideo_name() {
		return video_name;
	}
	public void setVideo_name(String video_name) {
		this.video_name = video_name;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	@Override
	public String toString() {
		return "Hobby [id=" + id + ", video_name=" + video_name
				+ ", video_url=" + video_url + ", img_url=" + img_url + "]";
	}

}
