package gdufsplayer.gdufs.com.gdufsplayer.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import gdufsplayer.gdufs.com.gdufsplayer.R;
import gdufsplayer.gdufs.com.gdufsplayer.util.PackageUtils;
import gdufsplayer.gdufs.com.gdufsplayer.view.RoundedImageView;

public class SplashActivity extends Activity {

    private TextView logo_text, logo_name;// 控件，logo文本和名称
    private RelativeLayout splash_id;// 布局id
    private Random random;// 随机颜色
    private int avatarColor;
    private RoundedImageView iv_main_left_head;// 圆形ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initColor();
        initView();
        mainHome();

    }



    /**
     * 进入主界面
     */
    private void mainHome() {
        //渐变启动屏
        AlphaAnimation aa = new AlphaAnimation(0.2f,1.0f);
        aa.setDuration(3000);
        aa.setFillAfter(true);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {


                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash_id.startAnimation(aa);
    }


    /**
     * 初始化背景颜色
     */
    private void initColor() {
        random = new Random();
        avatarColor = Color.argb(255,
                random.nextInt(256), random.nextInt(256), random.nextInt(256));


    }

    /**
     * 初始化控件
     */
    private void initView() {

        logo_text = (TextView) findViewById(R.id.logo_text);
        logo_name = (TextView) findViewById(R.id.logo_name);
        splash_id = (RelativeLayout) findViewById(R.id.splash_id);
        iv_main_left_head = (RoundedImageView) findViewById(R.id.iv_main_left_head);
        splash_id.setBackgroundColor(avatarColor);
        iv_main_left_head.setImageResource(R.mipmap.logo);
        try {
            String str = PackageUtils.getVersionName(this);
            logo_text.setText("当前版本号：" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
