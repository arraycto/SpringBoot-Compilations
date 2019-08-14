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
| [SpringBoot-Demo-elasticsearch](./SpringBoot-Demo-elasticsearch) | spring-boot 集成 ElasticSearch，集成 `spring-boot-starter-data-elasticsearch` 完成对 ElasticSearch 的高级使用技巧，包括创建索引、配置映射、删除索引、增删改查基本操作、复杂查询、高级查询、聚合查询等 |
| [SpringBoot-Demo-activiti](./SpringBoot-Demo-activiti) | spring-boot 集成 activiti(未完成) |
| [SpringBoot-Demo-cache-redis](./SpringBoot-Demo-cache-redis)| SpringBoot 集成 redis的操作，redis操作工具类|

### 各Module 开发案例

#### SpringBoot-Demo-elasticsearch


# License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2019 Jiawei.Wu