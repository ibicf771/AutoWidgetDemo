package com.auto.autosimilarwidgetview.bean;

/**
 * Created by bo.sun on 2015-3-23.
 */
public enum TmcColor {
    /**
     * 没有路况状态 灰色：#959899
     */
    NOTRAFFIC(0x95,0x98,0x99),

    /**
     * 未知状态 蓝色：#14a7dd，2.0改为#2086f6
     */
    UNKNOWN(0x20,0x86,0xf6),

    /**
     * 未知状态（黑夜）：1c65b5
     */
    UNKNOWN_NIGHT(0x1c,0x65,0xb5),

    /**
     * 畅通 绿色：#0ecc76，2.0改为#21af1a
     */
    UNBLOCK(0x21,0xaf,0x1a),

    /**
     * 畅通（黑夜）：499e28
     */
    UNBLOCK_NIGHT(0x49,0x9e,0x28),

    /**
     * 缓行 黄色：#fee504，2.0改为#e3c01b
     */
    SLOW(0xe3,0xc0,0x1b),

    /**
     * 缓行（黑夜）：c2af03
     */
    SLOW_NIGHT(0xc2,0xaf,0x03),

    /**
     * 阻塞 红色：#d12825，2.0改为#ee4545
     */
    BLOCK(0xee,0x45, 0x45),

    /**
     * 阻塞（黑夜）：cd4949
     */
    BLOCK_NIGHT(0xcd,0x49, 0x49),

    /**
     * 严重阻塞 深红：#9c2429，2.0改为#bc2e2e
     */
    GRIDLOCKED (0xa8,0x28,0x28),

    /**
     * 严重阻塞（黑夜）：922525
     */
    GRIDLOCKED_NIGHT (0x92,0x25,0x25);


    //构造枚举值，比如RED（255，0，0）
    private TmcColor(int rv,int gv,int bv){
        this.redValue=rv;
        this.greenValue=gv;
        this.blueValue=bv;
    }
    public String toString(){ //覆盖了父类Enum的toString（）
        return super.toString()+"("+redValue+"，"+greenValue+"，"+blueValue+")";
    }

    public int R(){
        return redValue;
    }
    public int G(){
        return greenValue;
    }
    public int B(){
        return blueValue;
    }

    private int redValue; //自定义数据域，private为了封装。
    private int greenValue;
    private int blueValue;
}
