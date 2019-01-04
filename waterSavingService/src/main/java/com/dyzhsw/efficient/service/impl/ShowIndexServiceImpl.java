package com.dyzhsw.efficient.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyzhsw.efficient.dao.EquipmentInfoDao;
import com.dyzhsw.efficient.dao.ShowIndexDao;
import com.dyzhsw.efficient.dao.SysOfficeDao;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.FlowmeterHomeVo;
import com.dyzhsw.efficient.entity.LineChartVo;
import com.dyzhsw.efficient.entity.MetercalcFlowVo;
import com.dyzhsw.efficient.entity.SysOffice;
import com.dyzhsw.efficient.entity.WaterConsumption;
import com.dyzhsw.efficient.entity.WaterConsumptionEchartsVo;
import com.dyzhsw.efficient.service.ShowIndexService;

/**
 * 
* 展示首页
* @author: hw 
* @date: 2018-12-4
 */
@Service
public class ShowIndexServiceImpl implements ShowIndexService {
	
	@Autowired
	private ShowIndexDao showIndexDao;
	
	@Autowired
	private EquipmentInfoDao equipmentInfoDao;
	
	@Autowired
	private SysOfficeDao sysOfficeDao;

	@Override
	public List<SysOffice> pianquList() {
		List<SysOffice> list = sysOfficeDao.selectPianquList();
		return list;
	}
	
	@Override
	public List<SysOffice> shoubuList() {
		List<SysOffice> list = sysOfficeDao.selectShoubuList();
		return list;
	}
	
	@Override
	public Map<String, Object> tabOne() {
		Map<String, Object> res = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		List<EquipmentInfo> shoubus = showIndexDao.queryMeterEquipmentNo();
		List<String> shoubuIds = new ArrayList<>();
		for(EquipmentInfo shoubu :shoubus) {
			Map<String, Object> save =  new HashMap<String, Object>();
			//根据流量计设备编号获取瞬时流量
			Map<String, Object> r = showIndexDao.queryValueByNo(shoubu.getEquipmentNo());
			BigDecimal last = new BigDecimal(r==null?"0.00":r.get("last").toString());
			BigDecimal now = new BigDecimal(r==null?"0.00":r.get("last").toString());
			if(!(last.compareTo(BigDecimal.ZERO)==0)) {
				save.put("name", shoubu.getName());
				save.put("now", now);
				save.put("rate", (now.subtract(last)).divide(last));
			} else if(last.equals(BigDecimal.ZERO)) {
				save.put("name", shoubu.getName());
				save.put("now", now);
				save.put("rate", now);
			} else {
				save.put("name", shoubu.getName());
				save.put("now", now);
				save.put("rate", 0);
			}
			shoubuIds.add(shoubu.getEquipmentNo());
			list.add(save);
		}
		res.put("list", list);
		String dataCopyTime = showIndexDao.queryInstantaneousflowCopyTimeByIds(shoubuIds);
		res.put("dataCopyTime", dataCopyTime);
		return res;
	}

	@Override
	public Map<String, Object> tabTwo() {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> waterCost = showIndexDao.queryWaterCost();
		Map<String, Object> solenoidValve = showIndexDao.querySolenoidValve ();
		res.put("waterCost", waterCost);
		res.put("solenoidValve", solenoidValve);
		res.put("dataCopyTime", new Date().getTime());
		return res;
	}

