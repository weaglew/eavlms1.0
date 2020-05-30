package com.eagle.eavlms.controller;

import cn.hutool.http.HttpUtil;
import com.eagle.eavlms.dao.AssetsDao;
import com.eagle.eavlms.entity.Assets;
import com.eagle.eavlms.entity.IpFront;
import com.eagle.eavlms.entity.IpInfo;
import com.eagle.eavlms.service.IpInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController  //@Controller + @ResponseBody -> 把数据序列化返回给前端
@CrossOrigin
@RequestMapping("ip")
public class IpInfoController {
    @Autowired
    private IpInfoService ipInfoService;
    @Autowired
    private AssetsDao assetsDao;

    private final static String[] LINE_COLOR = {"green", "red", "yellow"};  //变量不变 使用final static使得读取速度变快

    @RequestMapping("all")
    public List<IpFront> getAllIps() {
        HashMap<String, Object> paramMap = new HashMap<>();  //存调用api时参数key和ip   HashMap是基于哈希表的Map接口的非同步实现，它允许null键和null值 存取快
        ObjectMapper objectMapper = new ObjectMapper(); //springmvc自带的数据模型转换框架Jackson java序列化、反序列化的类
        List<IpFront> resultList = Collections.synchronizedList(new LinkedList<>()); //resultList返给前端的结果list集合 Collection包括set无序不重复 Map list有序可重复 Collections.synchronizedList自动加锁使线程安全
        paramMap.put("key", "afa98ddcb4583f00a9b7f4423a3e09f9");

        List<IpInfo> ipInfos = ipInfoService.getAllIpInfo();  //获取全部ip信息

        LinkedList<IpInfo> selectinfos = new LinkedList<>(); //存储随机的1500个ip用于演示
        Random random = new Random();

        for (int i=0;i<1500;i++){
            selectinfos.add(ipInfos.get(random.nextInt(ipInfos.size())));
        }

        assetsDao.findAll().forEach(k -> {
            paramMap.put("ip", k.getAssetsIp());  //把每个资产ip都换成经纬度
            String result = HttpUtil.get("https://restapi.amap.com/v3/ip?parameters", paramMap);  //https://hutool.cn/docs/#/http/Http客户端工具类-HttpUtil    发起http请求
            System.out.println(result);
            List<String> target = null;
            try {
                Map map = objectMapper.readValue(result, Map.class);  //将返回的json转换为Map类
                //资产地址，目的地址   Arrays.asList相当于new了一个list 在调用api返回的数据中取出rectangle的数据  根据分号分割，取第一个数据，然后再根据逗号分割 获取经纬度
                target = Arrays.asList(map.get("rectangle").toString().split(";")[0].split(","));

            } catch (IOException e) {
                e.printStackTrace();
            }

            List<String> finalTarget = target;

            selectinfos.parallelStream().forEach(v -> {  //parallelStream  多线程
                //源地址，发起请求的地址
                List<String> source = Arrays.asList(v.getLongitude(), v.getLatitude());
                assert finalTarget != null;  //断言异常处理
                if (finalTarget.size() >= 2) {
                    HashMap<String, Object> objectObjectHashMap = new HashMap<>();
                    //设置飞线图线条样式
                    objectObjectHashMap.put("color",LINE_COLOR[new Double(Math.floor(Math.random() * 3)).intValue()]);  //new Double(Math.floor(Math.random() * 3))返回double 调用intValue转换成int
                    objectObjectHashMap.put("width",1.5);
                    objectObjectHashMap.put("opacity",0.8);  //线条透明度

                    resultList.add(new IpFront().setCoords(Arrays.asList(source, finalTarget)).setLineStyle(
                            objectObjectHashMap
                    ));
                }

            });

        });

            return resultList;
    }
}
