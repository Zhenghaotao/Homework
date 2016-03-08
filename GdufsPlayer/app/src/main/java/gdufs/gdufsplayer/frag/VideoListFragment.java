package gdufs.gdufsplayer.frag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import gdufs.gdufsplayer.bean.local.ImageManager;
import gdufs.gdufsplayer.bean.local.VideoInfo;
import gdufs.gdufsplayer.bean.local.VideoList;
import gdufs.gdufsplayer.bean.local.VideoObject;
import gdufs.gdufsplayer.controller.VideoPlayerActivity;
import gdufs.gdufsplayer.util.LocalVideoUtil;
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


    private long lastTimeonItemClick;
    private long lastTimeonItemLongClick;
    private static long CLICK_INTERVAL = 800;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        long time = System.currentTimeMillis();
        if (time - lastTimeonItemClick < CLICK_INTERVAL) {
            return ;
        }
        lastTimeonItemClick = time;
        Log.v(TAG, "onItemClick position  = " + position);
        if (mCaller.equals(CALLER_VIDEOPLAYER) || mCaller.equals(CALLER_CAMERA)) {
            startActivity(position);
        } else if (mCaller.equals(CALLER_MMS)
                || mCaller.equals(CALLER_WATCHMSG)) {
            showExternalCallerOperationDialog(position);
        }
    }

    private void showExternalCallerOperationDialog(final int position) {
        String name = mActiveList.get(position).name;

        mListOperationDialog = new AlertDialog.Builder(getActivity())
                .setTitle(name).setItems(R.array.items_for_externalCaller,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
								/* Play */
                                    case 0:
                                        startActivity(position);
                                        break;
								/* setActivityResult */
                                    case 1:
                                        setActivityResult(position);
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }).create();

        mCurrrentActiveDialog = mListOperationDialog;
        mListOperationDialog.show();
        mRefreshHandler.sendEmptyMessage(REFRESH);
    }

    private void setActivityResult(final int position) {
        if (!LocalVideoUtil.isFileExist(mActiveList.get(position).datapath)) {
            LocalVideoUtil.hint(getActivity(), R.string.file_notexist);
            return;
        }

        mCurrrentActiveDialog = new AlertDialog.Builder(getActivity()).setMessage(
                getString(R.string.confirm_addvideo)).setPositiveButton(
                R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        Uri uri = ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                mActiveList.get(position).object.getMediaId());
                        intent.setData(uri);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        //modify by yangguangfu
//						finish();
                    }
                }).setNegativeButton(R.string.button_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
        mRefreshHandler.sendEmptyMessage(REFRESH);
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

    private enum ListEnum {
        NormalVideo, CameraVideo
    };



    private static final String NORMAL_VIDEO = "NORMAL_VIDEO";//正常的视频
    private static final String CAMERA_VIDEO = "CAMERA_VIDEO";//拍下来的视频
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

    public class ListLastPosition {
        public int normalVideo = 0;
        public int cameraVideo = 0;
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
    private ArrayList<VideoInfo> mCurrentPlayList;
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

        laytout_beij = (RelativeLayout)contentView.findViewById(R.id.layout);
//        Utils.setChangeBackground(getActivity(), laytout_beij);
        if (isSDcardEjected()) {
            // showDialog(PROCESS_DIALOG_START_KEY);
            mLoadingThread = createLoadingThread();
            mLoadingThread.start();
        }

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
                R.mipmap.bg_default);
    }


    private static final int REFRESH = 1;
    private Handler mRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH) {
                Log
                        .d(TAG,
                                "handleMessage()===============receive REFRESH message+++++++++++");
                showEmptyView();
            }
        }
    };



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
                        getActivity().showDialog(PROCESS_DIALOG_START_KEY);
                        createLoadingThread().start();
                    } else {
                        getActivity().removeDialog(PROCESS_MEDIA_SCANNING_KEY);
                        refreshLastest(true);
                    }
                    mountState = false;
                    mFinishScanning = true;
                }
            }
        }
    };

    private void refreshList(ListEnum list) {
        int lastPos = listview.getFirstVisiblePosition();

        if (mActiveList == mNormalVideoList) {
            listLastPosition.normalVideo = lastPos;
        } else if (mActiveList == mCameraList) {
            listLastPosition.cameraVideo = lastPos;
        }
        if (list.equals(ListEnum.NormalVideo)) {
            mActiveList = mNormalVideoList;
            lastPos = listLastPosition.normalVideo;
        } else if (list.equals(ListEnum.CameraVideo)) {
            mActiveList = mCameraList;
            lastPos = listLastPosition.cameraVideo;
        }

        if (mListAdapter != null) {
            mListAdapter.destory();
        }

        mListAdapter = new DisplayListAdapter(getActivity());
        /** hejn, optimizing thumbnail list, 20101210 begin */
        mListAdapter.setThumbHashtable(mThumbHash, mDefaultBitmap);
        /** hejn, optimizing thumbnail list, 20101210 end */
        mListAdapter.setListItems(mActiveList);

        listview.setAdapter(mListAdapter);
        listview.setSelection(lastPos);

        mCurrentListState = LIST_STATE_REQUEST_REFRESH;
    }




    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.v(TAG, "LoadDataThread  handleMessage APPSTATE_FIRST_START");
            mAppState = APPSTATE_INITIALIZED;

            if (mCaller.equals(CALLER_CAMERA)) {
                mActiveList = mCameraList;
                // setTitle(getString(R.string.cameravideo_list));
                // tabHost.setCurrentTab(TAB_INDEX_CAMERA_VIDEO);
            } else {
                mActiveList = mNormalVideoList;
                // setTitle(getString(R.string.allvideo_list));
                // tabHost.setCurrentTab(TAB_INDEX_NORMAL_VIDEO);
                refreshLastest(false);
            }
            getActivity().removeDialog(PROCESS_DIALOG_START_KEY);
            checkListScanning();
        }
    };

    public void checkListScanning() {
        if (LocalVideoUtil.isMediaScannerScanning(getActivity().getContentResolver())
                && !mFinishScanning) {
            getActivity().showDialog(PROCESS_MEDIA_SCANNING_KEY);
        }
    }

    private Thread createLoadingThread() {
        return new Thread(new Runnable() {
            private static final int STATE_STOP = 0;
            private static final int STATE_IDLE = 1;
            private static final int STATE_TERMINATE = 2;
            private int workStatus;
            private int currentPos;
            private int maxPos;
            private Object[] items;

            public void run() {
                Log.v(TAG, "LoadDataThread  run");
                mRequest_stop_thread = false;

                getVideoData();
                mHandler.sendMessage(mHandler.obtainMessage());

                init();
                loadThumbnails();
            }

            private void init() {
                mCurrentListState = LIST_STATE_IDLE;
                workStatus = STATE_STOP;

                items = mAllVideoList.toArray();
                maxPos = items.length;
                currentPos = 0;

                Log.v("LoadDataThread", "maxPos : " + maxPos);
            }

            private void loadThumbnails() {
                while (workStatus != STATE_TERMINATE) {
                    switch (workStatus) {
                        case STATE_STOP:
                            workStatus = onStop();
                            break;
                        case STATE_IDLE:
                            workStatus = onIdle();
                            break;
                        default:
                            break;
                    }
                }
                Log.v("LoadDataThread", "STATE_TERMINATE!!!");
            }

            private int onIdle() {
                Log.v(TAG, "createLoadingThread : onIdle");

                while (true) {
                    if (mRequest_stop_thread || (currentPos == maxPos)) {
                        return STATE_TERMINATE;
                    }
                    if (mCurrentListState == LIST_STATE_REQUEST_REFRESH) {
                        mCurrentListState = LIST_STATE_IDLE;
                        return STATE_STOP;
                    }

                    LocalVideoUtil.sleep(LocalVideoUtil.LONG_INTERVAL);
                }
            }

            private int onStop() {
                if (mRequest_stop_thread) {
                    return STATE_TERMINATE;
                }
                if (mActiveList == null || listview == null) {
                    LocalVideoUtil.sleep(LocalVideoUtil.SHORT_INTERVAL);
                    return STATE_STOP;
                }
                if (mActiveList.isEmpty()) {
                    return STATE_IDLE;
                }
                if (-1 == listview.getLastVisiblePosition()) {
                    LocalVideoUtil.sleep(LocalVideoUtil.SHORT_INTERVAL);
                    return STATE_STOP;
                }

                Log.v(TAG, "createLoadingThread : onStop");

                Object[] viewHolders = mListAdapter.getHolderObjects();
                int count = viewHolders.length;
                for (int i = 0; i < count; i++) {
                    if (mCurrentListState == LIST_STATE_BUSY) {
                        return STATE_IDLE;
                    } else if (mCurrentListState == LIST_STATE_REQUEST_REFRESH) {
                        mCurrentListState = LIST_STATE_IDLE;
                        return STATE_STOP;
                    }
                    RefreshThumbnail((DisplayListAdapter.ViewHolder) viewHolders[i]);
                    LocalVideoUtil.sleep(LocalVideoUtil.MINI_INTERVAL);
                }

                LocalVideoUtil.sleep(LocalVideoUtil.MIDDLE_INTERVAL);

                if (count < mListAdapter.getHolderObjects().length) {
                    return STATE_STOP;
                }
                if (mCurrentListState == LIST_STATE_IDLE) {
                    return STATE_IDLE;
                } else {
                    mCurrentListState = LIST_STATE_IDLE;
                    return STATE_STOP;
                }
            }

            private void RefreshThumbnail(DisplayListAdapter.ViewHolder holder) {
                if (holder == null) {
                    return;
                }
                if (!holder.mUseDefault
                        || holder.mItem == null
                        || LocalVideoUtil.THUMBNAIL_CORRUPTED == holder.mItem.object
                        .getThumbnailState()) {
                    return;
                }
                /** hejn, optimizing thumbnail list, 20101210 begin */
                holder.mBitmap = holder.mItem.object.miniThumbBitmap(false,
                        mThumbHash, mDefaultBitmap);
                /** hejn, optimizing thumbnail list, 20101210 end */
                if (LocalVideoUtil.THUMBNAIL_PREPARED == holder.mItem.object
                        .getThumbnailState()) {
                    mListAdapter.sendRefreshMessage(holder);
                    holder.mUseDefault = false;
                } else {
                    holder.mUseDefault = true;
                }
            }
        });
    }






    // help functions
    private void uninitialize() {
        Log.v(TAG, "uninitialize");
        Toast.makeText(getActivity(), getString(R.string.sd_shared), Toast.LENGTH_SHORT).show();
        if (mAllImages != null) {
            mAllImages.onDestory();
        }
        if (mCurrrentActiveDialog != null) {
            if (mCurrrentActiveDialog.isShowing()) {
                mCurrrentActiveDialog.dismiss();
            }
        }
        listLastPosition.cameraVideo = 0;
        listLastPosition.normalVideo = 0;
        mAllImages = null;
        mAllVideoList.clear();
        mNormalVideoList.clear();
        mCameraList.clear();
        if (mCurrentPlayList != null)
            mCurrentPlayList.clear();
        if (mLastPlayedItem != null) {
            mLastPlayedItem.object = null;
            mLastPlayedItem.isHighlight = false;
            mLastPlayedItem.lastPos = 0;
        }
        refreshLastest(false);
    }

    public void refreshLastest(boolean isRefreshData) {
        if (isRefreshData) {
            getVideoData();
        }
        if (mActiveList == mNormalVideoList) {
            refreshList(ListEnum.NormalVideo);
        } else if (mActiveList == mCameraList) {
            refreshList(ListEnum.CameraVideo);
        }
        if (isRefreshData) {
            //modify by yangguangfu
//			Toast.makeText(this, getString(R.string.list_refresh), 1500).show();
        }
    }

    public void showEmptyView() {
        if (mAllImages != null) {
            int totalNum = mAllImages.getCount();
            if (totalNum == 0) {
                setEmptyView(true);
            } else {
                setEmptyView(false);
            }
        }
    }

    private VideoList allImages() {
        mAllImages = null;
        return ImageManager.instance().allImages(getActivity(), getActivity().getContentResolver(),
                ImageManager.INCLUDE_VIDEOS, ImageManager.SORT_ASCENDING);
    }

    public void setEmptyView(boolean show) {
        if (show) {
            mNoFileTextView.setText(getEmptyString());
            mNoFileTextView.setVisibility(View.VISIBLE);
        } else {
            mNoFileTextView.setText(getEmptyString());
            mNoFileTextView.setVisibility(View.GONE);
        }
    }

    public String getEmptyString() {
        return getResources().getString(R.string.no_vide_file);
    }

    public void getVideoData() {
        Log.v(TAG, "getVideoData()");

        mAllVideoList.clear();
        mNormalVideoList.clear();
        mCameraList.clear();

        mAllImages = allImages(); // Video List

        if (mAllImages != null) {
            int totalNum = mAllImages.getCount();
            for (int i = 0; i < totalNum; i++) {
                VideoObject image = mAllImages.getImageAt(i);

                VideoWorkItem videoDisplayObj = new VideoWorkItem();
                videoDisplayObj.object = image;
                videoDisplayObj.name = image.getTitle();
                videoDisplayObj.duration = getString(R.string.duration_tag)
                        + " " + image.getDuration();
                videoDisplayObj.size = image.getSize();
                videoDisplayObj.datapath = image.getMediapath();

                long bucketId = image.getBucketId();

                if (LocalVideoUtil.getBucketId(CAMERAFOLDER_SDCARD_PATH) == bucketId) {
                    videoDisplayObj.dataModified = image.getDateModified();
                    mCameraList.add(videoDisplayObj);
                } else {
                    mNormalVideoList.add(videoDisplayObj);
                }


                mAllVideoList.add(videoDisplayObj);
            }
            mRefreshHandler.sendEmptyMessage(REFRESH);
            // sortList(mNormalVideoList, SORT_LIST_BY_TITLE);
            // sortList(mCameraList, SORT_LIST_BY_DATE);

            Log.v(TAG, "LoadDataThread  totalNum : " + totalNum);
        }
    }


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



    private void startActivity(int position) {
        if (!mIsVideoPlaying) {
            if (!LocalVideoUtil.isFileExist(mActiveList.get(position).datapath)) {
//				PublicTools
//						.hint(PlayListsActivity.this, R.string.file_notexist);
                deleteNoFileVideo(position);
                return;
            }
            mCurrentListState = LIST_STATE_BUSY;
//            Intent i = new Intent(getActivity(), MediaPlaybackService.class);
//            i.setAction(MediaPlaybackService.SERVICECMD);
//            i.putExtra(MediaPlaybackService.CMDNAME, MediaPlaybackService.CMDPAUSE);
//            getActivity().startService(i);

            setPlayList();
            Intent intent = new Intent(getActivity(),VideoPlayerActivity.class);

            Bundle mBundle = new Bundle();
            mBundle.putSerializable("MediaIdList", mCurrentPlayList);
            intent.putExtras(mBundle);
            intent.putExtra("CurrentPosInMediaIdList", position);

            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
            mRefreshHandler.sendEmptyMessage(REFRESH);
        }
    }

    private void setPlayList() {
        if (mCurrentPlayList != null){
            mCurrentPlayList.clear();
        }

        mCurrentPlayList = new ArrayList<VideoInfo>();
        int i = 0;
        while (i < mActiveList.size()) {
//			String path = ((VideoWorkItem) mActiveList.get(i)).name;
            VideoInfo info = new VideoInfo();
            info.setTitle(((VideoWorkItem) mActiveList.get(i)).name);
            info.setUrl(((VideoWorkItem) mActiveList.get(i)).datapath);
            mCurrentPlayList.add(info);
            // Log.v(TAG, "video id : " + idList[i]);
            i++;
        }
    }

    private void deleteNoFileVideo(final int position) {
        Log.v(TAG, "deleteVideo  :   " + position);

        mCurrrentActiveDialog = new AlertDialog.Builder(getActivity()).setMessage(
                getString(R.string.confirm_deletefile_isnofile)).setPositiveButton(
                R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        VideoWorkItem item = mActiveList.get(position);
                        VideoObject object = item.object;

                        if (item.datapath.equals(object.getMediapath())) {
                            if (mAllImages.removeImage(object)) {
                                mAllVideoList.set(mAllVideoList.indexOf(item),
                                        null);
                                mActiveList.remove(item);
                                refreshLastest(false);
                                mRefreshHandler.sendEmptyMessage(REFRESH);
                                return;
                            }
                        }
                        LocalVideoUtil.hint(getActivity(),
                                R.string.fail_deletefile);
                    }
                })/*.setNegativeButton(R.string.button_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})*/.show();
        mRefreshHandler.sendEmptyMessage(REFRESH);
    }


}
