package com.eagle.eavlms.service;

import com.eagle.eavlms.dao.CvesDao;
import com.eagle.eavlms.entity.Cves;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CvesService {
    @Autowired
    private CvesDao cvesDao;

    /*=====================查================*/
    //查询所有cves,pageable是分页，包含了当前页码以及每页的大小
    public Page<Cves> queryAll(Pageable pageable){
        return cvesDao.findAll(pageable);
    }

    //根据形参的条件查询cves,要查询的字段是不固定的，有值才会拼接字段
    public Page<Cves> query(Cves cves, Pageable pageable) {
        // 设置匹配器
        // withIgnoreCase是设置不区分大小写
        // withStringMatcher设置字段string类型的匹配方式
        // ExampleMatcher.StringMatcher.CONTAINING代表包含即模糊查询
        //ExampleMatcher这个是设置了怎么查询
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);  //matcher是静态方法生成的对象

        //example封装了查询的条件
        Example<Cves> example = Example.of(cves,matcher);
        return cvesDao.findAll(example, pageable);  //
    }

    /*===================增+改================*/
    public Cves addOrUpdate(Cves cves){
        return cvesDao.save(cves);
    }
    /*===================删除================*/
    public void deleteById(String cveid){
        cvesDao.deleteById(cveid);
    }

}
