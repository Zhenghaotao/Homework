package gdufsplayer.gdufs.com.gdufsplayer.frag;

import gdufsplayer.gdufs.com.gdufsplayer.config.C;
import android.os.Handler;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	protected Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case  C.EchoCode.INTERNET_ERROR:
				break;
			default:
				break;
			}
			
		};
	};
}
