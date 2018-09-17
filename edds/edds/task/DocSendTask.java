package com.saerom.edds.edds.task;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.saerom.edds.comm.NotesControl;
import com.saerom.edds.comm.Util;
import com.saerom.edds.edds.dao.DocReceiveDao;
import com.saerom.edds.edds.dao.DocSendDao;


/**
 * DocSendTask 
 * 
 */
public class DocSendTask {
	
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Autowired
	private DocSendDao docSendDao;
	
	@Autowired
	private DocReceiveDao docReceiveDao;
	
	
	private boolean sendProcess = false;

	/**
	 * Execute this task
	 */
	public void execute() {
		logger.debug(" 발신 프로세스 상태 :: " +sendProcess);
		if(!sendProcess){ // 이전 발송 프로세스 끝나면 수행
			selectSendDocument();
		}		
	}

	private void selectSendDocument() {
		sendProcess = true;
		
		try{
			NotesControl nc = new NotesControl();
			ArrayList alSendDoc = nc.sendDocument();
			
			logger.debug("####### alSendDoc" +alSendDoc.size());
			
			for(int i=0; i<alSendDoc.size();i++){
				HashMap hmInput = (HashMap)alSendDoc.get(i);
				
				hmInput.put("user", "batch");
				hmInput.put("seq", getSeqNo());
				
				docSendDao.insertSendDoc(hmInput);
			}
			
			logger.debug("####### end");
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		sendProcess = false;
	}
	
	private int getSeqNo(){
		HashMap hmDate = new HashMap();
		String today = Util.getToday();    		
		hmDate.put("s_date", today + "000000");
		hmDate.put("e_date", today + "235959");
		
		return docReceiveDao.selectSeq(hmDate);
	}
	
}
