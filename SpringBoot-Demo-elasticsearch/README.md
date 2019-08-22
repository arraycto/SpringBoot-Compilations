<a name="spring-boot-demo-elasticsearch"></a>
# 一、spring-boot-demo-elasticsearch

> 此 demo 主要演示了 Spring Boot 如何集成 `spring-boot-starter-data-elasticsearch` 完成对 ElasticSearch 的高级使用技巧，包括创建索引、配置映射、删除索引、增删改查基本操作、复杂查询、高级查询、聚合查询等。


<a name="138a6766"></a>
## 注意

作者编写本demo时，ElasticSearch版本为 `6.5.4`，使用 docker 运行，下面是所有步骤：

1. 下载镜像：`docker pull elasticsearch:6.5.4`
1. 运行容器：`docker run -d -e ES_JAVA_OPTS="-Xms256m -Xmx256m" -p 9200:9200 -p 9300:9300 -v /opt/docker/es/config/es1.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /opt/docker/es/data1/:/usr/share/elasticsearch/data/ -v /opt/docker/es/logs1/:/usr/share/elasticsearch/logs/ --name es1 elasticsearch:6.5.4`
1. 进入容器：`docker exec -it es1 /bin/bash`
1. 安装 ik 分词器：`./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.5.4/elasticsearch-analysis-ik-6.5.4.zip`
1. 修改 es 配置文件：`vi ./config/elasticsearch.yml`
```yaml
cluster.name: "docker-cluster"
network.host: 0.0.0.0

# minimum_master_nodes need to be explicitly set when bound on a public IP
# set to 1 to allow single node clusters
# Details: https://github.com/elastic/elasticsearch/pull/17288
discovery.zen.minimum_master_nodes: 1

elk
http.cors.enabled: true
http.cors.allow-origin: "*"
```

6. 退出容器：`exit`
6. 重启容器：`docker restart es1`<br />
【注】 docker目录的挂载，需要将宿主机的文件夹权限设置为777 ：chmod 777 /<文件夹路径>

<a name="pom.xml"></a>
## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <artifactId>spring-boot-demo-elasticsearch</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>spring-boot-demo-elasticsearch</name>
    <description>Demo project for Spring Boot</description>
    <parent>
        <groupId>com.wjwcloud</groupId>
        <artifactId>SpringBoot-Demo</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
        </dependency>
    </dependencies>
    <build>
        <finalName>spring-boot-demo-elasticsearch</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

<a name="Person.java"></a>
## Person.java

> 实体类
> [@Document ]() 注解主要声明索引名、类型名、分片数量和备份数量
> [@Field ]() 注解主要声明字段对应ES的类型


```java
/**
 * @author JiaweiWu
 */
@Document(indexName = EsConsts.INDEX_NAME, type = EsConsts.TYPE_NAME, shards = 1, replicas = 0)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 名字
     */
    @Field(type = FieldType.Keyword)
    private String name;
    /**
     * 国家
     */
    @Field(type = FieldType.Keyword)
    private String country;
    /**
     * 年龄
     */
    @Field(type = FieldType.Integer)
    private Integer age;
    /**
     * 生日
     */
    @Field(type = FieldType.Date)
    private Date birthday;
    /**
     * 介绍
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String remark;
}
```

<a name="PersonRepository.java"></a>
## PersonRepository.java

```java
/**
 * @author JiaweiWu
 */
public interface PersonRepository extends ElasticsearchRepository<Person, Long> {
    /**
     * 根据年龄区间查询
     *
     * @param min 最小值
     * @param max 最大值
     * @return 满足条件的用户列表
     */
    List<Person> findByAgeBetween(Integer min, Integer max);
}
```

<a name="TemplateTest.java"></a>
## TemplateTest.java

> 主要测试创建索引、映射配置、删除索引


```java
/**
 * @author JiaweiWu
 */
public class TemplateTest extends SpringBootDemoElasticsearchApplicationTests {
    @Autowired
    private ElasticsearchTemplate esTemplate;
    /**
     * 测试 ElasticTemplate 创建 index
     */
    @Test
    public void testCreateIndex() {
        // 创建索引，会根据Item类的@Document注解信息来创建
        esTemplate.createIndex(Person.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        esTemplate.putMapping(Person.class);
    }
    /**
     * 测试 ElasticTemplate 删除 index
     */
    @Test
    public void testDeleteIndex() {
        esTemplate.deleteIndex(Person.class);
    }
}
```

