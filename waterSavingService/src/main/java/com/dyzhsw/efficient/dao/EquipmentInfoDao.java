package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.EquipmentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EquipmentInfoDao {

    int addEquipmentInfo(EquipmentInfo equipmentInfo);

    List<EquipmentInfo> getEquipmentListByEquTypeAndOfficeId(@Param("officeId") String officeId, @Param("equType") Integer equType);

    List<EquipmentInfo> getCollectEquipmentListByOfficeId(String officeId);

    EquipmentInfo getEquipmentInfoById(String id);

    EquipmentInfo getEquipmentInfoByIdAndType(@Param("id") String id, @Param("equType") Integer equType);

    void updateGroupId(@Param("currUserId") String currUserId, @Param("groupId") String groupId);

    int updateGroupIdById(@Param("currUserId") String currUserId, @Param("id") String id);

    List<EquipmentInfo> getEquipmentListByOfficeId(String officeId);

    List<EquipmentInfo> getEquipmentListByGroupId(String groupId);

    List<EquipmentInfo> getValveList(int equType);

    List<EquipmentInfo> selectNotIn(Map<String, Object> map);

    List<Map<String,Object>> getEquListByOfficeId(Map<String,Object>map);

    int updateEquipmentInfo(EquipmentInfo equipmentInfo);

    List<EquipmentInfo> selectEquipmentByMap(Map map);

    List<EquipmentInfo> getFkqListByCondition(@Param("officeIdList") List<String> officeIdList, @Param("equType") Integer equType, @Param("name") String name, @Param("equipmentNo") String equipmentNo, @Param("groupId") String groupId, @Param("isonline") Integer isonline);

    List<EquipmentInfo> getSfjListByCondition(@Param("officeIdList") List<String> officeIdList, @Param("equType") Integer equType, @Param("name") String name, @Param("equipmentNo") String equipmentNo, @Param("groupId") String groupId, @Param("isonline") Integer isonline);

    List<EquipmentInfo> getGlqListByCondition(@Param("officeIdList") List<String> officeIdList, @Param("equType") Integer equType, @Param("name") String name, @Param("equipmentNo") String equipmentNo, @Param("groupId") String groupId, @Param("isonline") Integer isonline);

    List<EquipmentInfo> getCollectEquipmentListByConditions(@Param("officeIdList") List<String> officeIdList, @Param("name") String name, @Param("equipmentNo") String equipmentNo, @Param("isonline") Integer isonline);

    int deletedEquipmentInfoById(@Param("currUserId") String currUserId, @Param("id") String id);

	List<Map<String, Object>> getFkq();

	List<Map<String, Object>> getWm();

	List<EquipmentInfo> getOptionalEquByGroupId(@Param("officeId") String officeId);

	EquipmentInfo selectByEquNo(String equNo);

	List<EquipmentInfo> getEquipmentListByEquName(@Param("equipmentName")String equipmentName);

	String StringNoById(String id);

	//导出设备信息接口
	List<EquipmentInfo> getEquipmentInfoExport(@Param("name")String name,@Param("equipmentNo")String equipmentNo,@Param("isonline")Integer isonline,@Param("groupId")String groupId,@Param("officeId")String officeId);
	List<EquipmentInfo> getEquipmentInfoFilterExport(@Param("name")String name,@Param("equipmentNo")String equipmentNo,@Param("isonline")Integer isonline,@Param("officeId")String officeId);
	List<EquipmentInfo> getManureExport (@Param("name")String name,@Param("equipmentNo")String equipmentNo,@Param("isonline")Integer isonline,@Param("officeId")String officeId);
	List<EquipmentInfo> getCollectionExport (@Param("name")String name,@Param("equipmentNo")String equipmentNo,@Param("isonline")Integer isonline,@Param("officeId")String officeId);

	List<EquipmentInfo> getEquipmentInfo(EquipmentInfo equipmentInfo);

	List<EquipmentInfo> getEquipmentInfoFilter(EquipmentInfo equipmentInfo);

	List<EquipmentInfo> getEquipmentInfoCollection(EquipmentInfo equipmentInfo);


	List<Map<String, Object>> getWmByOfficeId(String officeId);

	int insertValveImport(EquipmentInfo equipmentInfo);

	void updateOfficeId(@Param("officeId") String officeId, @Param("currUserId") String currUserId);

	EquipmentInfo getEquipmentInfoByEquNo(@Param("equipmentNo")String equNo);

	void emptyGroupId(@Param("groupIdList") List<String> groupIdList, @Param("currUserId") String currUserId);

}
