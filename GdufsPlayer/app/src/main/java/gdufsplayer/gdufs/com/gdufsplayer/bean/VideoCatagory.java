package gdufsplayer.gdufs.com.gdufsplayer.bean;

import java.io.Serializable;
/**
 * 视频分类表
 */
public class VideoCatagory implements Serializable {

	private static final long serialVersionUID = 7968664490989649507L;
	
	private int id;
	private String typeName;//类型名称
	
	public VideoCatagory() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "VideoCatagory [id=" + id + ", typeName=" + typeName + "]";
	}

}