<a name="PersonRepositoryTest.java"></a>
## PersonRepositoryTest.java

> 主要功能，参见方法上方注释


```java
/**
 * @author JiaweiWu
 */
@Slf4j
public class PersonRepositoryTest extends SpringBootDemoElasticsearchApplicationTests {
    @Autowired
    private PersonRepository repo;
    /**
     * 测试新增
     */
    @Test
    public void save() {
        Person person = new Person(1L, "刘备", "蜀国", 18, DateUtil.parse("1990-01-02 03:04:05"), "刘备（161年－223年6月10日），即汉昭烈帝（221年－223年在位），又称先主，字玄德，东汉末年幽州涿郡涿县（今河北省涿州市）人，西汉中山靖王刘胜之后，三国时期蜀汉开国皇帝、政治家。\n刘备少年时拜卢植为师；早年颠沛流离，备尝艰辛，投靠过多个诸侯，曾参与镇压黄巾起义。先后率军救援北海相孔融、徐州牧陶谦等。陶谦病亡后，将徐州让与刘备。赤壁之战时，刘备与孙权联盟击败曹操，趁势夺取荆州。而后进取益州。于章武元年（221年）在成都称帝，国号汉，史称蜀或蜀汉。《三国志》评刘备的机权干略不及曹操，但其弘毅宽厚，知人待士，百折不挠，终成帝业。刘备也称自己做事“每与操反，事乃成尔”。\n章武三年（223年），刘备病逝于白帝城，终年六十三岁，谥号昭烈皇帝，庙号烈祖，葬惠陵。后世有众多文艺作品以其为主角，在成都武侯祠有昭烈庙为纪念。");
        Person save = repo.save(person);
        log.info("【save】= {}", save);
    }
    /**
     * 测试批量新增
     */
    @Test
    public void saveList() {
        List<Person> personList = Lists.newArrayList();
        personList.add(new Person(2L, "曹操", "魏国", 20, DateUtil.parse("1988-01-02 03:04:05"), "曹操（155年－220年3月15日），字孟德，一名吉利，小字阿瞒，沛国谯县（今安徽亳州）人。东汉末年杰出的政治家、军事家、文学家、书法家，三国中曹魏政权的奠基人。\n曹操曾担任东汉丞相，后加封魏王，奠定了曹魏立国的基础。去世后谥号为武王。其子曹丕称帝后，追尊为武皇帝，庙号太祖。\n东汉末年，天下大乱，曹操以汉天子的名义征讨四方，对内消灭二袁、吕布、刘表、马超、韩遂等割据势力，对外降服南匈奴、乌桓、鲜卑等，统一了中国北方，并实行一系列政策恢复经济生产和社会秩序，扩大屯田、兴修水利、奖励农桑、重视手工业、安置流亡人口、实行“租调制”，从而使中原社会渐趋稳定、经济出现转机。黄河流域在曹操统治下，政治渐见清明，经济逐步恢复，阶级压迫稍有减轻，社会风气有所好转。曹操在汉朝的名义下所采取的一些措施具有积极作用。\n曹操军事上精通兵法，重贤爱才，为此不惜一切代价将看中的潜能分子收于麾下；生活上善诗歌，抒发自己的政治抱负，并反映汉末人民的苦难生活，气魄雄伟，慷慨悲凉；散文亦清峻整洁，开启并繁荣了建安文学，给后人留下了宝贵的精神财富，鲁迅评价其为“改造文章的祖师”。同时曹操也擅长书法，唐朝张怀瓘在《书断》将曹操的章草评为“妙品”。"));
        personList.add(new Person(3L, "孙权", "吴国", 19, DateUtil.parse("1989-01-02 03:04:05"), "孙权（182年－252年5月21日），字仲谋，吴郡富春（今浙江杭州富阳区）人。三国时代孙吴的建立者（229年－252年在位）。\n孙权的父亲孙坚和兄长孙策，在东汉末年群雄割据中打下了江东基业。建安五年（200年），孙策遇刺身亡，孙权继之掌事，成为一方诸侯。建安十三年（208年），与刘备建立孙刘联盟，并于赤壁之战中击败曹操，奠定三国鼎立的基础。建安二十四年（219年），孙权派吕蒙成功袭取刘备的荆州，使领土面积大大增加。\n黄武元年（222年），孙权被魏文帝曹丕册封为吴王，建立吴国。同年，在夷陵之战中大败刘备。黄龙元年（229年），在武昌正式称帝，国号吴，不久后迁都建业。孙权称帝后，设置农官，实行屯田，设置郡县，并继续剿抚山越，促进了江南经济的发展。在此基础上，他又多次派人出海。黄龙二年（230年），孙权派卫温、诸葛直抵达夷州。\n孙权晚年在继承人问题上反复无常，引致群下党争，朝局不稳。太元元年（252年）病逝，享年七十一岁，在位二十四年，谥号大皇帝，庙号太祖，葬于蒋陵。\n孙权亦善书，唐代张怀瓘在《书估》中将其书法列为第三等。"));
        personList.add(new Person(4L, "诸葛亮", "蜀国", 16, DateUtil.parse("1992-01-02 03:04:05"), "诸葛亮（181年-234年10月8日），字孔明，号卧龙，徐州琅琊阳都（今山东临沂市沂南县）人，三国时期蜀国丞相，杰出的政治家、军事家、外交家、文学家、书法家、发明家。\n早年随叔父诸葛玄到荆州，诸葛玄死后，诸葛亮就在襄阳隆中隐居。后刘备三顾茅庐请出诸葛亮，联孙抗曹，于赤壁之战大败曹军。形成三国鼎足之势，又夺占荆州。建安十六年（211年），攻取益州。继又击败曹军，夺得汉中。蜀章武元年（221年），刘备在成都建立蜀汉政权，诸葛亮被任命为丞相，主持朝政。蜀后主刘禅继位，诸葛亮被封为武乡侯，领益州牧。勤勉谨慎，大小政事必亲自处理，赏罚严明；与东吴联盟，改善和西南各族的关系；实行屯田政策，加强战备。前后六次北伐中原，多以粮尽无功。终因积劳成疾，于蜀建兴十二年（234年）病逝于五丈原（今陕西宝鸡岐山境内），享年54岁。刘禅追封其为忠武侯，后世常以武侯尊称诸葛亮。东晋政权因其军事才能特追封他为武兴王。\n诸葛亮散文代表作有《出师表》《诫子书》等。曾发明木牛流马、孔明灯等，并改造连弩，叫做诸葛连弩，可一弩十矢俱发。诸葛亮一生“鞠躬尽瘁、死而后已”，是中国传统文化中忠臣与智者的代表人物。"));
        Iterable<Person> people = repo.saveAll(personList);
        log.info("【people】= {}", people);
    }
    /**
     * 测试更新
     */
    @Test
    public void update() {
        repo.findById(1L).ifPresent(person -> {
            person.setRemark(person.getRemark() + "\n更新更新更新更新更新");
            Person save = repo.save(person);
            log.info("【save】= {}", save);
        });
    }
    /**
     * 测试删除
     */
    @Test
    public void delete() {
        // 主键删除
        repo.deleteById(1L);
        // 对象删除
        repo.findById(2L).ifPresent(person -> repo.delete(person));
        // 批量删除
        repo.deleteAll(repo.findAll());
    }
    /**
     * 测试普通查询，按生日倒序
     */
    @Test
    public void select() {
        repo.findAll(Sort.by(Sort.Direction.DESC, "birthday"))
                .forEach(person -> log.info("{} 生日: {}", person.getName(), DateUtil.formatDateTime(person.getBirthday())));
    }
    /**
     * 自定义查询，根据年龄范围查询
     */
    @Test
    public void customSelectRangeOfAge() {
        repo.findByAgeBetween(18, 19).forEach(person -> log.info("{} 年龄: {}", person.getName(), person.getAge()));
    }
    /**
     * 高级查询
     */
    @Test
    public void advanceSelect() {
        // QueryBuilders 提供了很多静态方法，可以实现大部分查询条件的封装
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "孙权");
        log.info("【queryBuilder】= {}", queryBuilder.toString());
        repo.search(queryBuilder).forEach(person -> log.info("【person】= {}", person));
    }
    /**
     * 自定义高级查询
     */
    @Test
    public void customAdvanceSelect() {
        // 构造查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本的分词条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("remark", "东汉"));
        // 排序条件
        queryBuilder.withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC));
        // 分页条件
        queryBuilder.withPageable(PageRequest.of(0, 2));
        Page<Person> people = repo.search(queryBuilder.build());
        log.info("【people】总条数 = {}", people.getTotalElements());
        log.info("【people】总页数 = {}", people.getTotalPages());
        people.forEach(person -> log.info("【person】= {}，年龄 = {}", person.getName(), person.getAge()));
    }
    /**
     * 测试聚合，测试平均年龄
     */
    @Test
    public void agg() {
        // 构造查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        // 平均年龄
        queryBuilder.addAggregation(AggregationBuilders.avg("avg").field("age"));
        log.info("【queryBuilder】= {}", JSONUtil.toJsonStr(queryBuilder.build()));
        AggregatedPage<Person> people = (AggregatedPage<Person>) repo.search(queryBuilder.build());
        double avgAge = ((InternalAvg) people.getAggregation("avg")).getValue();
        log.info("【avgAge】= {}", avgAge);
    }
    /**
     * 测试高级聚合查询，每个国家的人有几个，每个国家的平均年龄是多少
     */
    @Test
    public void advanceAgg() {
        // 构造查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        // 1. 添加一个新的聚合，聚合类型为terms，聚合名称为country，聚合字段为age
        queryBuilder.addAggregation(AggregationBuilders.terms("country").field("country")
                // 2. 在国家聚合桶内进行嵌套聚合，求平均年龄
                .subAggregation(AggregationBuilders.avg("avg").field("age")));
        log.info("【queryBuilder】= {}", JSONUtil.toJsonStr(queryBuilder.build()));
        // 3. 查询
        AggregatedPage<Person> people = (AggregatedPage<Person>) repo.search(queryBuilder.build());
        // 4. 解析
        // 4.1. 从结果中取出名为 country 的那个聚合，因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
        StringTerms country = (StringTerms) people.getAggregation("country");
        // 4.2. 获取桶
        List<StringTerms.Bucket> buckets = country.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            // 4.3. 获取桶中的key，即国家名称  4.4. 获取桶中的文档数量
            log.info("{} 总共有 {} 人", bucket.getKeyAsString(), bucket.getDocCount());
            // 4.5. 获取子聚合结果：
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("avg");
            log.info("平均年龄：{}", avg);
        }
    }
}
```

