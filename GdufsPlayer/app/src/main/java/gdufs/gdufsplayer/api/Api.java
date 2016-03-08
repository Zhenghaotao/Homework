package gdufs.gdufsplayer.api;

import android.content.Context;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by taotao on 16-3-8.
 */
public class Api {
    private static volatile Map<String,String> headerMap = new HashMap<String,String>();


    /**
     * 用户请求接口
     * @param context
     * @param requestMap
     * @param callBack
     */
    public static void post(String url,Context context,Map<String,String> requestMap,AjaxCallBack callBack) {
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams params = new AjaxParams();
        initRequestMap(context,params, requestMap);
        finalHttp.post(url, params, callBack);
    }
}
