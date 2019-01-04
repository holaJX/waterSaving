package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.WmSensormonitor;

import java.util.List;

public interface WmSensormonitorDao {

    List<WmSensormonitor> selectGraphByEquNo(String equNo);

    List<WmSensormonitor> selectByEquNo(String equNo);
}
