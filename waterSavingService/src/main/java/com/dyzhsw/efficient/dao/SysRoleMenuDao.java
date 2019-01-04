package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuDao {

    void addRoleMenu(SysRoleMenu sysRoleMenu);

    void deletedByMenuId(String menuId);

    int deletedByRoleId(String roleId);

    void deletedByRoleMenu(SysRoleMenu sysRoleMenu);

    List<String> selectMenuIdByRoleId(String roleId);

    SysRoleMenu selectByRoleMenu(SysRoleMenu sysRoleMenu);

    void updateRoleMenuByRoleId(SysRoleMenu sysRoleMenu);


}
