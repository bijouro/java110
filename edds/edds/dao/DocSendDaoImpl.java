package com.saerom.edds.edds.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;



@Repository("docSendDao")
public class DocSendDaoImpl implements DocSendDao{
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int insertSendDoc(Map<String, Object> inputData) {
		return sqlSession.insert("EDDS.insertSendDoc", inputData);
	}	
}
