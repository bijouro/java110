package com.saerom.edds.edds.dao;

import java.util.List;
import java.util.Map;

public interface  DocReceiveDao {
	
	public int selectReceiveDoc(Map<String,Object> inputData);
	
	public int insertReceiveDoc(Map<String,Object> inputData);
	
	public List<Map<String, Object>> selectReceiveDocList(Map<String,Object> inputData);
	
	public int updateReceiveDocList(Map<String,Object> inputData);

	public int selectSeq(Map<String,Object> inputData);

	public int updateReceiveRst(Map<String,Object> inputData);
	
	public int selectResDocErrCnt(Map<String,Object> inputData);

	public int updateDocErrCnt(Map<String,Object> inputData);
}
