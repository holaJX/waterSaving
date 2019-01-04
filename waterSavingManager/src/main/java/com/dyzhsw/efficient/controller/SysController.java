package com.dyzhsw.efficient.controller;


import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.config.redis.RedisUtil;
import com.dyzhsw.efficient.entity.*;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.service.impl.SysServiceImpl;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.IDUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys")
@Api(value = "系统相关接口")
public class SysController {


    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysService sysService;

    @ResponseBody
    @ApiOperation(value="添加机构", notes="机构管理添加新机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "机构名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "机构编码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "机构类型(1:公司 2:部门 3:片区)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "归属机构id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentIds", value = "所有归属机构id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "primaryPerson", value = "主负责人", required = false, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "联系电话", required = false, dataType = "String")
    })
    @RequestMapping(value = "/addOffice", method = RequestMethod.POST)
    public BaseResponse addOffice(SysOffice sysOffice, HttpServletRequest request) {

        if (StringUtils.isEmpty(sysOffice.getName()) || StringUtils.isEmpty(sysOffice.getType()) || StringUtils.isEmpty(sysOffice.getParentId()) || StringUtils.isEmpty(sysOffice.getParentIds())) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
        String token = authHeader.substring(11);
        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
        String currUserId = map.get("id")+"";

        sysOffice.setId(IDUtils.createUUID());
        sysOffice.setCreateBy(currUserId);
        sysOffice.setCreateTime(new Date());
        if (sysService.addOffice(sysOffice) > 0) {
            return BaseResponseUtil.success();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="添加用户", notes="用户管理添加新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "officeId", value = "归属机构的id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "no", value = "工号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "loginName", value = "登录名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机", required = false, dataType = "String"),
            @ApiImplicitParam(name = "loginFlag", value = "是否允许登陆(0:允许 1:不允许)", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/no", method = RequestMethod.POST)
    public BaseResponse addUser(SysUser sysUser, String roleId, HttpServletRequest request) {

        if (StringUtils.isEmpty(sysUser.getName()) || StringUtils.isEmpty(sysUser.getOfficeId()) || StringUtils.isEmpty(sysUser.getLoginName()) || StringUtils.isEmpty(sysUser.getPassword()) || StringUtils.isEmpty(sysUser.getRoleId())) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
        String token = authHeader.substring(11);
        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
        String currUserId = map.get("id")+"";

        sysUser.setId(IDUtils.createUUID());
        sysUser.setCreateBy(currUserId);
        sysUser.setCreateTime(new Date());
        if (sysService.addUser(sysUser) > 0) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(roleId);
            sysService.addUserRole(sysUserRole);
            return BaseResponseUtil.success();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="添加角色", notes="角色管理添加新角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "角色名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "officeId", value = "归属机构", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "dataScope", value = "数据范围", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "isSys", value = "是否系统数据", required = true, dataType = "String"),
            @ApiImplicitParam(name = "useable", value = "是否可用", required = false, dataType = "String"),
            @ApiImplicitParam(name = "menuIds", value = "权限菜单的ids", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public BaseResponse addRole(SysRole sysRole, String menuIds, HttpServletRequest request) {

        if (StringUtils.isEmpty(sysRole.getName()) || StringUtils.isEmpty(sysRole.getOfficeId())) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
        String token = authHeader.substring(11);
        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
        String currUserId = map.get("id")+"";

        sysRole.setId(IDUtils.createUUID());
        sysRole.setCreateBy(currUserId);
        sysRole.setCreateTime(new Date());
        if (sysService.addRole(sysRole) > 0) {
            SysRoleOffice sysRoleOffice = new SysRoleOffice();
            sysRoleOffice.setRoleId(sysRole.getId());
            sysRoleOffice.setOfficeId(sysRole.getOfficeId());
            sysService.addRoleOffice(sysRoleOffice);
            String[] menuIdAry = menuIds.split(",");
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(sysRole.getId());
            for (String menuId : menuIdAry) {
                sysRoleMenu.setMenuId(menuId);
                sysService.addRoleMenu(sysRoleMenu);
            }
            List<String> hiddenMenuList = sysService.getHiddenMenu();
            for (String hiddenMenuId : hiddenMenuList) {
                sysRoleMenu.setMenuId(hiddenMenuId);
                sysService.addRoleMenu(sysRoleMenu);
            }
            return BaseResponseUtil.success();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="添加菜单", notes="菜单管理添加新菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "上级菜单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentIds", value = "所有上级菜单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "href", value = "链接", required = false, dataType = "String"),
            @ApiImplicitParam(name = "target", value = "目标", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isShow", value = "状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    public BaseResponse addMenu(SysMenu sysMenu, HttpServletRequest request) {

        if (StringUtils.isEmpty(sysMenu.getName()) || StringUtils.isEmpty(sysMenu.getParentId()) || StringUtils.isEmpty(sysMenu.getParentIds())) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
        String token = authHeader.substring(11);
        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
        String currUserId = map.get("id")+"";

        sysMenu.setId(IDUtils.createUUID());
        sysMenu.setOperation("0");
        sysMenu.setPermission("1");
        sysMenu.setComponent("404");
        sysMenu.setCreateBy(currUserId);
        sysMenu.setCreateTime(new Date());
        if (sysMenu.getParentId().equals("0")) {
            sysMenu.setMeta("developing");
            sysMenu.setIcon("developing");
        }
        if (sysService.addMenu(sysMenu) > 0) {
            List<String> isSysRoleIdList = sysService.getIsSysRoleId();
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setMenuId(sysMenu.getId());
            for (String id : isSysRoleIdList) {
                roleMenu.setRoleId(id);
                sysService.addRoleMenu(roleMenu);
            }
            return BaseResponseUtil.success();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="删除用户、菜单、角色、机构", notes="系统管理删除的统一接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "逗号分隔的id字符串", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "删除内容的类型（1、用户 2、菜单 3、角色 4、机构）", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deletedSys", method = RequestMethod.POST)
    public BaseResponse deletedSys(String ids, String type, HttpServletRequest request) {

        if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(type)) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            if (type.equals("1")) {
                sysService.deletedUser(ids, currUserId);
            } else if (type.equals("2")) {
                sysService.deletedMenu(ids, currUserId);
            } else if (type.equals("3")) {
                sysService.deletedRole(ids, currUserId);
            } else if (type.equals("4")) {
                sysService.deletedOffice(ids, currUserId);
            } else {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "未知的内容");
            }
            return BaseResponseUtil.success();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取组织机构树", notes="获取组织机构树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isAll", value = "是否获取全部机构？(0:获取全部 1:获取当前用户所在机构下)", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getSysOfficeTree", method = RequestMethod.POST)
    public BaseResponse<List<SysOffice>> getSysOfficeTree(String isAll, HttpServletRequest request) {


        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String currOfficeId = map.get("officeId")+"";
            
            List<SysOffice> sysOfficeList = new ArrayList<>();
            if (isAll.equals("0")) {
                sysOfficeList = sysService.getAllOfficeTree();
            } else {
                SysOffice sysOffice = sysService.getCurrOfficeTree(currOfficeId);
                sysOfficeList.add(sysOffice);
            }
            return BaseResponseUtil.success(sysOfficeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取组织机构树(添加机构专用)", notes="根据机构类型获取上层机构树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "机构类型", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getSysOfficeTreeByType", method = RequestMethod.POST)
    public BaseResponse<List<SysOffice>> getSysOfficeTreeByType(String type, HttpServletRequest request) {


        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String currOfficeId = map.get("officeId")+"";

            List<SysOffice> sysOfficeList = sysService.getCurrOfficeTreeByType(currOfficeId, type);

            return BaseResponseUtil.success(sysOfficeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="获取菜单树", notes="获取菜单树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isAll", value = "是否获取全部菜单？(0:获取全部 1:获取当前用户权限可见)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "菜单名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isShow", value = "菜单状态（0:展示 1:不展示）", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getSysMenuTree", method = RequestMethod.POST)
    public BaseResponse<List<SysMenu>> getSysMenuTree(String isAll, String name, String isShow, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            if (isAll.equals("0")) {
                List<SysMenu> sysMenuList = sysService.getAllMenuTree();
                return BaseResponseUtil.success(sysMenuList);
            } else {
//                List<String> menuIds = sysService.getCurrMenuTree(currUserId);
                List<SysMenu> sysMenuList = sysService.getSysMenuListByUserId(currUserId, name, isShow);
                return BaseResponseUtil.success(sysMenuList);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="获取侧边栏菜单", notes="获取侧边栏菜单")
    @RequestMapping(value = "/getMenu", method = RequestMethod.POST)
    public BaseResponse<List<MenuRoute>> getMenu(HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            List<MenuRoute> sysMenuList = sysService.getMenu(currUserId);
            return BaseResponseUtil.success(sysMenuList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取用户列表", notes="获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "loginName", value = "登录名", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getSysUserList", method = RequestMethod.POST)
    public BaseResponse<PageInfo<SysUser>> getSysUserList(Integer pageNo, Integer pageSize, String loginName, HttpServletRequest request) {


        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";
            String isSys = map.get("isSys")+"";

            PageInfo<SysUser> pageInfo = new PageInfo<>();
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;

            pageInfo = sysService.getUserListByOfficeId(pageNo, pageSize, officeId, loginName, isSys);
            return BaseResponseUtil.success(pageInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="获取机构列表", notes="获取机构列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "机构名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "机构层级", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getSysOfficeList", method = RequestMethod.POST)
    public BaseResponse<List<SysOffice>> getSysOfficeList(String name, String type, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            List<SysOffice> sysOfficeList = sysService.getOfficeListByCurrUser(officeId, name, type);

            return BaseResponseUtil.success(sysOfficeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }






    @ResponseBody
    @ApiOperation(value="获取角色列表", notes="获取角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "角色名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "officeId", value = "机构id (传000表示查询当前用户所在机构的角色列表)", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getSysRoleList", method = RequestMethod.POST)
    public BaseResponse<PageInfo<SysRole>> getSysRoleList(Integer pageNo, Integer pageSize, String name, String officeId, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String currOfficeId = map.get("officeId")+"";
            String isSys = map.get("isSys")+"";

            PageInfo<SysRole> pageInfo = new PageInfo<>();
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;

            String conditionOfficeId = "";
            if (officeId.equals("000")) {
                officeId = currOfficeId;
            } else {
                conditionOfficeId = officeId;
            }

            pageInfo = sysService.getRoleListByOfficeId(pageNo, pageSize, name, officeId, isSys, conditionOfficeId);
            return BaseResponseUtil.success(pageInfo);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取用户详情", notes="根据用户id获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getSysUserInfo", method = RequestMethod.POST)
    public BaseResponse<SysUser> getSysUserInfo(String id, HttpServletRequest request) {
    	 try {
             String authHeader = request.getHeader("Authorization");
             if (authHeader == null) {
                 return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
             }
             String token = authHeader.substring(11);
             Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
             String currUserId = map.get("id")+"";
             String officeId = map.get("officeId")+"";
             
             SysUser sysUser = sysService.findInfoById(id);

             return BaseResponseUtil.success(sysUser);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

     }


    @ResponseBody
    @ApiOperation(value="修改用户信息", notes="根据用户id修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "officeId", value = "归属机构的id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "no", value = "工号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "loginName", value = "登录名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机", required = false, dataType = "String"),
            @ApiImplicitParam(name = "loginFlag", value = "是否允许登陆", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = false, dataType = "String")
    })
    @RequestMapping(value = "/updateSysUserInfo", method = RequestMethod.POST)
    public BaseResponse updateSysUserInfo(SysUser sysUser, String roleId, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            sysUser.setUpdateBy(currUserId);
            sysUser.setUpdateTime(new Date());

            int result = sysService.updateSysUserInfo(sysUser);
            if (result > 0 && !StringUtils.isEmpty(roleId)) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(roleId);
                sysService.updateUserRole(sysUserRole);
            }
            return BaseResponseUtil.success();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    
    /**
     * 机构详情
     * @param id
     * @return
     */
    @ResponseBody
    @ApiOperation(value="获取机构详情", notes="根据机构id获取机构详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "机构id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getSysOfficeInfo", method = RequestMethod.POST)
    public BaseResponse<SysOffice> getSysOfficeInfo(String id, HttpServletRequest request) {
    	try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            SysOffice sysOffice = sysService.getOfficeInfoById(id);

            return BaseResponseUtil.success(sysOffice);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="修改机构信息", notes="根据机构id修改机构信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "机构id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "机构名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "机构编码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "机构类型(1:公司 2:部门 3:片区)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "归属机构id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentIds", value = "所有归属机构id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "primaryPerson", value = "主负责人", required = false, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "联系电话", required = false, dataType = "String")
    })
    @RequestMapping(value = "/updateSysOfficeInfo", method = RequestMethod.POST)
    public BaseResponse updateSysOfficeInfo(SysOffice sysOffice, HttpServletRequest request) {
        try {

            if (StringUtils.isEmpty(sysOffice.getId())) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
            }

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            sysOffice.setUpdateBy(currUserId);
            sysOffice.setUpdateTime(new Date());

            if (sysService.updateSysOfficeInfo(sysOffice) > 0) {
                return BaseResponseUtil.success();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="获取菜单详情", notes="根据菜单id获取菜单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getSysMenuInfo", method = RequestMethod.POST)
    public BaseResponse<SysMenu> getSysMenuInfo(String id, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            SysMenu sysMenu = sysService.getSysMenuInfo(id);

            return BaseResponseUtil.success(sysMenu);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }




    @ResponseBody
    @ApiOperation(value="修改菜单信息", notes="根据菜单id修改菜单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "上级菜单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentIds", value = "所有上级菜单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "href", value = "链接", required = false, dataType = "String"),
            @ApiImplicitParam(name = "target", value = "目标", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "isShow", value = "状态", required = false, dataType = "String"),
    })
    @RequestMapping(value = "/updateSysMenuInfo", method = RequestMethod.POST)
    public BaseResponse updateSysMenuInfo(SysMenu sysMenu, HttpServletRequest request) {
        try {

            if (StringUtils.isEmpty(sysMenu.getId())) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
            }

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            sysMenu.setUpdateBy(currUserId);
            sysMenu.setUpdateTime(new Date());

            if (sysService.updateSysMenuInfo(sysMenu) > 0) {
                return BaseResponseUtil.success();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取角色详情", notes="根据角色id获取角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getSysRoleInfo", method = RequestMethod.POST)
    public BaseResponse<SysRole> getSysRoleInfo(String id, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            SysRole sysRole = sysService.getSysRoleInfoById(id);
            return BaseResponseUtil.success(sysRole);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取角色详情中的用户列表", notes="根据角色id获取角色详情中的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getRoleInfoUserList", method = RequestMethod.POST)
    public BaseResponse<List<SysUser>> getRoleInfoUserList(String roleId, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            List<SysUser> sysUserList = sysService.selectUserListByRoleId(roleId);
            return BaseResponseUtil.success(sysUserList);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="修改角色信息", notes="根据角色id修改角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "角色名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "officeId", value = "归属机构", required = false, dataType = "String"),
//            @ApiImplicitParam(name = "dataScope", value = "数据范围", required = false, dataType = "String"),
//            @ApiImplicitParam(name = "isSys", value = "是否系统数据", required = false, dataType = "String"),
            @ApiImplicitParam(name = "useable", value = "是否可用", required = false, dataType = "String"),
            @ApiImplicitParam(name = "menuIds", value = "权限菜单的ids", required = false, dataType = "String")
    })
    @RequestMapping(value = "/updateSysRoleInfo", method = RequestMethod.POST)
    public BaseResponse updateSysRoleInfo(SysRole sysRole, String menuIds, HttpServletRequest request) {
        try {

            if (StringUtils.isEmpty(sysRole.getId())) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
            }

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String officeId = map.get("officeId")+"";

            sysRole.setUpdateBy(currUserId);
            sysRole.setUpdateTime(new Date());

            sysService.updateSysRoleInfo(sysRole);
            SysRoleOffice sysRoleOffice = new SysRoleOffice();
            sysRoleOffice.setRoleId(sysRole.getId());
            sysRoleOffice.setOfficeId(sysRole.getOfficeId());
            sysService.updateRoleOfficeByRoleId(sysRoleOffice);
            if (menuIds != null) {
                String[] menuIdAry = menuIds.split(",");
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(sysRole.getId());
                if (sysService.deletedRoleMenu(sysRoleMenu)) {
                    for (String menuId : menuIdAry) {
                        sysRoleMenu.setMenuId(menuId);
                        sysService.addRoleMenu(sysRoleMenu);
                    }
                    List<String> hiddenMenuList = sysService.getHiddenMenu();
                    for (String hiddenMenuId : hiddenMenuList) {
                        sysRoleMenu.setMenuId(hiddenMenuId);
                        sysService.addRoleMenu(sysRoleMenu);
                    }
                }
            }
            return BaseResponseUtil.success();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取机构类型列表", notes="获取机构类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isAll", value = "是否获取全部类型？(0:获取全部 1:获取当前用户权限可见)", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getOfficeTypeList", method = RequestMethod.POST)
    public BaseResponse<List<SysDict>> getOfficeTypeList(String isAll, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String currOfficeId = map.get("officeId")+"";

            List<SysDict> officeTypeList = new ArrayList<>();
            if (isAll.equals("0")) {
                officeTypeList = sysService.selectAllOfficeTypeList();
            } else if (isAll.equals("1")) {
                officeTypeList = sysService.selectCurrOfficeTypeList(currOfficeId);
            }

            return BaseResponseUtil.success(officeTypeList);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="验证当前用户菜单中是否包含某页面", notes="验证当前用户菜单中是否包含某页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "页面地址", required = true, dataType = "String")
    })
    @RequestMapping(value = "/checkPath", method = RequestMethod.POST)
    public BaseResponse<Boolean> checkPath(String path, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String currOfficeId = map.get("officeId")+"";

            Boolean result = false;
            SysRole role = sysService.selectByUserId(currUserId);
            if (role != null) {
                String roleId = role.getId();
                result = sysService.checkPath(roleId, path);
            }

            return BaseResponseUtil.success(result);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }

}
