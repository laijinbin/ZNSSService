package com.ittest.service;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SMSService {
    @Value("${SmsAppid}")
    private String appid;

    @Value("${Smsappkey}")
    private String appkey;


    @Value("${noticeTemplateId}")
    private String noticeTemplateId;

    @Value("${codeTemplateId}")
    private String codeTemplateId;

    @Value("${smsSign}")
    private String smsSign;


    public void sendSMS(List<Map<String, String>> reqList) {
        try {
            SmsSingleSender ssender = new SmsSingleSender(Integer.parseInt(appid), appkey);
            for (Map<String, String> map : reqList) {
                String[] params = {map.get("realName")};
                String phoneNumbers = map.get("phone");
                SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers,
                        Integer.parseInt(noticeTemplateId), params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
                System.out.println(result);
                System.out.println("用户" + params[0] + "," + phoneNumbers + ",成功发送报警短信");
            }
//

        } catch (Exception e) {

        }
    }

    public  void sendSMSCode(String code,String phone) {
        String[] params = {code};
        try {
            SmsSingleSender ssender = new SmsSingleSender(Integer.parseInt(appid), appkey);

            SmsSingleSenderResult result = ssender.sendWithParam("86",phone,
                    Integer.parseInt(codeTemplateId), params, smsSign, "", "");
            System.out.println(result);
        } catch (Exception e) {

        }
    }

}
