package gdufs.gdufsplayer.frag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import gdufs.gdufsplayer.R;
import gdufs.gdufsplayer.adapter.DisplayListAdapter;
import gdufs.gdufsplayer.bean.local.MediaItem;
import gdufs.gdufsplayer.bean.local.VideoList;
import gdufs.gdufsplayer.bean.local.VideoObject;
import gdufs.gdufsplayer.util.LogUtils;

/**
 * Created by taotao on 16-3-2.
 */
public class VideoListFragment extends Fragment  implements
        ListView.OnItemClickListener, ListView.OnItemLongClickListener,
        ListView.OnScrollListener{

    private static final String TAG = "VideoListFragment";

    private View contentView;

    private static final String CALLER_VIDEOPLAYER = "VIDEOPLAYER";
    private static final String CALLER_MMS = "MMS";
    private static final String CALLER_CAMERA = "CAMERA";
    private static final String CALLER_WATCHMSG = "WATCHMSG";
    private static final int PROCESS_DIALOG_START_KEY = 0;
    private static final int PROCESS_MEDIA_SCANNING_KEY = 1;
    private static final int INTENT_TERMINATED = 0;


    // private static final long CAMERAFOLDER_USERDATA_BUCKET_ID = 1712717414;
    private static final String CAMERAFOLDER_SDCARD_PATH = "/mnt/sdcard/Camera/Videos";

    public class ListLastPosition {
        public int normalVideo = 0;
        public int cameraVideo = 0;
    }

    private static final String NORMAL_VIDEO = "NORMAL_VIDEO";
    private static final String CAMERA_VIDEO = "CAMERA_VIDEO";
    private static final int TAB_INDEX_NORMAL_VIDEO = 0;
    private static final int TAB_INDEX_CAMERA_VIDEO = 1;
    private static final int LIST_STATE_IDLE = 0;
    private static final int LIST_STATE_BUSY = 1;
    private static final int LIST_STATE_REQUEST_REFRESH = 2;
    private static final int SORT_LIST_BY_DATE = 0;
    private static final int SORT_LIST_BY_TITLE = 1;
    private static final int APPSTATE_FIRST_START = 0;
    private static final int APPSTATE_INITIALIZED = 1;
    private static final int APPSTATE_FINISHED = 2;

    public class VideoWorkItem {
        public VideoObject object;
        public long dataModified = 0;
        public String datapath;
        public String name;
        public String duration;
        public String size;
        public boolean isHighlight = false;
        public int lastPos = 0;
    }


    private int mAppState;
    private boolean mRequest_stop_thread;
    private boolean mIsVideoPlaying;
    private boolean mFinishScanning;
    private int mCurrentListState;
    private String mCaller;
    private ListLastPosition listLastPosition = new ListLastPosition();
    // private ManagePreference mManagePreference = new ManagePreference();
    private VideoWorkItem mLastPlayedItem;
    private ListView listview;
    private DisplayListAdapter mListAdapter;
    // private BottomTabHost tabHost;
    private static Display mDisplay;
    private EditText mRenameEditText;
    private AlertDialog mListOperationDialog;
    private AlertDialog mCurrrentActiveDialog;
    private VideoList mAllImages;
    private List<VideoWorkItem> mAllVideoList = new ArrayList<VideoWorkItem>();
    private List<VideoWorkItem> mNormalVideoList = new ArrayList<VideoWorkItem>();
    private List<VideoWorkItem> mCameraList = new ArrayList<VideoWorkItem>();
    private List<VideoWorkItem> mActiveList;
    private ArrayList<MediaItem> mCurrentPlayList;
    private RelativeLayout layout;
    /** hejn, optimizing thumbnail list, 20101210 begin */
    private Hashtable<Integer, Bitmap> mThumbHash = new Hashtable<Integer, Bitmap>();
    private Bitmap mDefaultBitmap;
    private RelativeLayout laytout_beij;


    private Thread mLoadingThread = null;


    private ImageView noSdcard;


    private LayoutInflater mInflater;
    private View convertView;
    private TextView mNoFileTextView;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.videolist_frag,null,false);
        initialize();
        initView();
        initEvent();

        return  contentView;
    }

    private void initialize() {
        LogUtils.i(TAG, "VideoPlayerActivity  initialize");


        mAppState = APPSTATE_FIRST_START;
        mCaller = CALLER_VIDEOPLAYER;
        mIsVideoPlaying = false;
        mFinishScanning = false;

        mNoFileTextView = (TextView) contentView.findViewById(R.id.playListView);
        listview = (ListView) contentView.findViewById(R.id.list);
        noSdcard = (ImageView) contentView.findViewById(R.id.icon_nocard);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);

        // mManagePreference.initialize(VideoPlayerActivity.this);
        mDisplay = ((Activity)getActivity()).getWindow().getWindowManager().getDefaultDisplay();

        String caller = getActivity().getIntent().getStringExtra("Caller");
        if (caller != null) {
            mCaller = caller;
        }

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        iFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        iFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        iFilter.addDataScheme("file");
        getActivity().registerReceiver(mBroadcastReceiver, iFilter);
        /** hejn, optimizing thumbnail list, 20101210 begin */
        mThumbHash.clear();
        mDefaultBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.icon);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        boolean mountState = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAppState == APPSTATE_FINISHED) {
                return;
            }
            String action = intent.getAction();
            Log.v(TAG, "BroadcastReceiver action : " + action);
            // action.equals(Intent.ACTION_MEDIA_MOUNTED)

            //当SD卡挂载在电脑上时候，如果手动将语音备忘录中的录音删除的时
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                if (!mountState) {
                    Log.v(TAG, "BroadcastReceiver sdcard ejected/mounted");
                    if (mAppState == APPSTATE_INITIALIZED) {
                        uninitialize();
                    }
                    mountState = true;
                }
            } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
                Log.v(TAG, "BroadcastReceiver start scan media");
                // if (mountState) {
                // showDialog(PROCESS_DIALOG_SCAN_KEY);
                // }
            } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
                if (isSDcardEjected() && mAppState != APPSTATE_FINISHED) {
                    Log.v(TAG, "BroadcastReceiver stop scan media");
                    if (mAppState == APPSTATE_FIRST_START) {
                        showDialog(PROCESS_DIALOG_START_KEY);
                        createLoadingThread().start();
                    } else {
                        removeDialog(PROCESS_MEDIA_SCANNING_KEY);
                        refreshLastest(true);
                    }
                    mountState = false;
                    mFinishScanning = true;
                }
            }
        }
    };


    private void initEvent() {

    }
    private void initView() {

    }


    private boolean isSDcardEjected() {
        boolean isSdcard_ok = false;
        String status = Environment.getExternalStorageState();
        Log.v(TAG, "status : " + status
                + status.equals(Environment.MEDIA_REMOVED));
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            isSdcard_ok = true;
            return true;
        }
        if (!isSdcard_ok) {
            if(noSdcard!=null)
                noSdcard.setVisibility(View.VISIBLE);
            if (status.equals(Environment.MEDIA_UNMOUNTED)) {
                Toast.makeText(getContext(), R.string.sd_unmounted, Toast.LENGTH_SHORT)
                        .show();
            } else if (status.equals(Environment.MEDIA_SHARED)) {
                Toast.makeText(getContext(), R.string.sd_shared, Toast.LENGTH_SHORT)
                        .show();
            } else if (status.equals(Environment.MEDIA_REMOVED)) {
                Toast.makeText(getContext(), R.string.sd_removed, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getContext(), R.string.sd_noinsert, Toast.LENGTH_SHORT)
                        .show();
            }
        }else{
            noSdcard.setVisibility(View.GONE);
        }
        return isSdcard_ok;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
