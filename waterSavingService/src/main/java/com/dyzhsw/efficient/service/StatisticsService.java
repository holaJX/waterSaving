package com.dyzhsw.efficient.service;

import java.util.List;
import java.util.Map;

/**
 * 操作日志相关service
 * create by yr
 */
public interface StatisticsService {

	List<Map<String, Object>> selectWaterList(String id, String start, String end);

	List<Map<String, Object>> selectSfList(String areaId, String start, String end);

	List<Map<String, Object>> getSfList(String address, String start, String end);

	List<Map<String, Object>> getWaterList(String address, String start, String end);

	List<Map<String, Object>> selectWaterOrder(String start, String end);

	List<Map<String, Object>> selectSfOrder(String start, String end);
}                               