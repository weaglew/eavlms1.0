package com.eagle.eavlms.controller;

import com.eagle.eavlms.entity.NoticeInfo;
import com.eagle.eavlms.entity.ToCoder;
import com.eagle.eavlms.entity.User;
import com.eagle.eavlms.entity.Vulns;
import com.eagle.eavlms.service.EnterprisevulnsService;
import com.eagle.eavlms.service.ToCoderService;
import com.eagle.eavlms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/to-coder")
@CrossOrigin
public class ToCoderController {
    @Autowired
    private ToCoderService toCoderService;

    @Autowired
    private EnterprisevulnsService enterprisevulnsService;

    @Autowired
    private UserService userService;
    /**
     * 下发任务
     *主要目的：发送消息+更新漏洞主状态+保存进tocoder表中
     * @param toCoder
     * @return
     * @RequestBody  接收前端传递给后端的json字符串中的数据的(请求体中的数据的) get请求体内无数据 故用于post方法
     */
    @PostMapping
    public ToCoder save(@RequestBody ToCoder toCoder) {
        User fromUser = userService.getById(toCoder.getEnterprise().getId());
        User toUser = userService.getById(toCoder.getCoder().getId());
        //发送消息的具体内容
        NoticeInfo noticeInfo = new NoticeInfo().setFromUser(fromUser).setToUser(toUser)
                .setType(0).setMessage("有新的漏洞需要修复！");
        toCoder.setState(0); //设置tocoder对象漏洞状态属性处于已下发给开发人员的“修复中”子状态
        enterprisevulnsService.updateVulnsState(toCoder.getVulns().setVulnsState("1"));  //更新漏洞主状态为“漏洞修复”，即企业已下发状态
        return toCoderService.save(toCoder, noticeInfo);  //保存在tocoder表中
    }


    /**
     * 查询
     *
     * @param toCoder
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public Page<ToCoder> query(ToCoder toCoder, Integer page, int size) {
        toCoder.setVulns(new Vulns().setVulnsState("1"));
        return toCoderService.query(toCoder, PageRequest.of(page, size));
    }

    /**
     * 更新状态
     *
     * @param toCoder
     * @return
     */
    @PostMapping("updateState")
    public Integer toSecurityUpdateState(@RequestBody ToCoder toCoder) {
        //成功
        if (toCoder.getState() == 1) {
            //更新漏洞表
            enterprisevulnsService.updateVulnsState(toCoder.getVulns().setVulnsState("2"));
        }
        //误报
        if (toCoder.getState() == 3) {
            //更新漏洞表
            enterprisevulnsService.updateVulnsState(toCoder.getVulns().setVulnsState("0"));
        }
        return toCoderService.updateToCoderState(toCoder);
    }

}
