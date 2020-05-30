package com.eagle.eavlms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

//Assets

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Assets")
//解决Json循环引用的问题，Vulns包含Assets，Assets又包含Vulns包含Assets，造成无限循环引用
//解决办法：每个对象使用id作为Json序列化的id，每次比较是否相同即可。
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Assets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String assetsIp;

    private String assetsUrl;

    private String belongedDepartment;

    private String maintenanceDepartment;

    private String mainTenanceContactPerson;

    private String safetyContactPerson;

    private String safetyDepartment;
    private String attributes;

    /**
     * 一对多的关系，维护端在多的一端，即Vulns，用资产id查询vulns表。
     */
    @OneToMany(mappedBy = "assets",fetch = FetchType.EAGER)  //使用mappedBy映射则不生成assets_vulns表
    @JsonIgnoreProperties({"assets"})  //忽略vulns的assets字段 放入其他字段 以免引起无限递归调用
    private List<Vulns> vulns;

}
