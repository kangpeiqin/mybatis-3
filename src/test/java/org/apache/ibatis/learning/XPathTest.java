package org.apache.ibatis.learning;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.submitted.permissions.Resource;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kpq
 * @since 1.0.0
 */

public class XPathTest {

  @Test
  public void xpathParserTest() {
    try {
      //读取xml文件
      InputStream inputStream = Resources.getResourceAsStream("org/apache/ibatis/learning/user.xml");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      //对字节流进行解析
      Document doc = builder.parse(inputStream);
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
