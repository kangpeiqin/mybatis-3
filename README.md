## MyBatis 源码阅读
* 官方文档： [See the docs](http://mybatis.github.io/mybatis-3)
### MyBatis 执行流程
![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/58853c11b6c64138a34847b7d730e0bc~tplv-k3u1fbpfcp-watermark.image)
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
