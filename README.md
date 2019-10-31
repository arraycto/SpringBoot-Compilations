# SpringBoot Demo

### 分支

- master 分支：基于 SpringBoot 版本 2.1.0.RELEASE，每个 module 的 parent 依赖根目录下的pom.xml，主要用于管理每个module的依赖版本

### 开发环境

- **JDK1.8 +**
- **Maven 3.5 +**
- **IDEA 2018**
- **mysql 5.7 +** 
- **Navicat 12**

### 运行方式

1. `git clone https://github.com/wjw0315/SpringBoot-Demo.git`
2. 使用 IDEA 打开 clone 下来的项目
3. 在 IDEA 中打开项目
4. 在 IDEA 中 Maven Projects 的面板导入根目录下 的 `pom.xml`
5. 如果没有找到 Maven Projects ，可以勾上 View -> Tool Buttons ，然后Maven Projects的面板就会出现在IDEA的右侧
6. 找到各个 module 的 Application 类就可以运行各个 module 


### 各 Module 介绍

| Module 名称                                                  | Module 介绍                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [SpringBoot-Demo-elasticsearch](./SpringBoot-Demo-elasticsearch) | SpringBoot 集成 ElasticSearch，集成 `spring-boot-starter-data-elasticsearch` 完成对 ElasticSearch 的高级使用技巧，包括创建索引、配置映射、删除索引、增删改查基本操作、复杂查询、高级查询、聚合查询等。使用logstash进行MySQL的全量、增量同步到ES |
| [SpringBoot-Demo-ELK](./SpringBoot-Demo-ELK) | SpringBoot 通过AOP将日志通过Logstash管道导入ES中，并在Kibana中可视化的展示数据 |
| [SpringBoot-Demo-activiti](./SpringBoot-Demo-activiti) | SpringBoot 集成 activiti(未完成) |
| [SpringBoot-Demo-cache-redis](./SpringBoot-Demo-cache-redis)| SpringBoot 集成 redis的操作，redis操作工具类|
| [SpringBoot-Demo-cache-ehcache](./SpringBoot-Demo-cache-ehcache)| SpringBoot 集成 ehcache 的使用|
| [SpringBoot-Demo-qcloudsms](./SpringBoot-Demo-qcloudsms) | SpringBoot 集成腾讯短信云服务 |
| [SpringBoot-Demo-auth](./SpringBoot-Demo-auth) | SpringBoot 第三方认证登录设计实现 ...未完成|
| [SpringBoot-task](./SpringBoot-task) | SpingBoot 集成Quartz实现CRUD任务系统 | 
| [SpringBoot-iot](./SpringBoot-iot) | SpringBoot 对接天猫精灵语音控制 |
| [SpringBoot-Drools](./SpringBoot-Drools) | SpringBoot 集成 Drools规则引擎 |

### 各Module 开发案例

#### [SpringBoot-Demo-elasticsearch]()

#### [SpringBoot-iot 天猫精灵云云对接](https://www.yuque.com/wjwcloud/note/kfs7fm)



# License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2019 Jiawei.Wu