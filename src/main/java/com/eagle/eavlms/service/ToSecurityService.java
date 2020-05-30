package com.eagle.eavlms.service;

import com.eagle.eavlms.annotation.Notice;
import com.eagle.eavlms.dao.ToSecurityDao;
import com.eagle.eavlms.entity.NoticeInfo;
import com.eagle.eavlms.entity.ToSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ToSecurityService {
    @Autowired
    private ToSecurityDao toSecurityDao;
    @Autowired
    private EnterprisevulnsService enterprisevulnsServicel;


    @Notice
    public ToSecurity save(ToSecurity toSecurity, NoticeInfo noticeInfo){
        enterprisevulnsServicel.updateVulnsState(toSecurity.getVulns());
        return toSecurityDao.save(toSecurity);
    }

    public ToSecurity save(ToSecurity toSecurity){
        enterprisevulnsServicel.updateVulnsState(toSecurity.getVulns());
        return toSecurityDao.save(toSecurity);
    }

    public Page<ToSecurity> query(ToSecurity toSecurity, PageRequest pageable) {
        return toSecurityDao.findAll(Example.of(toSecurity), pageable);
    }

    public ToSecurity query(ToSecurity toSecurity) {
        return toSecurityDao.findOne(Example.of(toSecurity)).orElse(null);
    }

    public Integer updateToSecurityState(ToSecurity toSecurity) {
        return toSecurityDao.updateVulnsState(toSecurity);
    }
}
