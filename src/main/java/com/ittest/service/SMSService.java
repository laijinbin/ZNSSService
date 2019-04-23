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
    private String templateId;

    @Value("${smsSign}")
    private String smsSign;


    public void sendSMS(List<Map<String, String>> reqList) {
//        // 短信应用SDK AppID
//        int appid = 1400168165; // 1400开头

// 短信应用SDK AppKey
        // String appkey = "b67aea589cb2a6702294b47b973e3397";

// 需要发送短信的手机号码


// 短信模板ID，需要在短信应用中申请
//        int templateId = 309772; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
//templateId7839对应的内容是"您的验证码是: {1}"
// 签名
        //String smsSign = "zhongzewei博客";
        try {

            SmsSingleSender ssender = new SmsSingleSender(Integer.parseInt(appid), appkey);
            for (Map<String, String> map : reqList) {
                String[] params = {map.get("realName")};
                String phoneNumbers = map.get("phone");
                SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers,
                        Integer.parseInt(templateId), params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
                System.out.println(result);
                System.out.println("用户" + params[0] + "," + phoneNumbers + ",成功发送报警短信");
            }
//

        } catch (Exception e) {

        }
    }

    public static void sendSMS2() {
        // 短信应用SDK AppID
        int appid = 1400168165; // 1400开头

// 短信应用SDK AppKey
        String appkey = "b67aea589cb2a6702294b47b973e3397";

// 需要发送短信的手机号码
        String[] phoneNumbers = {"13247598772"};

// 短信模板ID，需要在短信应用中申请
        int templateId = 314788; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
//templateId7839对应的内容是"您的验证码是: {1}"
// 签名
        String smsSign = "zhongzewei博客";//zhongzewei\u535a\u5ba2
        try {
            String[] params = {"123"};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);

            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0],
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result);
        } catch (Exception e) {

        }
    }

}
