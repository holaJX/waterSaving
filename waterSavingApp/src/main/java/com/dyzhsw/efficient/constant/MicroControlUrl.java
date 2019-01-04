package com.dyzhsw.efficient.constant;

/**
 * @Author: pjx
 * @Date: 2018/12/12 11:36
 * @Version 1.0
 */

/**
 * 阀控器接口地址
 */
public interface MicroControlUrl {

    /**
     * 控制阀门开启与关闭
     */
    String control = "/MicroControl/valveControl/control";
    /**
     * 下发查询控制阀门状态和阀控器通讯质量电压接口
     */
    String readRealData = "/MicroControl/valveControl/readRealData";
}
