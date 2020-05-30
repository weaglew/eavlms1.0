package com.eagle.eavlms.dao;


import com.eagle.eavlms.entity.Assets;
import com.eagle.eavlms.entity.Vulns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

//继承jpa全部接口 example也在JpaRepository里 JpaSpecificationExecutor用于拼装sql语句【未用】
public interface AssetsDao extends JpaRepository<Assets, Integer>, JpaSpecificationExecutor<Assets> {
    @Query("select v from Assets v where v.assetsIp like %?1% or v.assetsUrl like %?1%")
    Page<Assets> queryByIpOrUrl(String ipOrUrl, Pageable pageable);

    @Query("select v from Assets v where  v.belongedDepartment " +
            "like %?1% or v.maintenanceDepartment like %?1% or v.mainTenanceContactPerson like" +
            " %?1% or v.safetyContactPerson like %?1% or v.safetyDepartment like %?1%" +
            " or v.belongedDepartment like %?1% or v.maintenanceDepartment like %?1%" +
            " or v.attributes like %?1%")
    Page<Assets> queryByKeywords(String keyword, Pageable pageable);

    @Query("select v from Assets v where v.assetsIp like %?2% or v.assetsUrl like %?2% or v.belongedDepartment " +
            "like %?1% or v.maintenanceDepartment like %?1% or v.mainTenanceContactPerson like" +
            " %?1% or v.safetyContactPerson like %?1% or v.safetyDepartment like %?1%" +
            " or v.belongedDepartment like %?1% or v.maintenanceDepartment like %?1%" +
            " or v.attributes like %?1%")
    Page<Assets> queryByKeywordsAndIp(String keyword, String ipOrUrl, Pageable pageable);
}
