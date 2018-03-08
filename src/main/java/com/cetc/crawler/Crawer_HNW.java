package com.cetc.crawler;

import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import com.cetc.utils.TimeUtils;
import java.io.PrintStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_HNW
{
  public static void main(String[] args)
    throws Exception
  {
    String host = "http://news.cnhnb.com/";
    String homePageUrl = "http://news.cnhnb.com/hangqing/0/key=/15/";
    Document docHomePage = HttpClient.getHtmlDocument(homePageUrl);
    
    String[] titleList = new String[6];
    Element titleListUl = ((Element)docHomePage.select(".column-other").get(0)).child(0);
    titleList[0] = titleListUl.child(0).text();
    titleList[1] = titleListUl.child(1).text();
    titleList[2] = titleListUl.child(2).text();
    titleList[3] = titleListUl.child(3).text();
    titleList[4] = titleListUl.child(4).text();
    titleList[5] = titleListUl.child(5).text();
    

    Element provinceListDiv = (Element)docHomePage.select(".ci-schq-fl").get(1);
    Elements provinceListLi = provinceListDiv.getElementsByTag("li");
    for (int i = 1; i < provinceListLi.size(); i++)
    {
      Element provinceLi = (Element)provinceListLi.get(i);
      String provinceHref = provinceLi.child(0).attr("href");
      String provinceName = provinceLi.child(0).text();
      
      String savePath = "D:/crawler/惠农网/" + TimeUtils.getTime() + "/";
      String name = provinceName + "农产品数据" + "_" + TimeUtils.getTime() + ".xlsx";
      ExcelUtils.createExcel(savePath, name, titleList);
      

      String provincePageUrl = host + provinceHref;
      
      Document provincePage = HttpClient.getHtmlDocument(provincePageUrl);
      
      Element pageCenter = provincePage.getElementById("bootstrappager");
      String totalPage = "1";
      if (pageCenter != null)
      {
        Elements aPage = pageCenter.children();
        totalPage = ((Element)aPage.get(aPage.size() - 2)).text();
      }
      Integer total = Integer.valueOf(Integer.parseInt(totalPage));
      for (int currentPage = 1; currentPage <= total.intValue(); currentPage++)
      {
        String provincePageNumUrl = provincePageUrl + "/" + currentPage + "/";
        provincePage = HttpClient.getHtmlDocument(provincePageNumUrl);
        Elements productDataListDivs = provincePage.select(".column-other");
        Element productDataListDiv = (Element)productDataListDivs.get(0);
        Elements dataUls = productDataListDiv.children();
        
        String[] productData = new String[6];
        for (int j = 1; j < dataUls.size() - 1; j++)
        {
          Elements productDataLi = ((Element)dataUls.get(j)).children();
          productData[0] = ((Element)productDataLi.get(0)).text();
          productData[1] = ((Element)productDataLi.get(1)).text();
          productData[2] = ((Element)productDataLi.get(2)).text();
          productData[3] = ((Element)productDataLi.get(3)).text();
          productData[4] = ((Element)productDataLi.get(4)).text();
          productData[5] = ((Element)productDataLi.get(5)).text();
          
          ExcelUtils.createExcelOfData(productData, savePath + name);
          System.out.println("crawer a data , save to : " + name);
        }
        System.out.println("crawer a page , currentPage : " + currentPage + ", totalPage : " + totalPage);
      }
    }
  }
}
