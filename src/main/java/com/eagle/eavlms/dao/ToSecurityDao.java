package com.eagle.eavlms.dao;

import com.eagle.eavlms.entity.ToSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ToSecurityDao extends JpaRepository<ToSecurity,Integer> {

    /**
     * 更新状态
     * @param toSecurity
     * @return 返回更新了几行
     */
    @Modifying
    @Transactional
    @Query("update ToSecurity t set t.state = :#{#toSecurity.state} where t.id = :#{#toSecurity.id}")
    Integer updateVulnsState(ToSecurity toSecurity);
}