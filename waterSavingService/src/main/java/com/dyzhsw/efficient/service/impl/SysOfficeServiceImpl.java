package com.dyzhsw.efficient.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.dao.SysDictDao;
import com.dyzhsw.efficient.dao.SysOfficeDao;
import com.dyzhsw.efficient.entity.SysDict;
import com.dyzhsw.efficient.entity.SysOffice;
import com.dyzhsw.efficient.service.SysDictService;
import com.dyzhsw.efficient.service.SysOfficeService;
import com.dyzhsw.efficient.utils.ListUtils;

@Service
public class SysOfficeServiceImpl implements SysOfficeService {

	@Autowired
	private SysOfficeDao sysOfficeDao;

	@Override
	public List<SysOffice> selectByOffice(SysOffice sysOffice) {
		return sysOfficeDao.selectByOffice(sysOffice);
	}

	@Override
	public SysOffice selectByNameImport(String name) {
		return sysOfficeDao.selectByNameImport(name);
	}

	@Override
	public SysOffice selectById(String id) {
		return sysOfficeDao.selectById(id);
	}

	@Override
	public List<SysOffice> selectShouBuByOfficeId(String officeId) {
		return sysOfficeDao.selectShouBuByOfficeId(officeId);
	}

	@Override
	public SysOffice selectByOfficeIdImport(String officeParentId, String name) {
		return sysOfficeDao.selectByOfficeIdImport(officeParentId, name);
	}

}
