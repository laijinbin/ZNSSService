package com.ittest.task;

import com.ittest.dao.TaskLogDao;
import com.ittest.entiry.TaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TaskController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TaskLogDao taskLogDao;

    public int i=0;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void dingShiTask(){
        System.out.println("i="+i);
        i++;
        List<Object> TaskLogList=redisTemplate.boundHashOps("TaskLogList").values();
        if (TaskLogList.size()>0){
            for (Object obj : TaskLogList) {
                TaskLog taskLog=(TaskLog) obj;
                long endTime=new Date().getTime()-(taskLog.getIntervalTime()*60*1000);
                if (taskLog.getCreateTime().getTime()<endTime){
                    //时间到了
                    System.out.println("时间到了");
                    //发送指令去设备

                    //更新到数据库
                    taskLog.setEndTime(new Date());
                    taskLog.setStatus("1");
                    taskLogDao.update(taskLog);
                    //从redis中移除
                    redisTemplate.boundHashOps("TaskLogList").delete(taskLog.getTaskId());

                }

            }
        }


    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void fengShanAutoTask(){
        System.out.println("5秒触发一次");

    }
}