<a name="188c62c2"></a>
# 二、ES集群配置

在/opt/docker/es/config的文件夹目录下添加如下的三个文件配置:<br />es1.yml

```yaml
cluster.name: docker-cluster
node.name: es-node1
network.bind_host: 0.0.0.0
# 本机的内网IP
network.publish_host: 192.168.233.137
http.port: 9200
transport.tcp.port: 9300
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true 
node.data: true  
# 发现其他的节点
discovery.zen.ping.unicast.hosts: ["192.168.233.137:9300","192.168.233.137:9301","192.168.233.137:9302"]
discovery.zen.minimum_master_nodes: 2
```

es2.yml

```yaml
cluster.name: docker-cluster
node.name: es-node2
network.bind_host: 0.0.0.0
network.publish_host: 192.168.233.137
http.port: 9201
transport.tcp.port: 9301
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true 
node.data: true  
discovery.zen.ping.unicast.hosts: ["192.168.233.137:9300","192.168.233.137:9301","192.168.233.137:9302"]
discovery.zen.minimum_master_nodes: 2
```

es3.yml

```yaml
cluster.name: docker-cluster
node.name: es-node3
network.bind_host: 0.0.0.0
network.publish_host: 192.168.233.137
http.port: 9202
transport.tcp.port: 9302
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true 
node.data: true  
discovery.zen.ping.unicast.hosts: ["192.168.233.137:9300","192.168.233.137:9301","192.168.233.137:9302"]
discovery.zen.minimum_master_nodes: 2
```

