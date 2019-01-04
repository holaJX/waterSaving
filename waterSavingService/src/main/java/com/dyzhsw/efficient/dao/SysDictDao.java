package com.dyzhsw.efficient.dao;

import java.util.List;

import com.dyzhsw.efficient.entity.SysDict;


public interface SysDictDao {
	
    List<SysDict> selectAll();

    List<SysDict> selectAllOfficeTypeList();

    List<SysDict> selectCurrOfficeTypeList(String currOfficeType);

    List<SysDict> selectWarnTypeByType(String type);

    String selectTypeNameByEquType(String equTypeValue);

    List<SysDict> getEquTypeList();

    List<SysDict> getOfficeTypeList();
    
    SysDict getEquInfoExport(String value);
    
    SysDict getWarnInfoExport(String value);
    
    SysDict getTaskInfoExport(String value);
    
    SysDict getSysOfficeExport(String value);
    
    SysDict getSfExport(String value);
    
    SysDict getLlExport(String value);
    
    SysDict getYlExport(String value);

}
