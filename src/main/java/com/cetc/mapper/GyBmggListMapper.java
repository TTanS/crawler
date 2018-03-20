package com.cetc.mapper;

import com.cetc.pojo.GyBmggList;

public interface GyBmggListMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(GyBmggList record);

    int insertSelective(GyBmggList record);


    GyBmggList selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(GyBmggList record);

    int updateByPrimaryKey(GyBmggList record);
}