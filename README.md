## MyBatis 源码阅读
* 官方文档： [See the docs](http://mybatis.github.io/mybatis-3)
### 架构图
![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/58853c11b6c64138a34847b7d730e0bc~tplv-k3u1fbpfcp-watermark.image)
### 1、配置文件的加载和解析流程
![note.jpg](https://s3.bmp.ovh/imgs/2021/09/3497ff641ce73e23.jpg)
- 读取主配置文件，转成字符流
```text
Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/no_param_type/mybatis-config.xml"));
```
- 通过`SqlSessionFactoryBuilder`创建`SqlSessionFactory`对象。
```text
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
```
#### 其他细节
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


