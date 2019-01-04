package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.SysRole;
import com.dyzhsw.efficient.entity.SysRoleOffice;

import java.util.List;

public interface SysRoleOfficeDao {

    void addRoleOffice(SysRoleOffice sysRoleOffice);

    void deletedByRoleId(String roleId);

    void deletedByOfficeId(String officeId);

    List<String> selectRoleIdByOfficeId(String officeId);

    void updateRoleOfficeByRoleId(SysRoleOffice sysRoleOffice);
}
