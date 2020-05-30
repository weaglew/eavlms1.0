package com.eagle.eavlms.service;

import com.eagle.eavlms.annotation.Notice;
import com.eagle.eavlms.dao.ToCoderDao;
import com.eagle.eavlms.entity.NoticeInfo;
import com.eagle.eavlms.entity.ToCoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ToCoderService {
    @Autowired
    private ToCoderDao toCoderDao;
    @Autowired
    private EnterprisevulnsService enterprisevulnsService;

    //此注解表示发送消息，对应aop包下NoticeAop的notice的方法  切点
    @Notice
    public ToCoder save(ToCoder toCoder, NoticeInfo noticeInfo) {
        return toCoderDao.save(toCoder);  //返回新插入的对象（jpa会先insert后select）
    }

    public ToCoder save(ToCoder toCoder) {
        return toCoderDao.save(toCoder);
    }

    public Page<ToCoder> query(ToCoder toCoder, Pageable pageable) {
        return toCoderDao.findAll(Example.of(toCoder), pageable);
    }
    public ToCoder query(ToCoder toCoder) {
        return toCoderDao.findOne(Example.of(toCoder)).orElse(null);
    }

    public Integer updateToCoderState(ToCoder toCoder) {
        return toCoderDao.updateToCoderState(toCoder);
    }
}

