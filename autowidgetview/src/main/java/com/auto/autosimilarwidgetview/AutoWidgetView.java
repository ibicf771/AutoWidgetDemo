package com.auto.autosimilarwidgetview;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auto.autosimilarwidgetview.function.DriveWayMsgHandler;
import com.auto.autosimilarwidgetview.function.TmcMsgHandler;
import com.auto.autosimilarwidgetview.util.AutoMsgUtil;
import com.autosimilarwidget.view.IAutoSimilarWidgetViewService;

/**
 * Created by simin.yang on 2017/3/18.
 */
public class AutoWidgetView extends RelativeLayout {
    //开始导航
    private static int GUIDE_START = 8;
    //结束导航
    private static int GUIDE_STOP = 9;
    //开始模拟导航
    private static int SIMULATION_START = 10;
    //结束模拟导航
    private static int SIMULATION_STOP = 12;
    //导航应用启动
    private static int AUTO_START = 1;
    //导航应用退出
    private static int AUTO_FINISH = 2;

    private int mExtraState;

    private Context mContext;

    //巡航地图图片
    private TextureView mMapImageView;
    //导航信息界面
    private RelativeLayout mAutoWidgetTurnInfoContainer;

    private TextView mAutoWidgetNextRoadDistanceTv;

    private TextView mAutoWidgetDistanceAfter;

    private TextView mAutoWidgetNextRoadNameTv;

    private TextView mAutoWidgetCurrentRoadNameTv;
    private ImageView mAutoWidgetTurnInfoIv;
    private ImageView mTmcBarView;
    private LinearLayout mNaviInfoLayout;
    private LinearLayout mNaviNoGpsLayout;
    private View mWidgetBackground;

    public static String sNextRoadName;
    public static String sCurRoadName;
    public static int sIcon;
    public static int sRoundAboutNum;
    private boolean mIsInRealNavi;

    public AutoWidgetView(Context context) {
        super(context);
    }

