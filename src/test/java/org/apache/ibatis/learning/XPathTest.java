package org.apache.ibatis.learning;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.submitted.permissions.Resource;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kpq
 * @since 1.0.0
 */

class XPathTest {

  String resource = "org/apache/ibatis/learning/user.xml";

  @Test
  void xpathParserTest() {
    try {
      //读取xml文件
      InputStream inputStream = Resources.getResourceAsStream(resource);
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      //对字节流进行解析，读取文件获得 xml 文档对象
      Document doc = builder.parse(inputStream);
      XPath xPath = XPathFactory.newInstance().newXPath();
      //获取给定的结点对象
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

  @Test
  void formatXNodeToString() {
    try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
      XPathParser parser = new XPathParser(inputStream, false, null, null);
      String usersNodeToString = parser.evalNode("/users").toString();
      String userNodeToString = parser.evalNode("/users/user").toString();
      System.out.println(usersNodeToString);
      System.out.println(userNodeToString);
    } catch (IOException e) {
      throw new BuilderException("xml文件解析错误：" + e.getMessage());
    }
  }

}
