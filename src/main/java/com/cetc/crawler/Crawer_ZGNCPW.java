package com.cetc.crawler;

import com.cetc.pojo.Product;
import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_ZGNCPW
{
  public static void main(String[] args)
    throws Exception
  {
    String homePageUrl = "http://www.zgncpw.com/sell/list/13/";
    Document docHomePage = HttpClient.getHtmlDocument(homePageUrl);
    
    Element productClassifyDiv = (Element)docHomePage.select("[class='tab-list auto select pos-rel clearfix']").get(0);
    Elements aProductClassify = productClassifyDiv.getElementsByTag("a");
    for (int i = 13; i < aProductClassify.size(); i++)
    {
      Element productClassify = (Element)aProductClassify.get(i);
      String productClassifyName = productClassify.attr("title");
      String productClassifyHref = productClassify.attr("href");
      
      Document productClassifyPage = HttpClient.getHtmlDocument(productClassifyHref);
      
      Integer totalPages = Integer.valueOf(1);
      try
      {
        String pageNumDetail = ((Element)productClassifyPage.getElementsByTag("cite").get(0)).text();
        String totalPage = pageNumDetail.substring(pageNumDetail.indexOf("/") + 1, pageNumDetail.length() - 1);
        totalPages = Integer.valueOf(Integer.parseInt(totalPage));
      }
      catch (Exception e2)
      {
        e2.printStackTrace();
        totalPages = Integer.valueOf(1);
      }
      for (int currentPageNum = 1; currentPageNum <= totalPages.intValue(); currentPageNum++)
      {
        String currentPageUrl = productClassifyHref + "/" + currentPageNum + "/";
        Document currentPage = HttpClient.getHtmlDocument(currentPageUrl);
        
        Elements productDetails = null;
        try
        {
          productDetails = currentPage.select("[class='clearfix pos-rel my-checkbox big']");
        }
        catch (Exception e1)
        {
          e1.printStackTrace();
          continue;
        }
        for (int j = 0; j < productDetails.size(); j++)
        {
          Element productDetail = (Element)productDetails.get(j);
          Element product = (Element)productDetail.getElementsByTag("a").get(1);
          String productHref = product.attr("href");
          
          Document productPage = null;
          try
          {
            productPage = HttpClient.getHtmlDocument(productHref);
          }
          catch (Exception e)
          {
            e.printStackTrace();
            continue;
          }
          Elements productPageDetailsf = null;
          try
          {
            productPageDetailsf = productPage.select("[class='fxr font clearfix']");
          }
          catch (Exception e)
          {
            e.printStackTrace();
            continue;
          }
          if ((productPageDetailsf != null) && (productPageDetailsf.size() != 0))
          {
            Element productPageDetails = (Element)productPage.select("[class='fxr font clearfix']").get(0);
            String productName = ((Element)productPageDetails.getElementsByTag("h1").get(0)).text();
            Elements lisProduct = 
              ((Element)productPageDetails.getElementsByTag("ul").get(productPageDetails.getElementsByTag("ul").size() - 1)).getElementsByTag("li");
            





            String price = ((Element)lisProduct.get(0)).text().substring(((Element)lisProduct.get(0)).text().indexOf("：") + 1, 
              ((Element)lisProduct.get(0)).text().length());
            if ((price != null) && (!price.equals("")) && (!price.equals("面议")))
            {
              String pcs = ((Element)lisProduct.get(1)).text().substring(((Element)lisProduct.get(1)).text().indexOf("：") + 1, 
                ((Element)lisProduct.get(1)).text().length());
              
              String supplyOrder = ((Element)lisProduct.get(2)).text().substring(((Element)lisProduct.get(2)).text().indexOf("：") + 1, 
                ((Element)lisProduct.get(2)).text().length());
              
              String deliver = ((Element)lisProduct.get(3)).text().substring(((Element)lisProduct.get(3)).text().indexOf("：") + 1, 
                ((Element)lisProduct.get(3)).text().length());
              
              String locationOfPublisher = ((Element)lisProduct.get(4)).text()
                .substring(((Element)lisProduct.get(4)).text().indexOf("：") + 1, ((Element)lisProduct.get(4)).text().length());
              
              String termOfValidity = ((Element)lisProduct.get(5)).text()
                .substring(((Element)lisProduct.get(5)).text().indexOf("：") + 1, ((Element)lisProduct.get(5)).text().length());
              
              String lastUpdate = ((Element)lisProduct.get(6)).text().substring(((Element)lisProduct.get(6)).text().indexOf("：") + 1, 
                ((Element)lisProduct.get(6)).text().length());
              
              String cityOfPublisher = locationOfPublisher;
              if (locationOfPublisher.indexOf(" ") != -1) {
                cityOfPublisher = locationOfPublisher.substring(0, locationOfPublisher.indexOf(" "));
              }
              Product productPo = new Product();
              productPo.setProductName(productName);
              
              productPo.setCityOfPublisher(cityOfPublisher);
              productPo.setDeliver(deliver);
              productPo.setLastUpdate(lastUpdate);
              productPo.setLocationOfPublisher(locationOfPublisher);
              productPo.setPcs(pcs);
              productPo.setPrice(price);
              productPo.setSupplyOrder(supplyOrder);
              productPo.setTermOfValidity(termOfValidity);
              

              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
              String date = simpleDateFormat.format(new Date());
              

              String savePath = "D:/crawler/中国农产品网/" + date + "/";
              String name = cityOfPublisher + "农产品数据_" + date + ".xlsx";
              
              ExcelUtils.createExcel97(savePath, name);
              
              ExcelUtils.createExcelOfData(productPo, savePath + name);
              
              System.out.println(savePath + name);
            }
          }
        }
        System.out.println("crawer a page: " + currentPageNum + ", productClassifyName:" + productClassifyName);
      }
      System.out.println();
    }
  }
}
