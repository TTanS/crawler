package com.cetc.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cetc.utils.HttpClient;

public class TestTotalSize {
	public static void main(String[] args) {
		String url = "http://www.gygov.gov.cn/gygov//openapi/info/ajaxpagelist.do?pagesize=4686&channelid=78&pageno=1";
		String data = HttpClient.getHtmlDocument(url).body().text();;
		JSONObject jsData = JSONObject.parseObject(data);
		JSONArray dataArray = JSONArray.parseArray(jsData.getString("infolist"));
		System.out.println(dataArray.size());
		
		
		
		
	}
}
