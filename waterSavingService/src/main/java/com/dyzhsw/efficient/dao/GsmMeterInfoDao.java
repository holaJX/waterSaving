package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.GsmMeterInfo;

public interface GsmMeterInfoDao {

    void addGsmMeterInfo(GsmMeterInfo gsmMeterInfo);

    GsmMeterInfo selectByEquipmentNo(String equipmentNo);
}
