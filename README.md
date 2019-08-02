# SpringBoot Demo

### 分支

- master 分支：基于 SpringBoot 版本 2.1.0.RELEASE，每个 module 的 parent 依赖根目录下的pom.xml，主要用于管理每个module的依赖版本

### 开发环境

- **JDK1.8 +**
- **Maven 3.5 +**
- **IDEA 2018**
- **mysql 5.7 +** 

### 运行方式

1. `git clone https://github.com/wjw0315/SpringBoot-Demo.git`
2. 使用 IDEA 打开 clone 下来的项目
3. 在 IDEA 中打开项目
4. 在 IDEA 中 Maven Projects 的面板导入根目录下 的 `pom.xml`
5. Maven Projects 找不到的童鞋，可以勾上 View -> Tool Buttons ，然后Maven Projects的面板就会出现在IDEA的右侧
6. 找到各个 module 的 Application 类就可以运行各个 module 


### 根目录下的 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <!--<parent>-->
        <!--<groupId>org.springframework.boot</groupId>-->
        <!--<artifactId>spring-boot-starter-parent</artifactId>-->
        <!--<version>2.1.3.RELEASE</version>-->
        <!--<relativePath/> &lt;!&ndash; lookup parent from repository &ndash;&gt;-->
    <!--</parent>-->

    <groupId>com.wjwcloud</groupId>
    <artifactId>SpringBoot-Demo</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <modules>
        <module>spring-boot-demo-activiti</module>
        <module>spring-boot-demo-elasticsearch</module>
    </modules>
    <packaging>pom</packaging>

    <name>SpringBoot-Demo</name>
    <url>http://wjwcloud.com</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring.boot.version>2.1.0.RELEASE</spring.boot.version>
        <mysql.version>8.0.16</mysql.version>
        <hutool.version>4.5.1</hutool.version>
        <guava.version>27.0.1-jre</guava.version>
        <user.agent.version>1.20</user.agent.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring.boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <!-- hutool工具类 -->
            <dependency>
                <groupId>cn.hutool</groupId>
                <artifactId>hutool-all</artifactId>
                <version>${hutool.version}</version>
            </dependency>
            <!-- guava工具类 -->
            <dependency>
                <groupId>com.google.guava</groupId>
                <artifactId>guava</artifactId>
                <version>${guava.version}</version>
            </dependency>
            <!-- 解析 UserAgent 信息 -->
            <dependency>
                <groupId>eu.bitwalker</groupId>
                <artifactId>UserAgentUtils</artifactId>
                <version>${user.agent.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <artifactId>maven-clean-plugin</artifactId>
                    <version>3.0.0</version>
                </plugin>
                <plugin>
                    <artifactId>maven-resources-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.7.0</version>
                </plugin>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>2.20.1</version>
                </plugin>
                <plugin>
                    <artifactId>maven-jar-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-install-plugin</artifactId>
                    <version>2.5.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-deploy-plugin</artifactId>
                    <version>2.8.2</version>
                </plugin>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>${spring.boot.version}</version>
                    <executions>
                        <execution>
                            <goals>
                                <goal>repackage</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```

### 各 Module 介绍

| Module 名称                                                  | Module 介绍                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [spring-boot-demo-elasticsearch](./spring-boot-demo-elasticsearch) | spring-boot 集成 ElasticSearch，集成 `spring-boot-starter-data-elasticsearch` 完成对 ElasticSearch 的高级使用技巧，包括创建索引、配置映射、删除索引、增删改查基本操作、复杂查询、高级查询、聚合查询等 |



# License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2019 Jiawei.Wu