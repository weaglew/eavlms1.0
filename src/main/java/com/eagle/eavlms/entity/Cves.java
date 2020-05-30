package com.eagle.eavlms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Entity()
@Table(name = "cves")

public class Cves {

    @Id
    @Column(name = "cveid")
    private String cveid;

    @Column(name = "description")
    private String description;

    @Column(name = "reference")
    private String reference;

    @Column(name = "updatetime")
    private String updatetime;

    @Column(name = "author")
    private String author;


}
