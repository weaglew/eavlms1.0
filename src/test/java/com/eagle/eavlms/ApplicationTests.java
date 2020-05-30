package com.eagle.eavlms;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.eagle.eavlms.dao.IpInfoDao;
import com.eagle.eavlms.dao.VulnsDao;
import com.eagle.eavlms.entity.IpInfo;
import com.eagle.eavlms.entity.Vulns;
import com.eagle.eavlms.service.EnterprisevulnsService;
import com.eagle.eavlms.util.IpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    VulnsDao vulnsDao;
    @Autowired
    private IpInfoDao ipInfoDao;

    /**
     * 根据ip的范围模拟生成ip地址
     * 首先把ip起始地址转化为十进制的数字
     * 然后从起点到终点生成ip地址
     * 最后把十进制的数字再次转化为ip地址
     */
    @Test
    public void contextLoads() {
        InputStream ipinfo = this.getClass().getResourceAsStream("/ipinfo.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ipinfo));
        //线程安全的类，由于bufferedReader.lines()是并发的，直接采用自增操作会引起数据自增失败
        AtomicLong sum = new AtomicLong();
        bufferedReader.lines().forEach(v -> {
            String[] split = v.split("\\s+");
            long begin = IpUtil.ipToLong(split[0]);
            long end = IpUtil.ipToLong(split[1]);
            List<IpInfo> ipInfos = new LinkedList<>();
            for (long i = begin; i <= end; i = i + 700) {
                IpInfo ipInfo = new IpInfo().setAddress(split[2]).setIp(IpUtil.longToIp(i));
                ipInfos.add(ipInfo);
                //采用CAS算法获取并且更新sum的值，JDK已经实现好的
                sum.getAndIncrement();
            }
            ipInfoDao.saveAll(ipInfos);
        });
        System.out.println("ip总个数：" + sum.get());
    }


    public Map<String, List<String>> getCountry() {
        //存储城市经纬度
        //线程安全的map
        Map<String, List<String>> stringListHashMap = new ConcurrentHashMap<>();
        InputStream ipinfo = this.getClass().getResourceAsStream("/country.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ipinfo));
        bufferedReader.lines().forEach(v -> {
            String[] split = v.split(",");
            List<String> locate = Arrays.asList(split[1].trim(), split[2].trim());
            stringListHashMap.put(split[0], locate);
        });
        return stringListHashMap;
    }

    @Test
    public void setLongitudeAndLatitude() {
//        Map<String, List<String>> country = getCountry();
        List<IpInfo> ipInfos = ipInfoDao.findAll();
        //json和对象之间相互转化的类
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", "afa98ddcb4583f00a9b7f4423a3e09f9");
        ipInfos.forEach(v -> {
            if (StringUtils.isEmpty(v.getLatitude())) {
                paramMap.put("ip", v.getIp());
                String result = null;
                try {
                    result = HttpUtil.get("https://restapi.amap.com/v3/ip?parameters", paramMap);
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = HttpUtil.get("https://restapi.amap.com/v3/ip?parameters", paramMap);
                }
                try {
                    Map map = objectMapper.readValue(result, Map.class);
                    List<String> rectangle = Arrays.asList(map.get("rectangle").toString().split(";")[0].split(","));
                    if (rectangle.size() >= 2) {
                        //经度
                        v.setLongitude(String.valueOf(rectangle.get(0)));
                        //纬度
                        v.setLatitude(String.valueOf(rectangle.get(1)));

                        System.out.println(v);

                        ipInfoDao.save(v);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        List<String> rectangle = Arrays.asList("".split(";")[0].split(","));
        System.out.println(rectangle);
    }
    @Autowired
    EnterprisevulnsService enterprisevulnsService;
    @Test
    public void test2(){

    }
}
