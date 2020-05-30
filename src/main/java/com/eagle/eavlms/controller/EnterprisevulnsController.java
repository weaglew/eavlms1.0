package com.eagle.eavlms.controller;

import com.eagle.eavlms.entity.Assets;
import com.eagle.eavlms.entity.Vulns;
import com.eagle.eavlms.service.EnterprisevulnsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController  //@ResponseBody 自动实现 将后端的数据序列化为json返回给前端+@controller
@RequestMapping("/assets-vulns")
//跨域
@CrossOrigin
@Slf4j
public class EnterprisevulnsController {

    @Autowired
    private EnterprisevulnsService enterprisevulnsService;

    /**
     * 查询所有资产和漏洞
     **/
    //查询所有,page是当前页码，从0开始，size是每页的个数,url是/assets/all,get方法
    @GetMapping("/all")
    public Page<Assets> queryAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Assets> assets = enterprisevulnsService.queryAllAssets(pageRequest);
        return assets;
    }

    @GetMapping
    public Page<Vulns> query(Vulns vulns, Integer page, Integer size) {
        log.info("/assets-vulns查询条件:{}", vulns); //{}占位符
        Page<Vulns> vulnsPage = enterprisevulnsService.queryVulns(vulns, PageRequest.of(page, size));
        return vulnsPage;  //包含vulns数据的page分页对象
    }

    @PostMapping("assets")
    public Assets saveAssets(@RequestBody Assets assets) {
        return enterprisevulnsService.saveAssets(assets);
    }
    @DeleteMapping("{id}")
    public void saveAssets(@PathVariable("id") Integer id) {
        enterprisevulnsService.deleteAssets(id);
    }


    /**
     * 根据url/ip或者其他关键字查询
     * @param ipOrUrl
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping("keywords")
    public Page<Assets> queryByKeywords(String ipOrUrl, String keyword, Integer page, Integer size) {
        log.info("/assets-vulns查询条件:{},{}", ipOrUrl, keyword);
        Page<Assets> assets = enterprisevulnsService.queryByKeywordsAndIp(ipOrUrl, keyword, PageRequest.of(page, size));
        return assets;
    }

    //漏洞新增 同时接收post和put两种请求方法，这两种方法主要用于保存和新增
    /**@RequestBody 用来接收前端传递给后端的json字符串中的数据 自动将json字符串转换为对象
     * 在后端的同一个接收方法里，@RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public Vulns save(@RequestBody Vulns vulns) {
        //设置初始提交状态
        vulns.setVulnsState("0");
        return enterprisevulnsService.save(vulns);
    }


    @PostMapping("updateState")
    public Integer vulnsUpdateState(@RequestBody Vulns vulns) {
        return enterprisevulnsService.updateVulnsState(vulns);
    }
}
