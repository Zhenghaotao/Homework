package gdufs.gdufsplayer.bean;

/**
 * Created by taotao on 16-3-2.
 */
public class Hobby {
    private int id;
    private int hid;
    private String video_name;
    private String img_url;
    private int video_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getVideo_url() {
        return video_url;
    }

    public void setVideo_url(int video_url) {
        this.video_url = video_url;
    }

    @Override
    public String toString() {
        return "Hobby{" +
                "id=" + id +
                ", hid=" + hid +
                ", video_name='" + video_name + '\'' +
                ", img_url='" + img_url + '\'' +
                ", video_url=" + video_url +
                '}';
    }
}
