package com.dyzhsw.efficient.crontab;



import org.quartz.*;
import org.springframework.stereotype.Component;


/**
 * quartz示例定时器类
 *
 * @author Administrator
 *
 */
@Component
public class QuartzJob implements Job {
	private final static boolean CLEAN_START = true;
	private final static String CLIENT_ID = "serverdingshi";
	private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s
	public final static long RECONNECTION_ATTEMPT_MAX = 6;
	public final static long RECONNECTION_DELAY = 2000;
	public final static int SEND_BUFFER_SIZE = 64;// 发送最大缓冲

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String msg = (String) jobDataMap.get("msg");
		String jobName = context.getJobDetail().getName();
        String companytype = (String) jobDataMap.get("companytype");
        System.out.println(msg+"========定时任务执行的========");
      
    }
    }