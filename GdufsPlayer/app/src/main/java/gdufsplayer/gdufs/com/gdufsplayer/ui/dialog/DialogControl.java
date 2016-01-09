package gdufsplayer.gdufs.com.gdufsplayer.ui.dialog;

import android.app.ProgressDialog;

/**
 * Created by zhenghaotao on 16-1-8.
 */
public interface DialogControl {
    void hideWaitDialog();

    ProgressDialog showWaitDialog();

    ProgressDialog showWaitDialog(int resid);

    ProgressDialog showWaitDialog(String text);

}
