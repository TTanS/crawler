package com.cetc.crawler;

import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import java.io.PrintStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_ZGZFGKW
{
  public static void main(String[] args)
  {
    String homePageUrl = "http://www.gov.cn/zhengce/nodeinfo/nodetree_zt.xml";
    
    Document homePage = HttpClient.getHtmlDocument(homePageUrl);
    Elements topicList = homePage.select("[parentID='160']");
    for (int i = 17; i < 22; i++)
    {
      String topicName = ((Element)topicList.get(i)).child(0).text();
      String topicPageUrl = ((Element)topicList.get(i)).child(1).text();
      
      Document topicPage = HttpClient.getHtmlDocument(topicPageUrl);
      
      Element dataTitleDiv = ((Element)topicPage.select(".dataBox").get(0)).child(0).child(0).child(0);
      String[] dataTitle = new String[5];
      dataTitle[0] = dataTitleDiv.child(0).text();
      dataTitle[1] = dataTitleDiv.child(1).text();
      dataTitle[2] = dataTitleDiv.child(2).text();
      dataTitle[3] = dataTitleDiv.child(3).text();
      dataTitle[4] = dataTitleDiv.child(4).text();
      
      String totalPage = "1";
      try
      {
        Element pageData = (Element)topicPage.select(".pageInfo").get(0);
        totalPage = pageData.child(0).text();
        totalPage = totalPage.substring(totalPage.indexOf("共") + 1, totalPage.indexOf("页"));
      }
      catch (Exception e7)
      {
        e7.printStackTrace();
      }
      Integer total = Integer.valueOf(Integer.parseInt(totalPage));
      for (int currentPage = 1; currentPage <= total.intValue(); currentPage++)
      {
        String topicPageInNumUrl = topicPageUrl + "&p=" + (currentPage - 1);
        Document topicPageInNum = HttpClient.getHtmlDocument(topicPageInNumUrl);
        
        Element dataListDiv = (Element)topicPageInNum.select(".dataBox").get(0);
        Elements dataList = dataListDiv.getElementsByTag("tr");
        for (int j = 1; j < dataList.size(); j++)
        {
          Element dataTr = (Element)dataList.get(j);
          String[] dataGeneralInfo = new String[5];
          dataGeneralInfo[0] = dataTr.child(0).text();
          dataGeneralInfo[1] = dataTr.child(1).child(0).text();
          dataGeneralInfo[2] = dataTr.child(2).text();
          dataGeneralInfo[3] = dataTr.child(3).text();
          dataGeneralInfo[4] = dataTr.child(4).text();
          
          String essayPageUrl = dataTr.child(1).child(0).attr("href");
          Document essayPage = HttpClient.getHtmlDocument(essayPageUrl);
          Element essayContent = null;
          try
          {
            essayContent = (Element)essayPage.select(".wrap").get(0);
          }
          catch (Exception e6)
          {
            e6.printStackTrace();
            continue;
          }
          Element e1 = essayContent.child(0).child(0).child(0).child(0);
          Element e3 = e1.child(0);
          Element e4 = null;
          try
          {
            e4 = e1.child(1).child(0);
          }
          catch (Exception e5)
          {
            e5.printStackTrace();
          }
          Element e2 = (Element)essayContent.select(".b12c").get(0);
          String e = "";
          if (e4 == null) {
            e = e3.toString() + e2.toString();
          } else {
            e = e3.toString() + e4.toString() + e2.toString();
          }
          String saveName = dataGeneralInfo[1] + ".doc";
          String savePath = "D:/crawler/中国政府公开网/" + topicName + "/";
          ExcelUtils.writeWordFile(savePath, saveName, e);
          System.out.println(
            "currentPage:" + currentPage + ", topicName:" + topicName + ", saveName" + saveName);
          int t = (int)(Math.random() * 1.0D * 1000.0D);
          try
          {
            Thread.sleep(t);
          }
          catch (InterruptedException e5)
          {
            e5.printStackTrace();
          }
        }
        System.out.println("crawer a page , totalPage : " + totalPage);
      }
      System.out.println("一个主题完毕");
    }
  }
}
