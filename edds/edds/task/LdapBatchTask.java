package com.saerom.edds.edds.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.saerom.edds.comm.Configuration;
import com.saerom.edds.comm.LdapSearch;
import com.saerom.edds.comm.Util;
import com.saerom.edds.edds.dao.LdapBatchDao;


/**
 * LdapBatchTask 
 * 
 */
public class LdapBatchTask {
	
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Autowired
	private LdapBatchDao ldapBatchDao;

	/**
	 * Execute this task
	 */
	public void execute() {
		
		HashMap input = new HashMap();
		
		input.put("user", "batch");
		input.put("state_0", "0");
		
		// 정부 디렉토리에서 수신처 목록 검색 중인지 확인
		String batchJob = ldapBatchDao.selectLdapBatch(input);
		
		if("0".equals(batchJob)){
			// ldap 배치 수행중이지 않으면 배치 수행 정보 insert
			ldapBatchDao.insertLdapDataList(input);
			
			String startCurrTime = Util.getTodayCurrTime();
			logger.info("##### LDAP 배치 수행 시작 : " + startCurrTime.substring(0, 4) +"-"+startCurrTime.substring(4, 6)+"-"+startCurrTime.substring(6, 8)+" " 
					+startCurrTime.substring(8, 10)+":"+startCurrTime.substring(10, 12)+":"+startCurrTime.substring(12, 14));
			
			
			String [] resultAttr = {"ouCode","ou", "topOuCode","parentOuCode","repOucode", "ucOrgFullName", "ouLevel", "ouOrder", "ucChieftitle", "ouReceiveDocumentYN", "ouSendOutDocumentYN" };				
			
			//정부 LDAP search batch 시작			
			ldapSearch(resultAttr);
			
			String endCurrTime = Util.getTodayCurrTime();
			logger.info("##### LDAP 배치 수행 종료 : " + endCurrTime.substring(0, 4) +"-"+endCurrTime.substring(4, 6)+"-"+endCurrTime.substring(6, 8)+" " 
					+endCurrTime.substring(8, 10)+":"+endCurrTime.substring(10, 12)+":"+endCurrTime.substring(12, 14));
			
			input.put("search_cnt", "0");
			input.put("state_s", "S");
			input.put("err_detail", "");
			
			// 정부 LDAP search batch 완료 정보 update
			int rst = ldapBatchDao.updateLdapDataList(input);
			
			if(rst>0){
				logger.info("##### LDAP search batch 완료");
			}
		}
		
		
		// {config.properties>attach_del}일이 지난 tmp 폴더 삭제.. 하루 한번 실행 하면 되므로, ldap 정보 batch 수행 후 실행.. 
		delTempFolder();
		
		// {config.properties>log_del}일이 지난 tmp 폴더 삭제.. 하루 한번 실행 하면 되므로, ldap 정보 batch 수행 후 실행..
		delLog();
	}


	// 최상위 노드 검색
	private void ldapSearch(String [] resultAttr) {
		LdapSearch ldapSearch = new LdapSearch();
		
		String base_dn = Configuration.getInstance().getValue("base_dn"); //Configuration.getInstance().getValue("base_dn");
		String filter = "(objectClass=ucOrg2)";
		
		String start_curTime = Util.getTodayCurrTime();
		logger.debug(" ---- LDAP 검색 시작  ====> " + start_curTime.substring(0, 4) +"-"+start_curTime.substring(4, 6)+"-"+start_curTime.substring(6, 8)+" " 
				+start_curTime.substring(8, 10)+":"+start_curTime.substring(10, 12)+":"+start_curTime.substring(12, 14));
		
		// 자식 노드만 검색. 
		ArrayList allist = ldapSearch.ldapSearch(base_dn, filter, 1, resultAttr);
		logger.debug("base dn 검색 건수 : " + allist.size());
		
		fristNodeInsert(allist);
		int iCnt = getLowOrgCode(allist, resultAttr) - allist.size();
		ucChieftitleUpdate();//2017.08.30 chieftitle 변경 로직 추가
		
		logger.debug("base_dn 포함 전체 검색 건수 : " + iCnt);
		
		logger.debug("전체 검색 건수 : " + (iCnt-allist.size()));	
		
		String end_curTime = Util.getTodayCurrTime();
		logger.debug(" ---- LDAP 검색 끝  ====> " + end_curTime.substring(0, 4) +"-"+end_curTime.substring(4, 6)+"-"+end_curTime.substring(6, 8)+" " 
				+end_curTime.substring(8, 10)+":"+end_curTime.substring(10, 12)+":"+end_curTime.substring(12, 14));
		
	}

