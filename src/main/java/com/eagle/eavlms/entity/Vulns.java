package com.eagle.eavlms.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

//Lombok注解
@AllArgsConstructor  //该构造函数含有所有已声明字段属性参数
@NoArgsConstructor   //使用后创建一个无参构造函数
@Data  //生成getter setter
@Accessors(chain = true)  //存取器，用于配置get和set方法的生成结果，chain为true时 set返回当前对象
@Entity()
@Table(name = "Vulns")


public class Vulns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String vulnsName;
    private String vulnsRand;

    /**
     * 漏洞在数据库中有六个状态，涉及工作流业务模式（状态转变）
     * 0 漏洞未修复状态/新增——企业未下发给开发
     * 1 漏洞修复——已下发给开发人员
     * 2 待复测（用于显示在企业的“需复测漏洞表中”）
     * 3 已下发给复测（安全）人员
     * 4 误报状态
     * 5 修复确认状态
     */
    private String vulnsState;

    private String vulnSystemName;

    private String vulnsCreatePerson;

    /**
     * 提交人
     */
    @ManyToOne
    private User createUser;

    private String vulnsCreateTime;
    private String vulnsDescription;

    /**
     * 对应资产，多对一的关系，这个是维护端，主要做增删改查，被维护端主要查
     */
    //使用@JsonIgnoreProperties({"vulns"}) 不序列化vulns类中assets属性带的vulns字段 该字段用null代替
    @ManyToOne //manytoone是通过在多端加个字段assetsid即外键来映射关系
    @JsonIgnoreProperties({"vulns"})  //忽略类中的vulns字段
    private Assets assets;

}