    public AutoWidgetView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Log.d(TAG, "AutoWidgetView create!");
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.auto_widget_default_layout_changan, this);
        mAutoWidgetTurnInfoContainer = (RelativeLayout)findViewById(R.id.auto_widget_turn_info_container);
        mAutoWidgetNextRoadDistanceTv = (TextView)findViewById(R.id.auto_widget_next_road_distance_tv);
        mAutoWidgetDistanceAfter = (TextView)findViewById(R.id.auto_widget_distance_after);
        mAutoWidgetNextRoadNameTv = (TextView)findViewById(R.id.auto_widget_next_road_name_tv);
        mAutoWidgetCurrentRoadNameTv = (TextView)findViewById(R.id.auto_widget_current_road_name_tv);
        mAutoWidgetTurnInfoIv = (ImageView)findViewById(R.id.auto_widget_turn_info_iv);
        mNaviInfoLayout = (LinearLayout)findViewById(R.id.navi_info_layout);
        mNaviNoGpsLayout = (LinearLayout)findViewById(R.id.navi_no_gps_layout);
        mMapImageView = (TextureView) findViewById(R.id.widget_image);
        mTmcBarView = (ImageView)findViewById(R.id.tmc_bar_view);
        mWidgetBackground = findViewById(R.id.widget_background);
        initLaneItems();
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG ,"onAttachedToWindow");
        super.onAttachedToWindow();
        registerBroadcast();
        bindTextureService();
    }


    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
        unRegisterBroadcast();
        if(mContext != null){
            mContext.unbindService(mServiceConnection);
        }
    }


    private AutoMsgBroadcaseReceiver mAutoMsgBroadcaseReceiver = new AutoMsgBroadcaseReceiver();

    private void registerBroadcast(){
        IntentFilter filter= new IntentFilter();
        filter.addAction("AUTONAVI_STANDARD_BROADCAST_SEND");
        filter.addAction("com.autonavi.amapauto.NAVI_GPS_ENABLED_ACTION");
        filter.addAction("action_auto_widget_lane_state");
        filter.addAction("action_auto_widget_tmc_bar_state");
        mContext.registerReceiver(mAutoMsgBroadcaseReceiver , filter);
    }

    private void unRegisterBroadcast(){
        mContext.unregisterReceiver(mAutoMsgBroadcaseReceiver);
    }

    class AutoMsgBroadcaseReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "接收到广播 action " + action);
            if("AUTONAVI_STANDARD_BROADCAST_SEND".equals(action)){
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    int keyType = bundle.getInt("KEY_TYPE");
                    if(keyType == 10001){
                        mAutoWidgetTurnInfoContainer.setVisibility(View.VISIBLE);
                        //导航状态信息
                        String nextRoadName = bundle.getString("NEXT_ROAD_NAME");
                        int segRemainDis = bundle.getInt("SEG_REMAIN_DIS");
                        int routeRemainDis = bundle.getInt("ROUTE_REMAIN_DIS");
                        int routeRemainTime = bundle.getInt("ROUTE_REMAIN_TIME");
                        int icon = bundle.getInt("ICON");
                        int roundAboutNum = bundle.getInt("ROUNG_ABOUT_NUM");
                        Log.d(TAG, "mNextRoadName " + nextRoadName
                                + " mRouteRemainTime: " + routeRemainTime
                                + " mRouteRemainDis: " + routeRemainDis
                                + " mSegRemainDis: " + segRemainDis);
                        String[] mDistanceStrArray = AutoMsgUtil.getLengDesc2(segRemainDis);

                        if (mDistanceStrArray != null || mDistanceStrArray.length > 1) {
                            mAutoWidgetNextRoadDistanceTv.setVisibility(View.VISIBLE);
                            mAutoWidgetNextRoadDistanceTv.setText(mDistanceStrArray[0]);
                            mAutoWidgetDistanceAfter.setText(mDistanceStrArray[1] + "后");
                        }

                        //下一条道路名
                        sNextRoadName = nextRoadName;
                        mAutoWidgetNextRoadNameTv.setText(nextRoadName);
                        sCurRoadName = bundle.getString("CUR_ROAD_NAME", "");
                        mAutoWidgetCurrentRoadNameTv.setText(sCurRoadName);
                        //转向信息图
                        sIcon = icon;
                        sRoundAboutNum = roundAboutNum;
                        Bitmap turnBitmap = AutoMsgUtil.setRoadSignToImage(mContext,icon,roundAboutNum , false,true);
                        mAutoWidgetTurnInfoIv.setImageBitmap(turnBitmap);

                    }else if(keyType == 10019){
                        mExtraState = bundle.getInt("EXTRA_STATE");
                        if(mExtraState == GUIDE_START || mExtraState == SIMULATION_START){
                            Log.d(TAG, "AutoWidgetView realNavi start");
                            //导航开始
                            mIsInRealNavi = true;
                            mAutoWidgetTurnInfoContainer.setVisibility(View.VISIBLE);
                        }
                        else if(mExtraState == SIMULATION_START){
                            Log.d(TAG, "AutoWidgetView simulationNavi start");
                            //模拟导航开始
                            mAutoWidgetTurnInfoContainer.setVisibility(View.VISIBLE);
                        }
                        else if(mExtraState == GUIDE_STOP ){
                            Log.d(TAG, "AutoWidgetView realNavi stop");
                            //导航结束
                            mIsInRealNavi = false;
                            mAutoWidgetTurnInfoContainer.setVisibility(View.GONE);
                        }
                        else if(mExtraState == SIMULATION_STOP){
                            //模拟导航结束
                            Log.d(TAG, "AutoWidgetView simulationNavi stop");
                            mAutoWidgetTurnInfoContainer.setVisibility(View.GONE);
                        }
                        else if(mExtraState == AUTO_START){
                            Log.d(TAG, "AutoWidgetView auto create");
                            mWidgetBackground.setVisibility(GONE);
                            //导航应用启动
                            mAutoWidgetTurnInfoContainer.setVisibility(View.GONE);
                        }else if(mExtraState == AUTO_FINISH){
                            Log.d(TAG, "AutoWidgetView auto finish");
                            mWidgetBackground.setVisibility(VISIBLE);
                            //导航应用关闭
                        }
                    }else if(keyType == 13012){
                        //车道线透出
                        String laneJsonStr = intent.getStringExtra("EXTRA_DRIVE_WAY");
                        DriveWayMsgHandler.getInstance().toLaneBean(laneJsonStr);
                        if(DriveWayMsgHandler.getInstance().mDriveWayEnabled){
                            if(DriveWayMsgHandler.getInstance().mLaneItems != null && DriveWayMsgHandler.getInstance().mLaneItems.length > 0){
                                setLaneItemsVisivale();
                            }
                        }else{
                            setLaneItemsInVisivale();
                        }
                    }
                }
            }
            //路况柱状图透出
            else if("action_auto_widget_tmc_bar_state".equals(action)) {
                Log.d(TAG, "路况柱状图");
                String tmcJsonStr = intent.getStringExtra("EXTRA_TMC_SEGMENT");
                TmcMsgHandler.getInstance().toTmcBean(tmcJsonStr);
                if(TmcMsgHandler.getInstance().mTmcSegmentEnabled){
                    mTmcBarView.setVisibility(View.VISIBLE);
                    setTmcImageView();
                }else{
                    mTmcBarView.setVisibility(View.GONE);
                }
            }
            //GPS定位
            else if("com.autonavi.amapauto.NAVI_GPS_ENABLED_ACTION".equals(action)) {
                boolean isGpsEnabled = intent.getBooleanExtra("isGpsEnabled", true);
                Log.d(TAG, "AutoWidgetProvider onReceive NAVI_GPS_ENABLED_ACTION isGpsEnabled " + isGpsEnabled);
                if(mIsInRealNavi){
                    if(isGpsEnabled){
                        mNaviInfoLayout.setVisibility(View.VISIBLE);
                        mNaviNoGpsLayout.setVisibility(View.GONE);
                    }else {
                        mNaviInfoLayout.setVisibility(View.GONE);
                        mNaviNoGpsLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private final static String TAG = "autoviewwidget";

    private IAutoSimilarWidgetViewService mService;

    Surface mSurface;

    private ServiceConnection mServiceConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            mService = IAutoSimilarWidgetViewService.Stub.asInterface(arg1);
            Log.e(TAG, " onServiceConnected");
            if(mSurface != null){
                try {
                    mService.addSurface(mSurface, 1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
        }

    };

    private void bindTextureService() {
        mContext.bindService(new Intent("com.similar.widget.view.service"), mServiceConnection, 0);
        mMapImageView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
                // TODO Auto-generated method stub
                Log.e(TAG, " onSurfaceTextureUpdated");
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
                                                    int arg2) {
                Log.e(TAG, " onSurfaceTextureSizeChanged");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0z) {
                Log.e(TAG, " onSurfaceTextureDestroyed");
                if(mService == null || mSurface == null) return false;
                try {
                    mService.removedSurface(mSurface, 1);
                    mSurface = null;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
                                                  int arg2) {
                Log.e(TAG, " onSurfaceTextureAvailable");
                if(mService == null) return;
                if(mSurface == null) mSurface = new Surface(arg0);
                try {
                    mService.addSurface(mSurface, 1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                // TODO Auto-generated method stub

            }
        });

    }

    private Bitmap mTmcBitmap;

    private void setTmcImageView(){
        Log.d(TAG, "setTmcImageView");
        mTmcBitmap = TmcMsgHandler.getInstance().loadBitmapFromView();
        mTmcBarView.setImageBitmap(mTmcBitmap);
    }


    //-------------------------------------车道线----------------------------------------------------------------

    private LinearLayout mLaneLineLayout;
    private FrameLayout mLaneItem0;
    private FrameLayout mLaneItem1;
    private FrameLayout mLaneItem2;
    private FrameLayout mLaneItem3;
    private FrameLayout mLaneItem4;
    private FrameLayout mLaneItem5;
    private FrameLayout mLaneItem6;
    private FrameLayout mLaneItem7;

    private ImageView mLaneItemImage0;
    private ImageView mLaneItemImage1;
    private ImageView mLaneItemImage2;
    private ImageView mLaneItemImage3;
    private ImageView mLaneItemImage4;
    private ImageView mLaneItemImage5;
    private ImageView mLaneItemImage6;
    private ImageView mLaneItemImage7;

    private void initLaneItems(){
        mLaneLineLayout = (LinearLayout)findViewById(R.id.lane_line_layout);
        mLaneItem0 = (FrameLayout)findViewById(R.id.lane_item_0);
        mLaneItem1 = (FrameLayout)findViewById(R.id.lane_item_1);
        mLaneItem2 = (FrameLayout)findViewById(R.id.lane_item_2);
        mLaneItem3 = (FrameLayout)findViewById(R.id.lane_item_3);
        mLaneItem4 = (FrameLayout)findViewById(R.id.lane_item_4);
        mLaneItem5 = (FrameLayout)findViewById(R.id.lane_item_5);
        mLaneItem6 = (FrameLayout)findViewById(R.id.lane_item_6);
        mLaneItem7 = (FrameLayout)findViewById(R.id.lane_item_7);

        mLaneItemImage0 = (ImageView)findViewById(R.id.lane_item_image_0);
        mLaneItemImage1 = (ImageView)findViewById(R.id.lane_item_image_1);
        mLaneItemImage2 = (ImageView)findViewById(R.id.lane_item_image_2);
        mLaneItemImage3 = (ImageView)findViewById(R.id.lane_item_image_3);
        mLaneItemImage4 = (ImageView)findViewById(R.id.lane_item_image_4);
        mLaneItemImage5 = (ImageView)findViewById(R.id.lane_item_image_5);
        mLaneItemImage6 = (ImageView)findViewById(R.id.lane_item_image_6);
        mLaneItemImage7 = (ImageView)findViewById(R.id.lane_item_image_7);

    }


    private void setLaneItemsVisivale(){
        int count = DriveWayMsgHandler.getInstance().mLaneItems.length;
        if(count > 0){
            mLaneLineLayout.setVisibility(View.VISIBLE);
            mLaneItem0.setVisibility(VISIBLE);
            Bitmap bmp = AutoMsgUtil.scaleBitmap(mContext, DriveWayMsgHandler.getInstance().getLanePicResId(DriveWayMsgHandler.getInstance().mLaneItems[0].driveWayLaneBackIcon));
            mLaneItemImage0.setImageBitmap(bmp);
        }
        if(count > 1){
            mLaneItem1.setVisibility(View.VISIBLE);
            Bitmap bmp = AutoMsgUtil.scaleBitmap(mContext, DriveWayMsgHandler.getInstance().getLanePicResId(DriveWayMsgHandler.getInstance().mLaneItems[1].driveWayLaneBackIcon));
            mLaneItemImage1.setImageBitmap(bmp);
        }
        if(count > 2){
            mLaneItem2.setVisibility(View.VISIBLE);
            Bitmap bmp = AutoMsgUtil.scaleBitmap(mContext, DriveWayMsgHandler.getInstance().getLanePicResId(DriveWayMsgHandler.getInstance().mLaneItems[2].driveWayLaneBackIcon));
            mLaneItemImage2.setImageBitmap(bmp);
        }
        if(count > 3){
            mLaneItem3.setVisibility(View.VISIBLE);
            Bitmap bmp = AutoMsgUtil.scaleBitmap(mContext, DriveWayMsgHandler.getInstance().getLanePicResId(DriveWayMsgHandler.getInstance().mLaneItems[3].driveWayLaneBackIcon));
            mLaneItemImage3.setImageBitmap(bmp);
        }
        if(count > 4){
            mLaneItem4.setVisibility(View.VISIBLE);
            Bitmap bmp = AutoMsgUtil.scaleBitmap(mContext, DriveWayMsgHandler.getInstance().getLanePicResId(DriveWayMsgHandler.getInstance().mLaneItems[4].driveWayLaneBackIcon));
            mLaneItemImage4.setImageBitmap(bmp);
        }
        if(count > 5){
            mLaneItem5.setVisibility(View.VISIBLE);
            Bitmap bmp = AutoMsgUtil.scaleBitmap(mContext, DriveWayMsgHandler.getInstance().getLanePicResId(DriveWayMsgHandler.getInstance().mLaneItems[5].driveWayLaneBackIcon));
            mLaneItemImage5.setImageBitmap(bmp);
        }
        if(count > 6){
            mLaneItem6.setVisibility(View.VISIBLE);
            Bitmap bmp = AutoMsgUtil.scaleBitmap(mContext, DriveWayMsgHandler.getInstance().getLanePicResId(DriveWayMsgHandler.getInstance().mLaneItems[6].driveWayLaneBackIcon));
            mLaneItemImage6.setImageBitmap(bmp);
        }
        if(count > 7){
            mLaneItem7.setVisibility(View.VISIBLE);
            Bitmap bmp = AutoMsgUtil.scaleBitmap(mContext, DriveWayMsgHandler.getInstance().getLanePicResId(DriveWayMsgHandler.getInstance().mLaneItems[7].driveWayLaneBackIcon));
            mLaneItemImage7.setImageBitmap(bmp);
        }
        if(count < 1 || count > 8){
            setLaneItemsInVisivale();
        }
    }

    private void setLaneItemsInVisivale(){
        mLaneLineLayout.setVisibility(GONE);
        mLaneItem0.setVisibility(GONE);
        mLaneItem1.setVisibility(GONE);
        mLaneItem2.setVisibility(GONE);
        mLaneItem3.setVisibility(GONE);
        mLaneItem4.setVisibility(GONE);
        mLaneItem5.setVisibility(GONE);
        mLaneItem6.setVisibility(GONE);
        mLaneItem7.setVisibility(GONE);
    }
}
