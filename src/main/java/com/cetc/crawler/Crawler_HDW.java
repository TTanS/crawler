package com.cetc.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cetc.utils.HttpClient;

/**
 * 汉典网
 * @author TS
 *
 */
public class Crawler_HDW {
	public static void main(String[] args) {
		List<String> pinyin = new ArrayList<>();
		List<String> hanzi = new ArrayList<>();
		String homePageUrl = "http://www.zdic.net/z/pyjs/";
		Document homePage = HttpClient.getHtmlDocument(homePageUrl);
		Elements dataDivs = homePage.select(".pyul");
		System.out.println(dataDivs.size());
		for (int i = 0; i < dataDivs.size(); i++) {
			Elements dataDiv = dataDivs.get(i).children();
			for (int j = 0; j < dataDiv.size() - 1; j++) {
				Element dataDt = dataDiv.get(j).child(0);
				Element dataHanZi = dataDiv.get(j).child(1);
				pinyin.add(dataDt.text());
				hanzi.add(dataHanZi.text());
			}
		}
		
		System.out.println(pinyin.size());
//		String fileName = "D:/crawler/汉典网/pinyin.txt";
//		String fileName = "D:/crawler/汉典网/hanzi.txt";
//		writeFile(fileName,hanzi.toString());
	}

	// 写文件
	public static void writeFile(String fileName, String fileContent) {
		try {
			File f = new File(fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(fileContent);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
