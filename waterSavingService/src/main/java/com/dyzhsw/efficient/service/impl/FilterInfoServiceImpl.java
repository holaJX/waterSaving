package com.dyzhsw.efficient.service.impl;

import com.dyzhsw.efficient.dao.FilterInfoDao;
import com.dyzhsw.efficient.entity.FilterInfo;
import com.dyzhsw.efficient.service.FilterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterInfoServiceImpl implements FilterInfoService {


    @Autowired
    private FilterInfoDao filterInfoDao;


    @Override
    public void addInfo(FilterInfo filterInfo) {
    	filterInfoDao.addInfo(filterInfo);
    }
    

	@Override
	public FilterInfo selectById(String id) {
		return filterInfoDao.selectById(id);
	}
	
	@Override
	public int deleteById(String id[]) {
		return filterInfoDao.deleteById(id);
	}
}
