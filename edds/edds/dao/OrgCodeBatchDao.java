package com.saerom.edds.edds.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface  OrgCodeBatchDao {
	
	public int updateDelCodeList(Map<String,Object> inputData);
	
	public List<Map<String, Object>> selectOrgStateInfo(Map<String,Object> inputData);

	public int updateNotesDocument(Map<String,Object> inputData);
}
