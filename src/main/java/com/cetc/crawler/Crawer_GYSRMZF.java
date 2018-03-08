package com.cetc.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import com.cetc.utils.TimeUtils;
import java.io.IOException;
import java.io.PrintStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_GYSRMZF
{
  public static void main(String[] args)
    throws IOException
  {
    String host = "http://www.gygov.gov.cn/";
    String homePageUrl = "http://www.gygov.gov.cn/c78/index.html";
    Document homePage = HttpClient.getHtmlDocument(homePageUrl);
    
    Element topicListTable = (Element)homePage.select("[style='padding:0px 15px 15px 15px;']").get(0);
    Elements topicListTd = topicListTable.getElementsByTag("td");
    for (int topicNum = 3; topicNum <= 3; topicNum++)
    {
      Element topicTd = (Element)topicListTd.get(topicNum);
      String topicName = topicTd.text();
      String topicHref = topicTd.child(1).attr("href");
      Document topicPage = HttpClient.getHtmlDocument(topicHref);
      


      Element pageInfoScript = (Element)topicPage.getElementsByTag("script").get(9);
      String pageInfo = pageInfoScript.toString();
      pageInfo = pageInfo.substring(pageInfo.lastIndexOf("{"), pageInfo.lastIndexOf("}") + 1);
      JSONObject jsPageInfo = JSONObject.parseObject(pageInfo);
      Integer pageTotal = jsPageInfo.getInteger("pageTotal");
      for (int currentPage = 15; currentPage <= pageTotal.intValue(); currentPage++)
      {
        String dataListUrl = "http://www.gygov.gov.cn/gygov//openapi/info/ajaxpagelist.do?pagesize=22&channelid=78&pageno=" + 
          currentPage;
        String dataList = HttpClient.getHtmlDocument(dataListUrl).body().text();
        JSONObject jsDataList = JSONObject.parseObject(dataList);
        JSONArray infolist = jsDataList.getJSONArray("infolist");
        for (int i = 0; i < infolist.size(); i++)
        {
          JSONObject jsData = infolist.getJSONObject(i);
          String dataName = jsData.getString("title");
          String dataPageUrl = jsData.getString("url");
          
          Document dataPage = HttpClient.getHtmlDocument(dataPageUrl);
          Element dataTable = dataPage.body().child(1).child(0).child(0).child(2).child(1);
          
          Element dataTbody = dataTable.child(0).child(0).child(0).child(1).child(0);
          
          Element dataDiv = dataPage.getElementById("zoom");
          
          Elements aDownloadFile = dataDiv.getElementsByTag("a");
          if ((aDownloadFile.size() != 0) && (((Element)aDownloadFile.get(0)).text() != null) && 
            (!((Element)aDownloadFile.get(0)).text().equals("")))
          {
            for (int j = 0; j < aDownloadFile.size(); j++)
            {
              String downloadHref = ((Element)aDownloadFile.get(j)).attr("href");
              if (downloadHref.length() >= 27)
              {
                String downloadSavePath = "D:/crawler/贵阳市人民政府/" + TimeUtils.getTime() + "/" + topicName + 
                  "/" + dataName + ((Element)aDownloadFile.get(j)).attr("title") + "/";
                String downloadUrl = "";
                if (downloadHref.substring(0, 4).equals("http")) {
                  downloadUrl = downloadHref;
                } else {
                  downloadUrl = host + downloadHref;
                }
                String downloadFormat = downloadHref.substring(downloadHref.lastIndexOf("."), 
                  downloadHref.length());
                String downloadName = "";
                if ((((Element)aDownloadFile.get(j)).text() == null) || (((Element)aDownloadFile.get(j)).text().equals(""))) {
                  downloadName = j + downloadFormat;
                } else {
                  downloadName = ((Element)aDownloadFile.get(j)).text() + downloadFormat;
                }
                HttpClient.download(downloadUrl, downloadName, downloadSavePath);
                System.out.println("download a file : " + downloadName);
                int t = (int)(Math.random() * 3.0D * 1000.0D);
                try
                {
                  Thread.sleep(t);
                }
                catch (InterruptedException e5)
                {
                  e5.printStackTrace();
                }
              }
            }
            Elements img = dataDiv.getElementsByTag("img");
            if (img.size() != 0)
            {
              String downloadSavePath = "D:/crawler/贵阳市人民政府/" + TimeUtils.getTime() + "/" + topicName + 
                "/" + dataName + "/";
              for (int j = 0; j < img.size(); j++)
              {
                String downloadImgSrc = ((Element)img.get(j)).attr("src");
                String downloadImgUrl = host + downloadImgSrc;
                String downloadImgFormat = downloadImgSrc
                  .substring(downloadImgSrc.lastIndexOf(".", downloadImgSrc.length()));
                String downloadImgName = dataName + j + downloadImgFormat;
                HttpClient.download(downloadImgUrl, downloadImgName, downloadSavePath);
                System.out.println("download a file : " + downloadImgName + " ,topicName: " + topicName + 
                  " ,currentPage:" + currentPage);
                int t = (int)(Math.random() * 2.0D * 1000.0D);
                try
                {
                  Thread.sleep(t);
                }
                catch (InterruptedException e5)
                {
                  e5.printStackTrace();
                }
              }
            }
          }
          String saveName = dataName + ".doc";
          String savePath = "D:/crawler/贵阳市人民政府/" + TimeUtils.getTime() + "/" + topicName + "/";
          String e2 = "<table></table>";
          ExcelUtils.writeWordFile(savePath, saveName, e2 + dataDiv.toString());
          System.out.println("download a file : " + saveName + " ,topicName: " + topicName + " ,currentPage:" + 
            currentPage);
          
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
        System.out.println();
      }
    }
    System.out.println();
  }
}
