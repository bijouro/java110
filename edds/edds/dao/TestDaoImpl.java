package com.saerom.edds.edds.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("testDao")
public class TestDaoImpl implements TestDao{
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;

	public List<Map<String, Object>> selectList(Map<String, Object> inputData) {
		return sqlSession.selectList("EDDS.selectListTest", inputData);
	}

}