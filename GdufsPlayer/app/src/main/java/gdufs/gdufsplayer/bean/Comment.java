package gdufs.gdufsplayer.bean;

/**
 * Created by taotao on 16-3-2.
 */
public class Comment {

    private int id;
    private int cid;
    private String username;
    private String account;
    private int video_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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
        return "Comment{" +
                "id=" + id +
                ", cid=" + cid +
                ", username='" + username + '\'' +
                ", account='" + account + '\'' +
                ", video_id=" + video_id +
                '}';
    }
}
