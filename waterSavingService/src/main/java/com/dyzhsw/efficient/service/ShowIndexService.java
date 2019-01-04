package com.dyzhsw.efficient.service;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.SysOffice;

/**
 * 
* 展示首页
* @author: hw 
* @date: 2018-12-4
 */
public interface ShowIndexService {

	Map<String, Object> tabOne();
	
	Map<String, Object> tabTwo();

	Map<String, Object> tabThree(int dataType);

	Map<String, Object> tabFour();

	Map<String, Object> tabFive(String equipmentId);

	Map<String, Object> tabSix(String terminaId);

	List<SysOffice> pianquList();

	List<SysOffice> shoubuList();

}
