package gdufs.gdufsplayer.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gdufs.gdufsplayer.R;

/**
 * Created by taotao on 16-3-2.
 */
public class LocalVideoFragment extends Fragment {

    private View contentView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.local_frag,null,false);


        initData();
        initView();
        initEvent();

        return  contentView;
    }

    private void initData() {

    }
    private void initEvent() {

    }
    private void initView() {

    }
}
