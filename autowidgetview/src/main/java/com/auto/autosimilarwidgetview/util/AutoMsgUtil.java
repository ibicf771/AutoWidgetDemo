package com.auto.autosimilarwidgetview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.auto.autosimilarwidgetview.R;

import java.lang.reflect.Field;

/**
 * Created by simin.yang on 2017/3/18.
 */
public class AutoMsgUtil {

    public   static  Bitmap scaleBitmap(Context context, int resId){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        Matrix matrix = new Matrix();
        matrix.postScale(0.6f, 0.6f);
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix,true);
        return newbm;
    }

    private static final float SCALE = 0.4f;

    public static Bitmap setRoadSignToImage(Context context, int maneuverId , int roundNum ,boolean isThreed,boolean isNight) {
        if (maneuverId == 0 || maneuverId == 1)
            return null;
        int rWidth = 85;
        int rHeight = rWidth;

        Bitmap roadSignBmp = null;
        if(roundNum>0){
            if(maneuverId == 12 || maneuverId == 11){
                if(isThreed || isNight) {
                    roundNum += 100;
                }
                roadSignBmp = BitmapFactory.decodeResource(context.getResources(), getDrawableID("sou", 49+roundNum));
                Matrix matrix = new Matrix();
                matrix.postScale(SCALE, SCALE);
                Bitmap newbm = Bitmap.createBitmap(roadSignBmp, 0, 0, roadSignBmp.getWidth(),
                        roadSignBmp.getHeight(), matrix,true);
                return newbm;
            }else if(maneuverId == 18 || maneuverId == 17){
                if(isThreed || isNight){
                    roundNum+=100;
                }
                roadSignBmp = BitmapFactory.decodeResource(context.getResources(), getDrawableID("sou", 59+roundNum));
                Matrix matrix = new Matrix();
                matrix.postScale(SCALE, SCALE);
                Bitmap newbm = Bitmap.createBitmap(roadSignBmp, 0, 0, roadSignBmp.getWidth(),
                        roadSignBmp.getHeight(), matrix,true);
                return newbm;
            }
        }

        if(isThreed || isNight){
            maneuverId+=100;
        }
        roadSignBmp = BitmapFactory.decodeResource(context.getResources(), getDrawableID("sou", maneuverId));
        Matrix matrix = new Matrix();
        matrix.postScale(SCALE, SCALE);
        Bitmap newbm = Bitmap.createBitmap(roadSignBmp, 0, 0, roadSignBmp.getWidth(),
                roadSignBmp.getHeight(), matrix,true);
        return newbm;
    }

    private static int getDrawableID(String path, int id) {
        Field f = null;
        int drawableId = 0;
        try {
            f = R.drawable.class.getDeclaredField(path + id); // FIXME A crash here! Caused by: java.lang.NoSuchFieldException: hud_sou0, on 2015-09-17
            drawableId = f.getInt(R.drawable.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawableId;
    }

    public static String[] getLengDesc2(int meter) {
        String[] tmp = new String[2];
        tmp[0] = "";
        tmp[1] = "";

        if (meter < 1000) {
            tmp[0] = meter + "";
            tmp[1] = "米";
            return tmp;
        }

        int kiloMeter = meter / 1000;
        int leftMeter = meter % 1000;
        leftMeter = leftMeter / 100;
        String rs = kiloMeter + "";
        if (leftMeter > 0) {
            tmp[0] = rs += "." + leftMeter;
            tmp[1] = "公里";
        } else {
            tmp[0] = rs;
            tmp[1] = "公里";
        }
        return tmp;
    }

}
