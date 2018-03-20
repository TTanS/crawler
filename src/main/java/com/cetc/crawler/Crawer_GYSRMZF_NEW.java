package com.cetc.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cetc.pojo.GyBmggList;
import com.cetc.service.GyBmggListService;
import com.cetc.service.impl.GyBmggListServiceImpl;
import com.cetc.utils.ExcelUtils;
import com.cetc.utils.FileUtils;
import com.cetc.utils.HttpClient;
import com.cetc.utils.HttpClient2;
import com.cetc.utils.TimeUtils;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.DataFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/***
 * 贵阳市人民政府部门公告
 * 
 * @author TS
 * 
 */

public class Crawer_GYSRMZF_NEW {
	public static void main(String[] args) throws IOException, ParseException {
		String host = "http://www.gygov.gov.cn/";
		// String homePageHrl = "http://www.gygov.gov.cn/c80/index.html";
		// Document homePage = HttpClient.getHtmlDocument(homePageHrl);
		// // 获取主题table
		// Element topicListTable = homePage.select("[style='padding:0px 15px
		// 15px 15px;']").get(0);
		// // Elements topicListTd = topicListTable.getElementsByTag("td");
		// Elements topicAs = topicListTable.getElementsByTag("a");
		// for (int i = 0; i < topicAs.size(); i++) {
		// Element topicA = topicAs.get(i);
		// String topicHref = topicA.attr("href");
		// // topicName = topicA
		//
		// System.out.println();
		//
		//
		// }
		// String homeUrl = "http://www.gygov.gov.cn/c79/index.html";
		// Document homePage = HttpClient.getHtmlDocument(homeUrl);
		GyBmggListService bmggListService = new GyBmggListServiceImpl();
		
		
		for (int currentPage = 1; currentPage <= 213; currentPage++) {
			String dataListUrl = "http://www.gygov.gov.cn/gygov//openapi/info/ajaxpagelist.do?pagesize=22&channelid=79&pageno="
					+ currentPage;
			Document homePage = HttpClient.getHtmlDocument(dataListUrl);
			String dataList = homePage.body().text();
			JSONObject jsDataList = JSONObject.parseObject(dataList);
			JSONArray dataArray = jsDataList.getJSONArray("infolist");
			for (int i = 0; i < dataArray.size(); i++) {
				JSONObject data = dataArray.getJSONObject(i);
				String keywords = data.getString("data");
				String source = data.getString("source");
				String title = data.getString("title");
				String summary = data.getString("summary");
				String author = data.getString("author");
				String url = data.getString("url");
				String username = data.getString("username");
				String daytime = data.getString("daytime");
				GyBmggList bmggList = new GyBmggList();
				bmggList.setAuthor(author);
				// 格式化日期
				SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
				if(daytime != null || !(daytime.equals(""))){
					bmggList.setDaytime(dataFormat.parse(daytime));
				}
				bmggList.setKeywords(keywords);
				bmggList.setSource(source);
				bmggList.setSummary(summary);
				bmggList.setTitle(title);
				bmggList.setUrl(url);
				bmggList.setUsername(username);
				// 将目录插入数据库
				bmggListService.insertSelective(bmggList);
				// 进如文件详情页
				
				
				
				
				
				
				
				System.out.println();
				
				
			}
			
			
			System.out.println();
			
			
			
			
			
			

		}

	}
}
