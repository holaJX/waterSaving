package com.dyzhsw.efficient.crontab;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "crontab")
@ResponseBody
/*设置定时任务*/
public class Task {
    static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    static Scheduler sche;
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
    
    
    @RequestMapping(value = "add")
    public BaseResponse add(String cron,String jobName,String msg,String companytype) {
        try {
            sche = gSchedulerFactory.getScheduler();
            JobDetail jobDetail = new JobDetail(jobName,JOB_GROUP_NAME,QuartzJob.class);// 任务名，任务组，任务执行类
            jobDetail.getJobDataMap().put("msg",msg);
            jobDetail.getJobDataMap().put("companytype",companytype);                 
            CronTrigger trigger = new CronTrigger(jobName,TRIGGER_GROUP_NAME);// 触发器名,触发器组
            trigger.setCronExpression(cron);// 触发器时间设定
            sche.scheduleJob(jobDetail, trigger);
            if (!sche.isShutdown()) {
                sche.start();
            }
            
            return BaseResponseUtil.success(200, "设置定时任务成功", null);
        } catch (Exception e) {
         
            return BaseResponseUtil.error(400,"设置定时任务失败:"+e);
        }
    }

    /*删除定时任务*/
    @RequestMapping(value = "delete")
    @ResponseBody
    public BaseResponse delete(String jobName) throws SchedulerException {
        try {
        	
        		 sche = gSchedulerFactory.getScheduler();
                 sche.getJobGroupNames();
                 sche.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
                 sche.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
                 sche.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
        	
                 return BaseResponseUtil.success(200, "定时任务删除成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.error(400,"定时任务删除失败："+e);
        }
    }

    /*查看已设置的定时任务*/
    @RequestMapping(value = "view")
    @ResponseBody
    public List<Trigger> view(){
        List<Trigger> list = new ArrayList<>();
        try {
        sche = gSchedulerFactory.getScheduler();
            for (String groupName : sche.getJobGroupNames()) {
                for (String jobName : sche.getJobNames(groupName)) {
                    Trigger[] triggers = sche.getTriggersOfJob(jobName,groupName);
                    list.add(triggers[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    /*判断jobname是否存在*/
    @RequestMapping(value = "viewjobname")
    @ResponseBody
    public boolean viewjobname(String njobName){
      boolean flag=false;
        try {
        sche = gSchedulerFactory.getScheduler();
            for (String groupName : sche.getJobGroupNames()) {
                for (String jobName : sche.getJobNames(groupName)) {
                	if(njobName.equals(jobName)){
                		flag=true;
                	}
                	
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
}
