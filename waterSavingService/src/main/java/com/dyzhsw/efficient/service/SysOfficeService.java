package com.dyzhsw.efficient.service;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.ControlLog;
import com.dyzhsw.efficient.entity.SysOffice;

/**
 * 区域相关service
 * create by yr
 */
public interface SysOfficeService {


	
	List<SysOffice> selectByOffice(SysOffice sysOffice);

	SysOffice selectByNameImport(String name);
	
	SysOffice selectById(String id);

	List<SysOffice> selectShouBuByOfficeId(String officeId);

	SysOffice selectByOfficeIdImport(String officeParentId, String name);
}                               