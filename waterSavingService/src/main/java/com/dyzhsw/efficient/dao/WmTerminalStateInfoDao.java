package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.WmTerminalStateInfo;

public interface WmTerminalStateInfoDao {


    WmTerminalStateInfo selectByEquNo(String equNo);
}
