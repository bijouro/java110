package com.saerom.edds.edds.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;



@Repository("ldapSearchDao")
public class LdapBatchDaoImpl implements LdapBatchDao{
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	
	@Resource(name="sqlBatchSessionTemplate")
	private SqlSessionTemplate sqlBatchSession;

	@Override
	public int insertLdapDataList(Map<String, Object> inputData) {
		return sqlSession.insert("EDDS.insertLdapDataList", inputData);
	}
	
	@Override
	public String selectLdapBatch(Map<String, Object> inputData) {
		return sqlSession.selectOne("EDDS.selectLdapBatch", inputData);
	}
	
	@Override
	public int updateLdapDataList(Map<String, Object> inputData) {
		return sqlSession.update("EDDS.updateLdapDataList", inputData);
	}

	@Override
	public int insertOrgCode(Map<String, Object> inputData) {
		return sqlSession.update("EDDS.insertOrgCode", inputData);
	}

	@Override
	public int insertOrgCode2(Map<String, Object> inputData) {
		return sqlSession.update("EDDS.insertOrgCode2", inputData);
	}

	@Override
	public int insertExceptionFilter(Map<String, Object> inputData) {
		return sqlSession.update("EDDS.insertExceptionFilter", inputData);
	}
	
	@Override
	public List<Map<String, Object>> selectOrgList(Map<String, Object> inputData) {
		return sqlSession.selectList("EDDS.selectOrgList", inputData);
	}
	
	@Override
	public List<Map<String, Object>> selectOrg(Map<String, Object> inputData) {
		return sqlSession.selectList("EDDS.selectOrg", inputData);
	}

	@Override
	public int updateUcChieftitle(List<Map<String,Object>> inputData) {
		int cnt=0;
		for(Map<String, Object> inputMap : inputData){
			sqlBatchSession.update("EDDS.updateUcChieftitle", inputMap);
			cnt++;
		}
		
		return cnt;
	}	
}
