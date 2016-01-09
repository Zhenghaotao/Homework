package gdufsplayer.gdufs.com.gdufsplayer.controller;

import gdufsplayer.gdufs.com.gdufsplayer.R;
import gdufsplayer.gdufs.com.gdufsplayer.frag.HistoryFrag;
import gdufsplayer.gdufs.com.gdufsplayer.frag.MyHobbyFrag;
import gdufsplayer.gdufs.com.gdufsplayer.frag.VideoDownloadFrag;
import gdufsplayer.gdufs.com.gdufsplayer.frag.VideoLocalFrag;
import gdufsplayer.gdufs.com.gdufsplayer.frag.VideoResFrag;

/**
 * Created by zhenghaotao on 16-1-8.
 */
public enum MainTab {
    VIDEO_RES(0, R.string.video_res, R.mipmap.widget_bar_tweet_nor,
            VideoResFrag.class),

    VIDEO_DOWNLOAD(1, R.string.video_download, R.mipmap.widget_bar_tweet_nor,
            VideoDownloadFrag.class),

    VIDEO_LOCAL(1, R.string.video_local, R.mipmap.widget_bar_tweet_nor,
            VideoLocalFrag.class),

    MYHOBBY(3, R.string.my_hobby, R.mipmap.widget_bar_tweet_nor,
            MyHobbyFrag.class),

    HISTORY_RECORD(4, R.string.history_record, R.mipmap.widget_bar_tweet_nor,
            HistoryFrag.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }


}
