package gdufs.gdufsplayer.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import gdufs.gdufsplayer.R;

/**
 * 播放界面
 */
public class VideoPlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.video_player_activity);
    }
}
