package com.saerom.edds.edds.dao;

import java.util.List;
import java.util.Map;

public interface  LdapBatchDao {		
	
	public int insertLdapDataList(Map<String,Object> inputData);
	
	public String selectLdapBatch(Map<String,Object> inputData);
	
	public int updateLdapDataList(Map<String,Object> inputData);
	
	public int insertOrgCode (Map<String,Object> inputData);
	
	public int insertOrgCode2 (Map<String,Object> inputData);
	
	public int insertExceptionFilter (Map<String,Object> inputData);
	
	public List<Map<String, Object>> selectOrgList (Map<String,Object> inputData);
	
	public List<Map<String, Object>> selectOrg (Map<String,Object> inputData);
	
	public int updateUcChieftitle (List<Map<String,Object>> inputData);
}
