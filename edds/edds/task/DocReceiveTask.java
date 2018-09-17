package com.saerom.edds.edds.task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.saerom.edds.comm.Configuration;
import com.saerom.edds.comm.FileHandling;
import com.saerom.edds.comm.NotesControl;
import com.saerom.edds.comm.Util;
import com.saerom.edds.comm.XmlParsing;
import com.saerom.edds.edds.dao.DocReceiveDao;
import com.saerom.edds.edds.dao.DocSendDao;
import com.saerom.edds.edds.vo.PackVo;
import com.saerom.edds.edds.vo.PubdocVo;


/**
 * DocReceiveTask 
 * 
 */
public class DocReceiveTask {
	
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Autowired
	private DocReceiveDao docReceiveDao;
	
	@Autowired
	private DocSendDao docSendDao;
	
	private boolean rcvProcess = false;
	
	/**
	 * Execute this task
	 */
	public void execute() {
		
		logger.debug(" 수신 프로세스 상태 :: " +rcvProcess);
		if(!rcvProcess){ // 이전 수신 프로세스 끝나면 수행
			rcvProcess = true;
			// 수신문서  insert
			getReceiveDoc();
			
			// 처리 대기중인 수신문서 처리
			processReceiveDoc();			
		}
		
	}

	private void processReceiveDoc() {
		HashMap<String, Object> hmInput = new HashMap<String, Object>();
		
		
		
		hmInput.put("state_0","0"); // state 0 : 대기
		hmInput.put("state_1","1"); // state 1 : 처리중
		hmInput.put("user","batch");
		
		List<Map<String, Object>> alList = docReceiveDao.selectReceiveDocList(hmInput);
		
		logger.debug(" 수신 처리 할 list @@ selectReceiveDocList  " + alList);
		
		for(int i=0;i<alList.size();i++){
			try{
				hmInput.put("file_id", (alList.get(i)).get("FILE_ID").toString());
				hmInput.put("seq", (alList.get(i)).get("SEQ").toString());
				
				logger.debug("seq :: " +(alList.get(i)).get("SEQ").toString());
				logger.debug("[receive_temp] file " +i+ ". : "+ alList.get(i).toString());
				logger.info("[====   문서 수신 시작     ==================]");
				
				// xml 파일 parse
				XmlParsing xmlparsing = new XmlParsing();
				
				String fileName = alList.get(i).get("FILE_ID").toString();
				String failePath = Configuration.getInstance().getValue("receive_temp");
				// pack 파일 분해
				PackVo pack = xmlparsing.unPack(failePath, new File(fileName));						
							
				NotesControl nc = new NotesControl();
				nc.getCodeValue(Configuration.getInstance().getValue("dbname"));
				
				String cont_role = pack.getDoc_type_type();
				
				hmInput.put("doc_id", pack.getDoc_id());
				hmInput.put("doc_type", cont_role);
				hmInput.put("title", pack.getTitle());		
				hmInput.put("send_name", pack.getSend_name());
				hmInput.put("send_id", pack.getSend_id());			
				
				docReceiveDao.updateReceiveDocList(hmInput);
				
				String err_code = "";
				String rst = "F";		
				
				String packNoVal = pack.getNoVal();			
				
				// pubdoc validation
				String err_file = Configuration.getInstance().getValue("attach_dir")+ "\\return.txt";
				File f_Err = new File(err_file);
				if(!f_Err.exists()){
					if("send".equals(cont_role) | "resend".equals(cont_role)){ //ACK 파일 아닐때
						
						String pubdocNoVal = ((PubdocVo)pack.getContents().getPubdoc().getContent()).getNoVal();			
						
						if("".equals(packNoVal) & "".equals(pubdocNoVal)){  // xml 문서  분해 후 필수 데이터 null 검사
							
							err_code = nc.createNotesDoc(pack);					
							// 노츠 문서 생성				
							if("".equals(err_code)){
								// ACK 생성
								
								createAck(nc, pack, "receive");
								
								logger.debug("노츠 문서 생성 후  receive 폴더로 파일 복사 시작 ..... ");
								FileHandling.moveFile(failePath+"\\"+fileName, Configuration.getInstance().getValue("receive_done")+"\\"+fileName);
								logger.debug("노츠 문서 생성 후  receive 폴더로 파일 복사 끝 ....... ");
								logger.info("문서 수신 완료 ............................ ");
								rst = "S";
							}else if("02".equals(err_code)){
								String err = "Document Receive Fail  : Check Attach file  ";
								Util.StringToFile(Configuration.getInstance().getValue("attach_dir")+ "\\return.txt", err);
								
								createAck(nc, pack, "fail");
								Util.delFile(err_file);
								
							}
						} else {							
							logger.info("  pack noVal : [" + packNoVal +"]");
							logger.info("  pubdoc noVal : [" + pubdocNoVal +"]");					
							err_code = "03";
							
							logger.info("문서 수신 실패  : 필수 data 없음  ");
							String err = "Document Receive Fail : Compulsory Data is empty\n" + packNoVal;
							Util.StringToFile(Configuration.getInstance().getValue("attach_dir")+ "\\return.txt", err);
							
							createAck(nc, pack, "fail");
							Util.delFile(err_file);
							
						}
														
					} else {//ACK 파일일떄						
						if("".equals(NotesControl.hmCode.get("eddslastrun").toString())){
							logger.debug("eddslastrun   아님 ");
							rst = nc.updateDocStatusByAgent(pack);
						} else{
							logger.debug("eddslastrun  OK"); // [kotra]
							rst = nc.updateDocStatus(pack);
						}
						
						logger.debug(" ACK 파일.. 처리 결과  " + rst);
						
						if(rst.equals("S")){
							logger.debug("receive 폴더로 파일 복사 시작 ..... ");
							FileHandling.moveFile(failePath+"\\"+fileName, Configuration.getInstance().getValue("receive_done")+"\\"+fileName);
							logger.debug(" receive 폴더로 파일 복사 끝 ....... ");
							logger.info("ACK 문서 수신 완료 ............................ ");
						} else {
							logger.debug("receive err 폴더로 파일 복사 시작 ..... ");
							FileHandling.moveFile(failePath+"\\"+fileName, Configuration.getInstance().getValue("receive_err")+"\\"+fileName);
							logger.debug(" receive err 폴더로 파일 복사 끝 ....... ");
							logger.info("ACK 문서 수신 err ............................ ");
						}
						logger.debug("#### 6" );
					}
				} else { //pubdoc validate 하지 않을 떄...
					logger.info("	pubdoc validate 하지 않음");
					String err = "Document Receive Fail : pubdoc dtd is not valid";
					Util.StringToFile(Configuration.getInstance().getValue("attach_dir")+ "\\return.txt", err);
					createAck(nc, pack, "fail");			
					logger.info("	fail ACK 생성.. ");
					
					
					logger.debug("receiveerr 폴더로 파일 복사 시작 ..... ");
					FileHandling.moveFile(failePath+"\\"+fileName, Configuration.getInstance().getValue("receive_err")+"\\"+fileName);
					logger.debug("receiveerr 폴더로 파일 복사 끝 ..... ");
					Util.delFile(err_file);
				}
				
				boolean errChk = true;
				// 결과 update
				if(!"".equals(err_code)){
					int err_cnt = docReceiveDao.selectResDocErrCnt(hmInput);
					
					logger.debug(fileName + " err_cnt :: " + err_cnt);
					
					if("02".equals(err_code) && err_cnt <= 5){ //5 번까지는 다시 수신한다.
						errChk = false;
						hmInput.put("err_cnt", err_cnt+1);
					}else{
						hmInput.put("state_rst", "F");
						FileHandling.moveFile(failePath+"\\"+fileName, Configuration.getInstance().getValue("receive_err")+"\\"+fileName);
					}
				}
				
				if(errChk){
					hmInput.put("err_code", err_code);
					hmInput.put("state_rst", rst);
					logger.debug(" input  >>" +  hmInput);
					
					docReceiveDao.updateReceiveRst(hmInput);
				}else{
					docReceiveDao.updateDocErrCnt(hmInput); //에러 count update
				}
				
			}catch(Exception e){				
				logger.error("EXCEPTION " + e.getMessage());
				e.printStackTrace();
				continue;
			}
		}
		rcvProcess = false;
	}
	
