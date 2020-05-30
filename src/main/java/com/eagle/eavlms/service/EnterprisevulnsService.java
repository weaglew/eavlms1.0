package com.eagle.eavlms.service;

import com.eagle.eavlms.dao.AssetsDao;
import com.eagle.eavlms.dao.VulnsDao;
import com.eagle.eavlms.entity.Assets;
import com.eagle.eavlms.entity.Vulns;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j //调用log打印日志
public class EnterprisevulnsService {
    @Autowired
    private AssetsDao assetsDao;

    @Autowired
    private VulnsDao vulnsDao;


    /**
     * 查询所有资产，资产包含漏洞，一对多的关系
     *
     * @param pageable
     * @return
     */
    public Page<Assets> queryAllAssets(Pageable pageable) {
        return assetsDao.findAll(pageable);
    }

    /**
     * 查询所有漏洞
     *
     * @param pageable
     * @return
     */
    public Page<Vulns> queryAllVnlns(Pageable pageable) {
        return vulnsDao.findAll(pageable);
    }

    public List<Vulns> queryVulnsByAssets(Assets assets) {
        Example<Vulns> example = Example.of(new Vulns().setAssets(assets));
        return vulnsDao.findAll(example);
    }

    /**
     * 根据字段的值查询漏洞
     * example封装了查询的条件
     * @param vulns
     * @param pageable
     * @return
     */
    public Page<Vulns> queryVulns(Vulns vulns, Pageable pageable) {
        Example<Vulns> example = Example.of(vulns);
        return vulnsDao.findAll(example, pageable);
    }

    /**
     * 保存
     *
     * @param vulns
     * @return
     */
    public Vulns save(Vulns vulns) {
        return vulnsDao.save(vulns);
    }


    /**
     * 更新漏洞状态
     *
     * @param vulns
     * @returnkeywords
     */
    public Integer updateVulnsState(Vulns vulns) {
        return vulnsDao.updateVulnsState(vulns);
    }

    /**
     * 废弃
     * 根据关键字模糊查询
     *
     * @param ipOrUrl
     * @param keyword
     * @param pageable
     * @return
     */
    @Deprecated
    public Page<Vulns> queryByKeywords(String ipOrUrl, String keyword, Pageable pageable) {
        return vulnsDao.findAll((Specification<Vulns>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> orList = new LinkedList<>();
            //当第二个关键词（keyword）为空时
            if (StringUtils.isEmpty(keyword)) {
                //assetsIP对应bean的字段名称
                orList.add(criteriaBuilder.like(root.get("assets").get("assetsIp"), String.format("%%%s%%", ipOrUrl)));
                orList.add(criteriaBuilder.like(root.get("assets").get("assetsUrl"), String.format("%%%s%%", ipOrUrl)));
                //当第一个关键词（ipOrURL）为空时
            } else if (StringUtils.isEmpty(ipOrUrl)) {
                Stream.of(Vulns.class.getDeclaredFields()).forEach(v -> {
                    //去除assetsIP、assetsUrl两个字段
                    if ("assets".equals(v.getName()) || "createUser".equals(v.getName()) || "id".equals(v.getName())) {
                    } else {
                        orList.add(criteriaBuilder.like(root.get(v.getName()), String.format("%%%s%%", keyword)));
                    }
                });

                Stream.of(Assets.class.getDeclaredFields()).forEach(v -> {
                    //去除assetsIP、assetsUrl两个字段
                    if ("assetsIp".equals(v.getName()) || "assetsUrl".equals(v.getName())
                            || "vulns".equals(v.getName()) || "id".equals(v.getName())) {
                    } else {
                        orList.add(criteriaBuilder.like(root.get("assets").get(v.getName()), String.format("%%%s%%", keyword)));
                    }
                });
                //ipOrURL与keyword都不为空时
            } else {
                //assetsIP与assetsUrl匹配方式
                orList.add(criteriaBuilder.like(root.get("assets").get("assetsIp"), String.format("%%%s%%", ipOrUrl)));
                orList.add(criteriaBuilder.like(root.get("assets").get("assetsUrl"), String.format("%%%s%%", ipOrUrl)));
                //其余字段匹配方式
                Stream.of(Vulns.class.getDeclaredFields()).forEach(v -> {
                    //去除assetsIP、assetsUrl两个字段
                    if ("assets".equals(v.getName()) || "createUser".equals(v.getName()) || "id".equals(v.getName())) {
                    } else {
                        //%%%s%%表示%｛keyword｝%，用来做模糊查询，其中第一个和倒数第二个%表示转义
                        orList.add(criteriaBuilder.like(root.get(v.getName()), String.format("%%%s%%", keyword)));
                    }
                });
                Stream.of(Assets.class.getDeclaredFields()).forEach(v -> {
                    //去除assetsIP、assetsUrl两个字段
                    if ("assetsIp".equals(v.getName()) || "assetsUrl".equals(v.getName())
                            || "vulns".equals(v.getName()) || "id".equals(v.getName())) {
                    } else {
                        orList.add(criteriaBuilder.like(root.get("assets").get(v.getName()), String.format("%%%s%%", keyword)));
                    }
                });
            }
            criteriaQuery.where(criteriaBuilder.or(orList.toArray(new Predicate[0])));
            return null;
        }, pageable);
    }



    public Page<Assets> queryByKeywordsAndIp(String ipOrUrl, String keyword, Pageable pageable) {
        //当第二个关键词（keyword）为空时
        if (StringUtils.isEmpty(keyword)) {
            log.debug("queryByKeywordsAndIp:{},{}", ipOrUrl, keyword);
            Page<Assets> assets = assetsDao.queryByIpOrUrl(ipOrUrl, pageable);
            return assets;
        } else if (StringUtils.isEmpty(ipOrUrl)) {
            log.debug("keyword:{}", keyword);
            return assetsDao.queryByKeywords(keyword, pageable);
        } else {
            log.debug("queryByKeywordsAndIp==>:{},{}", ipOrUrl, keyword);
            return assetsDao.queryByKeywordsAndIp(keyword, ipOrUrl, pageable);
        }
    }

    public Assets saveAssets(Assets assets) {
        return assetsDao.save(assets);
    }

    public void deleteAssets(Integer id) {
        assetsDao.deleteById(id);
    }
}

