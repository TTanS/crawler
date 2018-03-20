package com.cetc.change;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Main {
	public static void main(String[] args) throws IOException {
		 String path = "D:\\TS_WORK\\工作\\基础资料\\test.txt";
		 String readMsg = readFile(path);
		 Document doc = Jsoup.parse(readMsg);
		 String txt = doc.getAllElements().text();
		 txt = formatData(txt);
		 String fileName = "D:\\TS_WORK\\工作\\基础资料\\new.txt";
		 writeFile(fileName, txt);

//		String path = "D:\\TS_WORK\\工作\\基础资料\\清洗.txt";
//		String readMsg = readFile(path);
		// MZ_ZW: "汉族", 
		// XJZDZQZ: "贵阳市云岩区金鸭社区服务中心黎苏居委会北京西路金龙星岛国际58号2栋1单元1601室",
		// CSRQ: "1989-11-5 0.0.0.0", 
		// JZZBH: "520103201750370975",
		// FWCS: "",
		// HJDXZ: "新疆和静县开来镇21团人民东路2区28栋4号", 
		// ZZSY_ZW: "", 
		// GMSFHM:"41140319891105606X", 
		// HYZK_ZW: "已婚", 
		
		// XB_ZW: "女性", 
		// ZZDZSSXQ_ZW:"贵州省贵阳市云岩区", 
		// XM: "王笑笑"}
//		
//		JSONObject jsMsg = JSONObject.parseObject(readMsg);
//		JSONObject personData = jsMsg.getJSONObject("data");
//		JSONArray personItems = personData.getJSONArray("items");
//		String name = "D:\\TS_WORK\\工作\\基础资料\\jsonData.txt"; 
//		writeFile(name,personItems.toJSONString());
	}
	
	private static String formatData(String readMsg){
		readMsg = readMsg.replaceAll("rtnMsg", "\"rtnMsg\"");
		readMsg = readMsg.replaceAll("rtnCode", "\"rtnCode\"");
		readMsg = readMsg.replaceAll("data", "\"data\"");
		readMsg = readMsg.replaceAll("items", "\"items\"");
		readMsg = readMsg.replaceAll("MZ_ZW", "\"MZ_ZW\"");
		readMsg = readMsg.replaceAll("XJZDZQZ", "\"XJZDZQZ\"");
		readMsg = readMsg.replaceAll("CSRQ", "\"CSRQ\"");
		readMsg = readMsg.replaceAll("JZZBH", "\"JZZBH\"");
		readMsg = readMsg.replaceAll("FWCS", "\"FWCS\"");
		readMsg = readMsg.replaceAll("HJDXZ", "\"HJDXZ\"");
		readMsg = readMsg.replaceAll("ZZSY_ZW", "\"ZZSY_ZW\"");
		readMsg = readMsg.replaceAll("GMSFHM", "\"GMSFHM\"");
		readMsg = readMsg.replaceAll("HYZK_ZW", "\"HYZK_ZW\"");
		readMsg = readMsg.replaceAll("XB_ZW", "\"XB_ZW\"");
		readMsg = readMsg.replaceAll("ZZDZSSXQ_ZW", "\"ZZDZSSXQ_ZW\"");
		readMsg = readMsg.replaceAll("XM", "\"XM\"");
		return readMsg;
	}

	// 写文件
	public static void writeFile(String fileName, String fileContent) {
		try {
			File f = new File(fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "gbk");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(fileContent);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读文件
	public static String readFile(String fileName) {
		String fileContent = "";
		try {
			File f = new File(fileName);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(f), "gbk");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent += line;
				}
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent;
	}

}
