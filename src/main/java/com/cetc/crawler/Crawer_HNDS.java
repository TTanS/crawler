package com.cetc.crawler;

import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import com.cetc.utils.TimeUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.PrintStream;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_HNDS
{
  public static void main(String[] args)
    throws Exception
  {
    String host = "http://www.huinong99.com/";
    
    WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
    String url = "http://www.huinong99.com/list/supplylist.aspx";
    HtmlPage htmlPage = (HtmlPage)webClient.getPage(url);
    
    HtmlElement countryListUl = 
      (HtmlElement)htmlPage.getByXPath("/html/body/form/div[7]/div/div[4]/div[3]/ul").get(0);
    DomNodeList<HtmlElement> countryList = countryListUl.getElementsByTagName("a");
    for (int i = 0; i < countryList.size(); i++)
    {
      HtmlElement countryA = (HtmlElement)countryList.get(i);
      String countryName = countryA.asText();
      

      String[] dataTitle = new String[10];
      dataTitle[0] = "产品标题";
      dataTitle[1] = "产品名";
      dataTitle[2] = "价格";
      dataTitle[3] = "产地";
      dataTitle[4] = "产品类别";
      dataTitle[5] = "产品规格";
      dataTitle[6] = "供应量";
      dataTitle[7] = "联系人";
      dataTitle[8] = "联系方式";
      dataTitle[9] = "发布时间";
      String savePath = "D:/crawler/惠农电商/" + TimeUtils.getTime() + "/";
      String name = countryName + "_" + TimeUtils.getTime() + ".xlsx";
      ExcelUtils.createExcel(savePath, name, dataTitle);
      

      HtmlAnchor countryAnchor = (HtmlAnchor)countryA;
      HtmlPage countryPage = (HtmlPage)countryAnchor.click();
      
      boolean pageTag = true;
      
      Integer currentPage = Integer.valueOf(1);
      while (pageTag)
      {
        DomElement pageData = countryPage.getElementById("divlist");
        DomNodeList<DomNode> pageDataDivs = pageData.getChildNodes();
        if (pageDataDivs.size() < 2) {
          break;
        }
        DomNode pageDataDiv = (DomNode)pageDataDivs.get(pageDataDivs.size() - 2);
        DomNodeList<DomNode> pageDataAs = pageDataDiv.getChildNodes();
        DomNode pageDataA = (DomNode)pageDataAs.get(pageDataAs.size() - 6);
        
        HtmlPage countryPageInNum = countryPage;
        if ((pageDataA.asText().equals("   下一页")) && (currentPage.intValue() > 1))
        {
          HtmlAnchor newlink = (HtmlAnchor)pageDataA;
          countryPageInNum = (HtmlPage)newlink.click();
          countryPage = countryPageInNum;
        }
        else if (currentPage.intValue() > 1)
        {
          pageTag = false;
        }
        String[] goodsData = new String[10];
        
        String strCountryPageInNum = countryPageInNum.asXml();
        Document docPage = Jsoup.parse(strCountryPageInNum);
        Element divlist = docPage.getElementById("divlist");
        Elements goodsDivs = divlist.children();
        for (int j = 0; j < goodsDivs.size() - 2; j++)
        {
          Element goodDiv = (Element)goodsDivs.get(j);
          Element goodDetailA = (Element)goodDiv.child(0).getElementsByTag("a").get(0);
          
          String goodReleaseTime = goodDiv.child(1).child(0).child(3).text();
          goodReleaseTime = strSub(goodReleaseTime);
          
          String goodDetailHref = goodDetailA.attr("href");
          String goodDetailUrl = host + goodDetailHref;
          Document goodDetailPage = HttpClient.getHtmlDocument(goodDetailUrl);
          
          Element goodDetailDiv = (Element)goodDetailPage.select(".shopshowwrap").get(0);
          
          String goodTitle = goodDetailDiv.child(0).child(1).child(0).text();
          
          String goodPrice = goodDetailDiv.child(0).child(1).child(1).text();
          goodPrice = strSub(goodPrice);
          
          String goodLinkman = goodDetailDiv.child(0).child(1).child(2).text();
          goodLinkman = strSub(goodLinkman);
          
          String goodPhoneNum = goodDetailDiv.child(0).child(1).child(3).text();
          goodPhoneNum = strSub(goodPhoneNum);
          
          String goodLocalityOfGrowth = goodDetailDiv.child(0).child(1).child(4).text();
          goodLocalityOfGrowth = strSub(goodLocalityOfGrowth);
          
          String goodFlow = goodDetailDiv.child(0).child(1).child(5).text();
          goodFlow = strSub(goodFlow);
          
          String goodClassification = goodDetailDiv.child(1).child(0).child(0).text();
          goodClassification = strSub(goodClassification);
          
          String goodName = goodDetailDiv.child(1).child(0).child(1).text();
          goodName = strSub(goodName);
          
          String goodNorms = goodDetailDiv.child(1).child(0).child(2).text();
          goodNorms = strSub(goodNorms);
          
          goodsData[0] = goodTitle;
          goodsData[1] = goodName;
          goodsData[2] = goodPrice;
          goodsData[3] = goodLocalityOfGrowth;
          goodsData[4] = goodClassification;
          goodsData[5] = goodNorms;
          goodsData[6] = goodFlow;
          goodsData[7] = goodLinkman;
          goodsData[8] = goodPhoneNum;
          goodsData[9] = goodReleaseTime;
          
          ExcelUtils.createExcelOfData(goodsData, savePath + name);
          System.out.println();
        }
        currentPage = Integer.valueOf(currentPage.intValue() + 1);
      }
    }
    webClient.close();
    
    System.out.println("");
  }
  
  private static String strSub(String str)
  {
    str = str.substring(str.indexOf("：") + 1, str.length());
    return str;
  }
}
