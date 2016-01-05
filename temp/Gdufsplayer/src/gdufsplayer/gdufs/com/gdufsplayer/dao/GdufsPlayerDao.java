package gdufsplayer.gdufs.com.gdufsplayer.dao;

import gdufsplayer.gdufs.com.gdufsplayer.config.C;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DaoConfig;
import android.content.Context;

public class GdufsPlayerDao {
	private static final String TAG = "StraSqliteDao";
	private static FinalDb db = null;
	private static GdufsPlayerDao instance;

	private GdufsPlayerDao() {

	}

	public static GdufsPlayerDao getInstance(Context context) {
		if (instance == null) {
			synchronized (GdufsPlayerDao.class) {
				if (instance == null) {
					//配置数据库
					DaoConfig daoConfig = new DaoConfig();
					daoConfig.setContext(context);
					daoConfig.setDbName(C.DBConfig.DATABASE);
					daoConfig.setDebug(C.DEVELOPER_MODE);//开启debug模式
					daoConfig.setDbVersion(C.DBConfig.DB_VERSION);
					db = FinalDb.create(daoConfig);
					instance = new GdufsPlayerDao();
				}
			}
		}
		return instance;
	}

}