	private void createAck(NotesControl nc, PackVo pack, String type) {
		int seq = getSeqNo();
		String ackFilename = nc.generateAckXml(pack, type, "", seq);
		
		String state = "".equals(ackFilename)?"F":"S";
		
		HashMap<String, Object> ackInput = new HashMap<String, Object>();
		ackInput.put("doc_type", type);
		ackInput.put("state", state);
		ackInput.put("title", pack.getTitle());
		ackInput.put("seq", seq);
		ackInput.put("user", "batch");
		ackInput.put("file_id", ackFilename);
		ackInput.put("doc_id", pack.getDoc_id());
		ackInput.put("send_name", pack.getSend_name());
		ackInput.put("send_id", pack.getSend_id());
		
		docSendDao.insertSendDoc(ackInput);		
	}

	private void getReceiveDoc() {
    	String filepath = Configuration.getInstance().getValue("receive_temp");
    	
    	ArrayList<File> arrFilelist = FileHandling.getFileList(filepath);
    	
    	for(int i=0; i<arrFilelist.size();i++){
    		File f = new File(arrFilelist.get(i).toString());
    		
    		logger.debug(" ###" +arrFilelist.get(i).toString());    		
    		
    		// db 에 수신 문서 정보 검색 후 없으면 insert    		
    		
    		HashMap<String, Object> hmInput = new HashMap<String, Object>();
    		hmInput.put("file_id", f.getName());
    		hmInput.put("state_0","0");
    		hmInput.put("state_1","1");
    		hmInput.put("user","batch");
    		hmInput.put("err_cnt", 0);
    		
    		if(docReceiveDao.selectReceiveDoc(hmInput)==0){
    			hmInput.put("seq", getSeqNo());
    			docReceiveDao.insertReceiveDoc(hmInput);
    		}
    	}
	}
	
	private int getSeqNo(){
		HashMap<String, Object> hmDate = new HashMap<String, Object>();
		String today = Util.getToday();    		
		hmDate.put("s_date", today + "000000");
		hmDate.put("e_date", today + "235959");
		
		return docReceiveDao.selectSeq(hmDate);
	}
	
}
