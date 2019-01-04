package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.FilterInfo;

public interface FilterInfoDao {

    void addInfo(FilterInfo filterInfo);

    FilterInfo selectById(String id);

	int deleteById(String[] id);
}
