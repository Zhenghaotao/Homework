package gdufs.gdufsplayer.controller;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import gdufs.gdufsplayer.R;
import gdufs.gdufsplayer.frag.HistoryFragment;
import gdufs.gdufsplayer.frag.LocalVideoFragment;
import gdufs.gdufsplayer.frag.MainPageFragment;
import gdufs.gdufsplayer.frag.MyHobbyFragment;
import gdufs.gdufsplayer.frag.VideoListFragment;
import gdufs.gdufsplayer.util.DoubleClickExitHelper;
import gdufs.gdufsplayer.widget.ActionBarDrawerToggle;
import gdufs.gdufsplayer.widget.DrawerArrowDrawable;
import gdufs.gdufsplayer.widget.RoundedImageView;

/**
 * Created by taotao on 16-3-2.
 */
public class MainActivity extends FragmentActivity {

    private DoubleClickExitHelper mDoubleClickExitHelper;

    private TranslateAnimation myAnimation_Left;


    private ActionBar ab;

    //主界面部分
    private FragmentManager fm;
    private boolean openOrClose = false;


    //侧拉部分
    private DrawerLayout mDrawerLayout;


    private RelativeLayout rl;
    private TextView tv_user_name;
    private RoundedImageView riv_icon;
    private ListView mDrawerList;
    private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;

    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
        checkVersion();


    }


    /**
     * 检测版本更新
     */
    private void checkVersion() {
    }

    private void initEvent() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTitle(titles[position]);

                switch (position){
                    case 0://主页
                        initFragment(new MainPageFragment());
                        break;
                    case 1://视频列表
                        initFragment(new VideoListFragment());
                        break;
                    case 2://我的收藏
                        initFragment(new MyHobbyFragment());
                        break;
                    case 3://历史记录
                        initFragment(new HistoryFragment());
                        break;
                    case 4://本地视频
                        initFragment(new LocalVideoFragment());
                        break;
                    case 5://TODO:留着
                        break;
                }
                mDrawerLayout.closeDrawers();
                openOrClose = false;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        titles = new String[]{
                getStringFromRid(R.string.main_page),
                getStringFromRid(R.string.video_list),
                getStringFromRid(R.string.my_hobby),
                getStringFromRid(R.string.video_history),
                getStringFromRid(R.string.local_video)
        };
        adapter = new ArrayAdapter<String>(this,R.layout.item_text,titles);
    }

    /**
     * 根据resId获取String
     * @param resId
     * @return
     */
    private String getStringFromRid(int resId){
        return getResources().getString(resId);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rl = (RelativeLayout) findViewById(R.id.rl);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        riv_icon = (RoundedImageView) findViewById(R.id.riv_icon);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);

        mDrawerList.setAdapter(adapter);

        ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        fm = getSupportFragmentManager();

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                openOrClose = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                openOrClose = true;
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        
    }

    /**
     *  切換Fragment提供给外部
     */
    public void changeFragment(Fragment f) {
        changeFragment(f, false);
    }


    // 初始化Fragment(FragmentActivity中呼叫)
    public void initFragment(Fragment f) {
        changeFragment(f, true);
    }

    /**
     * 切换fragment
     * @param f
     * @param init
     */
    private void changeFragment(Fragment f, boolean init) {
        FragmentTransaction ft = fm.beginTransaction().setCustomAnimations(
                R.anim.umeng_fb_slide_in_from_left,
                R.anim.umeng_fb_slide_out_from_left,
                R.anim.umeng_fb_slide_in_from_right,
                R.anim.umeng_fb_slide_out_from_right);
        ;
        ft.replace(R.id.fl_content, f);
        if (!init)
            ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(rl)) {
                mDrawerLayout.closeDrawer(rl);
                openOrClose = false;
            } else {
                mDrawerLayout.openDrawer(rl);
                openOrClose = true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
