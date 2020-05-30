# eavlms1.0
资产漏洞管理平台

敏感信息配置：
1. 使用时需在resource文件夹下添加名为“ipinfo”和“country”的txt文件，ipinfo.txt中需有ip地址(网段也行)及定位信息，country.txt中需要有城市地点的名称和经纬度
2. 在applications.yml中配置数据库及邮箱协议
3. 使用了Lombok 配置好后运行起来自动添加相关表和字段
