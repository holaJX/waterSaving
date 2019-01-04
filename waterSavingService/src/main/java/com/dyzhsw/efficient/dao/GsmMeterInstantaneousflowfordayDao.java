package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.GsmMeterInstantaneousflowforday;

import java.util.List;

public interface GsmMeterInstantaneousflowfordayDao {


    List<GsmMeterInstantaneousflowforday> selectGraphByEquNo(String equNo);

    List<GsmMeterInstantaneousflowforday> selectByEquNo(String equNo);

}
