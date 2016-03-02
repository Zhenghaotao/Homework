package gdufs.gdufsplayer.bean;

/**
 * Created by taotao on 16-3-2.
 */
public class VideoCatagory {

    private int id;
    private int vcid;
    private String type_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVcid() {
        return vcid;
    }

    public void setVcid(int vcid) {
        this.vcid = vcid;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    @Override
    public String toString() {
        return "VideoCatagory{" +
                "id=" + id +
                ", vcid=" + vcid +
                ", type_name='" + type_name + '\'' +
                '}';
    }
}
