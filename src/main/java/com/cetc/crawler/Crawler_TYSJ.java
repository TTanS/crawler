package com.cetc.crawler;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.cetc.utils.HttpClient;

/**
 * 天元数据
 * 
 * @author TS https://www.tdata.cn/govbigdata/list/index.html
 */
public class Crawler_TYSJ {
	public static void main(String[] args) throws InterruptedException {
		String host = "https://www.tdata.cn/";
		String homeUrl = "https://www.tdata.cn/govbigdata/list/index.html";
		System.setProperty("webdriver.chrome.driver", "D:\\ts\\webDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		List<Cookie> cookies = new ArrayList<Cookie>();
		driver.get(homeUrl);
		cookies.add(new Cookie("Hm_lpvt_74a98112125a681eb0b14f9006659074", "1520834326"));
		cookies.add(new Cookie("Hm_lvt_74a98112125a681eb0b14f9006659074", "1520827204"));
		cookies.add(new Cookie("PHPSESSID", "ST-340-nHrtXmgPy0rFiLcr5dcA-cas01exampleorg"));
		// 添加cookie
		driver.manage().addCookie(cookies.get(0));
		driver.manage().addCookie(cookies.get(1));
		driver.manage().addCookie(cookies.get(1));
		WebElement area = driver.findElement(By.id("area"));
		List<WebElement> areaAs = area.findElements(By.tagName("a"));
		// 按地区分类
		for (int i = 1; i < areaAs.size(); i++) {
			WebElement areaA = areaAs.get(i);
			String areaName = areaA.getText();
			areaA.click();
			// 分页
			for (int currentPage = 1; true; currentPage++) {
				// 睡眠0.5s
				Thread.sleep(500);
				WebElement nextPage = null;
				try {
					nextPage = driver.findElement(By.className("layui-laypage-next"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 判断是否最后一页
				if (nextPage == null && currentPage != 1) {
					break;
				}
				// 如果不是第一页，就点击下一页
				if (currentPage != 1) {
					nextPage.click();
				}
				WebElement dataListDiv = driver.findElement(By.className("searchcontent"));
				// 定位数据详情的btn btn-primary
				List<WebElement> datalinkList = driver.findElements(By.cssSelector("a.btn.btn-primary"));
				for (int j = 0; j < datalinkList.size(); j++) {
					WebElement datalink = datalinkList.get(j);
					String href = datalink.getAttribute("href");
					// 通过jsoup来解析
//					String dataDetailPageUrl = host + href;
					Document dataDetailPage = HttpClient.getHtmlDocument(href);
					Elements downloadTables = dataDetailPage.select("[class='table table-bordered']");
					Element downloadTable = downloadTables.get(downloadTables.size()-1);
					Element downloadTr = downloadTable.child(0).child(0);
					String format = downloadTr.child(0).text();
					format = format.substring(0, format.indexOf("格"));
					
					
					
					
					
					
					
					
				}
				
				
				
				
				
				
				
				
				
				Thread.sleep(2 * 1000);
			}

		}

		driver.close();

	}
}
