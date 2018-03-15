package com.cetc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;

public class HttpClient2 {
	public static Document get(String url, Map<String, String> cookies) {
		Document doc = null;
		try {
			int i = (int) (Math.random() * 10.0D * 1000.0D);
			while (i != 0) {
				i--;
			}
			Connection con = Jsoup.connect(url);
			if (cookies != null) {
				con.cookies(cookies);
			}
			doc = con.data("query", "Java").userAgent("Mozilla").timeout(300000).get();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				doc = Jsoup.connect(url).timeout(5000000).get();
			} catch (IOException e1) {
				return doc;
			}
		}
		return doc;
	}
	
	public static Document get(String url, List<Cookie> cookies) {
		Map<String, String> cookie = listToMap(cookies);
		Document doc = null;
		try {
			int i = (int) (Math.random() * 10.0D * 1000.0D);
			while (i != 0) {
				i--;
			}
			Connection con = Jsoup.connect(url);
			if (cookies != null) {
				con.cookies(cookie);
			}
			doc = con.data("query", "Java").userAgent("Mozilla").timeout(300000).get();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				doc = Jsoup.connect(url).timeout(5000000).get();
			} catch (IOException e1) {
				return doc;
			}
		}
		return doc;
	}

	public static Boolean download(String url, String fileName, String savePath, List<Cookie> cookies)
			throws IOException {

		Map<String, String> cookie = new HashMap<>();
		if (cookies != null) {
			for (int i = 0; i < cookies.size(); i++) {
				cookie.put(cookies.get(i).getName(), cookies.get(i).getValue());
			}
		}

		Response response = Jsoup.connect(url).data("query", "Java").userAgent("Mozilla").timeout(300000)
				.cookies(cookie).ignoreContentType(true).execute();
		
		
		
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
		
		
		
		// output here
		FileOutputStream out = (new FileOutputStream(file));
		out.write(response.bodyAsBytes());
		// resultImageResponse.body() is where the image's contents are.
		out.close();

		return true;
	}

	public static Document post(String url, Map<String, String> cookies, Map<String, String> paras) {
		Document doc = null;
		try {
			int i = (int) (Math.random() * 10.0D * 1000.0D);
			while (i != 0) {
				i--;
			}
			Connection con = Jsoup.connect(url);
			if (paras != null) {
				for (Map.Entry<String, String> entry : paras.entrySet()) {
					con.data((String) entry.getKey(), (String) entry.getValue());
				}
			}
			if (cookies != null) {
				con.cookies(cookies);
			}
			doc = con.data("query", "Java").userAgent("Mozilla").timeout(300000).post();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				doc = Jsoup.connect(url).timeout(5000000).get();
			} catch (IOException e1) {
				return doc;
			}
		}
		return doc;
	}
	
	
	
	public static Map<String, String> listToMap(List<Cookie> cookies){
		
		Map<String, String> cookie = new HashMap<>();
		if (cookies != null) {
			for (int i = 0; i < cookies.size(); i++) {
				cookie.put(cookies.get(i).getName(), cookies.get(i).getValue());
			}
		}else{
			return null;
		}
		return cookie;
	}
}