	// 더이상 자식 노드가 없을때까지 검색 
	private int getLowOrgCode(ArrayList allist, String [] resultAttr) {
		LdapSearch ldapSearch = new LdapSearch();
		String base_dn = Configuration.getInstance().getValue("base_dn");
		String rstOuCode = "";
		ArrayList rst = null;
		HashMap rstLdap = null;
		
		int iTotCnt = allist.size();
		
		for(int i=0; i<allist.size();i++){
			rst = new ArrayList();
			String ou =  ((HashMap) allist.get(i)).get("ou").toString();
			String ouCode =  ((HashMap) allist.get(i)).get("ouCode").toString();
			String full_dn = ((HashMap) allist.get(i)).get("dn").toString();
			
			if(!("people".equals(ou) || "Systems".equals(ou)|| ou.contains("Group")|| "GPKI".equals(ou))){
				String filter = "(&(objectClass=ucOrg2)(parentOuCode="+ouCode+"))";
				//String filter = "(&(&(objectClass=ucOrg2)(parentOuCode=1015000))(!(ou=Systems))(!(ou=Group))(!(ou=people))(!(ou=GPKI)))"
				rst = ldapSearch.ldapSearch(full_dn, filter, 1, resultAttr);
				
				for(int j=0;j<rst.size();j++){
					rstLdap = new HashMap<>();
					rstLdap = (HashMap) rst.get(j);
					
					// ldap search 한 결과를 '|'를 구분자로 붙인다.
					String ldapInfo = rstLdap.get("ouCode").toString() + '|'
							 + rstLdap.get("ou").toString() + '|'
							 + rstLdap.get("topOuCode").toString() + '|'
							 + rstLdap.get("parentOuCode").toString() + '|'
							 + rstLdap.get("ucOrgFullName").toString() + '|'
							 + rstLdap.get("ouLevel").toString() + '|'
							 + rstLdap.get("ouOrder").toString() + '|'
							 + rstLdap.get("ucChieftitle").toString() + '|'
							 + rstLdap.get("ouReceiveDocumentYN").toString() + '|'
							 + rstLdap.get("ouSendOutDocumentYN").toString();
					
					String regdate = Util.getToday();
					String notesYN = "N"; 
					String state_u = "U"; // update
					String state_0 = "0";	 // 변경 사항 없음
					String state_i = "I";	 // insert
					String user = "batch";
					
					logger.debug("ldapInfo >" + ldapInfo);
					
					rstLdap.put("ldapInfo", ldapInfo);
					rstLdap.put("regdate", regdate);
					rstLdap.put("notesYN", notesYN);
					rstLdap.put("state_u", state_u);
					rstLdap.put("state_0", state_0);
					rstLdap.put("state_i", state_i);
					rstLdap.put("user", user);
					
					rstOuCode = rstLdap.get("ou").toString();
					
					if(!("people".equals(rstOuCode) || "Systems".equals(rstOuCode)|| rstOuCode.contains("Group")|| "GPKI".equals(rstOuCode))){
						ldapBatchDao.insertOrgCode2(rstLdap);
					}
				}
				
				// 상위코드로 자신을 가지고 있는 노드가 없으면 마지막노드. 검색 안함.
				if(rst.size()!=0){
					int iCnt = getLowOrgCode(rst, resultAttr);
					iTotCnt+=iCnt;
				}
			}
		}
		return iTotCnt;
	}
	
	private void fristNodeInsert(ArrayList rst){
		for(int j=0;j<rst.size();j++){
			HashMap rstLdap = (HashMap) rst.get(j);
			
			// ldap search 한 결과를 '|'를 구분자로 붙인다.
			String ldapInfo = rstLdap.get("ouCode").toString() + '|'
					 + rstLdap.get("ou").toString() + '|'
					 + rstLdap.get("topOuCode").toString() + '|'
					 + rstLdap.get("parentOuCode").toString() + '|'
					 + rstLdap.get("ucOrgFullName").toString() + '|'
					 + rstLdap.get("ouLevel").toString() + '|'
					 + rstLdap.get("ouOrder").toString() + '|'
					 + rstLdap.get("ucChieftitle").toString() + '|'
					 + rstLdap.get("ouReceiveDocumentYN").toString() + '|'
					 + rstLdap.get("ouSendOutDocumentYN").toString();
			
			String regdate = Util.getToday();
			String notesYN = "N"; 
			String state_u = "U"; // update
			String state_0 = "0";	 // 변경 사항 없음
			String state_i = "I";	 // insert
			String user = "batch";
			
			logger.debug("ldapInfo >" + ldapInfo);
			
			rstLdap.put("ldapInfo", ldapInfo);
			rstLdap.put("regdate", regdate);
			rstLdap.put("notesYN", notesYN);
			rstLdap.put("state_u", state_u);
			rstLdap.put("state_0", state_0);
			rstLdap.put("state_i", state_i);
			rstLdap.put("user", user);
			ldapBatchDao.insertOrgCode2(rstLdap);
		}
	}
	
