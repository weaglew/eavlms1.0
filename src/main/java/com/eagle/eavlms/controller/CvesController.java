package com.eagle.eavlms.controller;

import com.eagle.eavlms.entity.Cves;
import com.eagle.eavlms.service.CvesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
//使用RestFul风格的API，增删改查的四个功能对应PUT、DELETE、POST、GET四个方法
@RequestMapping("/cves")
public class CvesController {
    @Autowired
    private CvesService cvesService;

    /**==============查=================**/

    //查询所有,page是当前页码，从0开始，size是每页的个数,url是/cves/all,get方法
    @GetMapping("all")
    public Page<Cves> queryAll(Integer page, Integer size){
      // pagerequest只用来生成page对象的
        PageRequest pageRequest = PageRequest.of(page, size);  //of方法内有new可以将传入的当前页码和每页的数量封装成pagerequest对象
        return cvesService.queryAll(pageRequest);
    }

    //根据Cves类字段的值进行条件查询，url是/cves，get方法
    @GetMapping
    public Page<Cves> query(Cves cves, Integer page,Integer size){
        return cvesService.query(cves,PageRequest.of(page,size));
    }

    /**==========增改==========**/
    //POST对应修改，PUT对应新增，注意：新增id为null，修改id有值，根据id修改，url是/cves
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    public Cves addOrUpdate(@RequestBody Cves cves){
        System.out.println(cves);
        return cvesService.addOrUpdate(cves);
    }

    /**==========删============**/
    //根据id进行删除
    @DeleteMapping
    public void deleteById(String cveid){
        cvesService.deleteById(cveid);
    }
}

