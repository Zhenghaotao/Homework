package gdufsplayer.gdufs.com.gdufsplayer.controller;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import gdufsplayer.gdufs.com.gdufsplayer.R;
import gdufsplayer.gdufs.com.gdufsplayer.frag.HistoryFrag;
import gdufsplayer.gdufs.com.gdufsplayer.frag.MyHobbyFrag;
import gdufsplayer.gdufs.com.gdufsplayer.frag.VideoDownloadFrag;
import gdufsplayer.gdufs.com.gdufsplayer.frag.VideoLocalFrag;
import gdufsplayer.gdufs.com.gdufsplayer.frag.VideoResFrag;
import gdufsplayer.gdufs.com.gdufsplayer.graphics.ActionBarDrawerToggle;
import gdufsplayer.gdufs.com.gdufsplayer.graphics.DrawerArrowDrawable;
import gdufsplayer.gdufs.com.gdufsplayer.util.DoubleClickExitHelper;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    private DoubleClickExitHelper mDoubleClickExitHelper;//双击辅助类

    private String[] values = new String[] { "视频资源", "缓存视频", "本地视频", "我的收藏", "历史记录" };


    private Boolean openOrClose = false;

    //主界面
    public static FragmentManager fm;

    //抽屉部分
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private RelativeLayout rl;
    private LinearLayout ll_amin;
    private TextView tv_username;
    private TranslateAnimation left_translate;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    private int vc;// 获取当前版本号, 用于更新

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //双击初始化
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);



        fm = this.getSupportFragmentManager();

        initData();
        initViews();
        initDrawer();
        initEvents();
        initFirst();

    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rl = (RelativeLayout) findViewById(R.id.rl);
        ll_amin = (LinearLayout) findViewById(R.id.ll_amin);
        tv_username = (TextView) findViewById(R.id.tv_username);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.navdrawer);



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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_text, values);
        drawerList.setAdapter(adapter);

        drawerList
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        switch (position) {
                            case 0:
                                initFragment(new VideoResFrag());


                                break;
                            case 1:
                                initFragment(new VideoDownloadFrag());

                                break;
                            case 2:
                                initFragment(new VideoLocalFrag());


                                break;
                            case 3:
                                initFragment(new MyHobbyFrag());


                                break;

                            case 4:

                                initFragment(new HistoryFrag());

                                break;
                        }
                        setTitle(values[position]);
                        mDrawerLayout.closeDrawers();
                        openOrClose = false;
                    }
                });

    }
    // 初始化Fragment(FragmentActivity中呼叫)
    public void initFragment(Fragment f) {
        changeFragment(f, true);
    }

    private void changeFragment(Fragment f, boolean init) {
        FragmentTransaction ft = fm.beginTransaction().setCustomAnimations(
                R.anim.frag_slide_in_from_left,
                R.anim.frag_slide_out_from_left,
                R.anim.frag_slide_in_from_right,
                R.anim.frag_slide_out_from_right);
        ;
        ft.replace(R.id.fragment_content, f);
        if (!init)
            ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
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

        ActionBar ab = getActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

//            if (openOrClose == false) {
//
//                showCustomToast(getString(R.string.back_exit_tips),
//                        R.id.fragment_layout);
//                return mDoubleClickExitHelper.onKeyDown(keyCode, event);
//            } else {
//                mDrawerLayout.closeDrawers();
//            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
