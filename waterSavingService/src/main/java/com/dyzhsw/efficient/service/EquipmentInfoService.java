package com.dyzhsw.efficient.service;


import java.util.List;
import java.util.Map;
import com.dyzhsw.efficient.dto.AddEquipmentInfoDTO;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.EquipmentWarn;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.github.pagehelper.PageInfo;


/**
 * 设备信息相关service
 * create by LiHD
 */
public interface EquipmentInfoService {

    int addEquipmentInfo(EquipmentInfo equipmentInfo);

    PageInfo<EquipmentInfo> getEquipmentListByEquTypeAndOfficeId(Integer pageNo, Integer pageSize, String officeId, String equType, String name, String equipmentNo, String groupId, Integer isonline, String isSys, String conditionOfficeId);

    PageInfo<EquipmentInfo> getCollectEquipmentListByOfficeId(Integer pageNo, Integer pageSize, String officeId, String name, String equipmentNo, String affiliationId, Integer isonline, String isSys);

    EquipmentInfo getEquipmentInfoByIdAndType(String id);
    
    List<EquipmentInfo> getEquipmentInfo(EquipmentInfo equipmentInfo);
    
    EquipmentInfo getEquipmentInfoById(String id);
    
    List<EquipmentInfo> getEquipmentInfoFilter(EquipmentInfo equipmentInfo);
    
    List<EquipmentInfo> getEquipmentInfoCollection(EquipmentInfo equipmentInfo);

	List<EquipmentInfo> getValveList( int type);

	List<EquipmentInfo> selectNotIn(String[] taskList, String officeId,int type) ;

	List<Map<String,Object>> getEquListByOfficeId(String officeId,int type );

	List<EquipmentInfo> getOptionalsCollectEquipmentList(String officeId, Integer equType);

	int updateEquipmentInfo(EquipmentInfo equipmentInfo);

	PageInfo<EquipmentWarn> getEquWarnInfoList(Integer pageNo, Integer pageSize, String equipmentNo, Integer equipmentType);

    BaseResponse saveEquipmentTransaction(AddEquipmentInfoDTO equipmentInfoDTO);
	 List<EquipmentInfo> queryEquipmentListPage(Map map);
	 List<EquipmentInfo> processEquipmentHandle(List<EquipmentInfo> currentList);

	PageInfo<EquipmentWarn> getEquWarnList(Integer pageNo, Integer pageSize, Integer equType, Integer warnType, String warnInfo, String remarks, String startTime, String endTime);

	void deletedEquipmentInfoById(String currUserId, String ids, Integer equipmentType);

	List<Map<String, Object>> getFkq();

	List<Map<String, Object>> getWm();

	void deletedEquWarn(String ids);

	int getWarnNum(String beforeWarnTime);

	List<EquipmentInfo> getOptionalEquByGroupId(String officeId);

	void addEquToGroup(String ids, String groupId);

	Map<String, Object> getEquChartInfo(String id, Integer chartType, Integer pageNo, Integer pageSize);
	
	//导出设备信息接口
	List<EquipmentInfo> getEquipmentInfoExport(String name,String equipmentNo,Integer isonline,String groupId,String officeId);
	List<EquipmentInfo> getEquipmentInfoFilterExport(String name,String equipmentNo,Integer isonline,String officeId);
	List<EquipmentInfo> getManureExport (String name,String equipmentNo,Integer isonline,String officeId);
	List<EquipmentInfo> getCollectionExport (String name,String equipmentNo,Integer isonline,String officeId);
	EquipmentInfo selectByEquNo(String equNo);

	String StringNoById(String equipmentId);


	List<Map<String, Object>> getWmByOfficeId(String officeId);

	int insertValveImport(EquipmentInfo equipmentInfo);


}
