package gdufs.gdufsplayer.bean;

/**
 * Created by taotao on 16-3-2.
 */
public class Video {
    private int id;
    private int vid;
    private String videoname;
    private String detail_content;
    private String img_url;
    private String video_url;
    private int cata_id;
    private int count;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
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

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", vid=" + vid +
                ", videoname='" + videoname + '\'' +
                ", detail_content='" + detail_content + '\'' +
                ", img_url='" + img_url + '\'' +
                ", video_url='" + video_url + '\'' +
                ", cata_id=" + cata_id +
                ", count=" + count +
                '}';
    }
}
