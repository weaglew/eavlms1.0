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
public class NoticeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 消息类型
     * 0 下发开发
     * 1 下发安全厂商
     * 2 漏洞误报
     * 3 复测误报
     */
    private Integer type;

    private String message;


    @ManyToOne
    private User fromUser;

    @ManyToOne
    private User toUser;
}

