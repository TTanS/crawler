package com.cetc.crawler;

import com.cetc.utils.HttpClient;
import java.io.PrintStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_NCPXXW
{
  public static void main(String[] args)
  {
    String host = "http://www.nongnet.com/";
    String homePageUrl = "http://www.nongnet.com/classi_10.aspx";
    

    Document docHomePage = HttpClient.getHtmlDocument(homePageUrl);
    Element topicDiv = (Element)docHomePage.select("[style=width:800px]").get(0);
    Elements aTopic = topicDiv.getElementsByTag("a");
    for (int i = 1; i < aTopic.size(); i++)
    {
      String topicHref = ((Element)aTopic.get(i)).attr("href");
      String topicName = ((Element)aTopic.get(i)).text();
      String topicUrl = host + topicHref;
      
      Document topicPage = HttpClient.getHtmlDocument(topicUrl);
      Element type = (Element)topicPage.select("[class='divselect2']").get(0);
      Elements aType = type.getElementsByTag("a");
      for (int j = 0; j < aType.size(); j++)
      {
        String typeHref = ((Element)aType.get(j)).attr("href");
        String typeName = ((Element)aType.get(j)).text();
        String typeUrl = host + typeHref;
        
        Document locationPage = HttpClient.getHtmlDocument(typeUrl);
        Element locationDiv = (Element)topicPage.select("[class='divselect2']").get(1);
        Elements aLocation = locationDiv.getElementsByTag("a");
        for (int k = 3; k < aLocation.size(); k++)
        {
          String locationHref = ((Element)aLocation.get(k)).attr("href");
          String locationName = ((Element)aLocation.get(k)).text();
          String locationUrl = host + locationHref;
          
          Document supplyPage = HttpClient.getHtmlDocument(locationUrl);
          Element supplyDiv = (Element)supplyPage.select("[class='divselect2']").get(3);
          Element aSupply = (Element)supplyDiv.getElementsByTag("a").get(1);
          String supplyHref = aSupply.attr("href");
          String supplyName = aSupply.text();
          String supplyUrl = host + supplyHref;
          Document dataPage = HttpClient.getHtmlDocument(supplyUrl);
          
          Integer totalPage = Integer.valueOf(1);
          Element pageNumDiv = dataPage.getElementById("ContentMain_divpage");
          if ((pageNumDiv != null) && (!pageNumDiv.equals(""))) {
            totalPage = Integer.valueOf(Integer.parseInt(pageNumDiv.text().substring(pageNumDiv.text().indexOf("/") + 1, 
              pageNumDiv.text().indexOf("，") - 1)));
          }
          for (int currentPage = 1; currentPage <= totalPage.intValue(); currentPage++)
          {
            String dataPageByNumHref = ((Element)pageNumDiv.getElementsByTag("a").get(0)).attr("href");
            dataPageByNumHref = dataPageByNumHref.substring(0, dataPageByNumHref.lastIndexOf("_") + 1);
            String dataPageByNumUrl = host + dataPageByNumHref + currentPage + ".aspx";
            Document crawerPage = HttpClient.getHtmlDocument(dataPageByNumUrl);
            
            Element crawerDataDiv = 
              (Element)crawerPage.select("[style='text-align: left; line-height: 20px; margin-top: 10px;']").get(0);
            Elements crawerDataList = crawerDataDiv.select("[class='pic_divxinxi_title']");
            for (int l = 0; l < crawerDataList.size(); l++)
            {
              Element aCrawerData = (Element)((Element)crawerDataList.get(l)).getElementsByTag("a").get(0);
              String crawerDataHref = aCrawerData.attr("href");
              String crawerDataTitle = aCrawerData.attr("title");
              Document docCrawerData = HttpClient.getHtmlDocument(crawerDataHref);
              
              Element updateTimeDiv = (Element)docCrawerData.select("[style='float: left; width: 460px;']").get(0);
              String updateTime = ((Element)updateTimeDiv.getElementsByTag("font").get(0)).text();
              updateTime = updateTime.substring(updateTime.indexOf("："), updateTime.indexOf(" "));
              
              String newstPrice = ((Element)docCrawerData.select("[class='scdbj1']").get(0)).text();
              


              System.out.println("crawerDataHref: " + crawerDataHref + " ,crawerDataTitle:" + crawerDataTitle);
            }
            System.out.println(crawerDataDiv);
          }
          System.out.println(locationDiv);
        }
      }
      System.out.println("topicHref: " + topicHref + " ,topicName:" + topicName);
    }
  }
}
