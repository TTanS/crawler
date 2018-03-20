package com.cetc.service;

import java.io.IOException;

import com.cetc.pojo.GyBmggList;

public interface GyBmggListService {
	// 插入数据库
	public int insertSelective(GyBmggList record) throws IOException;
}
