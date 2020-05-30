package com.eagle.eavlms.service;

import com.eagle.eavlms.dao.IpInfoDao;
import com.eagle.eavlms.entity.IpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpInfoService {
    @Autowired
    private IpInfoDao ipInfoDao;

    public List<IpInfo> getAllIpInfo(){
        return ipInfoDao.findAll();
    }
}
