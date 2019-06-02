package com.ittest.task;

import com.ittest.dao.DeviceDao;
import com.ittest.dao.TaskLogDao;
import com.ittest.entiry.CheckWenShiDu;
import com.ittest.entiry.TaskLog;
import com.ittest.service.SMSService;
import com.ittest.socket.WIFIServiceSocket;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TaskController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TaskLogDao taskLogDao;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private SMSService smsService;




    @Scheduled(cron = "0 0/1 * * * ?")
    public void dingShiTask(){
        System.out.println("开始进行定时任务扫描");
        List<Object> TaskLogList=redisTemplate.boundHashOps("TaskLogList").values();
        if (TaskLogList.size()>0){
            for (Object obj : TaskLogList) {
                TaskLog taskLog=(TaskLog) obj;
                long endTime=new Date().getTime()-(taskLog.getIntervalTime()*60*1000);
                if (taskLog.getCreateTime().getTime()<endTime){
                    byte[] bytes=null;
                    //发送指令去设备
                    try {
                        Socket socket=WIFIServiceSocket.socketMap.get(taskLog.getDeviceName());
                        if (socket!=null){
                            OutputStream os = socket.getOutputStream();
                            bytes=(taskLog.getInstructions()+"\r\n").getBytes("utf-8");
                            os.write(bytes);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //更新到数据库
                    taskLog.setEndTime(new Date());
                    taskLog.setStatus("1");
                    taskLogDao.update(taskLog);
                    //从redis中移除
                    redisTemplate.boundHashOps("TaskLogList").delete(taskLog.getTaskId());
                }

            }
        }
        System.out.println("定时任务扫描结束");

    }
   @Scheduled(cron = "0 0/3 * * * ?")
    public void fengShanAutoTask(){
        System.out.println("开始向设备发送温度数据");
      Set commmonDeviceNameList= redisTemplate.boundHashOps("WenShiDuList").keys();
       if (commmonDeviceNameList!=null && commmonDeviceNameList.size()>0){
           for (Object s : commmonDeviceNameList) {
               List<String> deviceNameList=deviceDao.getDeviceNameList(s.toString());
               String wenduMsg= ((String) redisTemplate.boundHashOps("WenShiDuList").get(s)).substring(2,4);
               if (deviceNameList!=null && deviceNameList.size()>0){
                   for (String s1 : deviceNameList) {
                       if (WIFIServiceSocket.socketMap.get(s1)!=null){
                           try {
                               byte[] bytes=("L-"+wenduMsg+"\r\n").getBytes("utf-8");
                               Socket socket=WIFIServiceSocket.socketMap.get(s1);
                               OutputStream os = socket.getOutputStream();
                               os.write(bytes);
                               System.out.println("发送给"+s1+"温度"+wenduMsg);
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }

                   }
               }

           }
       }
        System.out.println("温度数据已发送至设备");

    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void checkWenShiDu(){
        System.out.println("开始检查温湿度");
        //拿到温湿度数据
        Set commmonDeviceNameList= redisTemplate.boundHashOps("WenShiDuList").keys();
        //遍历宿舍，拿到宿舍的设置的温度阈值
        if (commmonDeviceNameList!=null && commmonDeviceNameList.size()>0){
            for (Object o : commmonDeviceNameList) {
                CheckWenShiDu checkWenShiDu= (CheckWenShiDu) redisTemplate.boundHashOps("wenShiDuMaxList").get(o);
                if (checkWenShiDu==null){
                    CheckWenShiDu checkWenShiDu1=new CheckWenShiDu();
                    checkWenShiDu.setCommonDeviceName((String) o);
                    checkWenShiDu.setWenduMax("100");
                    checkWenShiDu.setShiDuMax("200");
                    redisTemplate.boundHashOps("wenShiDuMaxList").put(o,checkWenShiDu);
                }
                //如果溫度超过最大值
                String wenDuData= ((String) redisTemplate.boundHashOps("WenShiDuList").get(o)).substring(2,4);
                    if (Integer.parseInt(wenDuData)>Integer.parseInt(checkWenShiDu.getWenduMax())){
                        //先去数据库查询该宿舍的所有人
                        List<Map<String,String>> smsList=deviceDao.findAllUserByCommmonDeviceName((String)o);
                        smsService.sendSMS(smsList);
                    }

            }
        }
        System.out.println("温湿度检查结束");

    }
}
