package com.dyzhsw.efficient.service;

import com.dyzhsw.efficient.entity.GsmMeterInfo;

public interface GsmMeterInfoService {

    void addGsmMeterInfo(GsmMeterInfo gsmMeterInfo);

    GsmMeterInfo selectByEquNo(String equNo);
}
