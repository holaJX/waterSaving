package com.dyzhsw.efficient.service;

import com.dyzhsw.efficient.dto.SysAreaDTO;
import com.dyzhsw.efficient.entity.*;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


/**
 * 系统设置相关service（user、Office、role、menu、area、dict）
 * create by LiHD
 */
public interface SysService {


    BaseResponse login(String loginName, String password);

    BaseResponse loginOut(String token);

    SysUser selectById(String id);

    String getCurrOfficeId(String currUserId);

    int changePwd(Map map);

    int addOffice(SysOffice sysOffice);

    void addRoleOffice(SysRoleOffice sysRoleOffice);

    int addUser(SysUser sysUser);

    void addUserRole(SysUserRole sysUserRole);

    int addRole(SysRole sysRole);

    void addRoleMenu(SysRoleMenu sysRoleMenu);

    Boolean deletedRoleMenu(SysRoleMenu sysRoleMenu);

    int addMenu(SysMenu sysMenu);

    void deletedUser(String userIds, String currUserId);

    void deletedMenu(String menuIds, String currUserId);

    void deletedRole(String roleIds, String currUserId);

    void deletedOffice(String officeIds, String currUserId);

    String selectRoleIdByUserId(String userId);

    SysRole selectByUserId(String userId);

    PageInfo<SysUser> getUserListByOfficeId(Integer pageNo, Integer pageSize, String officeId, String loginName, String isSys);

    List<SysOffice> getOfficeListByCurrUser(String officeId, String name, String type);

    PageInfo<SysRole> getRoleListByOfficeId(Integer pageNo, Integer pageSize, String name, String officeId, String isSys, String conditionOfficeId);

    SysUser findInfoById(String id);
    
    SysOffice getOfficeInfoById(String id);

    SysRole getSysRoleInfoById(String id);

    List<SysUser> selectUserListByRoleId(String roleId);

    List<SysOffice> getAllOfficeTree();

    SysOffice getCurrOfficeTree(String officeId);

    List<SysOffice> getCurrOfficeTreeByType(String officeId, String type);

    List<SysMenu> getAllMenuTree();

    List<String> getCurrMenuTree(String currUserId);

    int updateSysUserInfo(SysUser sysUser);

    int updateSysOfficeInfo(SysOffice sysOffice);

    int updateSysMenuInfo(SysMenu sysMenu);

    void updateSysRoleInfo(SysRole sysRole);

    void updateRoleOfficeByRoleId(SysRoleOffice roleOffice);

    SysRoleMenu selectByRoleMenu(SysRoleMenu sysRoleMenu);

    void updateRoleMenuByRoleId(SysRoleMenu sysRoleMenu);

    void updateUserRole(SysUserRole sysUserRole);

    List<SysDict> selectAllOfficeTypeList();

    List<SysDict> selectCurrOfficeTypeList(String officeId);

    List<SysAreaDTO> getAreas(Map map);

    List<SysDict> getEquWarnTypeListByEquType(Integer equType);
    
    List<SysUser> getListSysUserInfo(SysUser sysUser);
    
    List<SysMenu> selectBySysMenu(SysMenu sysMenu);
    
    List<SysRole> selectBySysRole(SysRole sysRole);

    SysMenu getSysMenuInfo(String id);

    List<SysMenu> getSysMenuListByUserId(String currUserId, String name, String isShow);

    List<MenuRoute> getMenu(String currUserId);

    List<SysDict> getEquTypeList();

	SysUser selectByNameImport(String name);

	int insertUserImport(SysUser sysUser);

    String selectUserRoleByUserId(String userId);

    List<String> getHiddenMenu();

    Boolean checkPath(String roleId, String path);

    List<String> getIsSysRoleId();

}
