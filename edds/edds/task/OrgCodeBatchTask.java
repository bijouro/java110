package com.saerom.edds.edds.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.saerom.edds.comm.NotesControl;
import com.saerom.edds.comm.Util;
import com.saerom.edds.edds.dao.OrgCodeBatchDao;


/**
 * OrgBatchTask 
 * 
 */
public class OrgCodeBatchTask {
	
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Autowired
	private OrgCodeBatchDao orgCodeBatchDao;
	
	/**
	 * Execute this task
	 */
	public void execute() {
		// ldap에서 search 해온 수신처 코드 정보 update
		orgCodeInfoUpdate();	
		logger.debug(" 수신처 코드 업데이트 배치");
	}
	
	private void orgCodeInfoUpdate() {
		HashMap input = new HashMap();
		
		// update 되지 않은 ldap 수신처 코드는 삭제된 수신처 코드 이므로 오늘 날짜로 update 되지 않은 (regDate)row의   state컬럼 D로 update 하기.
		input.put("state_d", "D");
		input.put("user", "batch");		
		input.put("yesterday", Util.dateCal(-1));
		input.put("regdate", Util.getToday());
		input.put("notes_yn_n", "N");
		
		orgCodeBatchDao.updateDelCodeList(input);		
		
		// 신규 수신처 정보
		input.put("state", "I");
		List<Map<String, Object>> alNewOrg = orgCodeBatchDao.selectOrgStateInfo(input);
		
		//신규 수신처  정보 notes서버에 insert
		if(alNewOrg.size()>0){
			//logger.debug("insert List " + alNewOrg);
			insertOrgInfo("I", alNewOrg);
		}
		
		// 변경 수신처 정보
		input.put("state", "U");
		List<Map<String, Object>> alUpdateOrg = orgCodeBatchDao.selectOrgStateInfo(input);
		
		// 변경 수신처  정보 notes서버에 update
		if(alUpdateOrg.size()>0){
			//logger.debug("update List " + alUpdateOrg);
			insertOrgInfo("U", alUpdateOrg);
		}
		
		// 삭제 수신처 정보
		input.put("state", "D");
		input.put("regdate", Util.dateCal(-1));
		List<Map<String, Object>> alDelOrg = orgCodeBatchDao.selectOrgStateInfo(input);
		
		// 삭제 수신처  정보 notes서버에 delete
		if(alDelOrg.size()>0){
			//logger.debug("delete List " + alDelOrg);
			insertOrgInfo("D", alDelOrg);
		}
		
	}	

	private void insertOrgInfo(String state, List<Map<String, Object>> alCodeList) {
		//note 문서 생성
		NotesControl nc = new NotesControl();
		ArrayList alRst = nc.uploadLdapInfo(state, alCodeList);
		
		for(int i=0;i<alRst.size();i++){
			orgCodeBatchDao.updateNotesDocument((Map<String, Object>) alRst.get(i));
		}
	}
}
