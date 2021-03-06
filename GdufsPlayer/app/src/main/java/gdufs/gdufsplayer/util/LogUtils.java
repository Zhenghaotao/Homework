package gdufs.gdufsplayer.util;


import android.util.Log;

import gdufs.gdufsplayer.GdufsConfig;

/**
 * Created by taotao on 15-11-15.
 * 日志工具类
 */
public class LogUtils {
	
	
	
	/**
	 * 错误
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg){
		if(GdufsConfig.DEVELOPER_MODE){
			Log.e(tag, msg + "");
		}
	}
	
	/**
	 * 信息
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if(GdufsConfig.DEVELOPER_MODE){
			Log.i(tag, msg + "");
		}
	}
	/**
	 * 警告
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag,String msg){
		if(GdufsConfig.DEVELOPER_MODE){
			Log.i(tag, msg + "");
		}
	}
	
	
	
}
