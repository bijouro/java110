package com.saerom.edds.edds.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository("docReceiveDao")
public class DocReceiveDaoImpl implements DocReceiveDao{
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int selectReceiveDoc(Map<String, Object> inputData) {
		return sqlSession.selectOne("EDDS.selectReceiveDoc", inputData);
	}

	@Override
	public int insertReceiveDoc(Map<String, Object> inputData) {
		return sqlSession.insert("EDDS.insertReceiveDoc", inputData);
	}
	
	@Override
	public  List<Map<String, Object>> selectReceiveDocList(Map<String, Object> inputData) {
		return sqlSession.selectList("EDDS.selectReceiveDocList", inputData);
	}
	
	@Override
	public int updateReceiveDocList(Map<String, Object> inputData) {
		return sqlSession.update("EDDS.updateReceiveDocList", inputData);
	}

	@Override
	public int selectSeq(Map<String, Object> inputData) {
		return sqlSession.selectOne("EDDS.selectSeq", inputData);
	}

	@Override
	public int updateReceiveRst(Map<String, Object> inputData) {
		return sqlSession.update("EDDS.updateReceiveRst", inputData);
	}

	@Override
	public int selectResDocErrCnt(Map<String, Object> inputData) {
		return sqlSession.selectOne("EDDS.selectResDocErrCnt", inputData);
	}

	@Override
	public int updateDocErrCnt(Map<String, Object> inputData) {
		return sqlSession.update("EDDS.updateDocErrCnt", inputData);
		
	}		
}
