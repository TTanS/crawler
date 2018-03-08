package com.cetc.utils;

import java.io.IOException;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpClient2
{
  public static Document get(String url, Map<String, String> cookies)
  {
    Document doc = null;
    try
    {
      int i = (int)(Math.random() * 10.0D * 1000.0D);
      while (i != 0) {
        i--;
      }
      Connection con = Jsoup.connect(url);
      if (cookies != null) {
        con.cookies(cookies);
      }
      doc = con.data("query", "Java").userAgent("Mozilla").timeout(300000).get();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      try
      {
        doc = Jsoup.connect(url).timeout(5000000).get();
      }
      catch (IOException e1)
      {
        return doc;
      }
    }
    return doc;
  }
  
  public static Document post(String url, Map<String, String> cookies, Map<String, String> paras)
  {
    Document doc = null;
    try
    {
      int i = (int)(Math.random() * 10.0D * 1000.0D);
      while (i != 0) {
        i--;
      }
      Connection con = Jsoup.connect(url);
      if (paras != null) {
        for (Map.Entry<String, String> entry : paras.entrySet()) {
          con.data((String)entry.getKey(), (String)entry.getValue());
        }
      }
      if (cookies != null) {
        con.cookies(cookies);
      }
      doc = con.data("query", "Java").userAgent("Mozilla").timeout(300000).post();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      try
      {
        doc = Jsoup.connect(url).timeout(5000000).get();
      }
      catch (IOException e1)
      {
        return doc;
      }
    }
    return doc;
  }
}
