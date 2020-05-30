package com.eagle.eavlms.aop;

import com.eagle.eavlms.entity.NoticeInfo;
import com.eagle.eavlms.service.EmailService;
import com.eagle.eavlms.service.WebSocketEndpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;


//用于对加@Notice注解的操作
@Aspect
@Component  //让spring容器进行管理
public class NoticeAop {
    //websocket推送
    @Autowired
    private WebSocketEndpoint webSocketEndpoint;
    //发送邮件
    @Autowired
    private EmailService emailService;

    //save成功后再发通知邮件 后置返回通知,配置切点：within：指定类型（noticeinfo）annotation：定位到注解 execution：指定方法（无用）
    @AfterReturning("@within(com.eagle.eavlms.annotation.Notice)||@annotation(com.eagle.eavlms.annotation" +
            ".Notice)")//JoinPoint含被切的方法名，形参，返回值 即Service类的save（Tocoder tocoder,NoticeInfo noticeInfo）方法
    public void notice(JoinPoint joinPoint) throws JsonProcessingException, MessagingException {
        for (Object arg : joinPoint.getArgs()) {  //遍历获取service类save方法的形参
            //当被调用的方法含有noticeinfo类型的参数时
            if (arg instanceof NoticeInfo) {
                webSocketEndpoint.sendMessage((NoticeInfo) arg);  //把noticeinfo对象强转发送
                //发送邮件
                emailService.sendMail((NoticeInfo) arg);
            }
        }
    }
}
/*连接点：ToCoderService类的save（Tocoder tocoder,NoticeInfo noticeInfo）方法
切点：@Notice
通知：后置返回通知
切面：切点和通知的结合 当前notice方法
织入：通过动态代理对目标对象方法进行增强的过程
通知：共计五个
try{
    try{
        //@Before   前置通知（before）：在目标方法执行之前执行。
        method.invoke(..);
    }finally{
        //@After   后置通知（after）:在目标方法执行之后执行
    }
    //@AfterReturning   在目标方法返回之后执行，先执行后置通知再执行后置返回通知。
}catch(){
    //@AfterThrowing    异常通知(after throwing):在目标方法抛出异常时执行  环绕通知(around):在目标函数执行中执行
}

 */