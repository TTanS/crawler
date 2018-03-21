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
 * 贵阳市人民政府区市县公告
 * 
 * @author TS
 * 
 */

public class Crawer_GYSRMZF_NEW2 {
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

		for (int currentPage = 1; currentPage <= 10; currentPage++) {
			String dataListUrl = "http://www.gygov.gov.cn/gygov//openapi/info/ajaxpagelist.do?pagesize=22&channelid=80&pageno="
					+ currentPage;
			Document homePage = HttpClient.getHtmlDocument(dataListUrl);
			String dataList = homePage.body().text();
			JSONObject jsDataList = null;
			try {
				jsDataList = JSONObject.parseObject(dataList);
			} catch (Exception e1) {
				e1.printStackTrace();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			JSONArray dataArray = jsDataList.getJSONArray("infolist");
			for (int i = 0; i < dataArray.size(); i++) {
				JSONObject data = dataArray.getJSONObject(i);
				String keywords = data.getString("keywords");
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
				if (daytime != null || !(daytime.equals(""))) {
					bmggList.setDaytime(dataFormat.parse(daytime));
				}
				bmggList.setKeywords(keywords);
				bmggList.setSource(source);
				bmggList.setSummary(summary);
				bmggList.setTitle(title);
				bmggList.setUrl(url);
				bmggList.setUsername(username);
				// 将目录插入数据库
//				bmggListService.insertSelective(bmggList);
				// 进如文件详情页
				Document dataPage = HttpClient.getHtmlDocument(url);
				Element dataTable = null;
				try {
					dataTable = dataPage.body().child(1).child(0).child(0).child(2).child(1);
				} catch (Exception e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
					continue;
				}
				Element dataTbody = dataTable.child(0).child(0).child(0).child(1).child(0);
				Element dataDiv = dataPage.getElementById("zoom");
				Elements aDownloadFile = dataDiv.getElementsByTag("a");
				if ((aDownloadFile.size() != 0) && (((Element) aDownloadFile.get(0)).text() != null)
						&& (!((Element) aDownloadFile.get(0)).text().equals(""))) {
					for (int j = 0; j < aDownloadFile.size(); j++) {
						String downloadHref = ((Element) aDownloadFile.get(j)).attr("href");
						if (downloadHref.length() >= 27) {
							String downloadSavePath = "D:/crawler/贵阳市人民政府/" + TimeUtils.getTime() + "/区市县公告/" + title
									+ ((Element) aDownloadFile.get(j)).attr("title") + "/";
							String downloadUrl = "";
							if (downloadHref.substring(0, 4).equals("http")) {
								downloadUrl = downloadHref;
							} else {
								downloadUrl = host + downloadHref;
							}
							String downloadFormat = downloadHref.substring(downloadHref.lastIndexOf("."),
									downloadHref.length());
							String downloadName = "";
							if ((((Element) aDownloadFile.get(j)).text() == null)
									|| (((Element) aDownloadFile.get(j)).text().equals(""))) {
								downloadName = j + downloadFormat;
							} else {
								downloadName = ((Element) aDownloadFile.get(j)).text() + downloadFormat;
							}
							// 下载附件
							try {
								HttpClient.download(downloadUrl, downloadName, downloadSavePath);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								break;
							}
							System.out.println("download a file : " + downloadName);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					// 下载图片
					Elements img = dataDiv.getElementsByTag("img");
					if (img.size() != 0) {
						String downloadSavePath = "D:/crawler/贵阳市人民政府/" + TimeUtils.getTime() + "/区市县公告/" + title + "/";
						for (int j = 0; j < img.size(); j++) {
							String downloadImgSrc = ((Element) img.get(j)).attr("src");
							String downloadImgUrl = host + downloadImgSrc;
							String downloadImgFormat = downloadImgSrc
									.substring(downloadImgSrc.lastIndexOf(".", downloadImgSrc.length()));
							String downloadImgName = title + j + downloadImgFormat;
							try {
								HttpClient.download(downloadImgUrl, downloadImgName, downloadSavePath);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								break;
							}
							System.out.println("download a file : " + downloadImgName + " ,currentPage:" + currentPage);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e5) {
								e5.printStackTrace();
							}
						}
					}
				}
				String saveName = title + ".doc";
				String savePath = "D:/crawler/贵阳市人民政府/" + TimeUtils.getTime() + "/区市县公告/";
				String e2 = "<table></table>";
				try {
					ExcelUtils.writeWordFile(savePath, saveName, e2 + dataDiv.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				System.out.println("download a file : " + saveName + " ,currentPage:" + currentPage);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e5) {
					e5.printStackTrace();
				}
			}
		}

	}
}
