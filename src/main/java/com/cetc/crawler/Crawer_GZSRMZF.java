package com.cetc.crawler;

import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import java.io.PrintStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_GZSRMZF
{
  public static void main(String[] args)
  {
    String homePageUrl = "http://www.gzgov.gov.cn/xxgk/jbxxgk/fgwj/gzsfgjgz/";
    Document homePage = HttpClient.getHtmlDocument(homePageUrl);
    


    Integer total = Integer.valueOf(1);
    try
    {
      Element pageInfo = ((Element)homePage.select(".page").get(0)).child(0);
      String totalPage = pageInfo.toString();
      totalPage = totalPage.substring(totalPage.indexOf("(") + 1, totalPage.indexOf(","));
      total = Integer.valueOf(Integer.parseInt(totalPage));
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
      total = Integer.valueOf(1);
    }
    for (int currentPage = 1; currentPage <= total.intValue(); currentPage++)
    {
      String dataPageInNumUrl = "";
      if (currentPage == 1) {
        dataPageInNumUrl = homePageUrl;
      } else {
        dataPageInNumUrl = homePageUrl + "/index_" + (currentPage - 1) + ".html";
      }
      Document dataPageInNum = HttpClient.getHtmlDocument(dataPageInNumUrl);
      
      Element dataListDiv = (Element)dataPageInNum.select(".right-list-box").get(0);
      Elements dataList = dataListDiv.child(0).children();
      for (int i = 0; i < dataList.size(); i++)
      {
        String dataName = ((Element)dataList.get(i)).child(0).attr("title");
        String dataHref = ((Element)dataList.get(i)).child(0).attr("href");
        String dataDate = ((Element)dataList.get(i)).child(1).text();
        
        Document dataPage = HttpClient.getHtmlDocument(dataHref);
        
        Element data = (Element)dataPage.select(".zw-con").get(0);
        String temp = "<table cellspacing='0' cellpadding='0'> <tbody><tr class='firstRow'><td></td><td></td></tr></tbody></table>";
        String context = temp + data.toString();
        
        String savePath = "D:/crawler/贵州省人民政府/贵州省法规及规章/";
        String saveName = dataName + ".doc";
        ExcelUtils.writeWordFile(savePath, saveName, context);
        
        System.out.println("currentPage:" + currentPage + ", saveName:" + saveName);
      }
      System.out.println("crawer a page , totalPage : " + total);
    }
  }
}
