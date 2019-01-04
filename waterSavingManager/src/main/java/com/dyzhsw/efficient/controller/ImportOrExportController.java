package com.dyzhsw.efficient.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.entity.ControlLog;
import com.dyzhsw.efficient.entity.EquipmentGrouping;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.EquipmentTask;
import com.dyzhsw.efficient.entity.EquipmentWarn;
import com.dyzhsw.efficient.entity.FkqValveInfo;
import com.dyzhsw.efficient.entity.GsmMeterCumulativeflowforday;
import com.dyzhsw.efficient.entity.SysMenu;
import com.dyzhsw.efficient.entity.SysOffice;
import com.dyzhsw.efficient.entity.SysRole;
import com.dyzhsw.efficient.entity.SysUser;
import com.dyzhsw.efficient.entity.WmMetercalcFlowdetail;
import com.dyzhsw.efficient.service.ControlLogService;
import com.dyzhsw.efficient.service.EquipmentGroupingService;
import com.dyzhsw.efficient.service.EquipmentInfoService;
import com.dyzhsw.efficient.service.EquipmentRelationService;
import com.dyzhsw.efficient.service.EquipmentTaskService;
import com.dyzhsw.efficient.service.EquipmentWarnService;
import com.dyzhsw.efficient.service.FkqValveInfoService;
import com.dyzhsw.efficient.service.GsmMeterCountService;
import com.dyzhsw.efficient.service.SysDictService;
import com.dyzhsw.efficient.service.SysOfficeService;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.service.WmTerminalStateinfoService;
import com.dyzhsw.efficient.service.WmTerminalinfoService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.IDUtils;
import com.dyzhsw.efficient.utils.WDWUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author guyuqiao 导入导出功能接口
 */

@Controller
@RequestMapping("/importOrExport")
@Api(value = "导入导出接口")
public class ImportOrExportController {

	@Autowired
	private EquipmentInfoService equipmentInfoService;
	@Autowired
	private EquipmentRelationService equipmentRelationService;
	@Autowired
	private EquipmentWarnService equipmentWarnService;
	@Autowired
	private EquipmentGroupingService equipmentGroupingService;
	@Autowired
	private EquipmentTaskService equipmentTaskService;
	@Autowired
	private ControlLogService controlLogService;
	@Autowired
	private SysService sysService;
	@Autowired
	private SysOfficeService sysOfficeService;
	@Autowired
	private GsmMeterCountService gsmMeterCountService;
	@Autowired
	private WmTerminalinfoService wmTerminalinfoService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private WmTerminalStateinfoService wmTerminalStateinfoService;
	@Autowired
	private FkqValveInfoService fkqValveInfoService;
	@Resource
	RedisTemplate<Object, Object> redisTemplate;

	String fifleName = null;
	String equipmentRegion = null;
	String startOpenTime = null;
	String endCloseTime = null;