<a name="7b4a7f4d"></a>
### 配置说明

cluster.name：用于唯一标识一个集群，不同的集群，其 cluster.name 不同，集群名字相同的所有节点自动组成一个集群。如果不配置改属性，默认值是：elasticsearch。<br />node.name：节点名，默认随机指定一个name列表中名字。集群中node名字不能重复<br />index.number_of_shards: 默认的配置是把索引分为5个分片<br />index.number_of_replicas:设置每个index的默认的冗余备份的分片数，默认是1

```
通过 index.number_of_shards，index.number_of_replicas默认设置索引将分为5个分片，每个分片1个副本，共10个结点。
禁用索引的分布式特性，使索引只创建在本地主机上：
index.number_of_shards: 1
index.number_of_replicas: 0
但随着版本的升级 将不在配置文件中配置而实启动ES后，再进行配置
```

bootstrap.memory_lock: true 当JVM做分页切换（swapping）时，ElasticSearch执行的效率会降低，推荐把ES_MIN_MEM和ES_MAX_MEM两个环境变量设置成同一个值，并且保证机器有足够的物理内存分配给ES，同时允许ElasticSearch进程锁住内存<br />network.bind_host: 设置可以访问的ip,可以是ipv4或ipv6的，默认为0.0.0.0，这里全部设置通过<br />network.publish_host:设置其它结点和该结点交互的ip地址，如果不设置它会自动判断，值必须是个真实的ip地址

