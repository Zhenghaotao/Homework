package gdufsplayer.gdufs.com.gdufsplayer.bean;

import java.io.Serializable;

public class History implements Serializable {

	private static final long serialVersionUID = -3812419293094261224L;

	private int id;//播放历史id
	private String img_url;//获取视频的url
	private String video_name;//视频名称
	private String video_url;//视频地址
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
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
	@Override
	public String toString() {
		return "History [id=" + id + ", img_url=" + img_url + ", video_name="
				+ video_name + ", video_url=" + video_url + "]";
	}
}