	@ResponseBody
	@ApiOperation(value = "设备信息导出列表", notes = "设备信息导出列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "equType", value = "设备类型(1施肥机信息2过滤器信息3电磁阀信息4采集设备信息)", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "name", value = "设备名称", required = false, dataType = "String"),
			@ApiImplicitParam(name = "equipmentNo", value = "设备编号", required = false, dataType = "String"),
			@ApiImplicitParam(name = "groupId", value = "分组id", required = false, dataType = "String"),
			@ApiImplicitParam(name = "officeId", value = "归属id", required = false, dataType = "String"),
			@ApiImplicitParam(name = "isonline", value = "在线状态", required = false, dataType = "Integer") })
	@RequestMapping(value = "/exportEquipmentInfo", method = RequestMethod.POST)
	public void exportEquipmentInfoByAuto(HttpServletRequest request, HttpServletResponse response, String name,
			String equipmentNo, Integer isonline, Integer equType, String groupId,String officeId)
			throws UnsupportedEncodingException {
		response.reset();
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null) {
			return;
		}
		String token = authHeader.substring(11);
		Map<Object, Object> map = redisTemplate.opsForHash().entries(token);
		String currOfficeId = map.get("officeId") + "";
		String isSys = map.get("isSys")+"";
		if (!StringUtils.isEmpty(officeId)) {
            currOfficeId = officeId;
        }
		SysOffice sysOffice = sysOfficeService.selectById(currOfficeId);
		//判断是否为首部类型
		 List<String> shoubuIdList = new ArrayList<>();
		if (sysOffice.getType().equals("3")) { 
			shoubuIdList.add(currOfficeId);
		}else { // 非首部类型
            // 1.查询首部系统
            List<SysOffice> sysOfficeList = sysOfficeService.selectShouBuByOfficeId(currOfficeId);
            /*shoubuIdList.add(currOfficeId);*/
            for (SysOffice sysOffice1 : sysOfficeList) {
                shoubuIdList.add(sysOffice1.getId());
            }
		}
		
		// 创建新的Excel 工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (equType == 3) {
			List<EquipmentInfo> infos = new ArrayList<>();
			if(isSys.equals("1") && StringUtils.isEmpty(officeId)) {
				infos = equipmentInfoService.getEquipmentInfoExport(name, equipmentNo, isonline,
						groupId,officeId);
			} else {
				for(int j=0;j<shoubuIdList.size();j++) {
					 List<EquipmentInfo> infos2 = equipmentInfoService.getEquipmentInfoExport(name, equipmentNo, isonline,groupId,shoubuIdList.get(j));
					 for(int n=0;n<infos2.size();n++) {
						 EquipmentInfo equ = infos2.get(n);
						 infos.add(equ);
					 }
				}
			}
				fifleName = "阀门控制器信息";
				String valve1StatusStr = null;
				String valve2StatusStr = null;
				XSSFSheet sheet = workbook.createSheet(fifleName);
				XSSFRow row = sheet.createRow(0);
				XSSFCell cell = row.createCell(0);
				cell.setCellValue("");
				cell = row.createCell(1);
				cell.setCellValue("设备名称");
				cell = row.createCell(2);
				cell.setCellValue("设备编号");
				cell = row.createCell(3);
				cell.setCellValue("设备归属");
				cell = row.createCell(4);
				cell.setCellValue("设备分组");
				cell = row.createCell(5);
				cell.setCellValue("在线状态");
				cell = row.createCell(6);
				cell.setCellValue("控制器状态");
				for (int i = 0; i < infos.size(); i++) {
					EquipmentInfo info = infos.get(i);
					row = sheet.createRow(i + 1);
					row.createCell(0).setCellValue(i + 1);
					if (null == info.getName()) {
						row.createCell(1).setCellValue("");
					} else {
						row.createCell(1).setCellValue(info.getName());
					}
					if (null == info.getOfficeName()) {
						row.createCell(3).setCellValue("");
					} else {
						row.createCell(3).setCellValue(info.getOfficeName());
					}
					if (null == info.getEquipmentNo()) {
						row.createCell(2).setCellValue("");
					} else {
						row.createCell(2).setCellValue(info.getEquipmentNo());
					}
					if (null == info.getGroupingName()) {
						row.createCell(4).setCellValue("");
					} else {
						row.createCell(4).setCellValue(info.getGroupingName());
					}
					if (null == info.getIsonline()) {
						row.createCell(5).setCellValue("离线");
					} else {
						if (info.getIsonline() == 1) {
							row.createCell(5).setCellValue("在线");
						} else {
							row.createCell(5).setCellValue("离线");
						}
					}
					if (null == info.getValve1Status() && null == info.getValve2Status()) {
						row.createCell(6).setCellValue("1路:" + "" + "," + "2路:" + "");
					} else {
						if (info.getValve1Status() == 1) {
							valve1StatusStr = "关";
						}
						if (info.getValve1Status() == 2) {
							valve1StatusStr = "开";
						}
						if (info.getValve1Status() == 0) {
							valve1StatusStr = "未知状态";
						}
						if (info.getValve2Status() == 1) {
							valve2StatusStr = "关";
						}
						if (info.getValve2Status() == 2) {
							valve2StatusStr = "开";
						}
						if (info.getValve2Status() == 0) {
							valve2StatusStr = "未知状态";
						}
						row.createCell(6).setCellValue("1路:" + valve1StatusStr + "," + "2路:" + valve2StatusStr);
					}
				}
		} else if (equType == 2) {
			List<EquipmentInfo> infos = new ArrayList<>();
			if(isSys.equals("1") && StringUtils.isEmpty(officeId)) {
				currOfficeId = null;
				infos = equipmentInfoService.getEquipmentInfoFilterExport(name, equipmentNo, isonline,currOfficeId);
			} else {
				for(int j=0;j<shoubuIdList.size();j++) {
					 List<EquipmentInfo> infos2 = equipmentInfoService.getEquipmentInfoFilterExport(name, equipmentNo, isonline,shoubuIdList.get(j));
					 for(int n=0;n<infos2.size();n++) {
						 EquipmentInfo equ = infos2.get(n);
						 infos.add(equ);
					 }
				}
			}
			/*List<EquipmentInfo> infos = equipmentInfoService.getEquipmentInfoFilterExport(name, equipmentNo, isonline,
					officeId);*/
			fifleName = "过滤器信息";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("设备名称");
			cell = row.createCell(2);
			cell.setCellValue("设备编号");
			cell = row.createCell(3);
			cell.setCellValue("设备归属");
			for (int i = 0; i < infos.size(); i++) {
				EquipmentInfo info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getName());
				}
				if (null == info.getEquipmentNo()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getEquipmentNo());
				}
				if(info.getRegionName() == null) {
					if(info.getZoneName() == null) {
						row.createCell(3).setCellValue("");
					}else {
						row.createCell(3).setCellValue(info.getZoneName());
					}
				}else {
					if(info.getZoneName() == null) {
						row.createCell(3).setCellValue(info.getRegionName());
					}else {
						row.createCell(3).setCellValue(info.getRegionName() + info.getZoneName());
					}
				}
			}

		} else if (equType == 1) {
			List<EquipmentInfo> infos = new ArrayList<>();
			if(isSys.equals("1") && StringUtils.isEmpty(officeId)) {
				currOfficeId = null;
				infos = equipmentInfoService.getManureExport(name, equipmentNo, isonline,currOfficeId);
			} else {
				for(int j=0;j<shoubuIdList.size();j++) {
					 List<EquipmentInfo> infos2 = equipmentInfoService.getManureExport(name, equipmentNo, isonline,shoubuIdList.get(j));
					 for(int n=0;n<infos2.size();n++) {
						 EquipmentInfo equ = infos2.get(n);
						 infos.add(equ);
					 }
				}
			}
			/*List<EquipmentInfo> infos = equipmentInfoService.getManureExport(name, equipmentNo, isonline,
					officeId);*/
			fifleName = "施肥机信息";
			String fertilizerPump = "";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("设备名称");
			cell = row.createCell(2);
			cell.setCellValue("设备编号");
			cell = row.createCell(3);
			cell.setCellValue("设备归属");
			cell = row.createCell(4);
			cell.setCellValue("在线状态");
			cell = row.createCell(5);
			cell.setCellValue("控制器状态");
			for (int i = 0; i < infos.size(); i++) {
				EquipmentInfo info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getName());
				}
				if (null == info.getEquipmentNo()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getEquipmentNo());
				}
				if(info.getRegionName() == null) {
					if(info.getZoneName() == null) {
						row.createCell(3).setCellValue("");
					}else {
						row.createCell(3).setCellValue(info.getZoneName());
					}
				}else {
					if(info.getZoneName() == null) {
						row.createCell(3).setCellValue(info.getRegionName());
					}else {
						row.createCell(3).setCellValue(info.getRegionName() + info.getZoneName());
					}
				}
				if (null == info.getIsonline()) {
					row.createCell(4).setCellValue("离线");
				} else {
					if (info.getIsonline() == 1) {
						row.createCell(4).setCellValue("在线");
					} else {
						row.createCell(4).setCellValue("离线");
					}
				}
				// 施肥泵状态
				if (null == info.getEquipmentNo()) {
					row.createCell(5).setCellValue("");
				} else {
					if (null == wmTerminalStateinfoService.selectByEquNo(info.getEquipmentNo())) {
						row.createCell(5).setCellValue("施肥泵:未知");
					} else {
						Integer state = wmTerminalStateinfoService.selectByEquNo(info.getEquipmentNo()).getState();
						if (null == state || state.equals("")) {
							row.createCell(5).setCellValue("施肥泵:未知");
						} else {
							switch (state) {
							case 0:
								fertilizerPump = "关";
								break;
							case 1:
								fertilizerPump = "开";
								break;
							case 255:
								fertilizerPump = "未知";
								break;
							default:
								fertilizerPump = "未知";
								break;
							}
						}
						row.createCell(5).setCellValue("施肥泵:" + fertilizerPump);
					}
				}
			}

		} else if (equType == 4) {
			List<EquipmentInfo> infos = new ArrayList<>();
			if(isSys.equals("1") && StringUtils.isEmpty(officeId)) {
				currOfficeId = null;
				infos = equipmentInfoService.getCollectionExport(name, equipmentNo, isonline,currOfficeId);
			} else {
				for(int j=0;j<shoubuIdList.size();j++) {
					 List<EquipmentInfo> infos2 = equipmentInfoService.getCollectionExport(name, equipmentNo, isonline,shoubuIdList.get(j));
					 for(int n=0;n<infos2.size();n++) {
						 EquipmentInfo equ = infos2.get(n);
						 infos.add(equ);
					 }
				}
			}
			/*List<EquipmentInfo> infos = equipmentInfoService.getCollectionExport(name, equipmentNo, isonline,
					officeId);*/
			fifleName = "采集设备信息";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("在线状态");
			cell = row.createCell(2);
			cell.setCellValue("设备名称");
			cell = row.createCell(3);
			cell.setCellValue("设备编号");
			cell = row.createCell(4);
			cell.setCellValue("设备归属");
			cell = row.createCell(5);
			cell.setCellValue("设备类型");
			cell = row.createCell(6);
			cell.setCellValue("绑定设备");
			cell = row.createCell(7);
			cell.setCellValue("备注");
			for (int i = 0; i < infos.size(); i++) {
				EquipmentInfo info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getIsonline()) {
					row.createCell(1).setCellValue("离线");
				} else {
					if (info.getIsonline() == 1) {
						row.createCell(1).setCellValue("在线");
					} else {
						row.createCell(1).setCellValue("离线");
					}
				}
				if (null == info.getName()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getName());
				}
				if (null == info.getEquipmentNo()) {
					row.createCell(3).setCellValue("");
				} else {
					row.createCell(3).setCellValue(info.getEquipmentNo());
				}
				if(info.getRegionName() == null) {
					if(info.getZoneName() == null) {
						row.createCell(4).setCellValue("");
					}else {
						row.createCell(4).setCellValue(info.getZoneName());
					}
				}else {
					if(info.getZoneName() == null) {
						row.createCell(4).setCellValue(info.getRegionName());
					}else {
						row.createCell(4).setCellValue(info.getRegionName() + info.getZoneName());
					}
				}
				if (null == info.getEquipmentType()) {
					row.createCell(5).setCellValue("");
				} else {
					if(info.getEquipmentType() == 4) {
						row.createCell(5).setCellValue("流量计");
					}else if(info.getEquipmentType() == 5) {
						row.createCell(5).setCellValue("压力计");
					} else {
						row.createCell(5).setCellValue("");
					}
				}
				
				if (null == info.getEquipmentType()) {
					info.setRelationEquName("");
				} else {
					if (info.getEquipmentType() == 2) {
						if (null == equipmentRelationService.selectInfoByPercolatorId(info.getId())) {
							info.setRelationEquName("");
						} else {
							String flowmeterId = equipmentRelationService.selectInfoByPercolatorId(info.getId())
									.getFlowmeterId();
							if (null == flowmeterId) {
								info.setRelationEquName("");
							} else {
								if (null == equipmentInfoService.getEquipmentInfoById(flowmeterId)) {
									info.setRelationEquName("");
								} else {
									String relationEquName = equipmentInfoService.getEquipmentInfoById(flowmeterId)
											.getName();
									info.setRelationEquName(relationEquName);
								}
							}
						}
					} else if (info.getEquipmentType() == 3) {
						if (null == equipmentRelationService.selectInfoByValveControllerId(info.getId())) {
							info.setRelationEquName("");
						} else {
							String PiezometerId = equipmentRelationService.selectInfoByValveControllerId(info.getId())
									.getPiezometerId();
							if (null == PiezometerId) {
								info.setRelationEquName("");
							} else {
								if (null == equipmentInfoService.getEquipmentInfoById(PiezometerId)) {
									info.setRelationEquName("");
								} else {
									String relationEquName = equipmentInfoService.getEquipmentInfoById(PiezometerId)
											.getName();
									info.setRelationEquName(relationEquName);
								}
							}
						}
					} else if (info.getEquipmentType() == 4) {
						if (null == equipmentRelationService.selectInfoByFlowmeterId(info.getId())) {
							info.setRelationEquName("");
						} else {
							String percolatorId = equipmentRelationService.selectInfoByFlowmeterId(info.getId())
									.getPercolatorId();
							if (null == percolatorId) {
								info.setRelationEquName("");
							} else {
								if (null == equipmentInfoService.getEquipmentInfoById(percolatorId)) {
									info.setRelationEquName("");
								} else {
									String relationEquName = equipmentInfoService.getEquipmentInfoById(percolatorId)
											.getName();
									info.setRelationEquName(relationEquName);
								}
							}
						}
					} else if (info.getEquipmentType() == 5) {
						if (null == equipmentRelationService.selectInfoByPiezometerId(info.getId())) {
							info.setRelationEquName("");
						} else {
							String valveControllerId = equipmentRelationService.selectInfoByPiezometerId(info.getId())
									.getValveControllerId();
							if (null == valveControllerId) {
								info.setRelationEquName("");
							} else {
								if (null == equipmentInfoService.getEquipmentInfoById(valveControllerId)) {
									info.setRelationEquName("");
								} else {
									String relationEquName = equipmentInfoService
											.getEquipmentInfoById(valveControllerId).getName();
									info.setRelationEquName(relationEquName);
								}
							}
						}
					}
				}

				if (null == info.getRelationEquName()) {
					row.createCell(6).setCellValue("");
				} else {
					row.createCell(6).setCellValue(info.getRelationEquName());
				}
				if (null == info.getRemarks()) {
					row.createCell(7).setCellValue("");
				} else {
					row.createCell(7).setCellValue(info.getRemarks());
				}
			}
		}
		// 指定下载的文件名
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fifleName, "UTF-8") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {
			OutputStream output = response.getOutputStream();
			BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@ApiOperation(value = "报警信息导出列表", notes = "报警信息导出列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "equType", value = "设备类型", required = false, dataType = "Integer"),
			@ApiImplicitParam(name = "warnType", value = "报警类型", required = false, dataType = "Integer"),
			@ApiImplicitParam(name = "warnInfo", value = "报警信息", required = false, dataType = "String"),
			@ApiImplicitParam(name = "remarks", value = "备注信息", required = false, dataType = "String"),
			@ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String"),
			@ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String") })
	@RequestMapping(value = "/exportEquipmentWarn", method = RequestMethod.POST)
	public void exportEquipmentWarn(HttpServletRequest request, HttpServletResponse response, Integer equType,
			Integer warnType, String warnInfo, String remarks, String startTime, String endTime)
			throws UnsupportedEncodingException {
		response.reset();
		List<EquipmentWarn> infos = equipmentWarnService.getEquipmentWarnExport(equType, warnType, warnInfo, remarks,
				startTime, endTime);
		fifleName = "报警信息";
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(fifleName);
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue("");
		cell = row.createCell(1);
		cell.setCellValue("设备编号");
		cell = row.createCell(2);
		cell.setCellValue("设备名称");
		cell = row.createCell(3);
		cell.setCellValue("报警信息");
		cell = row.createCell(4);
		cell.setCellValue("报警类型");
		cell = row.createCell(5);
		cell.setCellValue("报警时间");
		cell = row.createCell(6);
		cell.setCellValue("设备经度");
		cell = row.createCell(7);
		cell.setCellValue("设备纬度");
		cell = row.createCell(8);
		cell.setCellValue("备注信息");
		for (int i = 0; i < infos.size(); i++) {
			EquipmentWarn info = infos.get(i);
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(i + 1);
			if (null == info.getEquipmentNo()) {
				row.createCell(1).setCellValue("");
			} else {
				row.createCell(1).setCellValue(info.getEquipmentNo());
			}
			if (null == info.getEquipmentInfo()) {
				row.createCell(2).setCellValue("");
			} else {
				if(null == info.getEquipmentInfo()) {
					row.createCell(2).setCellValue("");
				} else {
					if(null == info.getEquipmentInfo().getName()) {
						row.createCell(2).setCellValue("");
					}else {
						row.createCell(2).setCellValue(info.getEquipmentInfo().getName());
					}
				}
			}
			if (null == info.getWarnInfo()) {
				row.createCell(3).setCellValue("");
			} else {
				row.createCell(3).setCellValue(info.getWarnInfo());
			}
			if (null == info.getWarnType()) {
				row.createCell(4).setCellValue("");
			} else {
				if (null == info.getEquipmentType()) {
					row.createCell(4).setCellValue("");
				} else {
					if (info.getEquipmentType() == 1) {
						// 水肥报警
						if(null == sysDictService.getSfExport(info.getWarnType())) {
							row.createCell(4).setCellValue("");
						} else {
							row.createCell(4).setCellValue(sysDictService.getSfExport(info.getWarnType()).getLabel());
						}
					}
					else if (info.getEquipmentType() == 4) {
						// 流量计报警
						if(null == sysDictService.getLlExport(info.getWarnType())) {
							row.createCell(4).setCellValue("");
						} else {
							row.createCell(4).setCellValue(sysDictService.getLlExport(info.getWarnType()).getLabel());
						}
					}
					else if (info.getEquipmentType() == 5) {
						// 压力计报警
						if(null == sysDictService.getYlExport(info.getWarnType())) {
							row.createCell(4).setCellValue("");
						} else {
							row.createCell(4).setCellValue(sysDictService.getYlExport(info.getWarnType()).getLabel());
						}
					} else {
						row.createCell(4).setCellValue("");
					}
				}
			}
			if (null == info.getUpdateTime()) {
				row.createCell(5).setCellValue("");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				row.createCell(5).setCellValue(sdf.format(info.getCreateTime()));
			}
			if (null == info.getEquipmentInfo()) {
				row.createCell(6).setCellValue("");
			} else {
				if(null == info.getEquipmentInfo().getPointlng()) {
					row.createCell(6).setCellValue("");
				}else {
					row.createCell(6).setCellValue((info.getEquipmentInfo().getPointlng()).toString());
				}
			}
			if (null == info.getEquipmentInfo()) {
				row.createCell(7).setCellValue("");
			} else {
				if(null == info.getEquipmentInfo().getPointlat()) {
					row.createCell(7).setCellValue("");
				}
				else {
					row.createCell(7).setCellValue((info.getEquipmentInfo().getPointlat()).toString());
				}
			}
			if (null == info.getRemarks()) {
				row.createCell(8).setCellValue("");
			} else {
				row.createCell(8).setCellValue(info.getRemarks());
			}
		}
		// 指定下载的文件名
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fifleName, "UTF-8") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {
			OutputStream output = response.getOutputStream();
			BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@ApiOperation(value = "分组信息导出列表", notes = "分组信息导出列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "分组名称", required = false, dataType = "String"),
			@ApiImplicitParam(name = "officeId", value = "归属机构id", required = false, dataType = "String") })
	@RequestMapping(value = "/exportEquipmentGrouping", method = RequestMethod.POST)
	public void exportEquipmentGrouping(HttpServletRequest request, HttpServletResponse response,
			EquipmentGrouping equipmentGrouping) throws UnsupportedEncodingException {
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null) {
			return;
		}
		String token = authHeader.substring(11);
		Map<Object, Object> map = redisTemplate.opsForHash().entries(token);
		String currOfficeId = map.get("officeId") + "";
		String isSys = map.get("isSys")+"";
		if (!StringUtils.isEmpty(equipmentGrouping.getOfficeId())) {
            currOfficeId = equipmentGrouping.getOfficeId();
        }
		SysOffice sysOffice = sysOfficeService.selectById(currOfficeId);
		//判断是否为首部类型
		 List<String> shoubuIdList = new ArrayList<>();
		if (sysOffice.getType().equals("3")) { 
			shoubuIdList.add(currOfficeId);
		}else { // 非首部类型
            // 1.查询首部系统
            List<SysOffice> sysOfficeList = sysOfficeService.selectShouBuByOfficeId(currOfficeId);
            shoubuIdList.add(currOfficeId);
            for (SysOffice sysOffice1 : sysOfficeList) {
                shoubuIdList.add(sysOffice1.getId());
            }
		}
		response.reset();
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<EquipmentGrouping> infos = new ArrayList<>();
		if(isSys.equals("1") && StringUtils.isEmpty(equipmentGrouping.getOfficeId())) {
			currOfficeId = null;
			equipmentGrouping.setOfficeId(currOfficeId);
			infos = equipmentGroupingService.getGroupingListInfo(equipmentGrouping);
		} else {
			for(int j=0;j<shoubuIdList.size();j++) {
				equipmentGrouping.setOfficeId(shoubuIdList.get(j));
				 List<EquipmentGrouping> infos2 = equipmentGroupingService.getGroupingListInfo(equipmentGrouping);
				 for(int n=0;n<infos2.size();n++) {
					 EquipmentGrouping equ = infos2.get(n);
					 infos.add(equ);
				 }
			}
		}
		fifleName = "分组信息";
		XSSFSheet sheet = workbook.createSheet(fifleName);
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue("");
		cell = row.createCell(1);
		cell.setCellValue("分组名称");
		cell = row.createCell(2);
		cell.setCellValue("归属机构");
		cell = row.createCell(3);
		cell.setCellValue("更新时间");
		cell = row.createCell(5);
		cell.setCellValue("启用状态");
		cell = row.createCell(4);
		cell.setCellValue("备注");
		for (int i = 0; i < infos.size(); i++) {
			EquipmentGrouping info = infos.get(i);
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(i + 1);
			if (null == info.getName()) {
				row.createCell(1).setCellValue("");
			} else {
				row.createCell(1).setCellValue(info.getName());
			}
			if(info.getParentOfficeName() == null) {
				if(info.getOfficeName() == null) {
					row.createCell(2).setCellValue("");
				}else {
					row.createCell(2).setCellValue(info.getOfficeName());
				}
			}else {
				if(info.getOfficeName() == null) {
					row.createCell(2).setCellValue(info.getParentOfficeName());
				}else {
					row.createCell(2).setCellValue(info.getParentOfficeName() + info.getOfficeName());
				}
			}
			if (null == info.getUpdateTime()) {
				row.createCell(3).setCellValue("");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				row.createCell(3).setCellValue(sdf.format(info.getUpdateTime()));
			}
			if (null == info.getIsenabled()) {
				row.createCell(5).setCellValue("不启用");
			} else {
				if (info.getIsenabled() == 1) {
					row.createCell(5).setCellValue("不启用");
				} else {
					row.createCell(5).setCellValue("启用");
				}
			}
			if (null == info.getRemarks()) {
				row.createCell(4).setCellValue("");
			} else {
				row.createCell(4).setCellValue(info.getRemarks());
			}
		}
		// 指定下载的文件名
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fifleName, "UTF-8") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {
			OutputStream output = response.getOutputStream();
			BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@ApiOperation(value = "智能控制信息导出列表", notes = "智能控制信息导出列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "任务名称", required = false, dataType = "String"),
			@ApiImplicitParam(name = "officeId", value = "归属id", required = false, dataType = "String"),
			@ApiImplicitParam(name = "type", value = "任务类型(1定时灌溉2自动施肥)", required = true, dataType = "String") })
	@RequestMapping(value = "/exportEquipmentTask", method = RequestMethod.POST)
	public void exportEquipmentTask(HttpServletRequest request, HttpServletResponse response,
			EquipmentTask equipmentTask) throws UnsupportedEncodingException {
		response.reset();
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null) {
			return;
		}
		String token = authHeader.substring(11);
		Map<Object, Object> map = redisTemplate.opsForHash().entries(token);
		String currOfficeId = map.get("officeId") + "";
		String isSys = map.get("isSys")+"";
		if (!StringUtils.isEmpty(equipmentTask.getOfficeId())) {
            currOfficeId = equipmentTask.getOfficeId();
        }
		if(StringUtils.isEmpty(equipmentTask.getOfficeId()) && isSys.equals("1")) {
			currOfficeId = null;
		}
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (equipmentTask.getType().equals("1")) {
			equipmentTask.setOfficeId(currOfficeId);
			List<EquipmentTask> infos = equipmentTaskService.getEquipmentTaskExport(equipmentTask);
			fifleName = "定时灌溉";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("任务名称");
			cell = row.createCell(2);
			cell.setCellValue("任务类型");
			cell = row.createCell(3);
			cell.setCellValue("执行设备");
			cell = row.createCell(4);
			cell.setCellValue("开始时间");
			cell = row.createCell(5);
			cell.setCellValue("结束时间");
			cell = row.createCell(6);
			cell.setCellValue("重复周期");
			cell = row.createCell(7);
			cell.setCellValue("备注");
			cell = row.createCell(8);
			cell.setCellValue("启用状态");
			for (int i = 0; i < infos.size(); i++) {
				EquipmentTask info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getName());
				}
				if (null == info.getType()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getType());
				}
				if (null == info.getEquList()) {
					row.createCell(3).setCellValue("");
				} else {
					row.createCell(3).setCellValue(info.getEquList());
				}
				if (null == info.getOpenhour() && null == info.getOpenmin()
						|| (info.getOpenhour().equals("") && info.getOpenmin().equals(""))) {
					row.createCell(4).setCellValue("");
				} else {
					startOpenTime = info.getOpenhour() + ":" + info.getOpenmin() + "分";
					row.createCell(4).setCellValue(startOpenTime);
				}
				if (null == info.getClosehour() && null == info.getClosemin()) {
					row.createCell(5).setCellValue("");
				} else {
					endCloseTime = info.getClosehour() + ":" + info.getClosemin() + "分";
					row.createCell(5).setCellValue(endCloseTime);
				}
				String str = info.getReturnTime();
				if (null == str) {
					row.createCell(6).setCellValue("");
				} else {
					String[] str2 = str.split(",");
					String week = "";
					for (int j = 0; j < str2.length; j++) {
						switch (str2[j]) {
						case "1":
							week = week + "星期一";
							break;
						case "2":
							week = week + "星期二";
							break;
						case "3":
							week = week + "星期三";
							break;
						case "4":
							week = week + "星期四";
							break;
						case "5":
							week = week + "星期五";
							break;
						case "6":
							week = week + "星期六";
							break;
						case "0":
							week = week + "星期日";
							break;
						default:
							week = week + "";
							break;
						}
					}
					row.createCell(6).setCellValue(week);
				}
				if (null == info.getRemarks()) {
					row.createCell(7).setCellValue("");
				} else {
					row.createCell(7).setCellValue(info.getRemarks());
				}
				if (null == info.getStatus()) {
					row.createCell(8).setCellValue("停用");
				} else {
					if (info.getStatus() == 0) {
						row.createCell(8).setCellValue("启用");
					} else {
						row.createCell(8).setCellValue("停用");
					}
				}
			}

		} else if (equipmentTask.getType().equals("2")) {
			equipmentTask.setOfficeId(currOfficeId);
			List<EquipmentTask> infos = equipmentTaskService.getEquipmentTaskExport(equipmentTask);
			fifleName = "施肥机信息";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("任务名称");
			cell = row.createCell(2);
			cell.setCellValue("任务类型");
			cell = row.createCell(3);
			cell.setCellValue("执行设备");
			cell = row.createCell(4);
			cell.setCellValue("开始时间");
			cell = row.createCell(5);
			cell.setCellValue("注肥时间(min)");
			cell = row.createCell(6);
			cell.setCellValue("施肥量(L)");
			cell = row.createCell(7);
			cell.setCellValue("重复周期");
			cell = row.createCell(8);
			cell.setCellValue("备注");
			cell = row.createCell(9);
			cell.setCellValue("启用状态");
			for (int i = 0; i < infos.size(); i++) {
				EquipmentTask info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getName());
				}
				if (null == info.getType()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getType());
				}
				if (null == info.getEquList()) {
					row.createCell(3).setCellValue("");
				} else {
					row.createCell(3).setCellValue(info.getEquList());
				}
				if (null == info.getOpenhour() && null == info.getOpenmin()
						|| (info.getOpenhour().equals("") && info.getOpenmin().equals(""))) {
					row.createCell(4).setCellValue("");
				} else {
					startOpenTime = info.getOpenhour() + ":" + info.getOpenmin() + "分";
					row.createCell(4).setCellValue(startOpenTime);
				}
				// 注肥时间
				if(null == info.getFertilizingTime() || info.getFertilizingTime().equals("")) {
					row.createCell(5).setCellValue("");
				}else {
					row.createCell(5).setCellValue(info.getFertilizingTime());
				}
				/*if (null == info.getClosehour() && null == info.getClosemin()
						|| (info.getClosehour().equals("") && info.getClosemin().equals(""))) {
					row.createCell(5).setCellValue("");
				} else {
					endCloseTime = info.getClosehour() + ":" + info.getClosemin() + "分";
					int fertilizerTime = (Integer.parseInt(info.getClosehour()) - Integer.parseInt(info.getOpenhour()))
							* 60 + (Integer.parseInt(info.getClosemin()) - Integer.parseInt(info.getOpenmin()));
					row.createCell(5).setCellValue(fertilizerTime);
					System.out.println(fertilizerTime);
				}*/
				if (null == info.getFertilizingAmount() || info.getFertilizingAmount().equals("")) {
					row.createCell(6).setCellValue("");
				} else {
					row.createCell(6).setCellValue(info.getFertilizingAmount().toString());
				}
				String str = info.getReturnTime();
				if (null == str || str.equals("")) {
					row.createCell(7).setCellValue("");
				} else {
					String[] str2 = str.split(",");
					String week = "";
					for (int j = 0; j < str2.length; j++) {
						switch (str2[j]) {
						case "1":
							week = week + "星期一";
							break;
						case "2":
							week = week + "星期二";
							break;
						case "3":
							week = week + "星期三";
							break;
						case "4":
							week = week + "星期四";
							break;
						case "5":
							week = week + "星期五";
							break;
						case "6":
							week = week + "星期六";
							break;
						case "0":
							week = week + "星期日";
							break;
						default:
							week = week + "";
							break;
						}
					}
					row.createCell(7).setCellValue(week);
				}
				if (null == info.getRemarks()) {
					row.createCell(8).setCellValue("");
				} else {
					row.createCell(8).setCellValue(info.getRemarks());
				}
				if (null == info.getStatus()) {
					row.createCell(9).setCellValue("停用");
				} else {
					if (info.getStatus() == 0) {
						row.createCell(9).setCellValue("启用");
					} else {
						row.createCell(9).setCellValue("暂停");
					}
				}
			}
		}
		// 指定下载的文件名
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fifleName, "UTF-8") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {
			OutputStream output = response.getOutputStream();
			BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@ApiOperation(value = "日志信息导出列表", notes = "日志信息导出列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "任务类型(1定时灌溉2自动施肥)", required = false, dataType = "String"),
			@ApiImplicitParam(name = "start", value = "开始时间", required = false, dataType = "String"),
			@ApiImplicitParam(name = "end", value = "结束时间", required = false, dataType = "String"),
			@ApiImplicitParam(name = "name", value = "设备名称", required = false, dataType = "String") })
	@RequestMapping(value = "/exportControlLog", method = RequestMethod.POST)
	public void exportControlLogo(HttpServletRequest request, HttpServletResponse response, String name, String type,
			String start, String end,String officeId) throws UnsupportedEncodingException {
		response.reset();
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null) {
			return;
		}
		String token = authHeader.substring(11);
		Map<Object, Object> map = redisTemplate.opsForHash().entries(token);
		String currOfficeId = map.get("officeId") + "";
		String isSys = map.get("isSys")+"";
		if (!StringUtils.isEmpty(officeId)) {
            currOfficeId = officeId;
        }
		if(StringUtils.isEmpty(officeId) && isSys.equals("1")) {
			currOfficeId = null;
		}
		String stateName = "";
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<ControlLog> infos = controlLogService.getLogExport(name, type, start, end,currOfficeId);
		fifleName = "操作日志信息";
		XSSFSheet sheet = workbook.createSheet(fifleName);
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue("");
		cell = row.createCell(1);
		cell.setCellValue("任务名称");
		cell = row.createCell(2);
		cell.setCellValue("任务类型");
		cell = row.createCell(3);
		cell.setCellValue("执行设备");
		cell = row.createCell(4);
		cell.setCellValue("执行时间");
		cell = row.createCell(5);
		cell.setCellValue("执行结果");
		for (int i = 0; i < infos.size(); i++) {
			ControlLog info = infos.get(i);
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(i + 1);
			if (null == info.getName()) {
				row.createCell(1).setCellValue("");
			} else {
				row.createCell(1).setCellValue(info.getName());
			}
			if (null == info.getTaskType()) {
				row.createCell(2).setCellValue("");
			} else {
				row.createCell(2).setCellValue(info.getTaskType());
			}
			if (null == info.getEquList()) {
				row.createCell(3).setCellValue("");
			} else {
				row.createCell(3).setCellValue(info.getEquList());
			}
			if (null == info.getCreateDate() || info.getCreateDate().equals("")) {
				row.createCell(4).setCellValue("");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				row.createCell(4).setCellValue(sdf.format(info.getCreateDate()));
			}
			switch (info.getState()) {
			case 0:
				stateName = "执行成功";
				break;
			case 1:
				stateName = "执行失败";
				break;
			case 10:
				stateName = "开始成功";
				break;
			case 11:
				stateName = "开始失败";
				break;
			case 20:
				stateName = "关闭成功";
				break;
			case 21:
				stateName = "关闭失败";
				break;
			default:
				break;
			}
			row.createCell(5).setCellValue(info.getTaskType() + stateName);
		}
		// 指定下载的文件名
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fifleName, "UTF-8") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {
			OutputStream output = response.getOutputStream();
			BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@ApiOperation(value = "系统设置相关信息导出列表", notes = "系统设置相关信息导出列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "loginName", value = "登录名", required = false, dataType = "String"),
			@ApiImplicitParam(name = "name", value = "机构名称/菜单名称/角色名称", required = false, dataType = "String"),
			@ApiImplicitParam(name = "type", value = "机构类型(1公司2园区3首部)", required = false, dataType = "String"),
			@ApiImplicitParam(name = "isShow", value = "是否展示(0:展示 1:不展示)", required = false, dataType = "String"),
			@ApiImplicitParam(name = "requestType", value = "导出类型（0用户管理1机构管理2菜单管理3角色管理）", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "officeId", value = "归属id", required = false, dataType = "String") })
	@RequestMapping(value = "/exportSysInfo", method = RequestMethod.POST)
	public void exportSysInfo(HttpServletRequest request, HttpServletResponse response, SysUser sysUser,
			SysMenu sysMenu, SysRole sysRole, SysOffice sysOffice, int requestType)
			throws UnsupportedEncodingException {
		response.reset();
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null) {
			return;
		}
		String token = authHeader.substring(11);
		Map<Object, Object> map = redisTemplate.opsForHash().entries(token);
		String currOfficeId = map.get("officeId") + "";
		String isSys = map.get("isSys")+"";
		if (!StringUtils.isEmpty(sysUser.getOfficeId())) {
            currOfficeId = sysUser.getOfficeId();
        }
		SysOffice sysOffice1 = sysOfficeService.selectById(currOfficeId);
		//判断是否为首部类型
		 List<String> shoubuIdList = new ArrayList<>();
		if (sysOffice1.getType().equals("3")) { 
			shoubuIdList.add(sysOffice1.getId());
		}else { // 非首部类型
            // 1.查询首部系统
            List<SysOffice> sysOfficeList = sysOfficeService.selectShouBuByOfficeId(currOfficeId);
            shoubuIdList.add(currOfficeId);
            for (SysOffice sysOffice2 : sysOfficeList) {
                shoubuIdList.add(sysOffice2.getId());
            }
		}
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (requestType == 0) {
			List<SysUser> infos = new ArrayList<>();
			if(isSys.equals("1")/* && StringUtil.isEmpty(sysUser.getLoginName())*/) {
				infos = sysService.getListSysUserInfo(sysUser);
			} else {
				for(int j=0;j<shoubuIdList.size();j++) {
					sysUser.setOfficeId(shoubuIdList.get(j));
					 List<SysUser> infos2 = sysService.getListSysUserInfo(sysUser);
					 for(int n=0;n<infos2.size();n++) {
						 SysUser sys = infos2.get(n);
						 infos.add(sys);
					 }
				}
			}
			fifleName = "用户管理";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("归属机构");
			cell = row.createCell(2);
			cell.setCellValue("登录名");
			cell = row.createCell(3);
			cell.setCellValue("姓名");
			cell = row.createCell(4);
			cell.setCellValue("工号");
			cell = row.createCell(5);
			cell.setCellValue("手机");
			for (int i = 0; i < infos.size(); i++) {
				SysUser info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getOfficeName()) {
					row.createCell(1).setCellValue("");
				} else {
					if (null == info.getParentName()) {
						row.createCell(1).setCellValue(info.getOfficeName());
					} else {
						row.createCell(1).setCellValue(info.getParentName() + info.getOfficeName());
					}
				}
				if (null == info.getLoginName()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getLoginName());
				}
				if (null == info.getName()) {
					row.createCell(3).setCellValue("");
				} else {
					row.createCell(3).setCellValue(info.getName());
				}
				if (null == info.getNo()) {
					row.createCell(4).setCellValue("");
				} else {
					row.createCell(4).setCellValue(info.getNo());
				}
				if (null == info.getMobile()) {
					row.createCell(5).setCellValue("");
				} else {
					row.createCell(5).setCellValue(info.getMobile());
				}
			}
		} else if (requestType == 1) {
			List<SysOffice> infos = new ArrayList<>();
			if(isSys.equals("1")) {
				infos = sysOfficeService.selectByOffice(sysOffice);
			} else {
			for(int j=0;j<shoubuIdList.size();j++) {
				sysOffice.setId(shoubuIdList.get(j));
				 List<SysOffice> infos2 = sysOfficeService.selectByOffice(sysOffice);
				 for(int n=0;n<infos2.size();n++) {
					 SysOffice sysOff = infos2.get(n);
					 infos.add(sysOff);
				 }
			}
			}
			fifleName = "机构管理";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("机构名称");
			cell = row.createCell(2);
			cell.setCellValue("归属机构");
			cell = row.createCell(3);
			cell.setCellValue("机构编码");
			cell = row.createCell(4);
			cell.setCellValue("机构层级");
			cell = row.createCell(5);
			cell.setCellValue("备注");
			for (int i = 0; i < infos.size(); i++) {
				SysOffice info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getName());
				}
				if (null == info.getParentName()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getParentName());
				}
				if (null == info.getCode()) {
					row.createCell(3).setCellValue("");
				} else {
					row.createCell(3).setCellValue(info.getCode());
				}
				if (null == info.getType()) {
					row.createCell(4).setCellValue("");
				} else {
					String typeName = sysDictService.getSysOfficeExport(info.getType()).getLabel();
					row.createCell(4).setCellValue(typeName);
				}
				if (null == info.getRemarks()) {
					row.createCell(5).setCellValue("");
				} else {
					row.createCell(5).setCellValue(info.getRemarks());
				}
			}
		} else if (requestType == 2) {
	
			List<SysMenu> infos = sysService.selectBySysMenu(sysMenu);
			fifleName = "菜单管理";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("菜单名称");
			cell = row.createCell(2);
			cell.setCellValue("链接");
			cell = row.createCell(3);
			cell.setCellValue("排序");
			/*cell = row.createCell(4);
			cell.setCellValue("菜单状态");*/
			cell = row.createCell(4);
			cell.setCellValue("父级菜单");
			for (int i = 0; i < infos.size(); i++) {
				SysMenu info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getName());
				}
				if (null == info.getTarget()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getTarget());
				}
				if (null == info.getSort()) {
					row.createCell(3).setCellValue("");
				} else {
					row.createCell(3).setCellValue(info.getSort().toString());
				}
				/*if (null == info.getIsShow()) {
					row.createCell(4).setCellValue("");
				} else {
					if (info.getIsShow().equals("0")) {
						row.createCell(4).setCellValue("显示");
					} else {
						row.createCell(4).setCellValue("不显示");
					}
				}*/
				if (null == info.getParentName()) {
					row.createCell(4).setCellValue("");
				} else {
					row.createCell(4).setCellValue(info.getParentName());
				}
			}
		} else if (requestType == 3) {
			List<SysRole> infos = new ArrayList<>();
			if(isSys.equals("1") && StringUtils.isEmpty(sysRole.getOfficeId())) {
				infos = sysService.selectBySysRole(sysRole);
			} else {
				for(int j=0;j<shoubuIdList.size();j++) {
					sysRole.setOfficeId(shoubuIdList.get(j));
					 List<SysRole> infos2 = sysService.selectBySysRole(sysRole);
					 for(int n=0;n<infos2.size();n++) {
						 SysRole sysR = infos2.get(n);
						 infos.add(sysR);
					 }
				}
			}
			fifleName = "角色管理";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("角色名称");
			cell = row.createCell(2);
			cell.setCellValue("归属机构");
			
			for (int i = 0; i < infos.size(); i++) {
				SysRole info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getName());
				}
				if (null == info.getOfficeName()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getOfficeName());
				}
			}
		}
		// 指定下载的文件名
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fifleName, "UTF-8") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {
			OutputStream output = response.getOutputStream();
			BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@ApiOperation(value = "统计相关信息导出列表", notes = "统计相关信息导出列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "address", value = "设备id", required = false, dataType = "String"),
			@ApiImplicitParam(name = "start", value = "开始时间", required = false, dataType = "String"),
			@ApiImplicitParam(name = "end", value = "结束时间", required = false, dataType = "String"),
			@ApiImplicitParam(name = "type", value = "导出类型（0用水量明细1施肥量明细)", required = true, dataType = "Integer") })
	@RequestMapping(value = "/exportValveInfo", method = RequestMethod.POST)
	public void exportUserByAuto(HttpServletRequest request, HttpServletResponse response, String start, String end,
			String address, int type) throws UnsupportedEncodingException {
		response.reset();
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (type == 0) {
			List<GsmMeterCumulativeflowforday> infos = gsmMeterCountService.selectWaterInfoList(start, end, address);
			fifleName = "用水量明细";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("设备名称");
			cell = row.createCell(2);
			cell.setCellValue("设备编号");
			cell = row.createCell(3);
			cell.setCellValue("归属机构");
			cell = row.createCell(4);
			cell.setCellValue("用水量(m3)");
			cell = row.createCell(5);
			cell.setCellValue("时间");
			for (int i = 0; i < infos.size(); i++) {
				GsmMeterCumulativeflowforday info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getEquipmentName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getEquipmentName());
				}
				if (null == info.getEquipmentNo()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getEquipmentNo());
				}
				if (null == info.getOfficeName()) {
					row.createCell(3).setCellValue("");
				} else {
					row.createCell(3).setCellValue(info.getOfficeName());
				}
				if (null == info.getSumWaterDay()) {
					row.createCell(4).setCellValue("");
				} else {
					row.createCell(4).setCellValue(info.getSumWaterDay());
				}
				if (null == info.getCopyDate()) {
					row.createCell(5).setCellValue("");
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					row.createCell(5).setCellValue(sdf.format(info.getCopyDate()));
				}
			}
		} else if (type == 1) {
			List<WmMetercalcFlowdetail> infos = wmTerminalinfoService.selectSfInfoList(start, end, address);
			fifleName = "施肥量明细";
			XSSFSheet sheet = workbook.createSheet(fifleName);
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("设备名称");
			cell = row.createCell(2);
			cell.setCellValue("设备编号");
			cell = row.createCell(3);
			cell.setCellValue("归属机构");
			cell = row.createCell(4);
			cell.setCellValue("施肥量(m3)");
			cell = row.createCell(5);
			cell.setCellValue("时间");
			for (int i = 0; i < infos.size(); i++) {
				WmMetercalcFlowdetail info = infos.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				if (null == info.getEquipmentName()) {
					row.createCell(1).setCellValue("");
				} else {
					row.createCell(1).setCellValue(info.getEquipmentName());
				}
				if (null == info.getEquipmentNo()) {
					row.createCell(2).setCellValue("");
				} else {
					row.createCell(2).setCellValue(info.getEquipmentNo());
				}
				if (null == info.getOfficeName()) {
					row.createCell(3).setCellValue("");
				} else {
					row.createCell(3).setCellValue(info.getOfficeName());
				}
				if (null == info.getSfDay()) {
					row.createCell(4).setCellValue("");
				} else {
					row.createCell(4).setCellValue(info.getSfDay());
				}
				if (null == info.getCollectDate()) {
					row.createCell(5).setCellValue("");
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					row.createCell(5).setCellValue(sdf.format(info.getCollectDate()));
				}
			}
		}
		// 指定下载的文件名
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fifleName, "UTF-8") + ".xlsx");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		try {
			OutputStream output = response.getOutputStream();
			BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@ApiOperation(value = "阀控器批量导入", notes = "阀控器批量导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "filename", value = "文件名称", required = true, dataType = "MultipartFile") })
	@RequestMapping(value = "uploadFkqValve", method = RequestMethod.POST)
	public BaseResponse uploadFkqValve(@RequestParam(value = "filename") MultipartFile multipartFile,
			HttpServletRequest request, HttpServletResponse response) {
		String filename = multipartFile.getOriginalFilename();
		if (filename == null || "".equals(filename)) {
			return BaseResponseUtil.success("EXCEL内容不能为空！");
		}
		// 错误行
		int error;
		InputStream input = null;
		try {
			input = multipartFile.getInputStream();
			Workbook workBook = null;
			if (WDWUtil.isExcel2003(filename)) {
				workBook = new HSSFWorkbook(input);
			} else {
				workBook = new XSSFWorkbook(input);
			}
			List<EquipmentInfo> equipmentInfos = readExcelValue(workBook);
			List<EquipmentInfo> NewEquipmentInfos = new ArrayList<>();
			FkqValveInfo fkqValveInfo = new FkqValveInfo();
			// 参数校验循环
			for (int i = 0; i < equipmentInfos.size(); i++) {
				error = i + 2;
				EquipmentInfo oldEquipment = equipmentInfos.get(i);
				if (oldEquipment.getName() == null) {
					return BaseResponseUtil.success("EXCEL第" + error + "设备名称不能为空！");
				}
				if (oldEquipment.getEquipmentNo() == null) {
					return BaseResponseUtil.success("EXCEL第" + error + "设备编号不能为空！");
				}
				if (oldEquipment.getOfficeId() == null) {
					return BaseResponseUtil.success("EXCEL第" + error + "设备归属不能为空！");
				}
				// 判断ecxel中重复数据
				if (i == 0) {
					NewEquipmentInfos.add(equipmentInfos.get(0));
				} else {
					for (EquipmentInfo equipmentInfo : NewEquipmentInfos) {
						if (oldEquipment.getEquipmentNo().equals(equipmentInfo.getEquipmentNo())
								|| oldEquipment.getName().equals(equipmentInfo.getName())) {
							return BaseResponseUtil.success("EXCEL第" + error + "行设备名称或者设备编号重复，请修改后重新导入！");
						}
					}
					NewEquipmentInfos.add(oldEquipment);
				}
			}
			for (EquipmentInfo equipmentInfo : NewEquipmentInfos) {
				// 插入语句
				equipmentInfo.setEquipmentType(3);
				int res = equipmentInfoService.insertValveImport(equipmentInfo);
				fkqValveInfo.setId(IDUtils.createUUID());
				fkqValveInfo.setTelemetryStationAddr(equipmentInfo.getTelemetryStationAddr());
				fkqValveInfo.setCenterStationAddr("00");
				fkqValveInfo.setEquipmentId(equipmentInfo.getId());
				fkqValveInfo.setIsonline(0);
				int res1 = fkqValveInfoService.insertByValveInfoImport(fkqValveInfo);
				// 添加失败
				if (res == 0 || res1 == 0) {
					return BaseResponseUtil
							.success(equipmentInfo.getName() + "电磁阀添加失败,请将该电磁阀修改后和EXCEL中该电磁阀以后的电磁阀重新导入！");
				}
			}
			return BaseResponseUtil.success("批量导入EXCEL成功！");
		} catch (Exception e) {
			System.out.println(e);
			return BaseResponseUtil.success("EXCEL格式不正确！修改后请重新全部导入");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					input = null;
					e.printStackTrace();
				}
			}
		}
	}

	@ResponseBody
	@ApiOperation(value = "用户批量导入", notes = "用户批量导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "filename", value = "文件名称", required = true, dataType = "MultipartFile") })
	@RequestMapping(value = "uploadUser", method = RequestMethod.POST)
	public BaseResponse uploadUser(@RequestParam(value = "filename") MultipartFile multipartFile,
			HttpServletRequest request, HttpServletResponse response) {
		String filename = multipartFile.getOriginalFilename();
		if (filename == null || "".equals(filename)) {
			return BaseResponseUtil.success("EXCEL内容不能为空！");
		}
		// 错误行
		int error;
		InputStream input = null;
		try {
			input = multipartFile.getInputStream();
			Workbook workBook = null;
			if (WDWUtil.isExcel2003(filename)) {
				workBook = new HSSFWorkbook(input);
			} else {
				workBook = new XSSFWorkbook(input);
			}
			List<SysUser> sysUsers = readExcelUser(workBook);
			List<SysUser> NewSysUsers = new ArrayList<>();
			// FkqValveInfo fkqValveInfo = new FkqValveInfo();
			// 参数校验循环
			for (int i = 0; i < sysUsers.size(); i++) {
				error = i + 2;
				SysUser oldSysUsers = sysUsers.get(i);
				if (oldSysUsers.getOfficeId() == null) {
					return BaseResponseUtil.success("EXCEL第" + error + "归属机构不能为空！");
				}
				if (oldSysUsers.getLoginName() == null) {
					return BaseResponseUtil.success("EXCEL第" + error + "登录名不能为空！");
				}
				if (oldSysUsers.getPassword() == null) {
					return BaseResponseUtil.success("EXCEL第" + error + "密码不能为空！");
				}
				if (oldSysUsers.getName() == null) {
					return BaseResponseUtil.success("EXCEL第" + error + "姓名不能为空！");
				}
				// 判断ecxel中重复数据
				if (i == 0) {
					NewSysUsers.add(sysUsers.get(0));
				} else {
					for (SysUser sysUser : NewSysUsers) {
						if (oldSysUsers.getLoginName().equals(sysUser.getLoginName())) {
							return BaseResponseUtil.success("EXCEL第" + error + "登录名重复，请修改后重新导入！");
						}
					}
					NewSysUsers.add(oldSysUsers);
				}
			}
			for (SysUser sysUser : NewSysUsers) {
				// 插入语句
				int res = sysService.insertUserImport(sysUser);
				// 添加失败
				if (res == 0) {
					return BaseResponseUtil.success(sysUser.getLoginName() + "用户信息添加失败,请将修改后的用户和EXCEL中该用户以后的用户信息重新导入！");
				}
			}
			return BaseResponseUtil.success("批量导入EXCEL成功！");
		} catch (Exception e) {
			System.out.println(e);
			return BaseResponseUtil.success("EXCEL格式不正确！修改后请重新全部导入");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					input = null;
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取Excel里面阀控器的信息
	 * 
	 * @param wb
	 * @return
	 */
	private List<EquipmentInfo> readExcelValue(Workbook wb) {
		// 得到第一个shell
		Sheet sheet = wb.getSheetAt(0);
		List<EquipmentInfo> customerList = new ArrayList<EquipmentInfo>();
		EquipmentInfo equipmentInfo;
		String officeId = null;
		// 循环Excel行数,从第二行开始。标题不入库
		for (int r = 1; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			if (row == null)
				continue;
			equipmentInfo = new EquipmentInfo();
			equipmentInfo.setId(IDUtils.createUUID());
			equipmentInfo.setCreateTime(new Date());
			equipmentInfo.setDelFlag(0);
			// 循环Excel的列
			for (int c = 0; c < 10/* row.getPhysicalNumberOfCells() */; c++) {
				Cell cell = row.getCell(c);
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					if (c == 0) {
						equipmentInfo.setName(cell.getStringCellValue());
					} else if (c == 1) {
						equipmentInfo.setEquipmentNo(cell.getStringCellValue());
					} else if (c == 2) {
						equipmentInfo.setTelemetryStationAddr(cell.getStringCellValue());
					}
					else if (c == 3) {
						String[] addr = cell.getStringCellValue().split("-");
						String officeParentId = sysOfficeService.selectByNameImport(addr[0]).getId();
						if(addr.length>1) {
							officeId = sysOfficeService.selectByOfficeIdImport(officeParentId,addr[1]).getId();
							equipmentInfo.setOfficeId(officeId);
						}else {
							officeId = officeParentId;
							equipmentInfo.setOfficeId(officeId);
						}
					} 
					else if (c == 4) {
						if (null == equipmentGroupingService.selectByOfficeNameImport(officeId,cell.getStringCellValue())) {
							equipmentInfo.setGroupingId("");
						} else {
							String groupingId = equipmentGroupingService.selectByOfficeNameImport(officeId,cell.getStringCellValue())
									.getId();
							equipmentInfo.setGroupingId(groupingId);
						}
					} else if (c == 5) {
						if (null == cell.getStringCellValue() || cell.getStringCellValue().equals("离线")) {
							equipmentInfo.setIsonline(0);
						} else {
							equipmentInfo.setIsonline(1);
						}
					} else if (c == 6) {
						equipmentInfo.setPointlng(new BigDecimal(cell.getStringCellValue()));
					} else if (c == 7) {
						equipmentInfo.setPointlat(new BigDecimal(cell.getStringCellValue()));
					} else if (c == 8) {
						if (null == cell.getStringCellValue()) {
							equipmentInfo.setCreateBy("");
						} else {
							if (null == sysService.selectByNameImport(cell.getStringCellValue())) {
								equipmentInfo.setCreateBy("");
							} else {
								String createBy = sysService.selectByNameImport(cell.getStringCellValue()).getId();
								equipmentInfo.setCreateBy(createBy);
							}
						}
					} else if (c == 9) {
						equipmentInfo.setRemarks(cell.getStringCellValue());
					}
				}
			}
			customerList.add(equipmentInfo);
		}
		return customerList;
	}

	/**
	 * 读取Excel里面用户的信息
	 * 
	 * @param wb
	 * @return
	 */
	private List<SysUser> readExcelUser(Workbook wb) {
		// 得到第一个shell
		Sheet sheet = wb.getSheetAt(0);
		List<SysUser> customerList = new ArrayList<SysUser>();
		SysUser sysUser;
		// 循环Excel行数,从第二行开始。标题不入库
		for (int r = 1; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			if (row == null)
				continue;
			sysUser = new SysUser();
			sysUser.setId(IDUtils.createUUID());
			sysUser.setCreateTime(new Date());
			sysUser.setDelFlag(0);
			sysUser.setOnline("0");
			// 循环Excel的列
			for (int c = 0; c < 10; c++) {
				Cell cell = row.getCell(c);
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					if (c == 0) {
						sysUser.setName(cell.getStringCellValue());
					} else if (c == 1) {
						sysUser.setNo(cell.getStringCellValue());
					} else if (c == 2) {
						sysUser.setLoginName(cell.getStringCellValue());
					} else if (c == 3) {
						sysUser.setPassword(DigestUtils.sha1Hex(cell.getStringCellValue()));
					} else if (c == 4) {
						sysUser.setMobile(cell.getStringCellValue());
					} else if (c == 5) {
						
						String[] addr = cell.getStringCellValue().split("-");
						String officeParentId = sysOfficeService.selectByNameImport(addr[0]).getId();
						if(addr.length>1) {
							String officeId = sysOfficeService.selectByOfficeIdImport(officeParentId,addr[1]).getId();
							sysUser.setOfficeId(officeId);
						} else {
							sysUser.setOfficeId(officeParentId);
						}
					} else if (c == 6) {
						sysUser.setEmail(cell.getStringCellValue());
					} else if (c == 7) {
						if (null == cell.getStringCellValue()) {
							sysUser.setCreateBy("");
						} else {
							if (null == sysService.selectByNameImport(cell.getStringCellValue())) {
								sysUser.setCreateBy("");
							} else {
								String createBy = sysService.selectByNameImport(cell.getStringCellValue()).getId();
								sysUser.setCreateBy(createBy);
							}
						}
					} else if (c == 8) {
						if (null == cell.getStringCellValue() || cell.getStringCellValue().equals("否")) {
							sysUser.setLoginFlag(1);
						} else {
							sysUser.setLoginFlag(0);
						}
					} else if (c == 9) {
						sysUser.setRemarks(cell.getStringCellValue());
					}
				}
			}
			customerList.add(sysUser);
		}
		return customerList;
	}
}
