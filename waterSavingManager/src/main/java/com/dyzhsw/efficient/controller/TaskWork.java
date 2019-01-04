package com.dyzhsw.efficient.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dyzhsw.efficient.entity.ControlLog;
import com.dyzhsw.efficient.service.ControlLogService;
import com.dyzhsw.efficient.service.EquipmentInfoService;
import com.dyzhsw.efficient.service.EquipmentTaskService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.HttpClientTools;
import com.dyzhsw.efficient.utils.IDUtils;

@SuppressWarnings("deprecation")
@RestController
@RequestMapping("/taskWork")
public class TaskWork {
	private static final Logger logger = LoggerFactory.getLogger(TaskWork.class);
	
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
//	@Value("http://120.55.55.228:8104/MicroControl/valveControl/control")
//	private String fkqTask;
//
//	@Value("http://111.10.48.2:8031/jxwatermanureservice/remoteControllService/remoteControlReq")
//	private String wmTask;

	/**
	 * 施肥机接口地址
	 */
	@Value("${jxwater.manureservice}")
	private String manureHost;
	/**
	 * 阀控器接口地址
	 */
	@Value("${jxwater.microcontrol}")
	private String microcontrolHost;
	
	
	@Autowired
	private EquipmentTaskService taskService;
	
	@Autowired
	private ControlLogService controlLogService;
	
	@Autowired
	private EquipmentInfoService equipmentInfoService;
	
	private long time;
	
	// 安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行．
    // Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)
	
	public void timer(String equipmentId,String control1Status, String control2Status,String type,String taskId,String fertilizingTime,String fertilizingAmount) {
		//计算出第一次延迟执行时间
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			public void run() {
                //定时任务内容
             try {
            	 if(type.equals("定时灌溉")) {
            		 work(equipmentId,control1Status,control2Status,taskId); 
            	 }
            	 else if(type.equals("自动施肥")) {
            		 wmWork(equipmentId,fertilizingTime,fertilizingAmount,taskId);             		          		 
            	 }				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}

		
		};
		timer.scheduleAtFixedRate(timerTask, time, 7*24*60*60*1000);
	}
	
