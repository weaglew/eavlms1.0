package com.eagle.eavlms.dao;

import com.eagle.eavlms.entity.Cves;
import org.springframework.data.jpa.repository.JpaRepository;

//基本漏洞信息库-CVE 增删改 dao-service-controller
public interface CvesDao extends JpaRepository<Cves,String> {
}
