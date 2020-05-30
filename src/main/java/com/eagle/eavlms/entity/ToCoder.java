package com.eagle.eavlms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Entity()
//未修复漏洞——下发给开发人员需要修复的漏洞
public class ToCoder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 修复者
     */
    @ManyToOne
    private User coder;
    /**
     * 发送人（企业）
     */
    @ManyToOne
    private User enterprise;
    private String date;

    /**
     * 未修复漏洞
     */
    @ManyToOne  //多对一的关系，这个是维护端，是多端，多加个一端的主键，主要做增删改查，被维护端主要查
    private Vulns vulns;

    /**
     * 0 修复中
     * 1 成功
     * 2 失败
     * 3 误报
     */
    private Integer state;
}
