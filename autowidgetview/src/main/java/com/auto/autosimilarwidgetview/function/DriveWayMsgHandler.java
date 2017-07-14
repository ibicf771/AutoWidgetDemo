package com.auto.autosimilarwidgetview.function;

import android.util.Log;
import android.util.SparseIntArray;

import com.auto.autosimilarwidgetview.R;
import com.auto.autosimilarwidgetview.bean.LaneItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by simin.yang on 2017/6/20.
 */
public class DriveWayMsgHandler {

    private final static String TAG = "autowidgetview";

    private static DriveWayMsgHandler instance = new DriveWayMsgHandler();

    private DriveWayMsgHandler (){}

    public static DriveWayMsgHandler getInstance() {
        return instance;
    }

    private  int mDriveWaySize = 0;

    public  boolean mDriveWayEnabled = true;

    public   LaneItem[] mLaneItems;

    public  void toLaneBean(String json){
        JSONObject jobj = null;
        try {
            jobj = new JSONObject(json);
        } catch (JSONException e) {
        }
        mDriveWaySize = jobj.optInt("drive_way_size");
        mDriveWayEnabled = jobj.optBoolean("drive_way_enabled");
        String jsons = jobj.optString("drive_way_info");
        Log.d(TAG, "jsons " + jsons.toString());
        mLaneItems = toLaneItems(jsons);
    }

    public  LaneItem[] toLaneItems(String json) {
        try {
            JSONArray jsonarray = new JSONArray(json);
            int len = jsonarray.length();
            LaneItem[] laneItem = new LaneItem[len];
            for (int i = 0; i < len; i++) {
                JSONObject jobj = jsonarray.getJSONObject(i);
                laneItem[i] = toLaneItem(jobj);
            }
            return laneItem;
        } catch (JSONException e) {
        }
        return null;
    }

    public LaneItem toLaneItem(JSONObject jobj){
        LaneItem laneItem = new LaneItem();
        laneItem.driveWayLaneBackIcon = jobj.optString("drive_way_lane_Back_icon");
        Log.d(TAG, "driveWayLaneBackIcon " +  laneItem.driveWayLaneBackIcon);
        return laneItem;
    }

    public int getLanePicResId(String iconId){
        Log.d(TAG, "iconId " +  iconId);
        int id = Integer.parseInt(iconId);
        return DRIVE_WAY_ICON_FORM.get(id);
    }

    private static SparseIntArray DRIVE_WAY_ICON_FORM = new SparseIntArray();

    static {
        DRIVE_WAY_ICON_FORM.append(0, R.drawable.landback_0);
        DRIVE_WAY_ICON_FORM.append(1,R.drawable.landback_1);
        DRIVE_WAY_ICON_FORM.append(2,R.drawable.landback_2);
        DRIVE_WAY_ICON_FORM.append(3,R.drawable.landback_3);
        DRIVE_WAY_ICON_FORM.append(4,R.drawable.landback_4);
        DRIVE_WAY_ICON_FORM.append(5,R.drawable.landback_5);
        DRIVE_WAY_ICON_FORM.append(6,R.drawable.landback_6);
        DRIVE_WAY_ICON_FORM.append(7,R.drawable.landback_7);
        DRIVE_WAY_ICON_FORM.append(8,R.drawable.landback_8);
        DRIVE_WAY_ICON_FORM.append(9,R.drawable.landback_9);
        DRIVE_WAY_ICON_FORM.append(10,R.drawable.landback_a);
        DRIVE_WAY_ICON_FORM.append(11,R.drawable.landback_b);
        DRIVE_WAY_ICON_FORM.append(12,R.drawable.landback_c);
        DRIVE_WAY_ICON_FORM.append(13,R.drawable.landback_d);
        DRIVE_WAY_ICON_FORM.append(14,R.drawable.landback_e);

        DRIVE_WAY_ICON_FORM.append(15,R.drawable.auto_landback_0);
        DRIVE_WAY_ICON_FORM.append(16,R.drawable.auto_landback_1);
        DRIVE_WAY_ICON_FORM.append(17,R.drawable.auto_landback_2);
        DRIVE_WAY_ICON_FORM.append(18,R.drawable.auto_landback_3);
        DRIVE_WAY_ICON_FORM.append(19,R.drawable.auto_landback_4);
        DRIVE_WAY_ICON_FORM.append(20,R.drawable.auto_landback_5);
        DRIVE_WAY_ICON_FORM.append(21,R.drawable.auto_landback_6);
        DRIVE_WAY_ICON_FORM.append(22,R.drawable.auto_landback_7);
        DRIVE_WAY_ICON_FORM.append(23,R.drawable.auto_landback_8);
        DRIVE_WAY_ICON_FORM.append(24,R.drawable.auto_landback_9);
        DRIVE_WAY_ICON_FORM.append(25,R.drawable.auto_landback_a);
        DRIVE_WAY_ICON_FORM.append(26,R.drawable.auto_landback_b);
        DRIVE_WAY_ICON_FORM.append(27,R.drawable.auto_landback_c);
        DRIVE_WAY_ICON_FORM.append(28,R.drawable.auto_landback_d);
        DRIVE_WAY_ICON_FORM.append(29,R.drawable.auto_landback_e);

        DRIVE_WAY_ICON_FORM.append(30,R.drawable.landfront_20);
        DRIVE_WAY_ICON_FORM.append(31,R.drawable.landfront_21);
        DRIVE_WAY_ICON_FORM.append(32,R.drawable.landfront_40);
        DRIVE_WAY_ICON_FORM.append(33,R.drawable.landfront_43);
        DRIVE_WAY_ICON_FORM.append(34,R.drawable.landfront_61);
        DRIVE_WAY_ICON_FORM.append(35,R.drawable.landfront_63);
        DRIVE_WAY_ICON_FORM.append(36,R.drawable.landfront_70);
        DRIVE_WAY_ICON_FORM.append(37,R.drawable.landfront_71);
        DRIVE_WAY_ICON_FORM.append(38,R.drawable.landfront_73);
        DRIVE_WAY_ICON_FORM.append(39,R.drawable.landfront_90);
        DRIVE_WAY_ICON_FORM.append(40,R.drawable.landfront_95);
        DRIVE_WAY_ICON_FORM.append(41,R.drawable.landfront_a0);
        DRIVE_WAY_ICON_FORM.append(42,R.drawable.landfront_a8);
        DRIVE_WAY_ICON_FORM.append(43,R.drawable.landfront_b1);
        DRIVE_WAY_ICON_FORM.append(44,R.drawable.landfront_b5);
        DRIVE_WAY_ICON_FORM.append(45,R.drawable.landfront_c3);
        DRIVE_WAY_ICON_FORM.append(46,R.drawable.landfront_c8);
        DRIVE_WAY_ICON_FORM.append(47,R.drawable.landfront_e1);
        DRIVE_WAY_ICON_FORM.append(48,R.drawable.landfront_e5);
    }

}