	//得到定时任务的周期时间
	@RequestMapping(value="/delayValve",method=RequestMethod.POST)
	public BaseResponse startRun() {
		logger.info("开始执行定时任务");
		try {
			List<Map<String,Object>> valveTask = taskService.selectAllTask();
			//判断当前日期是星期几
			Date now = new Date();
			int currentWeek = getCurrentWeek(now);
			//拼接日期
			String currentDate = dateFormat.format(now).substring(0, 10);
			//得到定时任务表中的执行时间
			for(int i=0;i<valveTask.size();i++) {
				String equList = String.valueOf(valveTask.get(i).get("equList"));  //得到任务id根据指定id查找需要做此定时任务的设备id
                String taskId =  String.valueOf(valveTask.get(i).get("id"));
				String currentTime = currentDate + " " + valveTask.get(i).get("openhour")+ ":"+valveTask.get(i).get("openmin")+ ":00";
				String currentEndTime = currentDate + " " + valveTask.get(i).get("closehour")+ ":"+valveTask.get(i).get("closemin")+ ":00";
			    String control1 = String.valueOf(valveTask.get(i).get("routeOne"));
			    String control2 = String.valueOf(valveTask.get(i).get("routeTwo"));//获取一二两路需要操作状态
			    String conEnd = "1";
			    String type = String.valueOf(valveTask.get(i).get("type"));
				String fertilizingTime = String.valueOf(valveTask.get(i).get("fertilizingTime"));
				String fertilizingAmount = String.valueOf(valveTask.get(i).get("fertilizingAmount"));
			    String cycleDate = String.valueOf(valveTask.get(i).get("returnTime"));//得到的形式如0，1，2，3...需要转换成数组
				String arrCycle[] = cycleDate.split(",");
				int[] dayTime = new int[arrCycle.length];
				for(int j=0;j<arrCycle.length;j++) {
					dayTime[j] = Integer.parseInt(arrCycle[j]);
 					int n = dayTime[j] - currentWeek;
					if(n > 0) {
						time = dateFormat.parse(currentTime).getTime() + 24*60*60*1000*n - now.getTime();
							//获取阀控器id数组
							timer(equList,control1,control2,type,taskId,fertilizingTime,fertilizingAmount);	
							 if(type.equals("定时灌溉")) {
							 time = dateFormat.parse(currentEndTime).getTime() + 24*60*60*1000*n - now.getTime();
									//获取阀控器id数组
							 timer(equList,conEnd,conEnd,type,taskId,null,null);	 
							 }
					}
					if(n < 0) {						
						time = dateFormat.parse(currentTime).getTime() + 24*60*60*1000*(n+7) - now.getTime();
							//获取单个阀控器id							
						    timer(equList,control1,control2,type,taskId,fertilizingTime,fertilizingAmount);
						    if(type.equals("定时灌溉")) {
						    	time = dateFormat.parse(currentEndTime).getTime() + 24*60*60*1000*(n+7) - now.getTime();
										//获取阀控器id数组
								 timer(equList,conEnd,conEnd,type,taskId,null,null);	 
								 }
					}
					if(n == 0){
						long time1 = dateFormat.parse(currentTime).getTime() - now.getTime(); 
						if(time1 >= 0) {
							time = time1;
							//获取单个阀控器id	
							timer(equList,control1,control2,type,taskId,fertilizingTime,fertilizingAmount);	
							if(type.equals("定时灌溉")) {
								 time =  dateFormat.parse(currentEndTime).getTime() - now.getTime(); 
										//获取阀控器id数组
								 timer(equList,conEnd,conEnd,type,taskId,null,null);	 
								 }
						}
						if(time1 < 0) {
							time = dateFormat.parse(currentTime).getTime() + 24*60*60*1000*7 - now.getTime();
								//获取单个阀控器id	
							timer(equList,control1,control2,type,taskId,fertilizingTime,fertilizingAmount);						
							if(type.equals("定时灌溉")) {
								 time = dateFormat.parse(currentEndTime).getTime() + 24*60*60*1000*7 - now.getTime();
										//获取阀控器id数组
								 timer(equList,conEnd,conEnd,type,taskId,null,null);	 
								 }
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return BaseResponseUtil.success("定时任务启动成功。");
		}
		return BaseResponseUtil.success("定时任务启动成功");
	}

	//判断当前日期是周几
	private static int getCurrentWeek(Date date) {
		//0-6对应周日-周一
		int[] weeks = {0,1,2,3,4,5,6};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if(week_index<0){
			week_index = 0;
		} 
		return weeks[week_index];
	}
	
	private void work(String equipmentId,String control1Status, String control2Status,String taskId) throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
	        	String url = this.microcontrolHost + "/MicroControl/valveControl/control";	    		paramMap.put("equipmentId", equipmentId);
	    		paramMap.put("control1Status",control1Status);
	    		paramMap.put("control2Status",control2Status);
	    		String result =HttpClientTools.doPost(url, paramMap);
	            //增加操作日志
                  ControlLog con = new ControlLog();
                  con.setId(IDUtils.createUUID());
                  con.setCreateDate(new Date());
                  con.setControlType(1);
                  con.setTaskId(taskId);  
                  String re = result.substring(13, 16);
                  con.setContent(control1Status+","+control2Status);
                  con.setRemarks(result);
                 if(re.equals("200")) {
                	 if(control1Status.equals("1")&&control2Status.equals("1")) {
                	  con.setState(20);//关闭成功
                	 }else {
                	  con.setState(10);//开启成功 
                	 }               	 
                  }else {
                	  if(control1Status.equals("1")&&control2Status.equals("1")) {
                    	  con.setState(21);//关闭失败
                    	 }else {
                    	  con.setState(11);//开启失败
                    	 }       
                  }
                  controlLogService.insertInfo(con);
               System.out.println(result + new Date());
	 	        
    }
		
	private void wmWork(String equipmentId, String fertilizingTime, String fertilizingAmount,String taskId) {
 
			   String url = this.manureHost + "/jxwatermanureservice/remoteControllService/remoteControlReq";
	
	    		//获取设备No
	            String id = equipmentInfoService.StringNoById(equipmentId);
	            //添加参数
	            Map<String, Object> map = new HashMap<>();
	            map.put("duration", fertilizingTime);
	            map.put("waterYield", fertilizingAmount);
	            JSONObject json =new JSONObject(map);
	            
	            Map<String, Object> paramMap = new HashMap<String, Object>();
	            paramMap.put("paramStr", json.toString());
	    		paramMap.put("terminalId",id);
	    		String result =HttpClientTools.doPost(url, paramMap);
	    		

	            //增加操作日志
                ControlLog con = new ControlLog();
                con.setId(IDUtils.createUUID());
                con.setCreateDate(new Date());
                con.setControlType(2);
                con.setTaskId(taskId);  
                con.setEquipmentId(equipmentId);
                String re = result.substring(14, 17);
                con.setContent(fertilizingTime+"min"+","+fertilizingAmount+"L");
                con.setRemarks(result);
               if(re.equals("200")) {
              	  con.setState(0);
                }else {
              	  con.setState(1);
                }
                controlLogService.insertInfo(con);
	            System.out.println(result + new Date());

	       
		
	}


}
