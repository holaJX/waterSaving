package com.dyzhsw.efficient.service.impl;


import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.Constant;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.config.jwt.JwtConfigBean;
import com.dyzhsw.efficient.config.redis.RedisUtil;
import com.dyzhsw.efficient.dao.*;
import com.dyzhsw.efficient.dto.SysAreaDTO;
import com.dyzhsw.efficient.entity.*;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SysServiceImpl implements SysService {

    private static Logger log = LoggerFactory.getLogger(SysServiceImpl.class);


    @Autowired
    private JwtConfigBean jwtConfigBean;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysOfficeDao sysOfficeDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysRoleOfficeDao sysRoleOfficeDao;

    @Autowired
    private SysDictDao sysDictDao;

    @Autowired
    private  SysAreaDao sysAreaDao;

    @Autowired
    private EquipmentInfoDao equipmentInfoDao;

    @Autowired
    private TaskEquListDao taskEquListDao;

    @Autowired
    private EquipmentTaskMapper equipmentTaskMapper;

    @Autowired
    private EquipmentGroupingDao equipmentGroupingDao;

    @Resource
    RedisTemplate<Object, Object> redisTemplate;


    @Override
    public BaseResponse login(String loginName, String password) {

        SysUser sysUser = sysUserDao.findUserByLoginName(loginName);
        if (sysUser != null) {

            Integer loginFlag =  sysUser.getLoginFlag();
            if (loginFlag != null && loginFlag == 1) {
                return BaseResponseUtil.error(ResultEnum.PROHIBIT_LOGIN.getStateCode(), ResultEnum.PROHIBIT_LOGIN.getMessage());
            }

            ////根据规则加密密码
            String encodePwd = DigestUtils.sha1Hex(password);
            if (sysUser.getPassword().equals(encodePwd)) {
                Date now = new Date();
                String token = Jwts.builder().setSubject(sysUser.getLoginName())
                        .signWith(SignatureAlgorithm.HS256, Constant.JWT_SECRETE_KEY)
                        .setIssuedAt(now)
                        .claim("id", sysUser.getId())
                        .claim("loginName", sysUser.getLoginName())
                        //不设置过期时间，，此处jwt自带过期时间和redis过期时间冲突
                        // .setExpiration(Date.from(Instant.now().plus(jwtConfigBean.getValidity(), Constant.jwtValidityMap.get(jwtConfigBean.getValidityType()))))
                        .compact();

                System.out.println(token);
                SysRole sysRole = sysRoleDao.selectByUserId(sysUser.getId());
                Map<Object,Object> map = new HashMap<>();
                map.put("id", sysUser.getId());
                map.put("loginName", sysUser.getLoginName());
                map.put("userName", sysUser.getName());
                map.put("officeId", sysUser.getOfficeId());
                if (sysRole != null && sysRole.getIsSys().equals("1")) {
                    map.put("isSys", sysRole.getIsSys());
                }
                map.put("dayTime", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                //登录成功，用户信息存入redis
                redisTemplate.opsForHash().putAll(token, map);
                //设置token redis过期时间
                redisTemplate.expire(token, jwtConfigBean.getValidity(), TimeUnit.SECONDS);
                log.info("BillClient "+token);
                sysUserDao.changeLoginDate(sysUser.getId());
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("token", token);
                resultMap.put("userInfo", sysUser);
                resultMap.put("roleInfo", sysRole);
                return BaseResponseUtil.success(resultMap);
            } else {
                log.info("密码校验失败", sysUser.getLoginName());
                return BaseResponseUtil.error(ResultEnum.LOGIN_PASSWORD_ERROR.getStateCode(), ResultEnum.LOGIN_PASSWORD_ERROR.getMessage());
            }

        } else {
            return BaseResponseUtil.error(ResultEnum.USER_NOT_EXIST.getStateCode(), ResultEnum.USER_NOT_EXIST.getMessage());
        }
    }

    @Override
    public BaseResponse loginOut(String token) {
        redisTemplate.delete(token);
        if(redisTemplate.hasKey(token)){
            return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
        }
        return BaseResponseUtil.success();
    }

    @Override
    public SysUser selectById(String id) {
        return sysUserDao.selectById(id);
    }

    @Override
    public String getCurrOfficeId(String currUserId) {
        return sysUserDao.getCurrOfficeId(currUserId);
    }

    @Override
    public int changePwd(Map map) {

        return sysUserDao.changePwd(map);
    }


    @Override
    public int addOffice(SysOffice sysOffice) {

        return sysOfficeDao.addOffice(sysOffice);
    }

    @Override
    public void addRoleOffice(SysRoleOffice sysRoleOffice) {

        sysRoleOfficeDao.addRoleOffice(sysRoleOffice);
    }

    @Override
    public int addUser(SysUser sysUser) {

        String newPwdEncode = DigestUtils.sha1Hex(sysUser.getPassword());
        sysUser.setPassword(newPwdEncode);
        return sysUserDao.addUser(sysUser);
    }

    @Override
    public void addUserRole(SysUserRole sysUserRole) {

        sysUserRoleDao.addUserRole(sysUserRole);
    }

    @Override
    public int addRole(SysRole sysRole) {
        return sysRoleDao.addRole(sysRole);
    }

    @Override
    public void addRoleMenu(SysRoleMenu sysRoleMenu) {
        sysRoleMenuDao.addRoleMenu(sysRoleMenu);
    }

    @Override
    public Boolean deletedRoleMenu(SysRoleMenu sysRoleMenu) {
        return sysRoleMenuDao.deletedByRoleId(sysRoleMenu.getRoleId()) > 0;
    }

    @Override
    public int addMenu(SysMenu sysMenu) {
        return sysMenuDao.addMenu(sysMenu);
    }


    @Override
    public void deletedUser(String userIds, String currUserId) {

        String[] userIdAry = userIds.split(",");
        for (String userId : userIdAry) {
            if (sysUserDao.deletedUser(userId, currUserId) > 0) {
                sysUserRoleDao.deletedByUserId(userId);
            }
        }
    }

    @Override
    public void deletedMenu(String menuIds, String currUserId) {

        String[] menuIdAry = menuIds.split(",");
        for (String menuId : menuIdAry) {
            if (sysMenuDao.deletedMenu(menuId, currUserId) > 0) {
                sysRoleMenuDao.deletedByMenuId(menuId);
            }
        }
    }

    @Override
    public void deletedRole(String roleIds, String currUserId) {

        String[] roleIdAry = roleIds.split(",");
        for (String roleId : roleIdAry) {
            if (sysRoleDao.deletedRole(roleId, currUserId) > 0) {
                sysRoleMenuDao.deletedByRoleId(roleId);
                sysRoleOfficeDao.deletedByRoleId(roleId);
                sysUserRoleDao.deletedByRoleId(roleId);
            }
        }
    }

    @Override
    public void deletedOffice(String officeIds, String currUserId) {

        String[] officeIdAry = officeIds.split(",");
        for (String officeId : officeIdAry) {
            List<String> childIdList = sysOfficeDao.selectAllChildById(officeId);
            List<String> delOfficeIdList = new ArrayList<>();
            delOfficeIdList.add(officeId);
            delOfficeIdList.addAll(childIdList);
            if (sysOfficeDao.deletedOffice(officeId,currUserId) > 0) {
                for (String delOfficeId : delOfficeIdList) {
                    // 删除机构-角色绑定关系
                    sysRoleOfficeDao.deletedByOfficeId(delOfficeId);
                    // 将该机构下的角色归属机构置空
                    sysRoleDao.updateOfficeId(delOfficeId, currUserId);
                    // 将该机构下的用户归属机构置空
                    sysUserDao.updateOfficeId(delOfficeId, currUserId);
                    // 将该机构下的设备归属机构置空
                    equipmentInfoDao.updateOfficeId(delOfficeId, currUserId);
                    // 将该机构下的分组归属置空
                    List<String> groupIdList = equipmentGroupingDao.selectIdByOfficeId(delOfficeId);
                    if (groupIdList != null && groupIdList.size() > 0) {
                        equipmentInfoDao.emptyGroupId(groupIdList, currUserId);
                    }
                    equipmentGroupingDao.updateOfficeId(delOfficeId, currUserId);
                    // 删除该机构下的定时任务
                    List<String> taskIdList = equipmentTaskMapper.selectByOfficeId(delOfficeId);
                    if (taskIdList != null && taskIdList.size() > 0) {
                        String[] taskIdAry = new String[taskIdList.size()];
                        taskIdAry = taskIdList.toArray(taskIdAry);
                        taskEquListDao.deleteBytaskId(taskIdAry);
                        equipmentTaskMapper.deleteEquTask(taskIdAry);
                    }
                }
            }
        }
    }

    @Override
    public String selectRoleIdByUserId(String userId) {
        return sysUserRoleDao.selectRoleIdByUserId(userId);
    }

    @Override
    public SysRole selectByUserId(String userId) {
        return sysRoleDao.selectByUserId(userId);
    }

    @Override
    public PageInfo<SysUser> getUserListByOfficeId(Integer pageNo, Integer pageSize, String officeId, String loginName, String isSys) {

        List<SysUser> sysUserList = new ArrayList<>();
        if (isSys.equals("1")) {
            PageHelper.startPage(pageNo, pageSize);
            sysUserList = sysUserDao.getUserListByOfficeId(null, loginName);
            for (SysUser sysUser : sysUserList) {
                if (!StringUtils.isEmpty(sysUser.getOfficeId())) {
                    SysOffice sysOffice = sysOfficeDao.selectById(sysUser.getOfficeId());
                    if (sysOffice != null) {
                        sysUser.setOfficeName(sysOffice.getName());
                    }
                }
            }
        } else {
            List<String> officeIds = sysOfficeDao.selectAllChildById(officeId);
            officeIds.add(0,officeId);
            PageHelper.startPage(pageNo, pageSize);
            sysUserList = sysUserDao.getUserListByOfficeId(officeIds, loginName);
            for (SysUser sysUser : sysUserList) {
                if (!StringUtils.isEmpty(sysUser.getOfficeId())) {
                    SysOffice sysOffice = sysOfficeDao.selectById(sysUser.getOfficeId());
                    if (sysOffice != null) {
                        sysUser.setOfficeName(sysOffice.getName());
                    }
                }
            }
        }

        PageInfo<SysUser> page = new PageInfo<>(sysUserList);

        return page;
    }

    @Override
    public List<SysOffice> getOfficeListByCurrUser(String officeId, String name, String type) {

        List<SysDict> officeTypeList = sysDictDao.getOfficeTypeList();
        List<SysOffice> officeList = new ArrayList<>();
        SysOffice currOffice = sysOfficeDao.selectById(officeId);
        if (currOffice != null) {
            if (!StringUtils.isEmpty(name) || !StringUtils.isEmpty(type)) {
                officeList = sysOfficeDao.selectByCondition(officeId, name, type);
                for (SysOffice office : officeList) {
                    for (SysDict officeType : officeTypeList) {
                        if (office.getType().equals(officeType.getValue())) {
                            office.setTypeName(officeType.getLabel());
                        }
                    }
                }
            } else {
                SysOffice parentOffice = sysOfficeDao.selectById(currOffice.getParentId());
                if (parentOffice != null) {
                    currOffice.setParentName(parentOffice.getName());
                }
                for (SysDict officeType : officeTypeList) {
                    if (currOffice.getType().equals(officeType.getValue())) {
                        currOffice.setTypeName(officeType.getLabel());
                    }
                }
                currOffice.setChildList(getChildOffice(currOffice, officeTypeList));
                officeList.add(currOffice);
            }
        }
        return officeList;
    }


    private List<SysOffice> getChildOffice(SysOffice sysOffice, List<SysDict> officeTypeList) {

        List<SysOffice> childOfficeList = sysOfficeDao.selectChildById(sysOffice.getId());
        if (childOfficeList != null && childOfficeList.size() > 0) {
            for (SysOffice childOffice : childOfficeList) {
                childOffice.setParentName(sysOffice.getName());
                for (SysDict officeType : officeTypeList) {
                    if (childOffice.getType().equals(officeType.getValue())) {
                        childOffice.setTypeName(officeType.getLabel());
                    }
                }
            }
            sysOffice.setChildList(childOfficeList);
            for (SysOffice childOffice : childOfficeList) {
                getChildOffice(childOffice, officeTypeList);
            }
        }
        return childOfficeList;
    }


    private List<SysOffice> getChildOfficeByType(SysOffice sysOffice, String type, String currOfficeId) {

        List<SysOffice> resultList = new ArrayList<>();
        List<SysOffice> childOfficeList = sysOfficeDao.selectChildById(sysOffice.getId());
        if (childOfficeList != null && childOfficeList.size() > 0) {
            for (SysOffice childOffice : childOfficeList) {
                childOffice.setParentName(sysOffice.getName());
            }
            for (SysOffice childOffice : childOfficeList) {
                if (!childOffice.getType().equals(type)) {
                    String parentIds = childOffice.getParentIds();
                    if (currOfficeId.equals(childOffice.getId()) || parentIds.contains(currOfficeId)) {
                        resultList.add(childOffice);
                    }
                }
            }
            if (resultList.size() > 0) {
                sysOffice.setChildList(resultList);
                for (SysOffice tempOffice : resultList) {
                    getChildOfficeByType(tempOffice, type, currOfficeId);
                }
            }
        }
        return resultList;
    }


    @Override
    public PageInfo<SysRole> getRoleListByOfficeId(Integer pageNo, Integer pageSize, String name, String officeId, String isSys, String conditionOfficeId) {

        List<SysRole> tempList = new ArrayList<>();
        if (isSys.equals("1") && StringUtils.isEmpty(conditionOfficeId)) {
            PageHelper.startPage(pageNo, pageSize);
            tempList = sysRoleDao.selectByOfficeId(name, null);
        } else {
            SysOffice currOffice = sysOfficeDao.selectById(officeId);
            List<SysOffice> officeList = sysOfficeDao.selectAllChildInfoById(officeId);
            officeList.add(currOffice);
            List<String> officeIdList = new ArrayList<>();
            for (SysOffice office : officeList) {
                officeIdList.add(office.getId());
            }

            PageHelper.startPage(pageNo, pageSize);
            tempList = sysRoleDao.selectByOfficeId(name, officeIdList);
        }
        PageInfo<SysRole> page = new PageInfo<>(tempList);

        return page;

    }

	@Override
	public SysUser findInfoById(String id) {
		SysUser sysUser = sysUserDao.findInfoById(id);
		if (sysUser != null && !StringUtils.isEmpty(sysUser.getRoleId())) {
		    SysRole role = sysRoleDao.selectById(sysUser.getRoleId());
		    if (role != null) {
		        sysUser.setRoleName(role.getName());
            }
        }
        if (sysUser != null && !StringUtils.isEmpty(sysUser.getOfficeId())) {
            SysOffice sysOffice = sysOfficeDao.selectById(sysUser.getOfficeId());
            String parentIds = sysOffice.getParentIds();
            if (parentIds.equals("0")) {
                parentIds = sysOffice.getId();
            } else {
                parentIds = parentIds + "," + sysUser.getOfficeId();
            }
            String[] officeIds = parentIds.split(",");
            List<String> officeIdList = Arrays.asList(officeIds);
            sysUser.setOfficeIdList(officeIdList);
        }
        return sysUser;
	}

	@Override
	public SysOffice getOfficeInfoById(String id) {
        SysOffice sysOffice = sysOfficeDao.getOfficeInfoById(id);
        String parentIds = sysOffice.getParentIds();
        if (!parentIds.equals("0")) {
            String[] officeIds = parentIds.split(",");
            List<String> officeIdList = Arrays.asList(officeIds);
            sysOffice.setOfficeIdList(officeIdList);
        }
        List<SysDict> officeTypeList = sysDictDao.getOfficeTypeList();
        for (SysDict officeType : officeTypeList) {
            if (sysOffice.getType().equals(officeType.getValue())) {
                sysOffice.setTypeName(officeType.getLabel());
            }
        }
		return sysOffice;
	}


	@Override
    public SysRole getSysRoleInfoById(String id) {

        SysRole role = sysRoleDao.selectById(id);
        if (role != null) {
            SysOffice sysOffice = sysOfficeDao.selectById(role.getOfficeId());
            if (sysOffice != null) {
                String parentIds = sysOffice.getParentIds();
                if (parentIds.equals("0")) {
                    parentIds = sysOffice.getId();
                } else {
                    parentIds = parentIds + "," + role.getOfficeId();
                }
                String[] officeIds = parentIds.split(",");
                List<String> officeIdList = Arrays.asList(officeIds);
                role.setOfficeIdList(officeIdList);
                role.setOfficeName(sysOffice.getName());
            }
            List<String> menuIdList = sysRoleMenuDao.selectMenuIdByRoleId(role.getId());
            role.setMenuIdList(menuIdList);
        }
        return role;

    }


    @Override
    public List<SysUser> selectUserListByRoleId(String roleId) {

        List<SysUser> userList = new ArrayList<>();
        List<String> userIdList = sysUserRoleDao.selectUserIdByRoleId(roleId);
        for (String userId : userIdList) {
            SysUser sysUser = sysUserDao.findInfoById(userId);
            userList.add(sysUser);
        }
        return userList;
    }


	@Override
    public List<SysOffice> getAllOfficeTree() {

        List<SysDict> officeTypeList = sysDictDao.getOfficeTypeList();
        List<SysOffice> maxParentOfficeList = sysOfficeDao.getMaxParentOffice();
        for (SysOffice maxParentOffice : maxParentOfficeList) {
            maxParentOffice.setChildList(getChildOffice(maxParentOffice, officeTypeList));
        }

        return maxParentOfficeList;
    }


    @Override
    public SysOffice getCurrOfficeTree(String officeId) {
        List<SysDict> officeTypeList = sysDictDao.getOfficeTypeList();
        SysOffice currOffice = sysOfficeDao.selectById(officeId);
        currOffice.setChildList(getChildOffice(currOffice, officeTypeList));

        return currOffice;
    }

    @Override
    public List<SysOffice> getCurrOfficeTreeByType(String officeId, String type) {
        List<SysOffice> maxParentOfficeList = sysOfficeDao.getMaxParentOffice();
        for (SysOffice maxParentOffice : maxParentOfficeList) {
            List<SysOffice> childOfficeList = getChildOfficeByType(maxParentOffice, type, officeId);
            if (childOfficeList != null && childOfficeList.size() > 0) {
                maxParentOffice.setChildList(childOfficeList);
            }
        }

        return maxParentOfficeList;
    }


    @Override
    public List<SysMenu> getAllMenuTree() {

        List<SysMenu> maxParentMenuList = sysMenuDao.getMaxParentMenu();
        for (SysMenu maxParentMenu : maxParentMenuList) {
            maxParentMenu.setChildList(getChildMenu(maxParentMenu));
        }

        return maxParentMenuList;
    }

    private List<SysMenu> getChildMenu(SysMenu sysMenu) {

        List<SysMenu> childMenuList = sysMenuDao.selectChildById(sysMenu.getId(), null, null);
        if (childMenuList != null && childMenuList.size() > 0) {
            sysMenu.setChildList(childMenuList);
            for (SysMenu childMenu : childMenuList) {
                getChildMenu(childMenu);
            }
        }
        return childMenuList;
    }


    @Override
    public List<String> getCurrMenuTree(String currUserId) {

        String roleId = sysUserRoleDao.selectRoleIdByUserId(currUserId);
        if (!StringUtils.isEmpty(roleId)) {
            List<String> menuIds = sysRoleMenuDao.selectMenuIdByRoleId(roleId);
            return menuIds;
        } else {
            return null;
        }
    }


    @Override
    public int updateSysUserInfo(SysUser sysUser) {

//        String newPwdEncode = DigestUtils.sha1Hex(sysUser.getPassword());
//        sysUser.setPassword(newPwdEncode);
        return sysUserDao.updateSysUserInfo(sysUser);
    }


    @Override
    public int updateSysOfficeInfo(SysOffice sysOffice) {
        return sysOfficeDao.updateSysOfficeInfo(sysOffice);
    }


    @Override
    public int updateSysMenuInfo(SysMenu sysMenu) {
        return sysMenuDao.updateSysMenuInfo(sysMenu);
    }


    @Override
    public void updateSysRoleInfo(SysRole sysRole) {
        sysRoleDao.updateSysRoleInfo(sysRole);
    }


    @Override
    public void updateRoleOfficeByRoleId(SysRoleOffice roleOffice) {
        sysRoleOfficeDao.updateRoleOfficeByRoleId(roleOffice);
    }


    @Override
    public SysRoleMenu selectByRoleMenu(SysRoleMenu sysRoleMenu) {
        return sysRoleMenuDao.selectByRoleMenu(sysRoleMenu);
    }


    @Override
    public void updateRoleMenuByRoleId(SysRoleMenu sysRoleMenu) {
        sysRoleMenuDao.updateRoleMenuByRoleId(sysRoleMenu);
    }

    @Override
    public void updateUserRole(SysUserRole sysUserRole) {

        if (sysUserRoleDao.selectByUserRole(sysUserRole.getUserId()) != null) {
            sysUserRoleDao.updateRoleIdByUserId(sysUserRole);
        } else {
            sysUserRoleDao.addUserRole(sysUserRole);
        }
    }


    @Override
    public List<SysDict> selectAllOfficeTypeList() {
        return sysDictDao.selectAllOfficeTypeList();
    }

    @Override
    public List<SysDict> selectCurrOfficeTypeList(String officeId) {

        SysOffice office = sysOfficeDao.selectById(officeId);
        return sysDictDao.selectCurrOfficeTypeList(office.getType());
    }

    @Override
    public List<SysAreaDTO> getAreas(Map map) {
        return sysAreaDao.getAreas(map);
    }

    @Override
    public List<SysDict> getEquWarnTypeListByEquType(Integer equType) {

        List<SysDict> warnTypeList = new ArrayList<>();
        if (equType != null) {
            if (1 == equType) {
                warnTypeList = sysDictDao.selectWarnTypeByType("wm_warn_type");
            } else if (4 == equType) {
                warnTypeList = sysDictDao.selectWarnTypeByType("gsm_warn_type");
            } else if (5 == equType) {
                warnTypeList = sysDictDao.selectWarnTypeByType("pre_warn_type");
            }
        }

        return warnTypeList;
    }

    @Override
    public SysMenu getSysMenuInfo(String id) {

        SysMenu sysMenu = sysMenuDao.getSysMenuInfo(id, null, null);
        String parentIds = sysMenu.getParentIds();
        if (!parentIds.equals("0")) {
            String[] menuIds = parentIds.split(",");
            List<String> menuIdList = Arrays.asList(menuIds);
            sysMenu.setMenuIdList(menuIdList);
        }

        return sysMenu;
    }

    @Override
    public List<SysMenu> getSysMenuListByUserId(String currUserId, String name, String isShow) {

        List<SysMenu> parentMenuList = new ArrayList<>();
        String roleId = sysUserRoleDao.selectRoleIdByUserId(currUserId);
        List<String> menuIdList = sysRoleMenuDao.selectMenuIdByRoleId(roleId);
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(isShow)) {
//            Collections.reverse(menuIdList);
            List<SysMenu> tempParentMenuList = new ArrayList<>();
            for (String menuId : menuIdList) {
                SysMenu sysMenu = sysMenuDao.getSysMenuInfo(menuId, name, isShow);
                if (sysMenu != null && sysMenu.getParentId().equals("0")) {
                    tempParentMenuList.add(sysMenu);
                }
            }
            for (SysMenu parentMenu : tempParentMenuList) {
                parentMenuList.add(getChildMenu(parentMenu, menuIdList, name, isShow));
            }
        } else {
            for (String menuId : menuIdList) {
                SysMenu sysMenu = sysMenuDao.getSysMenuInfo(menuId, name, isShow);
                if (sysMenu != null) {
                    parentMenuList.add(sysMenu);
                }
            }
        }

        return parentMenuList;
    }


    private SysMenu getChildMenu(SysMenu parentMenu, List<String> menuIdList, String name, String isShow) {

        List<SysMenu> childMenuList = sysMenuDao.selectChildById(parentMenu.getId(), name, isShow);
        if (childMenuList != null && childMenuList.size() > 0) {
            List<SysMenu> tempList = new ArrayList<>();
            for (SysMenu childMenu : childMenuList) {
                for (String menuId : menuIdList) {
                    if (menuId.equals(childMenu.getId())) {
                        tempList.add(childMenu);
                    }
                }

            }
            parentMenu.setChildList(tempList);
            for (SysMenu menu : tempList) {
                getChildMenu(menu, menuIdList, name, isShow);
            }
        }
        return parentMenu;
    }



    @Override
    public List<MenuRoute> getMenu(String currUserId) {

        List<MenuRoute> parentMenuList = new ArrayList<>();
        String roleId = sysUserRoleDao.selectRoleIdByUserId(currUserId);
        List<String> menuIdList = sysRoleMenuDao.selectMenuIdByRoleId(roleId);
        List<MenuRoute> tempParentMenuList = new ArrayList<>();
        for (String menuId : menuIdList) {
            SysMenu sysMenu = sysMenuDao.getSysMenuInfo(menuId, null, null);
            if (sysMenu != null && sysMenu.getParentId().equals("0")) {
                MenuRoute menuRoute = new MenuRoute();
                menuRoute.setId(sysMenu.getId());
                menuRoute.setPath(sysMenu.getTarget());
                menuRoute.setComponent(sysMenu.getComponent());
                menuRoute.setRedirect(sysMenu.getRedirect());
                menuRoute.setName(sysMenu.getName());
                menuRoute.setMeta(sysMenu.getMeta());
                if (sysMenu.getOperation().equals("1")) {
                    menuRoute.setHidden(true);
                } else {
                    menuRoute.setHidden(false);
                }
                tempParentMenuList.add(menuRoute);
            }
        }
        for (MenuRoute parentMenu : tempParentMenuList) {
            parentMenuList.add(menuGetChild(parentMenu, menuIdList));
            handleMenuRedirect(parentMenu);
        }

        return parentMenuList;

    }


    private MenuRoute menuGetChild(MenuRoute parentMenu, List<String> menuIdList) {

        List<SysMenu> childMenuList = sysMenuDao.getMenuChild(parentMenu.getId());
        if (childMenuList != null && childMenuList.size() > 0) {
            List<MenuRoute> tempList = new ArrayList<>();
            for (SysMenu childMenu : childMenuList) {
                for (String menuId : menuIdList) {
                    if (menuId.equals(childMenu.getId())) {
                        MenuRoute menuRoute = new MenuRoute();
                        menuRoute.setId(childMenu.getId());
                        menuRoute.setPath(childMenu.getTarget());
                        menuRoute.setComponent(childMenu.getComponent());
                        menuRoute.setRedirect(childMenu.getRedirect());
                        menuRoute.setName(childMenu.getName());
                        menuRoute.setMeta(childMenu.getMeta());
                        if (childMenu.getOperation().equals("1")) {
                            menuRoute.setHidden(true);
                        } else {
                            menuRoute.setHidden(false);
                        }
                        tempList.add(menuRoute);
                    }
                }

            }
            parentMenu.setChildren(tempList);
            for (MenuRoute menu : tempList) {
                menuGetChild(menu, menuIdList);
            }
        } else {
            parentMenu.setChildren(new ArrayList<MenuRoute>());
        }
        return parentMenu;
    }


    private void handleMenuRedirect(MenuRoute parentMenu) {

        for (MenuRoute childMenu : parentMenu.getChildren()) {
            if (!childMenu.getHidden()) {
                parentMenu.setRedirect(childMenu.getRedirect());
                if (childMenu.getChildren() != null && childMenu.getChildren().size() > 0) {
                    for (MenuRoute childChildMenu : childMenu.getChildren()) {
                        if (!childChildMenu.getHidden()) {
                            childMenu.setRedirect(childChildMenu.getRedirect());
                            handleMenuRedirect(childChildMenu);
                            parentMenu.setRedirect(childMenu.getRedirect());
                            break;
                        }
                    }
                }
                break;
            }
        }
    }


    @Override
    public List<SysDict> getEquTypeList() {
        return sysDictDao.getEquTypeList();
    }

    @Override
    public String selectUserRoleByUserId(String userId) {
        return sysUserRoleDao.selectRoleIdByUserId(userId);
    }

    @Override
    public List<String> getHiddenMenu() {
        return sysMenuDao.getHiddenMenu();
    }

    @Override
    public Boolean checkPath(String roleId, String path) {

        List<String> menuIdList = sysRoleMenuDao.selectMenuIdByRoleId(roleId);
        SysMenu sysMenu = sysMenuDao.checkPath(menuIdList, path);
        if (sysMenu != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> getIsSysRoleId() {

        return sysRoleDao.getIsSysRoleId();
    }

	@Override
	public List<SysUser> getListSysUserInfo(SysUser sysUser) {
		return sysUserDao.getListSysUserInfo(sysUser);
	}

	@Override
	public List<SysMenu> selectBySysMenu(SysMenu sysMenu) {
		return sysMenuDao.selectBySysMenu(sysMenu);
	}

	@Override
	public List<SysRole> selectBySysRole(SysRole sysRole) {
		return sysRoleDao.selectBySysRole(sysRole);
	}

	@Override
	public SysUser selectByNameImport(String name) {
		return sysUserDao.selectByNameImport(name);
	}

	@Override
	public int insertUserImport(SysUser sysUser) {
		return sysUserDao.insertUserImport(sysUser);
	}
}
