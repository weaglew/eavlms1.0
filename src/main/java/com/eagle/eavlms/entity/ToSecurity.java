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
//下发给安全人员需要复测的
public class ToSecurity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User security;
    @ManyToOne
    private User enterprise;
    private String date;
    @ManyToOne
    private Vulns vulns;

    /**
     * 0 测试中
     * 1 成功
     * 2 失败
     * 3 误报
     */
    @Column(columnDefinition="INT default 0")
    private Integer state;
}