```
同时设置bind_host和publish_host两个参数可以替换成network.host
network.bind_host: 192.168.233.137
network.publish_host: 192.168.233.137
=>network.host: 192.168.233.137
```

http.port:设置对外服务的http端口，默认为9200<br />transport.tcp.port: 设置节点之间交互的tcp端口，默认是9300<br />http.cors.enabled: 是否允许跨域REST请求<br />http.cors.allow-origin: 允许 REST 请求来自何处<br />node.master: true 配置该结点有资格被选举为主结点（候选主结点），用于处理请求和管理集群。如果结点没有资格成为主结点，那么该结点永远不可能成为主结点；如果结点有资格成为主结点，只有在被其他候选主结点认可和被选举为主结点之后，才真正成为主结点。<br />node.data: true 配置该结点是数据结点，用于保存数据，执行数据相关的操作（CRUD，Aggregation）；<br />discovery.zen.minimum_master_nodes: //自动发现master节点的最小数，如果这个集群中配置进来的master节点少于这个数目，es的日志会一直报master节点数目不足。（默认为1）为了避免脑裂，个数请遵从该公式 => (totalnumber of master-eligible nodes / 2 + 1)。 _ 脑裂是指在主备切换时，由于切换不彻底或其他原因，导致客户端和Slave误以为出现两个active master，最终使得整个集群处于混乱状态_<br />discovery.zen.ping.unicast.hosts： 集群个节点IP地址，也可以使用es-node等名称，需要各节点能够解析

<a name="a9957162"></a>
## 启动集群

上面第一次启动的容器重启，并增加启动两个ES节点：

```
docker restart es1
docker run -d -e ES_JAVA_OPTS="-Xms256m -Xmx256m" -p 9201:9201 -p 9301:9301 -v /opt/docker/es/config/es2.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /opt/docker/es/data2:/usr/share/elasticsearch/data -v /opt/docker/es/logs2/:/usr/share/elasticsearch/logs --name es2 elasticsearch:6.5.4
docker run -d -e ES_JAVA_OPTS="-Xms256m -Xmx256m" -p 9202:9202 -p 9302:9302 -v /opt/docker/es/config/es3.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /opt/docker/es/data3:/usr/share/elasticsearch/data -v /opt/docker/es/logs3/:/usr/share/elasticsearch/logs --name es3 elasticsearch:6.5.4
```

<a name="38c74edf"></a>
## 查看是否搭建成功

在浏览器地址栏访问http://192.168.233.137:9200/_cat/nodes?pretty 查看节点状态

<a name="3e0c0d23"></a>
## 拉取elasticsearch-head前端框架

```
docker pull mobz/elasticsearch-head:5
# 启动
docker run -d -p 9100:9100 --name es-manager  mobz/elasticsearch-head:5
```

