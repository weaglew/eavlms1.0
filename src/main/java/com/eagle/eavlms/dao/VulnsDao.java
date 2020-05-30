package com.eagle.eavlms.dao;

import com.eagle.eavlms.entity.Assets;
import com.eagle.eavlms.entity.Vulns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VulnsDao extends JpaRepository<Vulns, Integer>, JpaSpecificationExecutor<Vulns> {
    // 用来更新漏洞的状态
    // 因为jpa的更新会更新整个对象数据，但只需要更新状态即可，所以使用#{#vulns.vulnsState}取形参变量的值
    @Modifying  //@Query注解中编写JPQL实现DELETE和UPDATE操作的时候必须加上@modifying注解，以通知Spring Data 这是一个DELETE或UPDATE操作。
    @Transactional
    @Query("update Vulns v set v.vulnsState = :#{#vulns.vulnsState} where v.id = :#{#vulns.id}")  //:#{#vulns.vulnsState} 取变量数据  自定义sql就用该注解
    Integer updateVulnsState(Vulns vulns);


    @Query("select v from Vulns v where v.vulnsName like %?1% or v.vulnsRand like %?1% or v.vulnsState " +
            "like %?1% or v.vulnSystemName like %?1% or v.vulnsCreatePerson like" +
            " %?1% or v.createUser.username like %?1% or v.vulnsDescription like %?1%" +
            " or v.assets.belongedDepartment like %?1% or v.assets.maintenanceDepartment like %?1%" +
            " or v.assets.maintenanceDepartment like %?1% or v.assets.mainTenanceContactPerson like %?1%" +
            "or v.assets.safetyContactPerson like %?1% or v.assets.safetyDepartment  like %?1% or v.assets.attributes" +
            " like %?1%")
    public Page<Vulns> queryByKeywords(String keyword, Pageable pageable);


    @Query("select v from Vulns v where v.vulnsName like %?1% or v.vulnsRand like %?1% or v.vulnsState " +
            "like %?1% or v.vulnSystemName like %?1% or v.vulnsCreatePerson like" +
            " %?1% or v.createUser.username like %?1% or v.vulnsDescription like %?1%" +
            " or v.assets.belongedDepartment like %?1% or v.assets.maintenanceDepartment like %?1%" +
            " or v.assets.maintenanceDepartment like %?1% or v.assets.mainTenanceContactPerson like %?1%" +
            "or v.assets.safetyContactPerson like %?1% or v.assets.safetyDepartment  like %?1% or v.assets.attributes" +
            " like %?1% or v.assets.assetsIp like %?2% or v.assets.assetsUrl like %?2%")
    public Page<Vulns> queryByKeywordsAndIp(String keyword, String ipOrUrl,Pageable pageable);



    @Query("select v from Vulns v where v.assets.assetsIp like %?1% or v.assets.assetsUrl like %?1%")
    Page<Vulns> queryByIpOrUrl(String ipOrUrl,Pageable pageable);



}
