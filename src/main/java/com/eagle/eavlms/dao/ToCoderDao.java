package com.eagle.eavlms.dao;


import com.eagle.eavlms.entity.ToCoder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ToCoderDao extends JpaRepository<ToCoder,Integer> {
    /**
     * 更新状态
     * @param toCoder
     * @return
     */
    @Modifying
    @Transactional
    @Query("update ToCoder t set t.state = :#{#toCoder.state} where t.id = :#{#toCoder.id}")
    Integer updateToCoderState(ToCoder toCoder);
}
