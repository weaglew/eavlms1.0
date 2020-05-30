package com.eagle.eavlms.service;

import com.eagle.eavlms.entity.NoticeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
//打印日志（46行）
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;


    @Async  //异步调用 sendMail方法成为异步方法
    public void sendMail(NoticeInfo noticeInfo) throws MessagingException {
        //创建邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        //发送人
//        mimeMessageHelper.setFrom("Eaglekyo_Li@163.com");
        //与配置授权码的用户一致
        mimeMessageHelper.setFrom("eagle_li@vip.163.com");

        //发送到
        mimeMessageHelper.setTo(noticeInfo.getToUser().getEmail());
        String title;
        /**
         * 消息类型
         * 0 下发开发
         * 1 下发安全厂商
         * 2 漏洞误报
         * 3 复测误报
         */
        switch (noticeInfo.getType()){
            case 0:title="漏洞修复提醒";break;
            case 1:title="漏洞测试提醒";break;
            case 2:title="漏洞修复误报提醒";break;
            case 3:title="漏洞测试误报提醒";break;
            default:
                title="漏洞提醒";break;
        }
        //标题
        mimeMessageHelper.setSubject(title);

        //格式，如果有自定义样式的话用html格式
        mimeMessageHelper.setText("Hi：<br /><br />您管理的某系统存在" +
                "<font color='red' style='font-weight: bold'>漏洞</font>，请尽快安排进行处置！ " +
                "详情请查看 <a href='http://localhost:9528/#/login'>企业漏洞生命周期管理平台</a>。<br /><br /><br />" +
                "此封邮件为系统自动发出，请勿回复，详情请您及时登陆企业漏洞生命周期管理平台查看。", true);

        //发送
        javaMailSender.send(mimeMessage);
        log.info("======>id为{}用户发送给{}一封邮件", noticeInfo.getFromUser().getId(),
                noticeInfo.getToUser().getId());
    }
}
