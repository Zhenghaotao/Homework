package gdufsplayer.gdufs.com.gdufsplayer.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by taotao on 15-11-15.
 * 网络工具类
 */
public class NetUtil {
    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }
}
