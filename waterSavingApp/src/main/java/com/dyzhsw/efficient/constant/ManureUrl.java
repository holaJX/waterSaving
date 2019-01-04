package com.dyzhsw.efficient.constant;

/**
 * @Author: pjx
 * @Date: 2018/12/12 11:36
 * @Version 1.0
 */

/**
 * 水肥机接口地址
 */
public interface ManureUrl {

    /**
     * 检查设备是否在线
     */
    String isOnline = "/online/service/isOnline";
    /**
     * 终端设备控制
     */
    String terminalControlReq = "/terminalControllService/terminalControlReq";
    /**
     * 远程手动控制
     */
    String remoteControlReq = "/remoteControllService/remoteControlReq";
    /**
     * 重启系统
     */
    String terminalRebootReq = "/rebootService/terminalRebootReq";


}
