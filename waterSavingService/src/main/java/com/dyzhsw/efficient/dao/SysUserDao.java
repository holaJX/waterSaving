package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysUserDao {

    String getCurrOfficeId(String currUserId);

    SysUser findUserByLoginName(String loginName);

    SysUser selectById(String id);

    void changeLoginDate(String id);

    int addUser(SysUser sysUser);

    int deletedUser(@Param("id") String id, @Param("currUserId") String currUserId);

    List<SysUser> getUserListByOfficeId(@Param("officeIds") List<String> officeIds, @Param("loginName") String loginName);

    int changePwd(Map map);
    
    SysUser findInfoById(String id);

    int updateSysUserInfo(SysUser sysUser);
    
    List<SysUser> getListSysUserInfo(SysUser sysUser);

    void updateOfficeId(@Param("officeId") String officeId, @Param("currUserId") String currUserId);

	SysUser selectByNameImport(String name);

	int insertUserImport(SysUser sysUser);
}