	/** 2017.08.30 추가 
	 * ucChieftitle update 로직 - ucChieftitle에 값이 있고 ouSendOutDocumentYN 값이 N이면 parentOuCode의 값을 가지고 ouCode값을 검색하여 ouSendOutDocumentYN 값이 Y이거나 자신이 최상위일 때까지 찾아 ou값을 넣어준다. 
	 */
	private void ucChieftitleUpdate(){
		HashMap<String, Object> input = new HashMap<String, Object>();
		Map<String, Object> input2 = null;
		HashMap<String, Object> topOrg = null;
		String ucchieftitle = "";
		int count = 0;
		List<Map<String, Object>> updateList = new LinkedList<Map<String, Object>>();
		
		input.put("ucchieftitle", "");
		input.put("likeUcchieftitle", "%(%");
		
		List<Map<String, Object>> orgList = ldapBatchDao.selectOrgList(input);
		//logger.debug("ucChieftitleUpdate :: "+ orgList);
		
		for(Map<String, Object> orgMap : orgList){
			input2 = new HashMap<String, Object>();
			//logger.debug("orgMap :: "+ orgMap);
			
			ucchieftitle = (String) orgMap.get("UCCHIEFTITLE");
			topOrg = findOrgCode(orgMap);
			//logger.debug("topOrg  ::"+ topOrg);
				
			input2.put("ucchieftitle", topOrg.get("OU")+"("+orgMap.get("UCCHIEFTITLE")+")");
			input2.put("oucode", orgMap.get("OUCODE"));
			
			updateList.add(input2);
				
		}
		
		String start_curTime = Util.getTodayCurrTime();
		//System.out.println(" ---- batch code 시작  ====> " + start_curTime.substring(0, 4) +"-"+start_curTime.substring(4, 6)+"-"+start_curTime.substring(6, 8)+" " 
		//		+start_curTime.substring(8, 10)+":"+start_curTime.substring(10, 12)+":"+start_curTime.substring(12, 14));
		
		count = ldapBatchDao.updateUcChieftitle(updateList);
		logger.debug("ucChiefTitle update count ::" + count);
	}
	
	private HashMap findOrgCode(Map<String, Object> reqMap){
		HashMap resMap = new HashMap();
		Map<String, Object> inputMap = new HashMap<String, Object>();
		boolean parentChk = true;
		
		//logger.debug("reqMap  ::"+ reqMap);
		
		inputMap.put("oucode", reqMap.get("PARENTOUCODE"));
		List<Map<String, Object>> findParentOrg = ldapBatchDao.selectOrg(inputMap);
		
		if(!findParentOrg.isEmpty()){
			parentChk = false;
		}
		
		logger.debug("parentChk  ::"+ parentChk);
		
		if((reqMap.get("OUSENDOUTDOCUMENTYN") != null && "Y".equals(reqMap.get("OUSENDOUTDOCUMENTYN"))) 
				|| reqMap.get("OUCODE").equals(reqMap.get("PARENTOUCODE")) 
				|| "0000000".equals(reqMap.get("PARENTOUCODE"))
				|| parentChk){
			
			resMap.putAll(reqMap);
			return resMap;
		}else{
			logger.debug("########findParentOrg :: "+ findParentOrg);
			
			return findOrgCode(findParentOrg.get(0));
		}
		
	}
	
	private void delTempFolder() {
		int day = Integer.parseInt(Configuration.getInstance().getValue("attach_del"));
		String attach_path = Configuration.getInstance().getValue("attach_dir")+"\\"+Util.dateCal(-day);
		String pubdoc_path = Configuration.getInstance().getValue("pubdoc_dir")+"\\"+Util.dateCal(-day);
		
		
		logger.info(attach_path);
		logger.info(pubdoc_path);
		
		Util.delFoler(attach_path);
		Util.delFoler(pubdoc_path);
	}
	

	private void delLog() {
		int day = Integer.parseInt(Configuration.getInstance().getValue("log_del"));
		String log_path = Configuration.getInstance().getValue("log_dir");  //+"\\"+Util.dateCal(-day);
		String log_file_day = Util.dateCal(-day);		
		log_file_day = log_file_day.substring(0, 4)+"-"+log_file_day.substring(4, 6)+"-"+log_file_day.substring(6, 8);		
		boolean rst = Util.delFile(log_path+"\\debug.log."+log_file_day);
		
		logger.info("로그 파일 삭제  : " + rst +" ["+log_path+"\\debug.log."+log_file_day + "]");
	}
}
