package com.dyzhsw.efficient.entity;

import java.io.Serializable;

public class SysRoleMenu implements Serializable {

    private String roleId;

    private String menuId;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
