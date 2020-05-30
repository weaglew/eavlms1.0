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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Column(unique = true)
    private String number;

    private String password;

    private String email;

    private Integer type;  //0企业 1开发 2安全

}