在浏览器中访问：[http://192.168.233.137:9100/](http://192.168.233.137:9100/)<br />**[注]** 索引分片设置以及副本：

```
curl -XPUT ‘http://localhost:9200/_all/_settings?preserve_existing=true’ -d ‘{
“index.number_of_replicas” : “1”,
“index.number_of_shards” : “10”
}’
```

<a name="10728499"></a>
# 三、logstash（MySql同步数据到ES）

<a name="a12e3a52"></a>
## 使用 Logstash 将mysql 数据库数据同步到 elasticsearch

```
# 拉取镜像
docker pull docker.elastic.co/logstash/logstash:6.5.4
# 创建挂载配置文件夹
mkdir /opt/docker/logstash/config/pipeline
```

- 在/opt/docker/logstash/config/pipeline的路径下创建一个配置文件<br />
logstash-mysql.conf

```
input {
    jdbc {
        jdbc_connection_string => "jdbc:mysql://192.168.0.143:3306/es_logstash?useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT%2B8"
        jdbc_user => "root"
        jdbc_password => "root"
    
        jdbc_driver_library => "/opt/docker/logstash/config/mysql-connector-java-8.0.17/mysql-connector-java-8.0.17.jar"
        jdbc_driver_class => "com.mysql.jdbc.Driver"
        jdbc_paging_enabled => "true"
        jdbc_page_size => "50000"
        jdbc_default_timezone => "Asia/Shanghai"
       
        statement  => "select * from person"
        #statement_filepath => "/usr/local/src/logstash-6.6.0/config/jdbc.sql"
        
        schedule => "* * * * *"
        type => "jdbc"
    }
}
output {
    elasticsearch {
        hosts => "192.168.233.137:9200"
      
        index => "demo"
   
        document_id => "%{id}"
    }
 
    stdout {
        codec => json_lines
    }
}
```

注意output中设置了elasticsearch的连接地址：192.168.233.137:9200

上面配置中需要用到数据库以及连接数据库的jar包：<br />在数据库es_logstash中创建person的表：

```sql
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `birthday` datetime(0) NULL DEFAULT NULL,
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `delete` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES (1, 'wu', 'CN', 23, '2019-08-21 15:40:49', '测试11', '2019-08-21 15:40:57', '2019-08-21 15:40:59', '0');

SET FOREIGN_KEY_CHECKS = 1;

```

- 下载mysql-connector-java放到文件夹：/opt/docker/logstash/config中<br />
下载地址：[https://dev.mysql.com/downloads/connector/j](https://dev.mysql.com/downloads/connector/j)

```
wget https://cdn.mysql.com/Downloads/Connector-J/mysql-connector-java-8.0.17.tar.gz
tar -xvf mysql-connector-java-8.0.17.tar.gz
```

- 启动容器

```
 docker run --name logstash -it -p 4560:4560 -v /opt/docker/logstash/config/:/opt/docker/logstash/config/ -v /opt/docker/logstash/config/pipeline/:/usr/share/logstash/pipeline/  -d docker.elastic.co/logstash/logstash:6.5.4 -f /usr/share/logstash/pipeline/logstash-mysql.conf
```

- 进入容器安装插件<br />
安装 jdbc 和 elasticsearch 插件

```
# 进入到容器执行
./bin/logstash-plugin install logstash-input-jdbc
./bin/logstash-plugin install logstash-output-elasticsearch
```

- 重新启动logstash
- 验证ES中是否有数据加入

在浏览器中访问：ip:9200/demo/_search 可以看到添加的数据<br />![image.png](https://cdn.nlark.com/yuque/0/2019/png/250511/1566395153825-921369fc-7c69-48ed-b014-e0f59e5a3153.png#align=left&display=inline&height=911&name=image.png&originHeight=967&originWidth=706&size=53306&status=done&width=664.7834794320743)
<a name="MaSr5"></a>
### logstash-mysql.conf配置文件同步配置示例：

```
input {
  jdbc {
    # mysql相关jdbc配置
    jdbc_connection_string => "jdbc:mysql://192.168.0.143:3306/es_logstash?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8"
    jdbc_user => "root"
    jdbc_password => "root"
    # jdbc连接mysql驱动的文件  此处路径一定要正确 否则会报com.mysql.cj.jdbc.Driver could not be loaded
    jdbc_driver_library => "/opt/docker/logstash/config/mysql-connector-java-8.0.17.jar"
    # the name of the driver class for mysql
    jdbc_driver_class => "com.mysql.cj.jdbc.Driver"
    jdbc_paging_enabled => true
    jdbc_page_size => "50000"
    jdbc_default_timezone =>"Asia/Shanghai"
    # mysql文件, 也可以直接写SQL语句在此处，如下：
    # 如果要使字段和实体类的驼峰命名法一致  则需要这样写sql  select d_name as dName, c_id as cId from area where update_time >= :sql_last_value order by update_time asc
    statement => "select * from person"
    # statement_filepath => "./config/jdbc.sql"
    # 这里类似crontab,可以定制定时操作，比如每分钟执行一次同步(分 时 天 月 年)
    schedule => "* * * * *"
    #type => "jdbc"
 
    # 是否记录上次执行结果, 如果为真,将会把上次执行到的 tracking_column 字段的值记录下来,保存到 last_run_metadata_path 指定的文件中
    #record_last_run => true
    # 是否需要记录某个column 的值,如果record_last_run为真,可以自定义我们需要 track 的 column 名称，此时该参数就要为 true. 否则默认 track 的是 timestamp 的值.
    # use_column_value => true
    # 如果 use_column_value 为真,需配置此参数. track 的数据库 column 名,该 column 必须是递增的. 一般是mysql主键
    # tracking_column => "update_time"
    # tracking_column_type => "timestamp"
    # last_run_metadata_path => "area_logstash_capital_bill_last_id"
    # 是否清除 last_run_metadata_path 的记录,如果为真那么每次都相当于从头开始查询所有的数据库记录
    # clean_run => false
    #是否将 字段(column) 名称转小写
    #lowercase_column_names => false
  }
}
filter {
  date {
    match => [ "update_time", "yyyy-MM-dd HH:mm:ss" ]
    timezone => "Asia/Shanghai"
  }
}
output {
  elasticsearch {
    hosts => ["192.168.233.137:9200"]
    # index名 自定义 相当于数据库 对于实体类上@Document(indexName = "person", type = "person"）indexName
    index => "person"  
    #索引的类型 相当于数据库里面的表 对于实体类上@Document(indexName = "person", type = "person"）type
    document_type => "person"
    #需要关联的数据库中有有一个id字段，对应索引的id号
    document_id => "%{id}"
    template_overwrite => true
  }
  # 这里输出调试，正式运行时可以注释掉
  stdout {
      codec => json_lines
  }
}
```
<a name="MTrx8"></a>
### 同步MySQL增量更新配置说明
1、首先要在数据库字段里增加记录修改日期时间的字段。<br />修改sql：<br />`SELECT * from person where update_time> :sql_last_value`<br />**:sql_last_value** 为上一次更新的最后时刻。<br />更新的前提是必须满足: update_time** > :sql_last_value** 。即如果mysql的时间修改为小于sql_last_value的时刻值，是无法进行同步的。<br />注意:如果你的语句比较复杂，**update_time > :sql_last_value** 一定要写在WHERE后面，然后接AND即可。<br />2、基于时间更新自定义脚本<br />在配置文件中有 **use_column_value** 字段，决定是否需要记录某个column 的值,如果不配置此项的话，默认 **track** 的是 **timestamp**的值。也就是说 **:sql_last_value** 记录的是上一次执行的最后时刻。<br />如果 **use_column_value** 字段设置为 **true**， 并且**record_last_run** 为真,我们就可以自定义我们需要 **track** 的 **column** 名称了

```
input {
  jdbc {
    jdbc_driver_library => "/opt/docker/logstash/config/mysql-connector-java-8.0.17.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://192.168.0.143:3306/es_logstash?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8"
    jdbc_user => "root"
    jdbc_password => "root"
    schedule => "* * * * *"
	# 是否使用数据库某一列的值，
    use_column_value => true
	# 是否记录上次运行的结果
    record_last_run => true
	# 使用update_time字段
    tracking_column => update_time
	# 查询update_time字段大于上一次执行时间的数据
    statement => "SELECT * from person where update_time > :sql_last_value"
	# 记录上次运行结果的文件位置
    last_run_metadata_path => "/opt/docker/logstash/config/last_run.txt"
  }
}

output {
  elasticsearch {
  hosts => "localhost:9200"
  index => "person"
  document_type => "person"
  document_id => "%{id}"
  }
  stdout {
  codec => json_lines
  }
}
```
<a name="d9ygh"></a>
### 多表导入
实现多张数据表导入到 Elasticsearch 中<br />通过多个jdbc来实现，需要在每一个jdbc配置项中加入 **type** 配置，然后 elasticsearch 配置项中加入 **document_type => “%{type}”**

```
input {
  jdbc {
    jdbc_driver_library => "/opt/docker/logstash/config/mysql-connector-java-8.0.17.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://192.168.0.143:3306/es_logstash?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8"
    jdbc_user => "root"
    jdbc_password => "root"
    schedule => "* * * * *"
    statement => "select * from article"
    type => "article"
  }
  jdbc {
    jdbc_driver_library => "/opt/docker/logstash/config/mysql-connector-java-8.0.17.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://192.168.0.143:3306/es_logstash?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8"
    jdbc_user => "root"
    jdbc_password => "root"
    schedule => "* * * * *"
    statement => "select * from comment"
    type => "comment"
  } 
}
output {
    elasticsearch {
		hosts => "localhost:9200"
        index => "information"
        document_type => "%{type}"
        document_id => "%{id}"
        
    }
}
```
<a name="KUFwC"></a>
### 执行导入
在logstash容器中配置文件的目录下执行命令，完成数据的导入：

```
./bin/logstash -f logstash-mysql.conf
```
<a name="h3-u67E5u770Bu6570u636E"></a>
### 查看数据
可以使用 `ip:9200/person/_search` 的形式在浏览器中查看，也可以使用curl请求查看es中的数据，可以看到mysql中的数据都导入到了es中。
<a name="kibana"></a>
# 四、kibana（可视化监控）

- 拉取镜像<br />
`docker pull docker.elastic.co/kibana/kibana:6.5.4`
- 创建默认配置文件

```
vi /opt/docker/kibana/kibana.yml
```

kibana.yml

```
server.host: "0.0.0.0"
elasticsearch.url: "http://192.168.233.137:9200"
```

- 创建并启动容器<br />
`docker run -d -p 5601:5601 --name kibana -v /opt/docker/kibana/kibana.yml:/usr/share/kibana/config/kibana.yml docker.elastic.co/kibana/kibana:6.5.4`


<a name="78ce53e3"></a>
# 部署环境相关问题

- es 启动报错<br />
max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
<br />解决办法：
<br />在/etc/sysctl.conf文件最后添加一行
<br />`vm.max_map_count=262144`
- Docker挂载主机目录Docker访问出现Permission denied
<br />原由：
<br />CentOS7中的安全模块selinux把权限禁掉了.
<br />解决：
<br />在容器启动的时候加上 --privileged=true
<br />eg: `docker run --privileged=true -d -e ES_JAVA_OPTS="-Xms256m -Xmx256m" -p 9200:9200 -p 9300:9300 -v /opt/docker/es/config/es1.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /opt/docker/es/data1/:/usr/share/elasticsearch/data/ -v /opt/docker/es/logs1/:/usr/share/elasticsearch/logs/ --name es1 elasticsearch:6.5.4`
<br />解决方案2：
<br />控制SELinux的位置在系统/etc/selinux/config文件中，将SELINUX=enforcing改为SELINUX=disabled，永久关闭SELinux状态。

<a name="d17a0f0b"></a>
## 参考

1. ElasticSearch 官方文档：[https://www.elastic.co/guide/en/elasticsearch/reference/6.x/getting-started.html](https://www.elastic.co/guide/en/elasticsearch/reference/6.x/getting-started.html)
2. spring-data-elasticsearch 官方文档：[https://docs.spring.io/spring-data/elasticsearch/docs/3.1.2.RELEASE/reference/html/](https://docs.spring.io/spring-data/elasticsearch/docs/3.1.2.RELEASE/reference/html/)
3. logstash官方文档：[https://www.elastic.co/cn/blog/logstash-jdbc-input-plugin](https://www.elastic.co/cn/blog/logstash-jdbc-input-plugin)
4. docker-logstash配置官方参考文章：[https://www.elastic.co/guide/en/logstash/current/docker-config.html](https://www.elastic.co/guide/en/logstash/current/docker-config.html)
