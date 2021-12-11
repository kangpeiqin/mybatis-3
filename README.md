## MyBatis 源码阅读
* 官方文档： [See the docs](http://mybatis.github.io/mybatis-3)
### 架构图
![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/58853c11b6c64138a34847b7d730e0bc~tplv-k3u1fbpfcp-watermark.image)
### 常用工具类
- MetaObject
> 用于获取和设置对象的属性值
- MetaClass
> 反射工具类，用于获取类相关的信息。例如，可以使用MetaClass判断某个类是否有默认构造方法，还可以判断类的属性是否有对应的Getter/Setter方法
- ObjectFactory
> MyBatis中的对象工厂，只有一个默认的实现，即DefaultObjectFactory
- ProxyFactory
> 代理工厂，主要用于创建动态代理对象，两个不同的实现类，分别为CglibProxyFactory和JavassistProxyFactory
### 核心组件
- Configuration
> 描述MyBatis主配置文件的信息。
- Executor
> 定义了对数据库的增删改查方法，SqlSession是MyBatis提供的操作数据库的API，但是真正执行SQL的是Executor组件。
- MappedStatement
> 描述<select|update|insert|delete>或者@Select、@Update等注解配置的SQL信息
- StatementHandle
> 封装了对JDBC Statement的操作
- TypeHandler
> 处理JDBC类型与Java类型之间的转换
- ParameterHandler
> 为`SQL`参数占位符设置值
- ResultSetHandler
> 对结果集或存储过程的执行结果进行处理。
### 缓存
> 在应用程序和数据库都是单节点的情况下，合理使用缓存能够减少数据库IO，显著提升系统性能。MyBatis的缓存分为一级缓存和二级缓存，一级缓存默认是开启的，而且不能关闭。
#### 一级缓存
> 级别：SESSION：缓存对整个SqlSession有效。STATEMENT：缓存仅对当前执行的语句有效
#### 二级缓存
- 使用
> 主配置文件中配置是否开启缓存
```text
<settings>
    <setting name="cacheEnabled" value="true"/>
<settings>
```
> 在Mapper文件中配置缓存相关属性
```text
<!--配置缓存刷新频率、大小-->
<cache flushInterval="3600000" size="512"/>
```
![cache](https://s3.bmp.ovh/imgs/2021/09/b21737bf4ac84cb4.jpg)
- 实现原理
> 利用CachingExecutor装饰Executor，增加了二级缓存功能
### 执行流程
#### 1、配置文件的加载和解析流程
![note.jpg](https://s3.bmp.ovh/imgs/2021/09/3497ff641ce73e23.jpg)
- 读取主配置文件，转成字符流
```text
Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/no_param_type/mybatis-config.xml"));
```
- 通过`SqlSessionFactoryBuilder`创建`SqlSessionFactory`对象。
```text
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
```
> 其他细节
##### (1).采用`XPath`方式对`XML`文件进行解析。MyBatis通过XPathParser工具类封装了对XML的解析操作，同时使用XNode类增强了对XML节点的操作
> JDK API中提供了3种方式解析XML，分别为DOM、SAX和XPath。下面的例子展示用`XPath`对`XML`文件进行解析
- 要解析的xml文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<users>
  <user>
    <name>雪花飘飘</name>
  </user>
</users>
```
- 对应的实体类
```java
@Data
@Accessors(chain = true)
public class User {
  private String name;
}
```
- 解析过程
```java
public class XPathTest {

  @Test
  public void xpathParserTest() {
    try {
      //读取xml文件
      InputStream inputStream = Resources.getResourceAsStream("org/apache/ibatis/learning/user.xml");
      //采用工厂模式创建Document对象
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      //对字节流进行解析
      Document doc = builder.parse(inputStream);
      //使用XPath对象执行表达式，获取XML内容
      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList nodeList = (NodeList) xPath.evaluate("/users/*", doc, XPathConstants.NODESET);
      List<User> list = new ArrayList<>();
      for (int i = 1; i < nodeList.getLength() + 1; i++) {
        String path = "/users/user[" + i + "]";
        String name = (String) xPath.evaluate(path + "/name", doc, XPathConstants.STRING);
        list.add(new User().setName(name));
      }
      System.out.println(list);
    } catch (Exception e) {
      throw new BuilderException("xml文件解析错误：" + e.getMessage());
    }
  }
}
```
总结：创建表示XML文档的Document对象 —> 创建用于执行XPath表达式的XPath对象 —> 使用XPath对象执行表达式，获取XML内容


