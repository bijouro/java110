package com.saerom.edds.edds.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;



@Repository("orgCodeBatchDao")
public class OrgCodeBatchDaoImpl implements OrgCodeBatchDao{
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int updateDelCodeList(Map<String,Object> inputData) {
		return sqlSession.update("EDDS.updateDelCodeList", inputData);
	}
	
	@Override
	public List<Map<String, Object>> selectOrgStateInfo(Map<String, Object> inputData) {
		return sqlSession.selectList("EDDS.selectOrgStateInfo", inputData);
	}

	@Override
	public int updateNotesDocument(Map<String, Object> inputData) {
		logger.debug("$$###");
		return sqlSession.update("EDDS.updateNotesDocument", inputData);
	}

	

		
}
