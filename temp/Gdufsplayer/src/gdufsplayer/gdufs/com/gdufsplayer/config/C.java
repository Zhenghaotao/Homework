package gdufsplayer.gdufs.com.gdufsplayer.config;

import android.R.integer;
import android.os.Environment;

/**
 * Created by taotao on 15-11-15. 欢迎界面
 */
public class C {
	// //******************************************************************************////
	public static final boolean DEVELOPER_MODE = true;// 开发者模式是否开启(开发时用)

	// //*********************************网络请求相关*********************************************////
	/**
	 * 请求服务器的Action类
	 */
	public interface RequestAction {
		//请求基地址
		String IP_BASE = "http://192.168.200.141:8080";
		// 前缀
		public static final String PRE_FIX = "";

	}
	
	/**
	 * 网络数据请求方式(get和post)
	 * 
	 */
	public static final class HTTP_METHOD {
		public static final int GET = 1;
		public static final int POST = 2;
	}
	// //**********************************数据库相关********************************************////

	/**
	 * 数据库配置
	 */
	public static final class DBConfig {
		public static final String DATABASE = "gdufsplayer.db";// 名称
		public static final int DB_VERSION = 7;// 数据库版本号,默认从1开始，2015/8/22升级为7，需要数据库更新时，更改此值。
	}

	/**
	 * 文件目录
	 */
	public static final class FileDir {
		// sd卡目录
		public static final String SD_PATH = Environment
				.getExternalStorageDirectory().getAbsolutePath();
		// rom目录
		public static final String ROM_PATH = Environment.getDataDirectory()
				.getAbsolutePath();
		public static final String APK_PATH = Environment
				.getExternalStorageDirectory().getPath() + "/StraHelper";
		public static final String DATA_PATH = APK_PATH + "/gudfsplayer";
	}

	/**
	 * 广播action常量
	 */
	public interface Action {
		String action = "com.gdufs.gdusfplayer.xxx";
	}

	/**
	 * 服务器的返回码
	 * 
	 * @author taotao
	 * 
	 */
	public interface EchoCode {
		int INTERNET_ERROR = -1;// 网络异常
		int LOGIN_ANOTHER = -2;// 在另外一台机上登陆
		int LOGIN_SUCCESS = 1;// 登陆成功
	}

}
