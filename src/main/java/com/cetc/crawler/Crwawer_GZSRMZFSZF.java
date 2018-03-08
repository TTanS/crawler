package com.cetc.crawler;

import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import java.io.PrintStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crwawer_GZSRMZFSZF
{
  public static void main(String[] args)
  {
    String host = "http://www.gzgov.gov.cn/xxgk/jbxxgk/fgwj/szfwj_8191/";
    String homePageUrl = "http://www.gzgov.gov.cn/xxgk/jbxxgk/fgwj/szfwj_8191/szfl_8192/index.html";
    Document homePage = HttpClient.getHtmlDocument(homePageUrl);
    
    Element topicListDiv = (Element)homePage.select(".left-nav").get(0);
    Elements topicListScript = topicListDiv.getElementsByTag("script");
    String topicHref = "szfl_8192";
    for (int i = 7; i < topicListScript.size(); i++)
    {
      String topicScript = ((Element)topicListScript.get(i)).toString();
      
      String topicScript1 = topicScript.substring(topicScript.indexOf("'") + 1);
      
      String topicName = topicScript1.substring(0, topicScript1.indexOf("'"));
      topicScript1 = topicScript1.substring(topicScript1.indexOf("'") + 1);
      topicScript1 = topicScript1.substring(topicScript1.indexOf("'") + 1);
      if (i != 0)
      {
        topicHref = topicScript1.substring(0, topicScript1.indexOf("'"));
        topicHref = topicHref.substring(3, topicHref.length());
        if (i == topicListScript.size() - 2) {}
      }
      else
      {
        String topicPageUrl = host + topicHref + "/";
        Document topicPage = HttpClient.getHtmlDocument(topicPageUrl);
        


        Integer total = Integer.valueOf(1);
        Elements pageInfos = topicPage.select(".page");
        if ((pageInfos != null) && (pageInfos.size() != 0)) {
          try
          {
            Element pageInfo = ((Element)topicPage.select(".page").get(0)).child(0);
            String totalPage = pageInfo.toString();
            totalPage = totalPage.substring(totalPage.indexOf("(") + 1, totalPage.indexOf(","));
            total = Integer.valueOf(Integer.parseInt(totalPage));
          }
          catch (NumberFormatException e)
          {
            e.printStackTrace();
            total = Integer.valueOf(1);
          }
        } else {
          total = Integer.valueOf(1);
        }
        for (int currentPage = 1; currentPage <= total.intValue(); currentPage++)
        {
          String dataPageInNumUrl = "";
          if (currentPage == 1) {
            dataPageInNumUrl = topicPageUrl;
          } else {
            dataPageInNumUrl = topicPageUrl + "/index_" + (currentPage - 1) + ".html";
          }
          Document dataPageInNum = HttpClient.getHtmlDocument(dataPageInNumUrl);
          
          Elements dataListDivs = dataPageInNum.select(".right-list-box");
          Element dataListDiv = (Element)dataPageInNum.select(".right-list-box").get(0);
          Elements dataList = dataListDiv.child(0).children();
          for (int j = 0; j < dataList.size(); j++)
          {
            String dataName = ((Element)dataList.get(j)).child(0).attr("title");
            String dataHref = ((Element)dataList.get(j)).child(0).attr("href");
            String dataDate = ((Element)dataList.get(j)).child(1).text();
            
            Document dataPage = HttpClient.getHtmlDocument(dataHref);
            
            Element data = null;
            try
            {
              data = (Element)dataPage.select(".zw-con").get(0);
            }
            catch (Exception e)
            {
              e.printStackTrace();
              continue;
            }
            String temp = "<table cellspacing='0' cellpadding='0'> <tbody><tr class='firstRow'><td></td><td></td></tr></tbody></table>";
            String context = temp + data.toString();
            
            String savePath = "D:/crawler/贵州省人民政府/政府文件/" + topicName + "/";
            String saveName = dataName + ".doc";
            ExcelUtils.writeWordFile(savePath, saveName, context);
            
            System.out.println("currentPage:" + currentPage + ", saveName:" + saveName);
            
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
          System.out.println("crawer a page , totalPage : " + total);
        }
        System.out.println(topicPageUrl);
      }
    }
  }
}
