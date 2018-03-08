package com.cetc.crawler;

import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import com.cetc.utils.TimeUtils;
import java.io.PrintStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_XNCSW
{
  private static int dataCounter = 0;
  private static int listCounter = 0;
  
  public static void main(String[] args)
    throws Exception
  {
    String host = "http://nc.mofcom.gov.cn/";
    for (int currentHomePage = 10; currentHomePage <= 34; currentHomePage++)
    {
      String homePageUrl = "http://nc.mofcom.gov.cn/channel/jghq2017/market_list.shtml?page=" + currentHomePage;
      Document homePage = HttpClient.getHtmlDocument(homePageUrl);
      
      Element marketListUl = (Element)homePage.select(".marketList").get(0);
      Elements marketListLi = marketListUl.children();
      for (int i = 1; i < marketListLi.size(); i++)
      {
        dataCounter = 0;
        listCounter = 0;
        Element market = (Element)marketListLi.get(i);
        Element amarket = ((Element)market.getElementsByTag("h1").get(0)).child(0);
        String marketHref = amarket.attr("href");
        
        String marketName = amarket.text();
        Element locationDiv = (Element)market.select(".mt20").get(0);
        
        String productionType = locationDiv.text();
        productionType = productionType.substring(productionType.indexOf("：") + 1, 
          productionType.indexOf("：") + 4);
        
        String marketLocation = locationDiv.child(0).text();
        marketLocation = marketLocation.substring(marketLocation.indexOf("：") + 1, marketLocation.length());
        
        String marketPageUrl = host + marketHref;
        Document marketPage = HttpClient.getHtmlDocument(marketPageUrl);
        
        Element excelHeadTr = null;
        try
        {
          excelHeadTr = ((Element)marketPage.getElementsByTag("tbody").get(0)).child(0);
        }
        catch (Exception e1)
        {
          e1.printStackTrace();
          continue;
        }
        String[] excelHead = new String[4];
        excelHead[0] = excelHeadTr.child(0).text();
        excelHead[1] = excelHeadTr.child(1).text();
        excelHead[2] = excelHeadTr.child(2).text();
        excelHead[3] = excelHeadTr.child(3).text();
        
        String savePath = "D:/crawler/新农村商网/" + TimeUtils.getTime() + "/";
        String name = marketName + "_" + TimeUtils.getTime() + ".xlsx";
        ExcelUtils.createExcel(savePath, name, excelHead);
        

        String pageScript = ((Element)marketPage.getElementsByTag("script").get(10)).data();
        String totalPage = pageScript.substring(pageScript.indexOf("=") + 1, pageScript.indexOf(";"))
          .replaceAll(" ", "");
        Integer total = Integer.valueOf(Integer.parseInt(totalPage));
        String[] productData = new String[4];
        for (int currentPage = 1; currentPage <= total.intValue(); currentPage++)
        {
          String marketPageNumUrl = marketPageUrl + "&page=" + currentPage;
          marketPage = HttpClient.getHtmlDocument(marketPageNumUrl);
          
          Element productDataListTbody = null;
          try
          {
            productDataListTbody = (Element)marketPage.getElementsByTag("tbody").get(0);
          }
          catch (Exception e)
          {
            e.printStackTrace();
            continue;
          }
          Elements productDataList = productDataListTbody.children();
          for (int j = 1; j < productDataList.size(); j++)
          {
            Element product = (Element)productDataList.get(j);
            productData[0] = product.child(0).text();
            productData[1] = product.child(1).text();
            productData[2] = product.child(2).text();
            productData[3] = product.child(3).text();
            ExcelUtils.createExcelOfData(productData, savePath + name);
            System.out.println("crawer a data , save to : " + name);
            
            dataCounter += 1;
            if (dataCounter >= 3200)
            {
              dataCounter = 0;
              listCounter += 1;
              name = marketName + "_" + TimeUtils.getTime() + "_" + listCounter + ".xlsx";
              ExcelUtils.createExcel(savePath, name, excelHead);
            }
          }
          System.out.println("crawer a page , currentPage : " + currentPage + ", totalPage : " + totalPage);
        }
      }
    }
    System.out.println("暂停");
  }
}
