package com.dyzhsw.efficient.service;

import com.dyzhsw.efficient.entity.FilterInfo;

public interface FilterInfoService {

    void addInfo(FilterInfo filterInfo);

	int deleteById(String[] id);

	FilterInfo selectById(String id);
}
