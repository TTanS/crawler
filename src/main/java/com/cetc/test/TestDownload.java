package com.cetc.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;

import com.cetc.utils.HttpClient;
import com.cetc.utils.HttpClient2;

public class TestDownload {
	public static void main(String[] args) throws IOException {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("Hm_lpvt_74a98112125a681eb0b14f9006659074", "1520927565"));
		cookies.add(new Cookie("Hm_lvt_74a98112125a681eb0b14f9006659074", "1520827204,1520906318"));
		cookies.add(new Cookie("JSESSIONID", "D4B2CE1DB38FA34B70BFA9530DCB3E2A"));
		cookies.add(new Cookie("referee", ""));
		String url = "https://www.tdata.cn//govbigdata/content/downloadfile/id/278/url/Ly9kZnMxLnRkYXRhLmNuL2dyb3VwMS9NMDAvMDAvMUMvckJZRVJGcFlZV3VBVUlFSEFBQVlWZnpDSlJNNTk4LnppcA%3D%3D.html";
		String fileName = "简易车管所社区服务站信息_xls.zip";
		String savePath = "D:/crawler/天元数据/20180313/济南市/";
		
//		HttpClient2.download(url, fileName, savePath, cookies);
		Document t = HttpClient2.get(url, cookies);
		System.out.println();
		
		
		
		
	}
}	
