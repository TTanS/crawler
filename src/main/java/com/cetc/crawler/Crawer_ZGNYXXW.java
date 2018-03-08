package com.cetc.crawler;

import com.cetc.utils.HttpClient;
import java.io.PrintStream;
import org.jsoup.nodes.Document;

public class Crawer_ZGNYXXW
{
  public static void main(String[] args)
  {
    String url = "http://jgsb.agri.cn/controller?marketUuid=76E4F160C02A936CE040A8C020017257&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&newsearch=true&login_result_sign=login&LTPAToken=dGFueGluZ3Nvbmc=";
    Document msg = HttpClient.getHtmlDocument(url);
    System.out.println(msg);
  }
}
