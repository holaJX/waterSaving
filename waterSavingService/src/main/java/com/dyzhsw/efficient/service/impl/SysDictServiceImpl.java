package com.dyzhsw.efficient.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.dao.SysDictDao;
import com.dyzhsw.efficient.entity.SysDict;
import com.dyzhsw.efficient.service.SysDictService;
import com.dyzhsw.efficient.utils.ListUtils;

@Service
public class SysDictServiceImpl implements SysDictService {

	@Autowired
	private SysDictDao sysDictDao;
	
	
	private static List<SysDict> dictionaries = null;
	/* (non-Javadoc)
	 * @see com.billing.manager.service.impl.SysDictionaryService#getList()
	 */

	
	@Override
	public  Map<String, String> getMap(String type) {
		if(!ListUtils.isNotEmpty(dictionaries)) {
			dictionaries = sysDictDao.selectAll();
		}
		Map<String, String> map = null;
		if(ListUtils.isNotEmpty(dictionaries)) {
			map = new HashMap<>();
			for(SysDict item:dictionaries) {
				if(!StringUtils.isEmpty(item.getType())&&type.equals(item.getType())) {
					map.put(item.getLabel(), item.getValue());
				}
			}
		}
		return map;
	}


	@Override
	public SysDict getEquInfoExport(String value) {
		return sysDictDao.getEquInfoExport(value);
	}

	@Override
	public SysDict getWarnInfoExport(String value) {
		return sysDictDao.getWarnInfoExport(value);
	}


	@Override
	public SysDict getTaskInfoExport(String value) {
		return sysDictDao.getTaskInfoExport(value);
	}


	@Override
	public SysDict getSysOfficeExport(String value) {
		return sysDictDao.getSysOfficeExport(value);
	}


	@Override
	public SysDict getSfExport(String value) {
		return sysDictDao.getSfExport(value);
	}


	@Override
	public SysDict getLlExport(String value) {
		return sysDictDao.getLlExport(value);
	}


	@Override
	public SysDict getYlExport(String value) {
		return sysDictDao.getYlExport(value);
	}
	
}
