package com.cetc.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;

public class HttpClient
{
  public static String get(String pageUrl, String charSet)
  {
    try
    {
      URL url = new URL(pageUrl);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Aceept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
      connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
      connection.setRequestProperty("Accept-Language", 
        "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
      connection.setRequestProperty("Connection", "keep-alive");
      connection.setRequestProperty("User-Agent", 
        "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");
      connection.setRequestProperty("cookie", "value");
      connection.setRequestProperty("cookie", "LjEcq-DzgeBP_e7XQewEirgxPqvzV3wHKeZUQWQ3nzs0076v2GWn!922934045");
      connection.setRequestProperty("cookie", 
        "ASP.NET_SessionId=bssjhioninb1mqlhy0p4x3zs; Hm_lvt_bb129473d3fd4d089096111b3bb925c5=c6569c60-893e-4017-bbc6-47ebbfe3a3b6; safedog-flow-item=; amvid=945a9ff61db638cf2ed6b674a878e298; Hm_lvt_e2be2e3dc83f01ec9cc65c3aaaa72b81=1516783315; Hm_lpvt_e2be2e3dc83f01ec9cc65c3aaaa72b81=1516783873");
      connection.setRequestProperty("cookie", 
        "finndy__refer=http%3A%2F%2Fwww.finndy.com%2Findex.php; _pk_id.1.1450=125ec269631b1900.1519611565.3.1519624113.1519624005.; Hm_lvt_fafaa7b25f2d04f0c302463ddf631057=1519611565; Hm_lpvt_fafaa7b25f2d04f0c302463ddf631057=1519617224; finndy.com.appdownload=finndy.com; finndy_seccode=d236dE6IgCbn8Ym771UrDi2PXs34dxjtAuV6zia7zDS%2F; PHPSESSID=o9m4l1mckek75o28q7vf1r3tf3; finndy_loginuser=QQ_2OVIJ6; finndy_verifyid=3ddcb9566e1290678efe043bff876690; finndy_auth=15a7%2Fc6aecAZLuENBCmwzixEGTjRsU0yTNg4Tw1av%2FG%2FN1St6%2F%2B0eLzswj7OL2M7cEy8GXivoNHVStTOtVYvq0pyEJA; _pk_ses.1.1450=*");
      
      connection.setConnectTimeout(10000);
      connection.setReadTimeout(10000);
      connection.connect();
      if (connection.getResponseCode() == 404) {
        return null;
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charSet));
      String line = null;
      StringBuilder sb = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      connection.disconnect();
      return sb.toString();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static String post(String pageUrl, Map<String, String> params, String charSet)
  {
    try
    {
      URL url = new URL(pageUrl);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("POST");
      
      conn.setDoOutput(true);
      
      conn.setUseCaches(false);
      conn.setConnectTimeout(60000);
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
      conn.connect();
      if ((params != null) && (params.keySet().size() > 0))
      {
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
          sb.append(key + "=" + URLEncoder.encode((String)params.get(key), charSet) + "&");
        }
        String param = sb.delete(sb.length() - 1, sb.length()).toString();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(param);
        out.close();
      }
      if (conn.getResponseCode() == 404) {
        return null;
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charSet));
      String line = null;
      StringBuilder sb = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      conn.disconnect();
      return sb.toString();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static boolean download(String urlStr, String fileName, String savePath)
    throws IOException
  {
    URL url = null;
    try
    {
      url = new URL(urlStr);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if ((url == null) || (url.equals(""))) {
      return false;
    }
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    
    conn.setConnectTimeout(10000);
    
    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
    
    InputStream inputStream = null;
    try
    {
      inputStream = conn.getInputStream();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if ((inputStream == null) || (inputStream.equals(""))) {
      return false;
    }
    byte[] getData = readInputStream(inputStream);
    

    File saveDir = new File(savePath);
    if (!saveDir.exists()) {
      saveDir.mkdir();
    }
    File file = new File(saveDir + File.separator + fileName);
    if (!file.getParentFile().exists()) {
      if (!file.getParentFile().mkdirs()) {
        return false;
      }
    }
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(getData);
    if (fos != null) {
      fos.close();
    }
    if (inputStream != null) {
      inputStream.close();
    }
    return true;
  }
  public static boolean download(String urlStr, String fileName, String savePath ,List<Cookie> cookies)
		    throws IOException
		  {
		    URL url = null;
		    try
		    {
		      url = new URL(urlStr);
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		    if ((url == null) || (url.equals(""))) {
		      return false;
		    }
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    
		    conn.setConnectTimeout(10000);
		    
		    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		    // 添加cookie
		    
		    String cookie = "";
		    if(cookies != null ){
		    	for (int i = 0; i < cookies.size(); i++) {
		    		cookie = cookie + (cookies.get(i).getName() + "=" + cookies.get(i).getValue() + ";") ;
		    	}
		    }
		    conn.setRequestProperty("Cookie",cookie);
		    
		    InputStream inputStream = null;
		    try
		    {
		      inputStream = conn.getInputStream();
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		    if ((inputStream == null) || (inputStream.equals(""))) {
		      return false;
		    }
		    byte[] getData = readInputStream(inputStream);
		    

		    File saveDir = new File(savePath);
		    if (!saveDir.exists()) {
		      saveDir.mkdir();
		    }
		    File file = new File(saveDir + File.separator + fileName);
		    if (!file.getParentFile().exists()) {
		      if (!file.getParentFile().mkdirs()) {
		        return false;
		      }
		    }
		    FileOutputStream fos = new FileOutputStream(file);
		    fos.write(getData);
		    if (fos != null) {
		      fos.close();
		    }
		    if (inputStream != null) {
		      inputStream.close();
		    }
		    return true;
		  }
  
  
  public static byte[] readInputStream(InputStream inputStream)
    throws IOException
  {
    byte[] buffer = new byte[1024];
    int len = 0;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((len = inputStream.read(buffer)) != -1) {
      bos.write(buffer, 0, len);
    }
    bos.close();
    return bos.toByteArray();
  }
  
  public static Document getHtmlDocument(String url)
  {
    Document doc = null;
    try
    {
      int i = (int)(Math.random() * 10.0D * 1000.0D);
      while (i != 0) {
        i--;
      }
      Map<String, String> cookies = new HashMap<String, String>();
      
      cookies.put("auth", "token");
      cookies.put("finndy_auth", 
        "cb36GYD6gWXnbJczUPToimfOybfcxwpp0u9IavvODeqOUfhwvFbCYtXococGY4wyCTtTOk3Qdmmu2GvmEGtlmsCYJvw");
      cookies.put("finndy_loginuser", "QQ_2OVIJ6");
      cookies.put("finndy_verifyid", "3ddcb9566e1290678efe043bff876690");
      
      doc = Jsoup.connect(url).data("query", "Java").userAgent("Mozilla").cookies(cookies).timeout(300000).get();
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
  
  public static Document postHtmlDocument(String url, Map<String, String> paras)
  {
    Document doc = null;
    try
    {
      int i = (int)(Math.random() * 10.0D * 1000.0D);
      while (i != 0) {
        i--;
      }
      Map<String, String> cookies = new HashMap<String, String>();
      
      cookies.put("auth", "token");
      cookies.put("_pk_id.1.1450", "d44e0f6a3e976c2f.1519611514.4.1519623625.1519623538.");
      cookies.put("_pk_ref.1.1450", "%5B%22%22%2C%22%22%2C1519623538%2C%22https%3A%2F%2Fmail.qq.com%2F%22%5D");
      cookies.put("_pk_ses.1.1450", "*");
      cookies.put("finndy_refer", "http%3A%2F%2Fwww.finndy.com%2Frequire.php");
      cookies.put("finndy_auth", 
        "cb36GYD6gWXnbJczUPToimfOybfcxwpp0u9IavvODeqOUfhwvFbCYtXococGY4wyCTtTOk3Qdmmu2GvmEGtlmsCYJvw");
      cookies.put("finndy_loginuser", "QQ_2OVIJ6");
      cookies.put("finndy_seccode", "c173DDz80MwhS0IvVsLh1eI%2Fs%2F2i6T2m%2FB76Z5nrjF4E");
      cookies.put("finndy_verifyid", "3ddcb9566e1290678efe043bff876690");
      cookies.put("finndy.com.appdownload", "finndy.com");
      cookies.put("Hm_lpvt_fafaa7b25f2d04f0c302463ddf631057", "1519623570");
      cookies.put("Hm_lvt_fafaa7b25f2d04f0c302463ddf631057", "1519611513,1519614704");
      cookies.put("PHPSESSID", "m1hadhjsvslu7i7v4bdb9fned6");
      
      Connection con = Jsoup.connect(url);
      if (paras != null) {
        for (int j = 0; j < paras.size(); j++) {
          con.data(paras);
        }
      }
      con.cookies(cookies).userAgent("Mozilla").timeout(300000);
      doc = con.post();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      try
      {
        doc = Jsoup.connect(url).timeout(5000000).post();
      }
      catch (IOException e1)
      {
        return doc;
      }
    }
    return doc;
  }
}
