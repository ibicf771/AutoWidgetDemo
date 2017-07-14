package com.auto.autosimilarwidgetview.bean;

/**
 * Created by simin.yang on 2017/6/19.
 */
public class TmcBarItem {
    public int segmentIndex;
    public int linkIndex;
    public int status;
    public int length;
    public static final int NOTRAFFICSTATUS = -1;
    public static final int UNKNOWNSTATUS = 0;
    public static final int UNBLOCKSTATUS = 1;
    public static final int SLOWSTATUS = 2;
    public static final int BLOCKSTATUS = 3;
    public static final int SUPBLOCKSTATUS = 4;

    public TmcBarItem() {
    }
}
