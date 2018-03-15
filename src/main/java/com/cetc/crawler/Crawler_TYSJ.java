package com.cetc.crawler;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.cetc.utils.ExcelUtils;
import com.cetc.utils.HttpClient;
import com.cetc.utils.TimeUtils;

/**
 * 天元数据
 * 
 * @author TS https://www.tdata.cn/govbigdata/list/index.html
 */
public class Crawler_TYSJ {
	public static void main(String[] args) throws Exception {
		String host = "https://www.tdata.cn/";
		String homeUrl = "https://www.tdata.cn/govbigdata/list/index.html";
		// 创建一个目录index.xlsx
		String savePath = "D:\\crawler\\天元数据\\" + TimeUtils.getTime() + "\\";
		String name = "index.xlsx";
		String[] paras = new String[8];
		paras[0] = "数据名称";
		paras[1] = "开放状态";
		paras[2] = "所属主题";
		paras[3] = "最后更新";
		paras[4] = "地区";
		paras[5] = "来源部门";
		paras[6] = "标签";
		paras[7] = "发布时间";

		ExcelUtils.createExcel(savePath, name, paras);
		// 分析
		System.setProperty("webdriver.chrome.driver", "D:\\ts\\webDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		String loginUrl = "https://www.tdata.cn//govbigdata/content/downloadfile/id/278/url/Ly9kZnMxLnRkYXRhLmNuL2dyb3VwMS9NMDAvMDAvMUMvckJZRVJGcFlZV3VBVUlFSEFBQVlWZnpDSlJNNTk4LnppcA%3D%3D.html";
		// 登陆
		driver.get(loginUrl);
		driver.findElement(By.id("username")).sendKeys("18723145300");
		driver.findElement(By.id("password")).sendKeys("195129");
		WebElement login = driver.findElement(By.id("login-btn"));
		login.click();

		// List<Cookie> cookies = new ArrayList<Cookie>();
		// cookies.add(new Cookie("Hm_lpvt_74a98112125a681eb0b14f9006659074",
		// "1520913500"));
		// cookies.add(new Cookie("Hm_lvt_74a98112125a681eb0b14f9006659074",
		// "1520827204,1520906318"));
		// cookies.add(new Cookie("JSESSIONID",
		// "DFAC7149ED4DA5B98FECBE16BC8DDFB6"));
		// cookies.add(new Cookie("referee", ""));
		// cookies.add(new Cookie("CASPRIVACY", ""));
		// cookies.add(new Cookie("CASTGC",
		// "TGT-248-kcKM9r3i7o6wenqrGU2LEFX7fF0J5TdJh2fDIaBJO1f9uhzOi7-cas01.example.org"));
		// cookies.add(new Cookie("PHPSESSID",
		// "ST-488-ODfCa3NAAcpGpUe4WEMX-cas01exampleorg"));
		// cookies.add(new Cookie("referee", ""));

		// 添加cookie
		// driver.manage().addCookie(cookies.get(0));
		// driver.manage().addCookie(cookies.get(1));
		// driver.manage().addCookie(cookies.get(1));
		// 进入下载
		driver.get(homeUrl);
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
				// String [] s = new String [datalinkList.size()];
				// for (int j2 = 0; j2 < datalinkList.size(); j2++) {
				// s[j2] = datalinkList.get(j2).getAttribute("href");
				// }
				int q = datalinkList.size();
				for (int j = 0; j < datalinkList.size(); j++) {
					WebElement datalink = datalinkList.get(j);
					System.out.println(datalink.getTagName());
					System.out.println(datalink.getText());
					System.out.println(datalink.toString());

					String href = datalink.getAttribute("href");
					// 通过jsoup来解析
					// String dataDetailPageUrl = host + href;
					Document dataDetailPage = HttpClient.getHtmlDocument(href);
					Elements downloadTables = dataDetailPage.select("[class='table table-bordered']");
					Element downloadTable = downloadTables.get(downloadTables.size() - 1);
					Element downloadTr = downloadTable.child(0).child(0);
					// String format = downloadTr.child(0).text();
					// format = format.substring(0, format.indexOf("格"));
					String downloadHref = downloadTr.child(1).child(0).attr("href");
					String downloadUrl = host + downloadHref;
					String downloadName = downloadTr.child(1).text();

					// 新建一个数组来存储
					String[] dataParas = new String[8];
					// 抓取数据信息
					Element dataMsgTable = downloadTables.get(0);
					// 数据名称
					String dataName = dataMsgTable.child(0).child(0).child(1).text();
					// 数据状态
					String dataStatus = dataMsgTable.child(0).child(0).child(3).text();
					// 数据主题
					String dataTheme = dataMsgTable.child(0).child(1).child(1).text();
					// 数据最后更新时间
					String dataLastUpdata = dataMsgTable.child(0).child(1).child(3).text();
					// 数据所属地区
					String dataArea = dataMsgTable.child(0).child(2).child(1).text();
					// 数据来源部门
					String DataSourceSector = dataMsgTable.child(0).child(2).child(3).text();
					// 数据标签
					String dataTag = dataMsgTable.child(0).child(3).child(1).text();
					// 数据发布时间
					String dataPublicTime = dataMsgTable.child(0).child(4).child(1).text();
					dataParas[0] = dataName;
					dataParas[1] = dataStatus;
					dataParas[2] = dataTheme;
					dataParas[3] = dataLastUpdata;
					dataParas[4] = dataArea;
					dataParas[5] = DataSourceSector;
					dataParas[6] = dataTag;
					dataParas[7] = dataPublicTime;
					// 存入数据
					ExcelUtils.createExcelOfData(dataParas, savePath + name);
					// // 下载数据
					// String downloadPath = savePath + dataArea + "/";
					// HttpClient.download(downloadUrl, downloadName,
					// downloadPath, cookies);
					// 下载文件
					String dataSavePath = savePath + dataName + "\\";
					
					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("profile.default_content_settings.popups", 0);
					chromePrefs.put("download.default_directory", dataSavePath);
					ChromeOptions options = new ChromeOptions();
					HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
					options.setExperimentalOption("prefs", chromePrefs);
					options.addArguments("--test-type");
					DesiredCapabilities cap = DesiredCapabilities.chrome();
					cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
					cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					cap.setCapability(ChromeOptions.CAPABILITY, options);
					

					driver.get(downloadUrl);
					Thread.sleep(1 * 1000);
					System.out.println();
				}
			}

		}

		driver.close();

	}
}