	@Override
	public Map<String, Object> tabThree(int dataType) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<WaterConsumptionEchartsVo> list = new ArrayList<WaterConsumptionEchartsVo>();
		List<SysOffice> pianqus = showIndexDao.queryPianQu();
		if(!pianqus.isEmpty()) {
			for(int i=0; i<pianqus.size(); i++) {
				WaterConsumptionEchartsVo vo = new WaterConsumptionEchartsVo();
				List<WaterConsumption> listToAdd = new ArrayList<WaterConsumption>();
				SysOffice pianqu = pianqus.get(i);
				List<SysOffice> heads = sysOfficeDao.selectShouBuByOfficeId(pianqus.get(i).getId());
				if(!heads.isEmpty()) {
					for(int j=0; j<heads.size(); j++) {
						SysOffice head = heads.get(j);
						List<EquipmentInfo> equList = showIndexDao.getEquipmentListByTypeOfficeId(4,head.getId());
						if(!equList.isEmpty()) {
							for(int k=0; k<equList.size(); k++) {
								EquipmentInfo equ = equList.get(k);
								//按天统计
								if(dataType==0) {
									WaterConsumption data = showIndexDao.waterConsumptionByDay(equ.getEquipmentNo());
									listToAdd.add(data);
								}
								
								//按月统计
								if(dataType==1) {
									WaterConsumption data = showIndexDao.waterConsumptionByMonth(equ.getEquipmentNo());
									listToAdd.add(data);
								}
								
								//按年统计
								if(dataType==2) {
									WaterConsumption data = showIndexDao.waterConsumptionByYear(equ.getEquipmentNo());
									listToAdd.add(data);
								}
							}
						}
					}
				}

				//处理查询的各个流量计数据
				Double value[] = {0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00};
				String[] date = getDateList(dataType);
				for(int k=0; k<listToAdd.size();k++) {
					BigDecimal v[] = {
							new BigDecimal(listToAdd.get(k).getValue1()==null?"0.00":listToAdd.get(k).getValue1()),
							new BigDecimal(listToAdd.get(k).getValue2()==null?"0.00":listToAdd.get(k).getValue2()),
							new BigDecimal(listToAdd.get(k).getValue3()==null?"0.00":listToAdd.get(k).getValue3()),
							new BigDecimal(listToAdd.get(k).getValue4()==null?"0.00":listToAdd.get(k).getValue4()),
							new BigDecimal(listToAdd.get(k).getValue5()==null?"0.00":listToAdd.get(k).getValue5()),
							new BigDecimal(listToAdd.get(k).getValue6()==null?"0.00":listToAdd.get(k).getValue6()),
							new BigDecimal(listToAdd.get(k).getValue7()==null?"0.00":listToAdd.get(k).getValue7()),
							new BigDecimal(listToAdd.get(k).getValue8()==null?"0.00":listToAdd.get(k).getValue8()),
							new BigDecimal(listToAdd.get(k).getValue9()==null?"0.00":listToAdd.get(k).getValue9()),
							new BigDecimal(listToAdd.get(k).getValue10()==null?"0.00":listToAdd.get(k).getValue10()),
							new BigDecimal(listToAdd.get(k).getValue11()==null?"0.00":listToAdd.get(k).getValue11()),
							new BigDecimal(listToAdd.get(k).getValue12()==null?"0.00":listToAdd.get(k).getValue12())
					};

					if(dataType==0||dataType==1) {
						for(int m=0;m<12;m++) {
							value[m] = Double.valueOf(new BigDecimal(value[m]).add(v[m]).toString());
						}
					} else {
						for(int m=0;m<3;m++) {
							value[m] = Double.valueOf(new BigDecimal(value[m]).add(v[m]).toString());
						}
					}
				}
				vo.setPianquName(pianqu.getName());
				vo.setValue(value);
				vo.setDate(date);
				list.add(vo);
			}
		}
		result.put("list", list);
		return result;
	}

	//获取年月日
	private String[] getDateList(int dataType){
		Calendar cal = Calendar.getInstance();
		//年
		int year = cal.get(Calendar.YEAR);
		//月
		int month = cal.get(Calendar.MONTH )+1;
		
		if(dataType==1) {
			String[] str  = new String[12]; 
			String currMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
			str[11] = currMonth;
			for(int i=0; i<11; i++){
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月
				str[10-i] = String.valueOf(cal.get(Calendar.MONTH)+1);
			}
			return str;
		} else {
			String[] str = {
					String.valueOf(cal.get(Calendar.YEAR)-2),
					String.valueOf(cal.get(Calendar.YEAR)-1),
					String.valueOf(cal.get(Calendar.YEAR))
			};
			return str;
		}
	}
	
	@Override
	public Map<String, Object> tabFour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> tabFive(String equipmentId) {
		Map<String, Object> res = new HashMap<String, Object>();
		List<FlowmeterHomeVo> equipments = showIndexDao.queryFilter();
		for(int i=0; i<equipments.size();i++) {
			FlowmeterHomeVo vo = equipments.get(i);
			//根据流量计获取累计流量
			List<LineChartVo> values = showIndexDao.queryFilterValue(vo.getFlowmeterId());
			vo.setFlowmeterCumulativeflow(values);
			//瞬时流量
			String instantaneousflow = showIndexDao.queryInstantaneousflow(vo.getFlowmeterId());
			vo.setInstantaneousflow(instantaneousflow);
			//瞬时最大流量
			String instantaneousflowMax = showIndexDao.queryInstantaneousflowMax(vo.getFlowmeterId());
			vo.setInstantaneousflowMax(instantaneousflowMax);
			//数据采集最新时间
			String copyTime = showIndexDao.queryInstantaneousflowCopyTimeById(vo.getFlowmeterId());
			vo.setCopyTime(copyTime);
		}
		res.put("list", equipments);
		return res;
	}

	@Override
	public Map<String, Object> tabSix(String terminaId) {
		Map<String, Object> res = new HashMap<String, Object>();
		List<MetercalcFlowVo> equipments = showIndexDao.queryFertilizer();
		for(int i=0; i<equipments.size();i++) {
			MetercalcFlowVo vo = equipments.get(i);
			//根据流量计获取累计流量
			List<LineChartVo> values = showIndexDao.queryFertilizerValue(vo.getMetercalcFlowNo());
			vo.setCumulativeflow(values);
			//施肥速度
			String instantaneousflow = showIndexDao.queryWmInstantaneousflow(vo.getMetercalcFlowId());
			BigDecimal speed = new BigDecimal(instantaneousflow==null?"0.00":instantaneousflow).divide(new BigDecimal(3600),2);
			vo.setCumulativeflowIncrementSpeed(speed.toString());
			//出口压力
			String amount = showIndexDao.queryAmount(vo.getMetercalcFlowId());
			vo.setAmount(amount);
			//瞬时最大出口压力
			String amountMax = showIndexDao.queryAmountMax(vo.getMetercalcFlowId());
			vo.setAmountMax(amountMax);
			//数据采集最新时间
			String copyTime = showIndexDao.queryCopyTimeById(vo.getMetercalcFlowId());
			vo.setCopyTime(copyTime);
		}
		res.put("list", equipments);
		return res;
	}

}
