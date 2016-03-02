package gdufs.gdufsplayer.bean.local;

import java.io.Serializable;

/**
 * Created by taotao on 16-3-2.
 */
public class MediaItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2872872605249209107L;

    private String episode;//表示当前是第几集

    private String is_play;//表示当前正在播放的是第几集

    //播放地址，但需要百度重定向
    private String  sourceUrl;

    //直接可以播放的播放地址
    private String url;

    //影片名称、电台名称、本地视频名称
    private String title;

    //是否是电视台直播
    private boolean isLive = false;

    private int flags;

    private String image;

    //文件大小
    private float fileSize;


    public String getSourceUrl() {
        return sourceUrl;
    }
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
    public float getFileSize() {
        return fileSize;
    }
    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getFlags() {
        return flags;
    }
    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isLive() {
        return isLive;
    }
    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }
    public String getEpisode() {
        return episode;
    }
    public void setEpisode(String episode) {
        this.episode = episode;
    }
    public String getIs_play() {
        return is_play;
    }
    public void setIs_play(String is_play) {
        this.is_play = is_play;
    }

}
