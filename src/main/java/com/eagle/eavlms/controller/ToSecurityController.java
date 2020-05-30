package com.eagle.eavlms.controller;

import com.eagle.eavlms.entity.NoticeInfo;
import com.eagle.eavlms.entity.ToSecurity;
import com.eagle.eavlms.entity.User;
import com.eagle.eavlms.entity.Vulns;
import com.eagle.eavlms.service.EnterprisevulnsService;
import com.eagle.eavlms.service.ToSecurityService;
import com.eagle.eavlms.service.UserService;
import javafx.application.Application;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/to-security")
@CrossOrigin
public class ToSecurityController {
    @Autowired
    private ToSecurityService toSecurityService;
    @Autowired
    private EnterprisevulnsService enterprisevulnsService;
    @Autowired
    private UserService userService;

    /**
     * 下发任务
     * @param toSecurity
     * @return
     */
    @PostMapping
    public ToSecurity save(@RequestBody ToSecurity toSecurity) {
        User fromUser = userService.getById(toSecurity.getEnterprise().getId());
        User toUser = userService.getById(toSecurity.getSecurity().getId());
        //消息具体内容
        NoticeInfo noticeInfo =
                new NoticeInfo().setFromUser(fromUser).setToUser(toUser)
                        .setType(0).setMessage("有新的漏洞需要测试！");
        toSecurity.setState(0);
        enterprisevulnsService.updateVulnsState(toSecurity.getVulns().setVulnsState("3"));
        return toSecurityService.save(toSecurity, noticeInfo);
    }

    /**
     * 查询
     * @param toSecurity
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public Page<ToSecurity> query(ToSecurity toSecurity, Integer page, int size) {
        toSecurity.setVulns(new Vulns().setVulnsState("3"));
        return toSecurityService.query(toSecurity, PageRequest.of(page, size));
    }

    /**
     * 更新状态
     * @param toSecurity
     * @return
     */
    @PostMapping("updateState")
    public Integer toSecurityUpdateState(@RequestBody ToSecurity toSecurity) {
        if (toSecurity.getState() == 1) {

            enterprisevulnsService.updateVulnsState(toSecurity.getVulns().setVulnsState("3"));
        }
        //误报状态
        if (toSecurity.getState() == 3) {
            //更新漏洞表
            enterprisevulnsService.updateVulnsState(toSecurity.getVulns().setVulnsState("0"));
        }
        return toSecurityService.updateToSecurityState(toSecurity);
    }
}

