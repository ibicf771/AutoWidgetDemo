package com.auto.autosimilarwidgetview.function;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.auto.autosimilarwidgetview.bean.TmcBarItem;
import com.auto.autosimilarwidgetview.bean.TmcColor;
import com.auto.autosimilarwidgetview.bean.TmcItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by simin.yang on 2017/6/20.
 */
public class TmcMsgHandler {
    private final static String TAG = "autowidgetview";

    private static TmcMsgHandler instance = new TmcMsgHandler();

    private TmcMsgHandler (){}

    public static TmcMsgHandler getInstance() {
        return instance;
    }

    private  int mTmcSegmentSize = 0;

    public  boolean mTmcSegmentEnabled = true;

    public TmcItem[] mTmcItems;

    public  void toTmcBean(String json){
        JSONObject jobj = null;
        try {
            jobj = new JSONObject(json);
        } catch (JSONException e) {
        }
        Log.d(TAG, "toTmcBean json= " + jobj.toString());
        mTmcSegmentSize = jobj.optInt("tmc_segment_size");
        mTmcSegmentEnabled = jobj.optBoolean("tmc_segment_enabled");
        String jsons = jobj.optString("tmc_info");
        mTmcItems = toTmcItems(jsons);
    }

    public  TmcItem[] toTmcItems(String json) {
        try {
            JSONArray jsonarray = new JSONArray(json);
            int len = jsonarray.length();
            TmcItem[] tmcItems = new TmcItem[len];
            for (int i = 0; i < len; i++) {
                JSONObject jobj = jsonarray.getJSONObject(i);
                tmcItems[i] = toTmcItem(jobj);
            }
            return tmcItems;
        } catch (JSONException e) {
        }
        return null;
    }

    public TmcItem toTmcItem(JSONObject jobj){
        TmcItem tmcItem = new TmcItem();
        tmcItem.tmcStatus = jobj.optString("tmc_status");
        tmcItem.tmcSegmentPercent = jobj.optString("tmc_segment_percent");
        return tmcItem;
    }


    /**
     * 数组顺序发转
     * @param Array
     * @return
     */
    private  TmcItem[] reverseArray(TmcItem[] Array) {
        TmcItem[] new_array = new TmcItem[Array.length];
        for (int i = 0; i < Array.length; i++) {
            // 反转后数组的第一个元素等于源数组的最后一个元素：
            new_array[i] = Array[Array.length - i - 1];
        }
        return new_array;
    }

    private Bitmap mTmcBitmap;

    private int getItemColor(String status){
        int tmcStatus = Integer.valueOf(status);
        return getColor(tmcStatus);
    }

    private boolean mIsNightMode = false;


    /**
     * 根据路况取对应的颜色值
     * @param status
     * @return
     */
    private int getColor(int status){

        switch(status){
            case TmcBarItem.UNKNOWNSTATUS: //0 未知状态 蓝色
                if (mIsNightMode) {
                    return Color.rgb(TmcColor.UNKNOWN_NIGHT.R(), TmcColor.UNKNOWN_NIGHT.G(), TmcColor.UNKNOWN_NIGHT.B());
                } else {
                    return Color.rgb(TmcColor.UNKNOWN.R(), TmcColor.UNKNOWN.G(), TmcColor.UNKNOWN.B());
                }
            case TmcBarItem.UNBLOCKSTATUS: //1 畅通 绿色
                if (mIsNightMode) {
                    return Color.rgb(TmcColor.UNBLOCK_NIGHT.R(), TmcColor.UNBLOCK_NIGHT.G(), TmcColor.UNBLOCK_NIGHT.B());
                } else {
                    return Color.rgb(TmcColor.UNBLOCK.R(), TmcColor.UNBLOCK.G(), TmcColor.UNBLOCK.B());
                }
            case TmcBarItem.SLOWSTATUS:  //2 缓行 黄色
                if (mIsNightMode) {
                    return Color.rgb(TmcColor.SLOW_NIGHT.R(), TmcColor.SLOW_NIGHT.G(), TmcColor.SLOW_NIGHT.B());
                } else {
                    return Color.rgb(TmcColor.SLOW.R(), TmcColor.SLOW.G(), TmcColor.SLOW.B());
                }
            case TmcBarItem.BLOCKSTATUS://3  阻塞 红色
                if (mIsNightMode) {
                    return Color.rgb(TmcColor.BLOCK.R(), TmcColor.BLOCK.G(), TmcColor.BLOCK.B());
                } else {
                    return Color.rgb(TmcColor.BLOCK.R(), TmcColor.BLOCK.G(), TmcColor.BLOCK.B());
                }
            case TmcBarItem.SUPBLOCKSTATUS://4 严重阻塞 深红
                if (mIsNightMode) {
                    return Color.rgb(TmcColor.GRIDLOCKED_NIGHT.R(), TmcColor.GRIDLOCKED_NIGHT.G(), TmcColor.GRIDLOCKED_NIGHT.B());
                } else {
                    return Color.rgb(TmcColor.GRIDLOCKED.R(), TmcColor.GRIDLOCKED.G(), TmcColor.GRIDLOCKED.B());
                }
            default://已经走过 灰色
                return Color.rgb(TmcColor.NOTRAFFIC.R(), TmcColor.NOTRAFFIC.G(), TmcColor.NOTRAFFIC.B());
        }
    }

    private final int TOTAL_LENGTH = 390;
    private final int TMC_WIDTH = 20;

    private int getItemLength(String precentStr){
        int precent = Integer.parseInt(precentStr);
        int length = TOTAL_LENGTH * precent / 100;
        return length;
    }

    private int lastItemLength = 0;
    Paint mPaint = new Paint();

    public  Bitmap loadBitmapFromView() {
        Bitmap bmp = Bitmap.createBitmap(TMC_WIDTH, TOTAL_LENGTH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.BLUE);
        lastItemLength = 0;
        if(mTmcItems != null && mTmcItems.length > 0){
            mTmcItems = reverseArray(mTmcItems);
            for(int i =0 ; i< mTmcItems.length; i++){
                int color = getItemColor(mTmcItems[i].tmcStatus);
                int length = getItemLength(mTmcItems[i].tmcSegmentPercent);
                mPaint.setColor(color);
                mPaint.setStyle(Paint.Style.FILL);
                c.drawRect(0, lastItemLength, TMC_WIDTH, lastItemLength + length, mPaint);
                lastItemLength = lastItemLength + length;
            }
        }
        return bmp;
    }

}
