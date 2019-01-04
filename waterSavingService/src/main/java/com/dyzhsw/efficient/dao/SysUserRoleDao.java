package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleDao {


    void addUserRole(SysUserRole sysUserRole);

    void deletedByUserId(String userId);

    void deletedByRoleId(String roleId);

    String selectRoleIdByUserId(String userId);

    SysUserRole selectByUserRole(String userId);

    void updateRoleIdByUserId(SysUserRole sysUserRole);

    List<String> selectUserIdByRoleId(String roleId);

}
