package gdufsplayer.gdufs.com.gdufsplayer.controller;

import gdufsplayer.gdufs.com.gdufsplayer.R;
import gdufsplayer.gdufs.com.gdufsplayer.graphics.DrawerArrowDrawable;
import gdufsplayer.gdufs.com.gdufsplayer.util.DoubleClickExitHelper;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	
	private DoubleClickExitHelper mDoubleClickExitHelper;//双击辅助类
	
	
	//抽屉部分
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerArrowDrawable drawerArrow;
	private RelativeLayout rl;
	private LinearLayout ll_amin;
	private TextView tv_username;
	private TranslateAnimation left_translate;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	
	private int vc;// 获取当前版本号, 用于更新
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initData();
        initViews();
        initEvents();
        initFirst();
        
    }
    /**
     * 首次进入页面时的初始化
     */
	private void initFirst() {
		
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		
	}
	/**
	 * 初始化事件
	 */
	private void initEvents() {
		
	}
	/**
	 * 初始化控件
	 */
	private void initViews() {
		
	}
    
}
