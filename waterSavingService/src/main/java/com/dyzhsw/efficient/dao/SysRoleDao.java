package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleDao {


    int addRole(SysRole sysRole);

    int deletedRole(@Param("id") String id, @Param("currUserId") String currUserId);

    SysRole selectByUserId(String userId);

    List<SysRole> selectByOfficeId(@Param("name") String name, @Param("officeIdList") List<String> officeIdList);

    SysRole selectById(String id);

    void updateSysRoleInfo(SysRole sysRole);

    List<String> getIsSysRoleId();

    void updateOfficeId(@Param("officeId") String officeId, @Param("currUserId") String currUserId);
    //导出接口
    List<SysRole> selectBySysRole(SysRole sysRole);
}
