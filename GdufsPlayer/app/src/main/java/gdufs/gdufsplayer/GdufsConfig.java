package gdufs.gdufsplayer;

/**
 * Created by taotao on 16-3-2.
 */
public class GdufsConfig {

    public static final boolean DEVELOPER_MODE = true;//开启调试模式

    public static final String IMAGE_CACHE_PATH = "gdufsplayer/Cache"; // 图片缓存路径


    public interface Ation {
        String BASE_URL = "";//基地址

        interface GET{

        }
        interface POST{

        }
    }

    public interface  Preference {
        interface File {

        }
    }


    /**
     * 服务器的返回码
     */
    public interface ResponseCode{

    }

    /**
     * 廣播
     */
    public interface BroadcastAction{

    }


    /**
     * 數據庫信息
     */
    public interface DbBase{
        int VERSION = 1;
        String DB_NAME = "jodo_treasure.db";
    }

}
