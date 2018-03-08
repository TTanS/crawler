package com.cetc.crawler;

import com.cetc.pojo.HCProduct;
import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import com.cetc.utils.TimeUtils;
import java.io.PrintStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawer_HCW
{
  public static void main(String[] args)
    throws Exception
  {
    String homeUrl = "https://s.hc360.com/?w=%C5%A9%B2%FA%C6%B7%C5%FA%B7%A2&mc=seller&ap=B&pab=B&t=1&ee=1";
    Document docHomePage = HttpClient.getHtmlDocument(homeUrl);
    
    String date = TimeUtils.getTime();
    String savePath = "D:/crawler/慧聪网/" + date + "/";
    String name = "农产品批发.xlsx";
    String[] paras = new String[5];
    paras[0] = "商品名";
    paras[1] = "价格";
    paras[2] = "所属公司";
    paras[3] = "农产品地址";
    paras[4] = "获取数据日期";
    ExcelUtils.createExcel(savePath, name, paras);
    

    String totalPage = ((Element)docHomePage.select(".total").get(0)).text();
    totalPage = totalPage.substring(1, totalPage.length() - 1);
    int total = Integer.parseInt(totalPage);
    for (int currentPage = 1; currentPage <= total; currentPage++)
    {
      String productPageUrl = "https://s.hc360.com/?w=%C5%A9%B2%FA%C6%B7%C5%FA%B7%A2&mc=seller&ap=B&pab=B&t=1&ee=" + 
        currentPage;
      Document productPage = HttpClient.getHtmlDocument(productPageUrl);
      
      Element productListDiv = (Element)productPage.select("[class='wrap-grid']").get(0);
      Element productListUl1 = (Element)productListDiv.getElementsByTag("ul").get(0);
      Elements productListLis1 = productListUl1.getElementsByTag("li");
      for (int i = 0; i < productListLis1.size() - 1; i++)
      {
        Element productListLi = (Element)productListLis1.get(i);
        
        System.out.println("第" + (i + 1) + "条");
        
        String productPrice = "0";
        try
        {
          productPrice = ((Element)productListLi.select("[class='seaNewPrice']").get(0)).text();
        }
        catch (Exception e)
        {
          e.printStackTrace();
          continue;
        }
        productPrice = productPrice.substring(1, productPrice.length());
        
        String productName = ((Element)productListLi.select("[class='newName']").get(0)).child(0).attr("title");
        
        String productCompany = ((Element)productListLi.select("[class='newCname']").get(0)).child(0).child(0)
          .attr("title");
        
        String productLoacation = "";
        try
        {
          productLoacation = ((Element)productListLi.select("[class='areaBox']").get(0)).attr("title");
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        HCProduct hcProduct = new HCProduct();
        hcProduct.setDate(date);
        hcProduct.setProductCompany(productCompany);
        hcProduct.setProductLoacation(productLoacation);
        hcProduct.setProductName(productName);
        hcProduct.setProductPrice(productPrice);
        ExcelUtils.createExcelOfData(hcProduct, savePath + name);
      }
      Element productListUl2 = null;
      try
      {
        productListUl2 = (Element)productListDiv.getElementsByTag("ul").get(1);
      }
      catch (Exception e1)
      {
        e1.printStackTrace();
        continue;
      }
      String productListPage2Url = "https:" + productListUl2.attr("data-url");
      Document productListPage2 = HttpClient.getHtmlDocument(productListPage2Url);
      Elements productListList2 = productListPage2.getElementsByTag("li");
      for (int i = 0; i < productListList2.size(); i++)
      {
        Element productListLi = (Element)productListLis1.get(i);
        
        System.out.println("第" + (i + 1) + "条");
        
        String productPrice = "0";
        try
        {
          productPrice = ((Element)productListLi.select("[class='seaNewPrice']").get(0)).text();
        }
        catch (Exception e)
        {
          e.printStackTrace();
          continue;
        }
        productPrice = productPrice.substring(1, productPrice.length());
        
        String productName = ((Element)productListLi.select("[class='newName']").get(0)).child(0).attr("title");
        
        String productCompany = ((Element)productListLi.select("[class='newCname']").get(0)).child(0).child(0)
          .attr("title");
        
        String productLoacation = "";
        try
        {
          productLoacation = ((Element)productListLi.select("[class='areaBox']").get(0)).attr("title");
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        HCProduct hcProduct = new HCProduct();
        hcProduct.setDate(date);
        hcProduct.setProductCompany(productCompany);
        hcProduct.setProductLoacation(productLoacation);
        hcProduct.setProductName(productName);
        hcProduct.setProductPrice(productPrice);
        ExcelUtils.createExcelOfData(hcProduct, savePath + name);
      }
      Element productListUl3 = null;
      try
      {
        productListUl3 = (Element)productListPage2.getElementsByTag("ul").get(0);
      }
      catch (Exception e1)
      {
        e1.printStackTrace();
        continue;
      }
      String productListPage3Url = "https:" + productListUl3.attr("data-url");
      Document productListPage3 = HttpClient.getHtmlDocument(productListPage3Url);
      Elements productListList3 = productListPage3.getElementsByTag("li");
      for (int i = 0; i < productListList3.size(); i++)
      {
        Element productListLi = (Element)productListLis1.get(i);
        
        System.out.println("第" + (i + 1) + "条");
        
        String productPrice = "0";
        try
        {
          productPrice = ((Element)productListLi.select("[class='seaNewPrice']").get(0)).text();
        }
        catch (Exception e)
        {
          e.printStackTrace();
          continue;
        }
        productPrice = productPrice.substring(1, productPrice.length());
        
        String productName = ((Element)productListLi.select("[class='newName']").get(0)).child(0).attr("title");
        
        String productCompany = ((Element)productListLi.select("[class='newCname']").get(0)).child(0).child(0)
          .attr("title");
        
        String productLoacation = "";
        try
        {
          productLoacation = ((Element)productListLi.select("[class='areaBox']").get(0)).attr("title");
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        HCProduct hcProduct = new HCProduct();
        hcProduct.setDate(date);
        hcProduct.setProductCompany(productCompany);
        hcProduct.setProductLoacation(productLoacation);
        hcProduct.setProductName(productName);
        hcProduct.setProductPrice(productPrice);
        ExcelUtils.createExcelOfData(hcProduct, savePath + name);
      }
      Elements productListUl4 = productListPage3.getElementsByTag("ul");
      if (productListUl4.size() != 0) {
        System.out.println("还有一张表");
      }
      System.out.println("crawer a page , pageNum = " + currentPage + ", nextPage = " + (currentPage + 1));
    }
  }
}
