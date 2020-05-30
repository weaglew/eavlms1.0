package com.eagle.eavlms.entity;

import com.eagle.eavlms.annotation.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Accessors(chain = true)

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IpFront {
    private List<List<String>> coords;  //前端使用的数据格式 IP起始经纬度 coords: [ [120, 66], [122, 67] ]
    private Map<String,Object> lineStyle;  //线条样式
}
