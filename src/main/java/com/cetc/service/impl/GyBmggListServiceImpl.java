package com.cetc.service.impl;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.cetc.mapper.GyBmggListMapper;
import com.cetc.pojo.GyBmggList;
import com.cetc.service.GyBmggListService;

public class GyBmggListServiceImpl implements GyBmggListService {

	
	// 得到session
	public SqlSession getSession() throws IOException{
		// 获取资源文件
		Reader reader = Resources.getResourceAsReader("mybatis/mybatisconfig.xml");
		// 获取sessionFactory
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		SqlSessionFactory factory = builder.build(reader);
		// 打开session，创建能执行配置文件中的sql
		SqlSession session = factory.openSession();
		return session;
	}
	

	public int insertSelective(GyBmggList record) throws IOException {
		// 得到mapper
		SqlSession session = getSession();
		GyBmggListMapper mapper = session.getMapper(GyBmggListMapper.class);
		int retCode = mapper.insertSelective(record);
		session.commit();
		session.close();
		return retCode;
	}

}
