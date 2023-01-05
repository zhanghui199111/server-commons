# server-commons

## 工程简介

- 封装了Java工程中常见的Utils类(redis,http)
- 整合了常用的静态工具类(hutool,commons-lang3)
- 通过jar包的方式引入到各个工程中使用

## 包含模块

### RedisUtil

- Testcase: RedisUtilTest
- 基于spring-boot-starter-data-redis

### HttpClientUtil

- Testcase: HttpClientUtilTest(待补充)
- 基于org.apache.httpcomponents.httpclient

### AsyncHttpClientUtil
- Testcase: AsyncHttpClientUtilTest(待补充)
- 基于org.apache.httpcomponents.httpasyncclient  

### ElasticsearchUtil  
- Testcase: ElasticsearchUtilTest
- 基于spring-boot-starter-data-elasticsearch

## 开发规范
### TestCase
Utils中的所有方法都需要有完整的TestCase覆盖，否则不允许代码merge进主分支
#### RedisUtilTest
- 使用localhost redis作为testcase，使用单节点redis


## 打包流程
### local/dev
- 修改version为 1.0-SNAPSHOT
- .\gradlew.bat(./gradlew) jar，执行完成后可以在build/libs文件夹下看到server-commons-1.0-SNAPSHOT.jar
- .\gradlew.bat(./gradlew) install，将SNAPSHOT包安装到本地maven仓库(local)
- .\gradlew.bat(./gradlew) publish，将SNAPSHOT包发布到Nexus仓库(dev)
- 在目标工程使用compile (group: 'outfox.infra', name: 'server-commons', version: '1.0-SNAPSHOT')引入即可

### prod

- 修改version为 X.X.X 固定版本号
- .\gradlew(./gradlew) jar，执行完成后可以在build/libs文件夹下看到server-commons-X.X.X.jar
- .\gradlew(./gradlew) publish，将jar包发布到Nexus仓库
- 在目标工程使用compile (group: 'outfox.infra', name: 'server-commons', version: 'X.X.X')引入即可

本地开发推荐使用SNAPSHOT包，提测与线上环境使用稳定版本包
