package gdufsplayer.gdufs.com.gdufsplayer.bean;

import java.io.Serializable;
/**
 * 视频表
 * @author taotao
 *
 */
public class Video implements Serializable{

	private static final long serialVersionUID = 5792589398283684996L;
	
	private int id;//id号
	private String videoname;//视频名称
	private String detail_content;//详细内容
	private String img_url;//图片显示的url
	private String video_url;//获取视频的url
	private int cata_id;//类型id
	private int count;//播放次数
	public Video() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVideoname() {
		return videoname;
	}
	public void setVideoname(String videoname) {
		this.videoname = videoname;
	}
	public String getDetail_content() {
		return detail_content;
	}
	public void setDetail_content(String detail_content) {
		this.detail_content = detail_content;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public int getCata_id() {
		return cata_id;
	}
	public void setCata_id(int cata_id) {
		this.cata_id = cata_id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "Video [id=" + id + ", videoname=" + videoname
				+ ", detail_content=" + detail_content + ", img_url=" + img_url
				+ ", video_url=" + video_url + ", cata_id=" + cata_id
				+ ", count=" + count + "]";
	}
	
}
