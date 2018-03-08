package com.cetc.crawler;

import com.cetc.utils.HttpClient;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class Crawer_FYD {
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		System.setProperty("webdriver.chrome.driver", "D:\\ts\\webDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		Actions actions = new Actions(driver);

		String homeUrl = "http://www.finndy.com/midycp.php?action=robots&ccatid=0&viewfree=on";
		int currentPage = 0;

		Document homePage = HttpClient.getHtmlDocument(homeUrl);
		Elements nextPage = homePage.select("[class='next']");
		// 判断下一页
		currentPage = 0;
		while (nextPage.size() != 0) {
			currentPage++;
			String numDataPageUrl = homeUrl + "&page=" + currentPage;
			Document numDataPage = HttpClient.getHtmlDocument(numDataPageUrl);
			Element dataList = (Element) numDataPage.getElementsByTag("tbody").get(0);
			Elements dataListTr = dataList.children();
			for (int i = 1; i < dataListTr.size(); i++) {
				Element isBuyTd = ((Element) dataListTr.get(i)).child(6);
				// 如果已经购买就跳过
				if (isBuyTd.text().equals("已购")) {
					continue;
				}
				// 获取购买链接
				Element buyTd = ((Element) dataListTr.get(i)).child(7);
				String onclick = buyTd.child(0).attr("onclick");
				onclick = onclick.substring(onclick.indexOf("'") + 1, onclick.lastIndexOf("'"));

				// 购买
				String domain = "http://www.finndy.com/";

				List<Cookie> cookies = new ArrayList<Cookie>();

				cookies.add(new Cookie("finndy_auth",
						"cb36GYD6gWXnbJczUPToimfOybfcxwpp0u9IavvODeqOUfhwvFbCYtXococGY4wyCTtTOk3Qdmmu2GvmEGtlmsCYJvw"));
				cookies.add(new Cookie("finndy_loginuser", "QQ_2OVIJ6"));
				cookies.add(new Cookie("finndy_verifyid", "3ddcb9566e1290678efe043bff876690"));
				driver.get(domain);
				driver.manage().addCookie((Cookie) cookies.get(0));
				driver.manage().addCookie((Cookie) cookies.get(1));
				driver.manage().addCookie((Cookie) cookies.get(2));
				driver.get(onclick);

				WebElement payButton = driver.findElement(By.cssSelector("button.btn.btn-info.input-small"));

				payButton.click();

				WebElement payOkButton = driver.findElement(By.cssSelector("button.btn.btn-primary.btn-sm"));

				Thread.sleep(2000);
				payOkButton.click();

			}
		}

		driver.close();
	}
}
