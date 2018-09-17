package com.saerom.edds.comm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.saerom.edds.edds.dao.DocReceiveDao;
import com.saerom.edds.edds.vo.ContentVo;
import com.saerom.edds.edds.vo.ContentsVo;
import com.saerom.edds.edds.vo.PackVo;
import com.saerom.edds.edds.vo.PubdocVo;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.Item;
import lotus.domino.MIMEEntity;
import lotus.domino.MIMEHeader;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.RichTextItem;
import lotus.domino.Session;
import lotus.domino.Stream;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;



public class NotesControl {
	private static Logger logger = Logger.getLogger(Class.class.getName());
	
	@Autowired
	private DocReceiveDao docReceiveDao;

	
	public static HashMap hmCode = new HashMap();
	
	public Session session = null;
	public Database database = null;
	public String host = null;
	public String domino_user = null;
	public String domino_pw = null;
	public String server = null;
	public String dbname = null;
	
	public NotesControl(){
		logger.debug(" ####   NotesControl");
		host = Configuration.getInstance().getValue("host");
		domino_user = Util.decodeBase64(Configuration.getInstance().getValue("domino_user"));
		domino_pw = Util.decodeBase64(Configuration.getInstance().getValue("domino_pw"));
		server = Configuration.getInstance().getValue("server");
		dbname = Configuration.getInstance().getValue("dbname");		
		
		//notesConnection(dbname);
		
		/*try {
			session = NotesFactory.createSession(host, domino_user, domino_pw);
			database = session.getDatabase(server, dbname);
		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/	
	}	
	
	
	public void notesConnection(String databseName){
		dbname = databseName;
		
		try {
			session = NotesFactory.createSession(host, domino_user, domino_pw);
			database = session.getDatabase(server, dbname);
			//getCodeValue();
		} catch (NotesException e) {
			e.printStackTrace();
		}
	}
	
	public Database notesConnectionDb(String databseName){
		Database db = null;
		
		try {
			session = NotesFactory.createSession(host, domino_user, domino_pw);
			db = session.getDatabase(server, databseName);
			//getCodeValue();
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return db;
	}
	
	public boolean notesConn(){
		boolean dbconn=false;
		
		try {
			if(database.isOpen()){
				logger.debug("\n[ Notes Connect OK ]===================================\n"+
						"   host: "+ host + "\n   server: "+ server+"\n   dbname: "+ dbname+
						"\n============================================================");
				dbconn = true;
			} else {
				logger.debug("\n[ Notes Connect Fail ] ===== "+dbname);
			}
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return dbconn;
	}

	/**
	 * Notes code 세팅
	 */
	public void getCodeValue(String dbName) {
		View notesView = null;
		Document notesDoc = null;
		ArrayList alEntry = new ArrayList();
		
		Database db = notesConnectionDb(dbName);
		
		try {
		
			notesView = db.getView(Configuration.getInstance().getValue("codebook_view").toString());
			
			ViewEntryCollection entry = notesView.getAllEntries();			
			
			Vector<String> resourceCode = entry.getFirstEntry().getDocument().getItemValue("ResourceCode");
			
			String [] code = resourceCode.toArray(new String[resourceCode.size()]);			
					
			if(code.length>0){
				alEntry.add(code[0]);
				hmCode.put(code[0], notesView.getDocumentByKey(code[0]).getItemValue("subject").toArray()[0]);

				int i=1;
				while (i<entry.getCount()){
					resourceCode = entry.getNextEntry().getDocument().getItemValue("ResourceCode");
					if(resourceCode.size()>0){
						code = resourceCode.toArray(new String[resourceCode.size()]);
						alEntry.add(code[0]);
						
						String value = "";
						if (notesView.getDocumentByKey(code[0]).getItemValue("subject").size()>0){
							value = notesView.getDocumentByKey(code[0]).getItemValue("subject").toArray()[0].toString();
						}
						hmCode.put(code[0], value);
					}
					i++;
				}
			}
			logger.debug(" hmCode " + hmCode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != notesView) {
				try {
					notesView.recycle();
				} catch (NotesException e) {
					e.printStackTrace();
				}
				notesView = null;
			}
			notesClose(db);
		}
	}
	
	public ArrayList uploadLdapInfo(String state, List<Map<String, Object>> alCodeList){
		Database db = null;
		Document code_doc = null;
		View code_view = null;
		
		ArrayList alRst = new ArrayList();
		
		String dbName = Configuration.getInstance().getValue("ldapbuffer_path"); //common/ldapbuffer.nsf
		String ldapView = Configuration.getInstance().getValue("ldapbuffer_view"); // ldap buffer index view
		logger.debug(dbName);
		logger.debug("state >> " + state);
		try {
			db = notesConnectionDb(dbName);
			code_view = db.getView(ldapView);
			//logger.debug("alCodeList " + alCodeList);
			logger.debug("alCodeList size " + alCodeList.size());
			for(int i=0;i<alCodeList.size();i++){
				boolean succ = false;
				
				HashMap hmOrgCode = (HashMap) alCodeList.get(i);
				String ouCode = hmOrgCode.get("OUCODE").toString();
				
				logger.debug("ouCode [" +i+ "] >> " + ouCode);
				ViewEntry entry = code_view.getEntryByKey(ouCode, true);
				logger.debug(" entry  [" +i+ "] >> " + entry);
				if("I".equals(state) | "U".equals(state)){
					logger.debug("state :  "+ state);
					
					if (entry == null){
						code_doc = db.createDocument();
						code_doc.replaceItemValue("form", "form01");						
						setNotesValue(code_doc, "ouCode", ouCode, true);					
						setCodeValue(code_doc, hmOrgCode);
						
						logger.debug("insert... ");	
					} else {
						logger.debug("이미 있음... ");
						
						//if (entry != null) {
						code_doc = entry.getDocument();						
						setCodeValue(code_doc, hmOrgCode);
						logger.debug("update... ");
						//}
					}
					
					if (code_doc.computeWithForm(false, false)) {
						succ = code_doc.save(true, true);
						logger.debug("Save Document " + succ);
					}else{
						logger.debug("Document not saved.. ");
					}
				
				} else if("D".equals(state)){
					if (entry != null){
						code_doc = entry.getDocument();	
						succ = code_doc.remove(true);
						logger.debug("delete... ");					
						
					}	
				}
				logger.debug("처리 결과  >> " + succ);
				if(succ){
					Map<String, Object> input = new HashMap<String, Object>();
					String rstCode = "D".equals(state)?state:"0";
					
					input.put("state", rstCode);
					input.put("oucode", ouCode);
					input.put("notesyn", "Y");
					input.put("user", "batch");
					logger.debug("input >> "+input);
					
					alRst.add(input);
				}
				/*try {
					logger.debug("==org sleep==s");
					Thread.sleep(200);
					logger.debug("==org sleep==e");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
			}
			
		} catch (NotesException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error("#####uploadLdapInfo error#####");
		} finally {
			if (null != code_doc) {
				try {
					code_doc.recycle();
				} catch (NotesException e) {
					e.printStackTrace();
				}
				code_doc = null;
			}
			
			notesClose(db);
		}
		return alRst;
	}
	
	private void setCodeValue(Document code_doc, HashMap hmOrgCode) {
		logger.debug("hmOrgCode "+hmOrgCode);
		setNotesValue(code_doc, "ou", hmOrgCode.get("OU").toString(), true);
		setNotesValue(code_doc, "topOuCode", hmOrgCode.get("TOPOUCODE").toString(), true);
		setNotesValue(code_doc, "parentOuCode", hmOrgCode.get("PARENTOUCODE").toString(), true);
		setNotesValue(code_doc, "repOucode", hmOrgCode.get("REPOUCODE").toString(), true);
		setNotesValue(code_doc, "ucOrgFullName", hmOrgCode.get("UCORGFULLNAME").toString(), true);
		setNotesValue(code_doc, "ouLevel", hmOrgCode.get("OULEVEL").toString(), true);
		setNotesValue(code_doc, "ouOrder", hmOrgCode.get("OUORDER").toString(), true);
		setNotesValue(code_doc, "ucChieftitle", hmOrgCode.get("UCCHIEFTITLE").toString(), true);
		setNotesValue(code_doc, "ouReceiveDocumentYN", hmOrgCode.get("OURECEIVEDOCUMENTYN").toString(), true);
		setNotesValue(code_doc, "ouSendOutDocumentYN", hmOrgCode.get("OUSENDOUTDOCUMENTYN").toString(), true);
	}


	public String createNotesDoc(PackVo pack){
		Database db = null;
		Document newNotesDoc = null;
		String dbName = hmCode.get("database_path_edds").toString();
		String rcv_edds = hmCode.get("receivetoedds").toString();
		String err_cdoe = "";
		
		if("yes".equals(rcv_edds)){
			dbName = hmCode.get("database_path_edds").toString();
		} else{
			dbName = hmCode.get("database_path_receive").toString();
		}
		
		db = notesConnectionDb(dbName); //문서 수신함 notes db path
		
		if(db == null){
			err_cdoe = "01";
			logger.error(err_cdoe + "notes db 연결 실패  [" + dbName + "]");
		} else {
		
			try {
				newNotesDoc = db.createDocument();
				newNotesDoc.replaceItemValue("form", "form02");
				
				String addfields[] = hmCode.get("addfields").toString().split(";");
				for(int i=0;i<addfields.length;i++){
					newNotesDoc.replaceItemValue(addfields[i].split(":")[0], addfields[i].split(":")[1]);
				
				}
				//default data setting
				setDefaultDataSet(newNotesDoc);// 기본 데이터
				//Item data set
				setNewNoteDataSet(newNotesDoc, pack);
				//attach set
				setAttachItem(newNotesDoc, pack);
				
				//if(true) 	throw new Exception("02 발생"); //강제 에러 테스트용
				
				// ACK sample
				makeAckSample(newNotesDoc, pack);
				
				String body_type = hmCode.get("receivebodytype")!=null?hmCode.get("receivebodytype").toString():"mime";
				
				if(!"html".equals(body_type)){
	            	logger.debug("COLSE MIME body.. ");
	            	newNotesDoc.closeMIMEEntities(true, "Body");
	            }
				
				if (newNotesDoc.computeWithForm(false, false)) {
					logger.debug("Saving document ...");
					boolean succ = newNotesDoc.save(true, true);
					session.setConvertMIME(true);
					
				}else{
					logger.debug("Document not saved.. no subject");
				}			
			}catch (Exception e) {
				err_cdoe = "02"; // 노츠 문서 생성 중 에러
				logger.error("Exception makeMainNotes()\n" + e.getMessage());
				e.printStackTrace();
			} finally {
				if (null != newNotesDoc) {
					try {
						newNotesDoc.recycle();
					} catch (NotesException e) {
						e.printStackTrace();
					}
					newNotesDoc = null;
				}
				
				notesClose(db);
			}
		}
		return err_cdoe;
	}
	
	private void makeAckSample(Document newNotesDoc, PackVo pack) {
		HashMap hmPackInfo = getPackInfo(pack.getPack_xmlname());
		
		StringBuilder sbAckSample = new StringBuilder();
		sbAckSample.append("<?xml version=\"1.0\" encoding=\"EUC-KR\" ?>\n");
		sbAckSample.append("<!DOCTYPE pack SYSTEM \"pack.dtd\">\n");
		sbAckSample.append("<pack>\n");
		sbAckSample.append("<header>\n");
		sbAckSample.append("<send-orgcode>"+hmPackInfo.get("rcv_dept_code").toString()+"</send-orgcode>\n");
		sbAckSample.append("<send-id>"+hmPackInfo.get("rcv_dept_code").toString()+"</send-id>\n");
		sbAckSample.append("<send-name>"+Util.encodeBase64((hmCode.get("send_name").toString()))+"</send-name>\n");
		sbAckSample.append("<receive-id>"+hmPackInfo.get("send_dept_code").toString()+"</receive-id>\n");
		sbAckSample.append("<date>#CURTIME</date>\n");
		sbAckSample.append("<title>"+Util.encodeBase64(pack.getTitle())+"</title>\n");
		sbAckSample.append("<doc-id>"+pack.getDoc_id()+"</doc-id>\n");
		sbAckSample.append("<doc-type type=\"#ACKTYPE\" dept=\"#ACKDEPT\" name=\"#ACKNAME\"/>\n");
		sbAckSample.append("<send-gw>"+Util.encodeBase64(hmCode.get("gwfullname").toString())+"</send-gw>\n");
		sbAckSample.append("<dtd-version>2.0</dtd-version>\n");
		sbAckSample.append("<xsl-version>2.0</xsl-version>\n");
		sbAckSample.append("</header>\n");
		sbAckSample.append("<contents></contents>\n");
		sbAckSample.append("</pack>");
		
		String ackSample = sbAckSample.toString();
		String ackfilenmae = hmPackInfo.get("rcv_dept_code").toString() + hmPackInfo.get("send_dept_code").toString()+Util.getTodayCurrTime()+"00.xml";
		
		setNotesValue(newNotesDoc, "xml_acksample", ackSample, true);	
		setNotesValue(newNotesDoc, "xml_acksample_filename", hmPackInfo.get("rcv_dept_code").toString() + hmPackInfo.get("send_dept_code").toString()+"#CREATETIME#ACKCNT.xml", true);
		
		/*XmlParsing xmlParsing = new XmlParsing();
		FileHandling fh = new FileHandling();
		try {
			org.w3c.dom.Document doc = xmlParsing.stringtToJdom(sbAckSample.toString());
			
			logger.debug("path // "+Configuration.getInstance().getValue("send_temp"));
			logger.debug("ackSample filename//"+ackfilenmae);
			
			fh.domToXmlFile(Configuration.getInstance().getValue("send_temp")+"\\",ackfilenmae, doc);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		*/
	}


	private HashMap getPackInfo(String pack_xmlname) {
		HashMap hmPackInfo = new HashMap();
		hmPackInfo.put("send_dept_code", pack_xmlname.substring(0, 7));
		hmPackInfo.put("rcv_dept_code", pack_xmlname.substring(7, 14));
		hmPackInfo.put("time_stamp", pack_xmlname.substring(14, 28));
		hmPackInfo.put("no", pack_xmlname.substring(28, 30));	
		return hmPackInfo;
	}


	private void setAttachItem(Document newNotesDoc, PackVo pack) {
		String body_type = "mime";
		PubdocVo  pubdoc = (PubdocVo) pack.getContents().getPubdoc().getContent();		
		RichTextItem richITem = null;
		String doc_id = pack.getDoc_id();
		Item item = null;
		
		boolean sealHas = false;
		String xml_body_cont = "";
		try {
			richITem = newNotesDoc.createRichTextItem("xml_rec2");
			
			// 수신처 목록 ( 수신처 목록.Length > 10000 이면 richItem)
			String recipient = pubdoc.getRec();
			if(recipient.length()>10000){
				richITem.appendText(recipient);
				setNotesValue(newNotesDoc, "xml_rec_rich", "Y", true);
				
				// recipient(수신처 목록)이 500개 이하면, "xml_rec"에.. 이상이면 500개 ','를 구분자로 끊어서 500개까지는  도미노 필드 "xml_rec"에.. 이 이후 데이터 부터 "xml_rec_{idx}"에 순차적으로 넣음
				String [] recipentArr = recipient.split(",");				
				int stdCnt = 300;
				if(recipentArr.length < stdCnt){
					//setNotesValue(newNotesDoc, "xml_rec", recipient);
					logger.debug("xml_rec : " + recipient);
				} else {
					int perCnt = recipentArr.length/stdCnt;
					int idx = 0;
					String recipent_tmp="";
					for(int i=0;i<recipentArr.length;i++){
						recipent_tmp = String.join(",",recipent_tmp, recipentArr[i]);
					
						if((idx==perCnt) & (i==(recipentArr.length-1))){
							setNotesValue(newNotesDoc, "xml_rec_"+(idx+1), recipent_tmp.replaceFirst(",", ""), false);
						} else {
							if(((i+1)%stdCnt==0)){
								setNotesValue(newNotesDoc, "xml_rec_"+(idx+1), recipent_tmp.replaceFirst(",", ""), false);
								idx +=1;
								recipent_tmp="";
							}
						}
					}
				}
			}	
			    
			// 수신처 코드 목록	 
			String receive_id = String.join(";",pack.getReceive_id());
			if(receive_id.length()>500){
				richITem = newNotesDoc.createRichTextItem("xml_receive_id2");
				richITem.appendText(receive_id);
				
				setNotesValue(newNotesDoc, "xml_receive_id_rich", "Y", true);				
			}
		
			String body_content = pubdoc.getBody_content();			
			
			//org.jsoup.nodes.Document docTag= Jsoup.parse(body_content , "UTF-8");
			org.jsoup.nodes.Document docTag= Jsoup.parseBodyFragment(body_content);
			
			String tagList[] = {"p","table","tr","td"};
							
			xml_body_cont = Util.bodyContUnitConvertToPx(docTag, tagList,"px");
			
			
			//본문부 첨부
			if(body_content.length() > 25000){
				
				setNotesValue(newNotesDoc, "xml_body_rich", "Y", false);				
				richITem = newNotesDoc.createRichTextItem("xml_body");
				//richITem.appendText(body_content);
				richITem.appendText(xml_body_cont);
			}
	        
			// recipient ?? codebook > defaultrecipient 
			item = newNotesDoc.getFirstItem("recipient");
			item.setAuthors(true);
			
			if(hmCode.get("receivebodytype")!=null){
				body_type = hmCode.get("receivebodytype").toString();
			}
			
			logger.debug("@@@@@$$$$ HTML ?? " + body_type);
			
			// pubdoc > body > content data setting
			/*if("html".equals(body_type)){
				logger.debug("@@@@@   Body RichText 생성 ");
				// 1. body type이 mime일 경우  ??
		        richITem = newNotesDoc.createRichTextItem("body");
		        richITem.appendText(pubdoc.getBody_content());				
			}*/
		
			// 2. body type이 mime일 경우  ??
			boolean hasStream = false;
			Stream str = session.createStream();
			session.setConvertMIME(false); // Do not convert MIME to RT
			if(str.open("mime.tmp", "utf-8")){
				hasStream = true;
				if(str.getBytes()>0){
					str.truncate();
				}
			}
	       // str.writeText(pubdoc.getBody_content().replaceAll("></p>", ">&nbsp;</p>"));
	        
	        str.writeText(xml_body_cont);
	        logger.debug("#### body contents result body_content str >>>>>>> " + xml_body_cont);
			MIMEEntity newMime = newNotesDoc.createMIMEEntity("Body");
			MIMEEntity htmlMime = newMime.createChildEntity();
	        
			htmlMime.setContentFromText(str, "text/html; charset=\"euc-kr\"",MIMEEntity.ENC_NONE);
			htmlMime.encodeContent(MIMEEntity.ENC_BASE64);
	        
			if(hasStream){
				str.close();
			}
				        
			String attr_dir = Configuration.getInstance().getValue("attach_dir")+"\\" + Util.getToday()+"\\";
	        
			ContentVo contLogo = pack.getContents().getLogo();
			ContentVo contSeal = pack.getContents().getSeal();
			ContentVo contSymbol = pack.getContents().getSymbol();
			List<ContentVo> contSignList = pack.getContents().getSign();
			List<ContentVo> contAttach = pack.getContents().getAttach();	       
	        
			// 관인 이미지는 필수        	
			if(contSeal!=null){
				sealHas = true;
			}
	        
			if("html".equals(body_type)){
	        	
				String path = attr_dir + doc_id;
				
				if(contLogo!=null){
					UploadNotesEmbed(newNotesDoc, "EmbedObject", path + contLogo.getContent_role()+"^"+contLogo.getFilename());					
					setNotesAppendValue(newNotesDoc, "xml_imgLink", "/"+contLogo.getFilename() , true);
					logger.debug("LOGO   ::::::: >>> " + path + contLogo.getContent_role()+"^"+contLogo.getFilename());
				} 
	        	
				/*if(contSeal==null){
					logger.error("ERR >> SEAL(인장) 이미지 없음");
				}*/
				if(contSeal!=null){
					UploadNotesEmbed(newNotesDoc, "EmbedObject", path + contSeal.getContent_role()+"^"+contSeal.getFilename());
					setNotesAppendValue(newNotesDoc, "xml_imgLink", "/"+contSeal.getFilename() , true);
					logger.debug("SEAL   ::::::: >>> " + path + contSeal.getContent_role()+"^"+contSeal.getFilename());
				}
				
				if(contSymbol!=null){
					UploadNotesEmbed(newNotesDoc, "EmbedObject", path + contSymbol.getContent_role()+"^"+contSymbol.getFilename());
					setNotesAppendValue(newNotesDoc, "xml_imgLink", "/"+contSymbol.getFilename() , true);
					logger.debug("SYMBOL   ::::::: >>> " + path + contSymbol.getContent_role()+"^"+contSymbol.getFilename());
				}				

				for(int i=0;i<contSignList.size();i++){
					logger.debug("contSignList : " + path + contSignList.get(i).getContent_role()+"^"+contSignList.get(i).getFilename());
					UploadNotesEmbed(newNotesDoc, "EmbedObject", path + contSignList.get(i).getContent_role()+"^"+contSignList.get(i).getFilename());
				}
				for(int i=0;i<contAttach.size();i++){
					String fileExt = contAttach.get(i).getFilename();
					fileExt = fileExt.substring(fileExt.lastIndexOf("."), fileExt.length());
					logger.debug("contAttach : " + path + "^attach"+i+fileExt);
					//UploadNotesEmbed(newNotesDoc, "EmbedObject", path + contAttach.get(i).getContent_role()+"^"+contAttach.get(i).getFilename());
					UploadNotesEmbed(newNotesDoc, "EmbedObject", path + "^attach"+i+fileExt);
					
					String filename = contAttach.get(i).getFilename();
					
					logger.debug("@@@#@#@# FILE NAME >>> " +  path + "^attach"+i+fileExt);
					
					setNotesAppendValue(newNotesDoc, "xml_filename", contAttach.get(i).getFilename() , true);
					setNotesAppendValue(newNotesDoc, "xml_filekind", "별첨"+(i+1) , true);
					//setNotesAppendValue(newNotesDoc, "xml_fileurl", "/"+contAttach.get(i).getFilename(), true);
					setNotesAppendValue(newNotesDoc, "xml_fileurl", "/attach"+i+fileExt, true);
					setNotesAppendValue(newNotesDoc, "xml_filesize", String.valueOf(Util.getFileSize(path + "^attach"+i+fileExt)), true);
				
				}
			} else {
				
				UploadNotesMIMEFile(newMime, contLogo, str,  pack.getDoc_id());				
				UploadNotesMIMEFile(newMime, contSeal, str, pack.getDoc_id());
				UploadNotesMIMEFile(newMime, contSymbol, str, pack.getDoc_id());
				
				setNotesAppendValue(newNotesDoc, "xml_imgLink", "/"+contLogo.getFilename() , true);
				setNotesAppendValue(newNotesDoc, "xml_imgLink", "/"+contSeal.getFilename() , true);
				setNotesAppendValue(newNotesDoc, "xml_imgLink", "/"+contSymbol.getFilename() , true);
				
				for(int i=0;i<contSignList.size();i++){
					UploadNotesMIMEFile(newMime, contSignList.get(i), str,  pack.getDoc_id());
					setNotesAppendValue(newNotesDoc, "xml_imgLink", "/"+contSignList.get(i).getFilename() , true);
				}
                
				for(int i=0;i<contAttach.size();i++){
					UploadNotesMIMEFile(newMime, contAttach.get(i), str, pack.getDoc_id());
					
					String filename = contAttach.get(i).getContent_role()+"^"+contAttach.get(i).getFilename();
				
					setNotesAppendValue(newNotesDoc, "xml_filename", contAttach.get(i).getFilename() , true);
					setNotesAppendValue(newNotesDoc, "xml_filekind", "별첨"+(i+1) , true);
					setNotesAppendValue(newNotesDoc, "xml_fileurl", "/"+contAttach.get(i).getFilename(), true);
					setNotesAppendValue(newNotesDoc, "xml_filesize", String.valueOf(Util.getFileSize(filename)), true);
				}
			}
	        
			str.close();
			str.truncate();
			setNotesValue(newNotesDoc, "eddsFlag", "receive" , true);
            
		} catch (NotesException e) {
			logger.error(" 관인이미지 " + sealHas);
			logger.error("Exception makeMainNotes()\n" + e.getMessage()+" 이미지 upload 실패");
		}
	}


	private void UploadNotesMIMEFile(MIMEEntity newMime, ContentVo cont, Stream stream,  String doc_id) {
		
		String filepath = Configuration.getInstance().getValue("attach_dir")+"\\" + Util.getToday()+"\\"+ doc_id+ cont.getContent_role()+"^"+cont.getFilename();
		try {
			if(stream!=null){
				stream.close();
			}			
			logger.debug("filepath " + filepath);
			stream = session.createStream();
			
			logger.debug("fileeName > "+cont.getFilename());
			String fileName = cont.getFilename();
			//fileName = fileName.split(fileName, fileName.indexOf("\\"))[1];
			
			MIMEHeader fileHeader = newMime.createHeader("tmp");				
			fileHeader.addValText(fileName, cont.getCharset());				
			fileName = fileHeader.getHeaderVal(false);
			fileHeader.remove();			

			logger.debug("fileName " + fileName);
			MIMEEntity fileMime = newMime.createChildEntity();
			
			logger.debug("cont-type : " + cont.getContent_type());
			
			if(stream.open(filepath, "binary")){
				if(stream.getBytes() != 0) {
					//fileMime.setContentFromBytes(stream, "", MIMEEntity.ENC_NONE);
					
					MIMEHeader headers = fileMime.createHeader("Content-Type");
					//headers.setHeaderValAndParams("\""+ cont.getContent_type() + "\"; name=\"\"" + fileName + "\"");
					headers.setHeaderValAndParams(cont.getContent_type() + "\"; name=\"\"" + fileName + "\"");
					
					headers = fileMime.createHeader("Content-Disposition");
					headers.setHeaderValAndParams("attachment; filename=\"" + fileName + "\"\"");
					fileMime.encodeContent(MIMEEntity.ENC_IDENTITY_BINARY);
					fileMime.setContentFromBytes(stream, cont.getContent_type(), MIMEEntity.ENC_IDENTITY_BINARY);
					logger.debug("upload 완료!!!");
					stream.close();
					logger.debug("stream close!!!");
				}
				stream.close();
			}
		} catch (NotesException e) {
			e.printStackTrace();
		} 
	}


	//문서에  파일을 EmbedObject 형식으로 추가
	private void UploadNotesEmbed(Document newNotesDoc, String fieldName, String filename) {
		Item attachItem;
		try {
			//embedObject 필드 유무 확인
			if(newNotesDoc.hasItem(fieldName)){
				attachItem = newNotesDoc.getFirstItem(fieldName);
			}else{
				attachItem = newNotesDoc.createRichTextItem(fieldName);
			}
			
			logger.debug(" 원본 파일이름" + filename);
			String target = Configuration.getInstance().getValue("attach_dir")+"\\"+ Util.getToday()+"\\"+filename.split("\\^")[1];
			Util.copyFile(filename, target);
			
			File f = new File(target);		
			
			logger.debug("full filename ::: "+filename);
			logger.debug("target ::: "+target);
			logger.debug("filename ::: "+f.getName());
			
			String filePath = f.getPath().replaceAll("\\\\", "/");
			logger.debug(" filePath ::: " + filePath);
			
			//((RichTextItem) attachItem).embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", target, filename.split("\\^")[1]);
			((RichTextItem) attachItem).embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", filePath, f.getName());
			
			Util.delFile(target);
			

		} catch (NotesException e) {
			e.printStackTrace();
		}
	}
	

	private void setDefaultDataSet(Document newNotesDoc) {
		// 기본데이터
		
		Item item = null;
		try {
			String mgrGroup [] = {"[sysadmin]", "[appadmin]"};
			newNotesDoc.replaceItemValue("DefaultEditors", mgrGroup);
			newNotesDoc.replaceItemValue("DefaultReaders", mgrGroup);			
			item = newNotesDoc.getFirstItem("DefaultEditors"); //기본독서자
			item.setReaders(true);			
			item = newNotesDoc.getFirstItem("DefaultReaders"); //기본독서자
			item.setAuthors(true);
			
			setNotesValue(newNotesDoc, "saveoptions", "1", true);
			
			setNotesValue(newNotesDoc, "recipient", hmCode.get("defaultrecipient").toString(), true); // 기본 접수 담당자
			item = newNotesDoc.getFirstItem("recipient");
			item.setAuthors(true);
			
		} catch (NotesException e) {
			e.printStackTrace();
		}
	}


	private void setNewNoteDataSet(Document newNotesDoc, PackVo pack) {
		org.jsoup.nodes.Document doc = null;
		Element element = null;
		//setNotesValue(newNotesDoc, "form", "form08", true); // 폼
		
		// pack Data
		setNotesValue(newNotesDoc, "subject", pack.getTitle(), true);
		setNotesValue(newNotesDoc, "Xml_send_orgcode", pack.getSend_orgcode(), true);
		setNotesValue(newNotesDoc, "Xml_send_id", pack.getSend_id(), true);
		setNotesValue(newNotesDoc, "Xml_send_name", pack.getSend_name(), true);
		
		String [] receive_id = pack.getReceive_id();
		String receive_id_tmp = "";
		if(receive_id.length > 500){
			for(int i=0;i<500;i++){
				receive_id_tmp = String.join("", receive_id_tmp,  receive_id[i]);
			}
			receive_id_tmp = receive_id_tmp.replaceFirst(";", "");
		} else {
			receive_id_tmp = String.join(";",receive_id);
		}	
		setNotesValue(newNotesDoc, "Xml_receive_id", receive_id_tmp, true); // ??? vb 소스에 주석처리...(receive_id는 사용하는 곳이 없으므로 500개까지 저장하도록 함) ???
		
		//setNotesValue(newNotesDoc, "Xml_date", hmData.get("date").toString());	// ?? notes 문서에 항목 없음
		setNotesValue(newNotesDoc, "Xml_title", pack.getTitle(), true);
		setNotesValue(newNotesDoc, "Xml_doc_id", pack.getDoc_id(), true);
		//setNotesValue(newNotesDoc, "Xml_send_gw", pack.getSend_gw(), true);	// ?? notes 문서에 항목 없음
		setNotesValue(newNotesDoc, "Xml_dtd_version", pack.getDtd_version(), true);
		setNotesValue(newNotesDoc, "Xml_xsl_version", pack.getXsl_version(), true);
		
		// pubdoc Data		
		PubdocVo  pubdoc = (PubdocVo) pack.getContents().getPubdoc().getContent();
		setNotesValue(newNotesDoc, "Xml_footcampaign", pubdoc.getFootcampaign(), true);
		setNotesValue(newNotesDoc, "Xml_headcampaign", pubdoc.getHeadcampaign(), true);
		setNotesValue(newNotesDoc, "xml_zipcode", pubdoc.getZipcode(), true);
		setNotesValue(newNotesDoc, "Xml_address", pubdoc.getAddress(), true);
		setNotesValue(newNotesDoc, "Xml_homeurl", pubdoc.getHomeurl(), true);
		setNotesValue(newNotesDoc, "Xml_telephone", pubdoc.getTelephone(), true);
		setNotesValue(newNotesDoc, "Xml_fax", pubdoc.getFax(), true);
		setNotesValue(newNotesDoc, "Xml_email", pubdoc.getEmail(), true);
		setNotesValue(newNotesDoc, "Xml_publication", pubdoc.getPublication(), true);
		setNotesValue(newNotesDoc, "Xml_publication_code", String.join(";", pubdoc.getPublication_code()), true);
		
		String symbol_img = pubdoc.getSymbol();
		String symbol_filename="";
		String symbol_height="";
		String symbol_width="";
		
		if(!"".equals(symbol_img)){
			element = Jsoup.parse(symbol_img).select("img").first();			
			
			String rst[] = Util.imgUnitConvert(element, Configuration.getInstance().getValue("symbol_img_width"), Configuration.getInstance().getValue("symbol_img_height"));
			
			String src = element.attr("src");
			
			element.attr("src","/"+src).attr("width",rst[0]).attr("height",rst[1]);
			symbol_img = element.toString();			
		}
		
		setNotesValue(newNotesDoc, "Xml_symbol", symbol_img, true);
		setNotesValue(newNotesDoc, "Xml_symbol_filename", symbol_filename, true);
		setNotesValue(newNotesDoc, "Xml_symbol_height", symbol_height, true);
		setNotesValue(newNotesDoc, "Xml_symbol_width", symbol_width, true);
		
		String logo_img = pubdoc.getLogo();
		String logo_filename="";
		String logo_height="";
		String logo_width="";
		
		if(!"".equals(logo_img)){
			element = Jsoup.parse(logo_img).select("img").first();
			String rst[] = Util.imgUnitConvert(element, Configuration.getInstance().getValue("logo_img_width"), Configuration.getInstance().getValue("logo_img_height"));
			String src = element.attr("src");
			
			element.attr("src","/"+src).attr("width",rst[0]).attr("height",rst[1]);
			logo_img = element.toString();
			
		}
		
		setNotesValue(newNotesDoc, "Xml_logo", logo_img, true);
		setNotesValue(newNotesDoc, "Xml_logo_filename", logo_filename, true);
		setNotesValue(newNotesDoc, "Xml_logo_height", logo_height, true);
		setNotesValue(newNotesDoc, "Xml_logo_width", logo_width, true);
		
		String regnumber = pubdoc.getRegnumber();
		setNotesValue(newNotesDoc, "Xml_regnumber", regnumber, true);
		String serial = "";
		if(regnumber.indexOf("-")>-1){
			setNotesValue(newNotesDoc, "xml_senddept", regnumber.split("-")[0], true);
			serial = regnumber.split("-")[1];
		}
		setNotesValue(newNotesDoc, "xml_serial", serial, true);
		
		String regnumber_cd = pubdoc.getRegnumber_code();
		setNotesValue(newNotesDoc, "Xml_regnumber_code", regnumber_cd, true);
		
		if(regnumber_cd.length()<7){
			logger.debug("[regnumbercode] length is not enough : " + regnumber_cd);
			setNotesValue(newNotesDoc, "Xml_deptCode",regnumber_cd, true);
		}else{
			setNotesValue(newNotesDoc, "Xml_deptCode",regnumber_cd.substring(0, 7), true);
		}
				
		
		String enforcedate = pubdoc.getEnforcedate();
		logger.debug("$$$##### enforcedate 변환 전 : " + enforcedate);
		if(!"".equals(enforcedate.trim())){
			enforcedate = enforcedate.replaceAll("-", ".");
		}
		
		logger.debug("$$$##### enforcedate 변환 후 : " + enforcedate);
		
		setNotesValue(newNotesDoc, "Xml_enforcedate", enforcedate, true);
		
		String seal = pubdoc.getSeal_omit();
		
		String seal_img = pubdoc.getSeal_img();
		String seal_filename="";
		String seal_height="";
		String seal_width="";
		
		if("false".equals(seal)){
			element = Jsoup.parse(seal_img).select("img").first();
			
			if("".equals(element.attr("width"))||"mm".equals(element.attr("width"))||"px".equals(element.attr("width"))){
				logger.debug("seal width : " + element.attr("width"));
				logger.debug("seal img width 사이즈 지정 안됨. 기본 100px 로 조정 하여 수신 처리....  ");
				element.attr("width","");
			}
			
			if("".equals(element.attr("height"))||"mm".equals(element.attr("height"))||"px".equals(element.attr("height"))){
				logger.debug("seal height : " + element.attr("height"));
				logger.debug("seal img height 사이즈 지정 안됨. 기본 100px 로 조정 하여 수신 처리....  ");
				element.attr("height","");
			}
			
			String rst[] = Util.imgUnitConvert(element, Configuration.getInstance().getValue("seal_img_width"), Configuration.getInstance().getValue("seal_img_height"));
			String src = element.attr("src");
			
			element.attr("src","/"+src).attr("width",rst[0]).attr("height",rst[1]);
			seal_img = element.toString();
		} 
		
		setNotesValue(newNotesDoc, "xml_seal_img", seal_img, true);
		setNotesValue(newNotesDoc, "xml_seal_filename", seal_filename, true);
		setNotesValue(newNotesDoc, "xml_seal_height", seal_height, true);
		setNotesValue(newNotesDoc, "xml_seal_width", seal_width.toString(), true);
		setNotesValue(newNotesDoc, "xml_seal_omit", seal, true);		
		
		setNotesValue(newNotesDoc, "Xml_title", pubdoc.getBody_title(), true);		
		
		//pubdoc > body(body_separate=true) 이면 본문부 붙임 파일
		if("true".equals(pubdoc.getBody_seperate())){
			List<ContentVo> alcontsVo = pack.getContents().getAttach_body();
			
			if(alcontsVo.size()==0){
				pubdoc.setBody_seperate("false");
			} else {
				setNotesValue(newNotesDoc, "xml_file_body1", "0", true); //첨부용 임시
				setNotesValue(newNotesDoc, "xml_is_attachReq", "y", true); //첨부용 임시			
				
				for(int i=0;i<alcontsVo.size();i++){					
					setNotesAppendValue(newNotesDoc, "xml_file1", "본문부첨부", true); //첨부용 임시
					setNotesAppendValue(newNotesDoc, "xml_filename", alcontsVo.get(i).getFilename() , true); //첨부용 임시
				}	
			}
		}
		
		Item item =null; 
		String body_content = pubdoc.getBody_content();
		
		if(body_content.length() <= 25000){
			
			//logger.debug(" 단위변환 하기  시작 >> ");
			org.jsoup.nodes.Document docTag= Jsoup.parse(body_content , "UTF-8");
			
			String tagList[] = {"p","table","tr","td"};
			String rst = Util.bodyContUnitConvertToPx(docTag, tagList, "px");			
			
			//logger.debug(" 단위변환 하기  끝 >> " +rst );
			setNotesValue(newNotesDoc, "xml_body", rst, true);
			
			//setNotesValue(newNotesDoc, "xml_body", body_content, true);
		}		
		
		//head
		String recipient = pubdoc.getRec();
		if(recipient.length()<=10000){
			setNotesValue(newNotesDoc, "xml_rec", recipient, true);
		}
		setNotesValue(newNotesDoc, "Xml_via", pubdoc.getVia(), true);		
		setNotesValue(newNotesDoc, "Xml_enforcedate", pubdoc.getEnforcedate(), true);		
		setNotesValue(newNotesDoc, "Xml_refer_1", pubdoc.getReceipient_refer(), true);
		setNotesValue(newNotesDoc, "xml_organ", pubdoc.getOrgan(), true);
		
		//결재자		
		ArrayList alApproval = (ArrayList) pubdoc.getApproval();//(ArrayList) hmData.get("approval");
		int size = alApproval.size();
		String [] approval_date = new String[size];
		String [] approval_type = new String[size];
		String [] approval_order = new String[size];
		String [] approval_signposition = new String[size];
		String [] approval_name = new String[size];
		String [] approval_time = new String[size];		
		String [] approval_sign = new String[size];
		
		for(int i=0;i<alApproval.size();i++){
			HashMap hmAppr = (HashMap) alApproval.get(i);
			
			approval_order[i] = hmAppr.get("order").toString();
			approval_date[i] = hmAppr.get("date").toString();
			approval_type[i] = hmAppr.get("type").toString();
			approval_signposition[i] = hmAppr.get("signposition").toString();
			approval_name[i] = hmAppr.get("name").toString();
			approval_time[i] = hmAppr.get("time").toString();
			
			String singImg = hmAppr.get("signimage").toString();
			
			if("".equals(singImg)){
				approval_sign[i] = hmAppr.get("name").toString();
			}else {
				approval_sign[i] = "<img src=/"+singImg+"' height='"+Configuration.getInstance().getValue("sign_img_height")+"' width='"+Configuration.getInstance().getValue("sign_img_width")+"' />";
			}
			
			
		}
		
		// 결재자 정보 세팅
		//setNotesValue(newNotesDoc, "Xml_signimage", hmData.get("signimage").toString());		
		setNotesValue(newNotesDoc, "Xml_approval_date", Util.arrayToString(approval_date, ";"), true);
		setNotesValue(newNotesDoc, "Xml_approval_name", Util.arrayToString(approval_name, ";"), true);
		setNotesValue(newNotesDoc, "Xml_approval_signposition", Util.arrayToString(approval_signposition, ";"), true);
		/*setNotesValue(newNotesDoc, "Xml_approval_time", Util.arrayToString(approval_time, ";"), true);*/
		setNotesValue(newNotesDoc, "Xml_approval_type", Util.arrayToString(approval_type, ";"), true);
		setNotesValue(newNotesDoc, "Xml_approval_sign", Util.arrayToString(approval_sign, ";"), true);
		
		
		// 협조자
		ArrayList alAssit = (ArrayList) pubdoc.getAssist();
		size = alAssit.size();
		
		String assist_date[] = new String[size];
		String assist_type[] = new String[size];
		String assist_signposition [] = new String[size];
		String assist_name[] = new String[size];
		String assist_time[] = new String[size];
		String assist_sign[] = new String[size];
		
		for(int i=0;i<alAssit.size();i++){
			HashMap hmAssist = (HashMap) alAssit.get(i);
			assist_date[i] = hmAssist.get("date").toString();
			assist_type[i] = hmAssist.get("type").toString();
			assist_signposition[i] = hmAssist.get("signposition").toString();
			assist_name[i] = hmAssist.get("name").toString();
			assist_time[i] = hmAssist.get("time").toString();
			
			String singImg = hmAssist.get("signimage").toString();
			
			if("".equals(singImg)){
				assist_sign[i] = hmAssist.get("name").toString();
			}else {
				assist_sign[i] = "<img src=/"+singImg+"' height='"+Configuration.getInstance().getValue("sign_img_height")+"' width='"+Configuration.getInstance().getValue("sign_img_width")+"' />";
			}
		}
		
		setNotesValue(newNotesDoc, "Xml_assist_date", Util.arrayToString(assist_date, ";"), true);
		setNotesValue(newNotesDoc, "Xml_assist_name", Util.arrayToString(assist_name, ";"), true);
		setNotesValue(newNotesDoc, "Xml_assist_signposition", Util.arrayToString(assist_signposition, ";"), true);
		setNotesValue(newNotesDoc, "Xml_assist_time", Util.arrayToString(assist_time, ";"), true);
		setNotesValue(newNotesDoc, "Xml_assist_type", Util.arrayToString(assist_type, ";"), true);
		setNotesValue(newNotesDoc, "Xml_assist_sign", Util.arrayToString(assist_sign, ";"), true);
		
		//??? recipient?? <recipient refer="true"><rec>...</rec></recipient>
		
		setNotesValue(newNotesDoc, "recipient", hmCode.get("defaultrecipient").toString(), true);
		
		//???? 노츠에도 값 없음... 		
		setNotesValue(newNotesDoc, "xml_original_filename", "", true);
	}
	

	/**
	 * Notes Close
	 */
	public void notesClose(Database db) {
		try {
			if (db != null) {
				db.recycle();
				db = null;
			}
			if (session != null) {
				session.recycle();
				session = null;
			}

		} catch (NotesException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Notes Document에 있는 field에 값을 넣는다.
	 */
	public void setNotesValue(Document notes, String fieldName, String value, boolean summary) {
		try {
			if (!"".equals(value) && null != value) {
				Item item = null;
				item = notes.replaceItemValue(fieldName, null);
				item.setValueString(value);
				item.setSummary(summary);
				
				//logger.debug(fieldName+"/ value: "+value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public void setNotesAppendValue(Document notes, String fieldName, String value, boolean summary) {
		Item item = null;
		try {
			item = notes.getFirstItem(fieldName);
			if (item==null ) {
				item = notes.replaceItemValue(fieldName, null);
				item.setValueString(value);
				item.setSummary(summary);
			} else {
				item.appendToTextList(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}


	public String updateDocStatus(PackVo pack) {
		Database db = null;
		Document send_doc = null;
		View send_view = null;
		
		String dbName = hmCode.get("database_path_send").toString();
		String lastrun = hmCode.get("eddslastrun")==null?"":hmCode.get("eddslastrun").toString();
		
		String state_rst = "F";
		
		if(!"".equals(lastrun)){ //[kotra]
			logger.debug("lastrun 설정 되어있음.. ");
			state_rst = updateDocStatusByAgent(pack);
			
		} else {
			db = notesConnectionDb(dbName);
			String view = hmCode.get("viewname_send")==null?"ByDocID":hmCode.get("viewname_send").toString();
			
			if("".equals(view)){
				view = "ByDocID";
			}
			
			try {
				send_view = db.getView(view);
				
				logger.debug("view : " + view);
				logger.debug("send_view" + send_view);
				logger.debug("doc_id :" + pack.getDoc_id());
				
				send_doc = send_view.getDocumentByKey(pack.getDoc_id(), true);
				
				logger.debug("send_doc" + send_doc);
				
				logger.debug("items " + send_doc.getItems());
				String doc_type = pack.getDoc_type_type();
				
				logger.debug("eddsStatus " + send_doc.getItemValue("eddsStatus"));
				
				String state = send_doc.getItemValueString("eddsStatus");
				
				
				if("arrive".equals(doc_type)){
					send_doc.replaceItemValue("eddsStatus", state + " 도달");
				} else if("receive".equals(doc_type)){
					send_doc.replaceItemValue("eddsStatus", state + " 수신");
				} else if("accept".equals(doc_type)){
					String [] date = pack.getDate().split(" ");			
					if(date.length <2){
						logger.debug("date 형식 맞지 않음");
						date[1] = "";
					}
					send_doc.replaceItemValue("receiptdate", date[0]);
					send_doc.replaceItemValue("receipttime", date[1]);
					send_doc.replaceItemValue("eddsStatus", state + " 접수");
					
				} else if("return".equals(doc_type)){
					send_doc.replaceItemValue("eddsStatus", "반송");
					String msg = pack.getContents_msg();
					if(!"".equals(msg)){
						send_doc.replaceItemValue("Err_Message", Util.encodeBase64(msg));
					}
				} else if("req-resend".equals(doc_type)){
					send_doc.replaceItemValue("eddsStatus", "접수실패");
					String msg = pack.getContents_msg();
					if(!"".equals(msg)){
						send_doc.replaceItemValue("Err_Message", Util.encodeBase64(msg));
					}			
					send_doc.replaceItemValue("tmp_Status", "fail");
				} else if("fail".equals(doc_type)){
					send_doc.replaceItemValue("eddsStatus", "전송실패");
					String msg = pack.getContents_msg();
					if(!"".equals(msg)){
						send_doc.replaceItemValue("Err_Message", Util.encodeBase64(msg));
					}
					
					send_doc.replaceItemValue("tmp_Status", "fail");
				}
				
				if (send_doc.computeWithForm(false, false)) {
					System.out.println("Update Saving document ...");
					boolean succ = send_doc.save(true, true);
					logger.debug("Document success >" + succ);
				}else{
					logger.debug("Document not saved.. no subject");
				}
				
				logger.debug("DOC_Status  :: " + send_doc.getItemValueString("eddsStatus"));
				
				state_rst = "S";
			} catch (NotesException e) {
				logger.error("Exception makeMainNotes()\n" + e.getMessage());
				e.printStackTrace();
			} finally {
				if (null != send_doc) {
					try {
						send_doc.recycle();
					} catch (NotesException e) {
						e.printStackTrace();
					}
					send_doc = null;
				}
				
				notesClose(db);
			}
		}
		
		return state_rst;
		
	}

	public String updateDocStatusByAgent(PackVo pack) {
		Database db = null;
		Document send_doc = null;
		View send_view = null;
		String dbName = hmCode.get("database_path_edds").toString();
		String state_rst = "F";
		db = notesConnectionDb(dbName);
		
		try {
			send_doc = db.createDocument();
			
			setDefaultDataSet(send_doc);		
			
			send_doc.replaceItemValue("form", "form02");
			send_doc.replaceItemValue("xml_send_orgcode", pack.getSend_orgcode());
			logger.debug("1.  >>> " + pack.getSend_orgcode());
			
			send_doc.replaceItemValue("xml_send_id", pack.getSend_id());			
			logger.debug("2.  >>> " + pack.getSend_id());			
			
			send_doc.replaceItemValue("xml_send_name", pack.getSend_name());
			logger.debug("3.  >>> " + pack.getSend_name());		
			
			send_doc.replaceItemValue("xml_receive_id", pack.getReceive_id());
			logger.debug("4.  >>> " +  pack.getReceive_id()[0]);
			
			send_doc.replaceItemValue("xml_date", pack.getDate());
			logger.debug("5.  >>> " +  pack.getDate());
			
			send_doc.replaceItemValue("xml_title", pack.getTitle());
			logger.debug("6.  >>> " +  pack.getTitle());			
			
			send_doc.replaceItemValue("subject", pack.getTitle());
			logger.debug("7.  >>> " +  pack.getTitle());
			
			send_doc.replaceItemValue("xml_doc_id", pack.getDoc_id());
			logger.debug("8.  >>> " +  pack.getDoc_id());
			
			String doc_type = pack.getDoc_type_type();
			send_doc.replaceItemValue("xml_doc_type_type", doc_type);
			logger.debug("9.  >>> " +  doc_type);
			
			send_doc.replaceItemValue("xml_doc_type_dept", pack.getDoc_type_dept());
			logger.debug("10.  >>> " +  pack.getDoc_type_dept());
			send_doc.replaceItemValue("xml_doc_type_name", pack.getDoc_type_name());
			logger.debug("11.  >>> " +  pack.getDoc_type_name());
			
			send_doc.replaceItemValue("xml_gwfullname", pack.getSend_gw());
			logger.debug("12.  >>> " +  pack.getSend_gw());
			
			send_doc.replaceItemValue("xml_dtd_version", pack.getDtd_version());
			logger.debug("13.  >>> " +  pack.getDtd_version());
			
			send_doc.replaceItemValue("xml_xsl_version", pack.getXsl_version());
			logger.debug("14.  >>> " +  pack.getXsl_version());
			
			if("fail".equals(doc_type)){
				send_doc.replaceItemValue("xml_body", pack.getContents_msg());
				logger.debug("15.  >>> " +  pack.getContents_msg());
				
			}
			
			send_doc.replaceItemValue("eddsFlag", "receive");
			logger.debug("eddsFlag.  >>> receive");
			
			send_doc.save(true, false);
			logger.debug("16.  >>> " +  pack.getXsl_version());
			
			state_rst = "S";
			logger.debug("result  >>> " + state_rst);

		} catch (NotesException e) {
			logger.error(" NotesException" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != send_doc) {
				try {
					send_doc.recycle();
				} catch (NotesException e) {
					e.printStackTrace();
				}
				send_doc = null;
			}
			notesClose(db);
		}
		return state_rst;
	}

	public ArrayList sendDocument() {
		Database db = null;
		Document send_doc = null;				
		View send_view = null;
		ArrayList alSendList = new ArrayList();
		
		if(hmCode.isEmpty()){
			getCodeValue(dbname);
		}
		
		String dbName = hmCode.get("database_path_edds").toString();
		
		logger.debug("발송 처리 대기 ...");
		try {
			db = notesConnectionDb(dbName);
			send_view = db.getView(hmCode.get("viewname_edds").toString());
			
			ViewEntryCollection entry = send_view.getAllEntries();
			
			send_view.setAutoUpdate(false);
			
			for(int i=1; i<entry.getCount();i++){
				//logger.debug("entry >> " + entry.getNextEntry().getDocument().getItems());
				send_doc = entry.getNthEntry(i).getDocument();
				
				String edds_flag = send_doc.getItemValueString("eddsflag");				
				String sendXml = "";
				
				if("send".equals(edds_flag)){
					logger.debug("edds_flag"+ edds_flag);
					
					logger.info("[====   문서 발송 시작     ==================]");
					
					String doc_type = send_doc.getItemValue("xml_doc_type_type")==null?"":send_doc.getItemValue("xml_doc_type_type").get(0).toString();					
					logger.debug("doc_type : [" + doc_type+"]");
					logger.info("	> subject   : "+ send_doc.getItemValueString("subject"));
					logger.info("	> doc_id   : "+ send_doc.getItemValueString("xml_doc_id"));
					logger.info("	> xml_title   : "+ send_doc.getItemValueString("xml_title"));
					logger.info("	> docnumber   : "+ send_doc.getItemValueString("docnumber"));
					
					PackVo packVo = new PackVo();
					
					if(!("send".equals(doc_type) | "resend".equals(doc_type))){ 
						logger.info("	> doc_type : "+ doc_type);
						
						// pack 세팅
						packVo = packDocument(send_doc);
						if(checkPackVo(packVo)){
							//ack 파일 생성 
							sendXml = generateAckFromSample(send_doc, doc_type);
							if("".equals(sendXml)){
								send_doc.replaceItemValue("eddsFlag","sendError");
								send_doc.replaceItemValue("eddsmsg",doc_type + "ACK XML 생성 오류.");
								send_doc.replaceItemValue("eddsActTime", Util.getDocFormatCurDateTime());
								
								send_doc.save(true, false);
								logger.error("	ACK XML 생성 오류");
								//continue;
							} else {
								send_doc.replaceItemValue("eddsFlag", "complete");
								send_doc.replaceItemValue("eddsActTime", Util.getDocFormatCurDateTime());
								send_doc.save(true, false);
							}							
						} else {
							send_doc.replaceItemValue("eddsFlag","sendError");
							send_doc.replaceItemValue("eddsmsg",doc_type + "ACK XML 생성 오류.");
							send_doc.replaceItemValue("eddsActTime", Util.getDocFormatCurDateTime());
							
							send_doc.save(true, false);
                            logger.error("Pack 파싱 오류");
						}						
												
					} else {						
						//Doc Size Check
						if(send_doc.getSize()/ 1024 / 1024 > 8){
							logger.info("	문서 사이즈 너무 큼  : " + send_doc.getSize()/ 1024 +"Kb");
							send_doc.replaceItemValue("eddsFlag", "sendError");
							send_doc.replaceItemValue("eddsmsg", "문서 사이즈가 커서 전송 오류가 발생할 가능성이 있음. 문서 사이즈 : " +  send_doc.getSize()/ 1024 +"Kb");
							send_doc.replaceItemValue("eddsActTime", Util.getDocFormatCurDateTime());
							send_doc.save(true, false);	
						} else {
							sendXml = generateXml(send_doc);   // sendXml => xmlfile 명 
							if(!"".equals(sendXml)){
								send_doc.replaceItemValue("eddsFlag", "complete");
								send_doc.replaceItemValue("eddsActTime", Util.getDocFormatCurDateTime());
								send_doc.save(true, false);
							} else {
								logger.debug("xml 생성 실패");
								
								send_doc.replaceItemValue("eddsFlag","sendError");
								send_doc.replaceItemValue("eddsmsg", "XML 생성 오류.");
								send_doc.replaceItemValue("eddsActTime", Util.getDocFormatCurDateTime());
								send_doc.save(true, false);
							}
						}
					}
					
					// db에 로그 쌓기 위해 데이터 set
					if(!"".equals(sendXml)){
						HashMap hmInput = new HashMap();						
						String state = "complete".equals(send_doc.getItemValue("eddsFlag").get(0))?"S":"F";
						
						logger.debug ("send Result state ::: " + state);
						
						hmInput.put("doc_id", send_doc.getItemValue("xml_doc_id").get(0));
						hmInput.put("doc_type", doc_type);
						hmInput.put("title", send_doc.getItemValue("subject").get(0));
						hmInput.put("state", state);
						hmInput.put("file_id", sendXml);

						hmInput.put("send_id", hmCode.get("send_orgcode").toString());
						hmInput.put("send_name", hmCode.get("send_name").toString());
						
						alSendList.add(hmInput);
						logger.info("	> doc_id : " + send_doc.getItemValue("xml_doc_id").get(0));
						logger.info("	> doc_type : " + doc_type);
						logger.info("	> title : " + send_doc.getItemValue("subject").get(0));
						logger.info("	> file_id : " + sendXml);						
						logger.info("[====   문서 발송  완료     ==================]");
					}					
				}
			}
		} catch (NotesException e) {
			e.printStackTrace();
		} finally {
			if (null != send_doc) {
				try {
					send_doc.recycle();
				} catch (NotesException e) {
					e.printStackTrace();
				}
				send_doc = null;
			}
			
			notesClose(db);
		}
		return alSendList;
	}

	private boolean checkPackVo(PackVo packVo) {
		boolean rst = true;
		// pack data 검증
		logger.debug("filename : " + packVo.getPack_filename());
		
		if("".equals(packVo.getPack_filename()) | null == packVo.getPack_filename()){
			rst = false;
			logger.error("pack > filename : 전송용 통합 파일의 파일명  빈값");
		}
		
		if("".equals(packVo.getDoc_type_type()) | null == packVo.getDoc_type_type()){
			rst = false;
			logger.error("pack > doc-type : 문서 종류  빈값");
		}
		
		if("".equals(packVo.getDoc_type_dept()) | null == packVo.getDoc_type_dept()){
			rst = false;
			logger.error("pack > doc_type : 발송자 및 접수자의 부서명  빈값");
		}
		
		if("".equals(packVo.getDoc_type_name()) | null == packVo.getDoc_type_name()){
			rst = false;
			logger.error("pack > doc_type : 발송자 및 접수자의  성명  빈값");
		}
		
		return true;
	}


	private String generateXml(Document send_doc) {
		String xmlFilename = "";
		// pack 세팅 
		PackVo packVo = new PackVo();
		
		logger.debug(" GENERATE XML ::::  ");
		packVo = packDocument(send_doc);
		
		// 일련번호는 db 에서 조회 하는 걸로... 
		String xmlfilename = packVo.getSend_orgcode() + packVo.getReceive_id()[0] + Util.getTodayCurrTime() + "00" + ".xml";		
		packVo.setPack_filename(xmlfilename);
		
		logger.info("	xml 문서 생성 시작  : ["+xmlfilename+"]");
		
		ContentsVo contsVo = new ContentsVo();		
		
		//if(checkPackVo(packVo)){
			PubdocVo pubdocVo = new PubdocVo();			
			pubdocVo= pubdocDocument(send_doc);		
			XmlParsing xmlParse = new XmlParsing();
			//if(checkPubdocVo){
				// contenVo pubdoc 데이터 세팅
				ContentVo pubdocContVo = new ContentVo();
				pubdocContVo.setContent(pubdocVo);
				pubdocContVo.setContent_role("pubdoc");
				pubdocContVo.setContent_transfer_encoding("base64");
				pubdocContVo.setContent_type("text/xml");
				pubdocContVo.setCharset("euc-kr");
				pubdocContVo.setFilename(Util.encodeBase64("pubdoc.xml"));
				
				//pubdoc 조합
				String pubdoc = xmlParse.unionPubdocXml(pubdocContVo, packVo.getReceive_id()); // 수신처 정보
				pubdocContVo.setContent(Util.encodeBase64(pubdoc));
				
				contsVo.setPubdoc(pubdocContVo);
				
				//XmlAttach
				ContentsVo attachConts = attachDocument(send_doc);
			
				attachConts.setPubdoc(pubdocContVo);
				packVo.setContents(attachConts);
				
				String xmlErr = "";
				org.w3c.dom.Document packDom = null;
				FileHandling fhdl = new FileHandling();
				
				try {
					//pubdoc dtd validation 체크
					xmlErr = xmlParse.validateWithDTDUsingDOM(pubdoc);
					logger.debug(" pubdoc validateion check : " + xmlErr);
					if("".equals(xmlErr)){
						//pack 조합						
					    String pack = xmlParse.unionPackXml(packVo);
					    xmlErr = xmlParse.validateWithDTDUsingDOM(pack);
					    if("".equals(xmlErr)){					    	
							packDom = xmlParse.stringtToJdom(pack);
							fhdl.domToXmlFile(Configuration.getInstance().getValue("send_temp")+"\\", xmlfilename , packDom);
							logger.info("	xml 문서 생성 완료 : ["+xmlfilename+"]");
							xmlFilename = xmlfilename;
						}
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JDOMException e) {
					e.printStackTrace();
				}
		//	}
			logger.info("	pack 생성 완료");
		/*} else {
			logger.info("	pack 생성중 오륲 ");
		}*/
		
		return xmlFilename;
	}


	private ContentsVo attachDocument(Document send_doc) {
		ContentsVo contsVo = new ContentsVo();		
				
		String filename = "";
		String fileurl = "";
		String targetPath = Configuration.getInstance().getValue("attach_dir")+ "\\" + Util.getToday(); // 첨부 파일 저장 path
		
		ArrayList alNotesImg = new ArrayList();
		
		String notes_imgpath = hmCode.get("notes_imgpath")==null? "" : hmCode.get("notes_imgpath").toString();
		
		org.jsoup.nodes.Document doc = null;
		try {
			String doc_id = send_doc.getItemValueString("xml_doc_id");
			
			//seal	
			String seal_omit = send_doc.getItemValueString("xml_seal_omit");
			if("false".equals(seal_omit)){
				doc = Jsoup.parse(send_doc.getItemValueString("xml_seal_img"), "UTF-8");
				
				filename = doc.select("img").attr("src");
				/*if(filename.contains("/")){
					filename = filename.substring(filename.lastIndexOf("/")+1,filename.length()); 
				}*/
				fileurl = Configuration.getInstance().getValue("http").replace("http://", "") + "/" + filename;
				String seal_img =  "<img alt=\"관인이미지\" src=\"" + send_doc.getItemValueString("xml_seal_filename")
                        +" width=\"" + send_doc.getItemValueString("xml_seal_width")+ "mm\""
                        +" height=\"" + send_doc.getItemValueString("xml_seal_height")+"mm\"/>";
				
				send_doc.replaceItemValue("xml_seal_img", seal_img);
				logger.debug("SEND attachDocument filename" + filename);
				logger.debug("SEND attachDocument targetPath" + targetPath);
				logger.debug("SEND attachDocument http " + Configuration.getInstance().getValue("http"));
				
				Util.attachFileDownload(Configuration.getInstance().getValue("http"), filename, targetPath);
				
				HashMap hmSeal = new HashMap();
				
				logger.debug("seal file name >>> " + filename.substring(filename.lastIndexOf("/")+1, filename.length()));
				
				hmSeal.put("fileName", filename.substring(filename.lastIndexOf("/")+1, filename.length()));
				hmSeal.put("fileType", "seal");
				
				alNotesImg.add(hmSeal);
				
			}
			
			// logo
			String logo = send_doc.getItemValueString("xml_logo");
			if(!"".equals(logo)){
				doc = Jsoup.parse(logo, "UTF-8");
				filename = doc.select("img").attr("src");
				
				/*if(filename.contains("/")){
					filename = filename.substring(filename.lastIndexOf("/"),filename.length()+1); 
				}*/
				
				fileurl = Configuration.getInstance().getValue("http").replace("http://", "") + "/" + filename;
				String logo_img = "<img alt=\"로고이미지\" src=\"/"+ send_doc.getItemValueString("xml_logo_filename")+"\""
	                    + " width=\"" + send_doc.getItemValueString("xml_logo_width")+ "mm\""
	                    + " height=\"" + send_doc.getItemValueString("xml_logo_height")+"mm\" />";
				
				send_doc.replaceItemValue("xml_logo", logo_img);
				
				Util.attachFileDownload(Configuration.getInstance().getValue("http"), filename, targetPath);			
	
				HashMap hmLogo = new HashMap();
				hmLogo.put("fileName", filename.substring(filename.lastIndexOf("/")+1, filename.length()));
				hmLogo.put("fileType", "logo");
				alNotesImg.add(hmLogo);
			}
			
			// symbol			
			String symbol = send_doc.getItemValueString("xml_symbol");
			if(!"".equals(symbol)){
				doc = Jsoup.parse(symbol, "UTF-8");
				filename = doc.attr("src");
				/*if(filename.contains("/")){
					filename = filename.substring(filename.lastIndexOf("/"),filename.length()+1); 
				}*/
				
				fileurl = Configuration.getInstance().getValue("http").replace("http://", "") + "/" + filename;
				String symbol_img = "<img alt=\"심볼이미지\" src=\""+ send_doc.getItemValueString("xml_symbol_filename")+"\""
	                    + " width=\"" + send_doc.getItemValueString("xml_symbol_width")+ "mm\""
	                    + " height=\"" + send_doc.getItemValueString("xml_symbol_height")+"mm\" />";
				
				send_doc.replaceItemValue("xml_symbol", symbol_img);
				
				Util.attachFileDownload(Configuration.getInstance().getValue("http"), filename, targetPath);
				
				HashMap hmSymbol = new HashMap();
				hmSymbol.put("fileName", filename.substring(filename.lastIndexOf("/")+1, filename.length()));
				hmSymbol.put("fileType", "symbol");
				
				alNotesImg.add(hmSymbol);
			}
			
			for(int i=0;i<alNotesImg.size();i++){
				ContentVo contVo= new ContentVo();
				HashMap hmImg = (HashMap)alNotesImg.get(i);
				
				logger.debug("fileType : " + hmImg.get("fileType"));
				logger.debug("fileName : " + hmImg.get("fileName"));
				
				
				String imgType = hmImg.get("fileType").toString(); 
				String imgFilename = hmImg.get("fileName").toString();
				
				logger.debug("imgFilename 1: " + imgFilename);
				
				if(imgFilename.contains("/")){
					imgFilename = imgFilename.substring(filename.lastIndexOf("/")+1,imgFilename.length()); 
				}
				
				logger.debug("imgFilename : " + imgFilename);
				
				contVo.setFilename(Util.encodeBase64(imgFilename));
				contVo.setCharset("euc-kr");
				contVo.setContent_role(imgType);
				contVo.setContent_transfer_encoding("base64");
				contVo.setContent_type(Util.getMimeType(imgFilename));
				contVo.setContent(Util.fileToBase64String(targetPath + "\\" + imgFilename));
				
				if("logo".equals(imgType)){
					contsVo.setLogo(contVo);
				} else if("symbol".equals(imgType)){
					contsVo.setSymbol(contVo);
				} else if("seal".equals(imgType)){
					contsVo.setSeal(contVo);
				}
				
				logger.debug(" 파일명 >>>> " + contVo.getFilename());
			}
			
			
			if(!"".equals(notes_imgpath)){ //https 경우 download 받기.  
			  contsVo = notesImageDownload(alNotesImg, notes_imgpath, doc_id);
			}
			
			
			// attach
			
			 // ???? 어디에 씀???
			/*String [] attach_filekind = (send_doc.getItemValue("xml_filekind").get(0).toString()).split(";");
			String [] attach_fileurl = (send_doc.getItemValue("xml_fileurl").get(0).toString()).split(";");*/
			
			RichTextItem richITem = null;
			EmbeddedObject embedObj = null;
			richITem = (RichTextItem) send_doc.getFirstItem("Body");
			
			logger.debug("Type  >>> 1이어야 하는데.. ::: "+ richITem.getType());
			
			if(richITem.getType()==1){				
				if(send_doc.getItemValue("xml_filename").size()>0){
					String [] attach_filename = (send_doc.getItemValue("xml_filename").get(0).toString()).split(";");
					
					ArrayList alAttach = new ArrayList();
					for(int i=0;i<attach_filename.length;i++){
						embedObj = richITem.getEmbeddedObject(attach_filename[i]);
						if(embedObj==null){
							embedObj = send_doc.getAttachment(attach_filename[i]);
						}
						
						embedObj.extractFile(targetPath + "\\"+doc_id+"_" + attach_filename[i]);
						
						ContentVo contAttach= new ContentVo();
						contAttach.setFilename(Util.encodeBase64(attach_filename[i]));
						contAttach.setCharset("euc-kr");
						contAttach.setContent_role("attach");
						contAttach.setContent_transfer_encoding("base64");
						contAttach.setContent_type(Util.getMimeType(attach_filename[i]));
						contAttach.setContent(Util.fileToBase64String(targetPath + "\\"+doc_id+"_" + attach_filename[i]));
						
						alAttach.add(contAttach);
					}
					contsVo.setAttach(alAttach);
				}
			}
			 
		} catch (NotesException e) {
			e.printStackTrace();
		}		
		return contsVo;
	}


	private ContentsVo notesImageDownload(ArrayList alNotesImg, String dbName, String doc_id) {		
		Database db = null;		
		View view = null;
		Document doc = null;
		EmbeddedObject ebdObj = null;
		RichTextItem richITem = null;
		ContentsVo contsVo = new ContentsVo();
		
		String downFilePath = "";
		
		logger.debug("notesImageDownload ################" );
		
		db = notesConnectionDb(dbName);
		
		try {
			view = db.getView("link");
			for(int i=0; i<alNotesImg.size();i++){
				HashMap hmTmp = (HashMap) alNotesImg.get(i);
				String fileType = hmTmp.get("filetype").toString();
				String fileName = hmTmp.get("filename").toString();
				
				view.getDocumentByKey(fileType);
				
				richITem = (RichTextItem) doc.getFirstItem("HTML");
				
				if(richITem.getType()== 1){
					ebdObj = richITem.getEmbeddedObject(fileName);
					if(ebdObj==null){
						ebdObj = doc.getAttachment(fileName);
					}					
					
					downFilePath = Configuration.getInstance().getValue("attach_dir")+ "\\" + Util.getToday(); // 첨부 파일 저장 path
					
					File fdir = new File(downFilePath);
					if(!fdir.exists()){
						fdir.mkdirs();
					}
					downFilePath += downFilePath+"\\"+Util.getTodayCurrTime()+"_"+doc_id+".file";
					ebdObj.extractFile(downFilePath);
					ContentVo contVo = new ContentVo();
					
					contVo.setCharset("euc-kr");
					contVo.setContent_role("attach");
					contVo.setFilename(fileName);
					contVo.setContent_transfer_encoding("base64");
					contVo.setContent_type(Util.getMimeType(fileName));
					contVo.setContent(Util.fileToBase64String(downFilePath));
					
					if("logo".equals(fileType)){
						contsVo.setLogo(contVo);
					} else if("symbol".equals(fileType)){
						contsVo.setSymbol(contVo);
					} else if("seal".equals(fileType)){
						contsVo.setSeal(contVo);
					}
				}
			}
		} catch (NotesException e) {
			e.printStackTrace();
		}
		
		return contsVo;		
	}


	private PubdocVo pubdocDocument(Document send_doc) {
		PubdocVo pubdocVo = new PubdocVo();
		
		try {
			// head
			pubdocVo.setOrgan(send_doc.getItemValueString("Xml_organ"));
			//pubdocVo.setRec(send_doc.getItemValueString("xml_rec"));
			pubdocVo.setRec(Util.joinRegex(send_doc.getItemValueString("xml_rec"), ","));
			
			logger.debug(" |||||||| rec getItemValueString" + send_doc.getItemValueString("xml_rec"));
			logger.debug(" |||||||| rec getItemValue" + send_doc.getItemValue("xml_rec"));
			
			pubdocVo.setReceipient_refer(send_doc.getItemValueString("xml_refer_1"));
			pubdocVo.setVia(send_doc.getItemValueString("xml_via"));
			
			// body			
			pubdocVo.setBody_content(send_doc.getItemValueString("xml_body"));
			pubdocVo.setBody_title(send_doc.getItemValueString("xml_title"));
			pubdocVo.setBody_seperate("false"); // false로 고정

			// foot
			pubdocVo.setSendername(send_doc.getItemValueString("xml_send_name"));
			pubdocVo.setSeal_omit(send_doc.getItemValueString("xml_seal_omit"));
			pubdocVo.setSeal_img(send_doc.getItemValueString("xml_seal_img"));
			
			// foot/approval			
			ArrayList alApproval = new ArrayList();
			
			String [] appr_signposition = send_doc.getItemValueString("xml_approval_signposition").split(";");
			String [] appr_type = send_doc.getItemValueString("xml_approval_type").split(";");
			String [] appr_name = send_doc.getItemValueString("xml_approval_name").split(";");
			String [] appr_date = send_doc.getItemValueString("xml_approval_date").split(";");			
			String [] appr_time = send_doc.getItemValueString("xml_approval_time").split(";");
			
			
			for(int i=0;i<appr_signposition.length;i++){
				HashMap hmAppr = new HashMap();
				hmAppr.put("signposition", appr_signposition[i]);
				hmAppr.put("type", appr_type[i]);
				hmAppr.put("name", appr_name[i]);
				hmAppr.put("date", appr_date[i]);
				hmAppr.put("time", appr_time[i]);
				
				String order = String.valueOf(i+1);				
				if(i==(appr_signposition.length-1)){
					order = "final";
				}
				hmAppr.put("order", order);
				
				alApproval.add(hmAppr);
			}
						
			pubdocVo.setApproval(alApproval);			
			
			// foot/assist			
			ArrayList alAssist = new ArrayList();
			
			
			if(!"".equals(send_doc.getItemValueString("xml_assist_signposition").toString())){
				String [] assist_signposition = send_doc.getItemValueString("xml_assist_signposition").split(";");
				String [] assist_type = send_doc.getItemValueString("xml_assist_type").split(";");
				String [] assist_name = send_doc.getItemValueString("xml_assist_name").split(";");
				String [] assist_date = send_doc.getItemValueString("xml_assist_date").split(";");			
				String [] assist_time = send_doc.getItemValueString("xml_assist_time").split(";");
				
				
				for(int i=0;i<assist_signposition.length;i++){
					HashMap hmAssist = new HashMap();
					hmAssist.put("signposition", assist_signposition[i]);
					hmAssist.put("type", assist_type[i]);
					hmAssist.put("name", assist_name[i]);
					hmAssist.put("date", assist_date[i]);
					hmAssist.put("time", assist_time[i]);
					String order = String.valueOf(i);				
					if(i==(assist_signposition.length-1)){
						order = "final";
					}
					hmAssist.put("order", order);
					
					alAssist.add(hmAssist);
				}
				pubdocVo.setAssist(alAssist);
			}			
			
			
			// foot/processinfo
			pubdocVo.setRegnumber_code(send_doc.getItemValueString("xml_regnumbercode"));
			pubdocVo.setRegnumber(send_doc.getItemValueString("xml_regnumber"));
			pubdocVo.setEnforcedate(send_doc.getItemValueString("xml_enforcedate"));			
			
			// foot/sendinfo
			pubdocVo.setZipcode(send_doc.getItemValueString("xml_zipcode"));
			pubdocVo.setAddress(send_doc.getItemValueString("xml_address"));
			pubdocVo.setHomeurl(send_doc.getItemValueString("xml_homeurl"));
			pubdocVo.setTelephone(send_doc.getItemValueString("xml_telephone"));
			pubdocVo.setFax(send_doc.getItemValueString("xml_fax"));
			pubdocVo.setEmail(send_doc.getItemValueString("xml_email"));
			logger.debug("   xml_publication_code : " + send_doc.getItemValue("xml_publication_code"));
			
			String publicationCode = send_doc.getItemValueString("xml_publication_code");
			if("-1".equals(publicationCode)){ // 공개 문서일 경우 -1				
				publicationCode="";
			}
			
			pubdocVo.setPublication_code(publicationCode.split(";"));
			pubdocVo.setPublication(send_doc.getItemValueString("xml_publication"));
			pubdocVo.setSymbol(send_doc.getItemValueString("xml_symbol"));
			pubdocVo.setLogo(send_doc.getItemValueString("xml_logo"));			

			// foot/campaign
			pubdocVo.setHeadcampaign(send_doc.getItemValueString("xml_headcampaign"));
			pubdocVo.setFootcampaign(send_doc.getItemValueString("xml_footcampaign"));
			
			//pubdocVo.setAttach_title();		
			
			if(send_doc.getItemValue("xml_filename").size()>0){
				List<String> altitle = Arrays.asList(send_doc.getItemValue("xml_filename").toString());
				//altitle = send_doc.getItemValue("xml_filename").toString().split(","); 
				pubdocVo.setAttach_title(altitle);
			}
			
			
		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}          
		
		return pubdocVo;
	}


	private String generateAckFromSample(Document send_doc, String doc_type) {
		String ackFile = "";
		try {
			String filename = send_doc.getItemValueString("xml_acksample_filename");
			String ackhtml = send_doc.getItemValueString("xml_acksample");
			String ackdept = send_doc.hasItem("xml_doc_type_dept")==true?send_doc.getItemValueString("xml_doc_type_dept"):"";
			String ackname = send_doc.hasItem("xml_doc_type_name")==true?send_doc.getItemValueString("xml_doc_type_name"):"";
			
			logger.debug("!!!! ## doc_type " + doc_type + " generateAckFromSample 생성.. " );
			logger.debug("!!!! ## ackdept " + ackdept);
			
			if(!"".equals(ackhtml)){
				logger.info("	ACK Sample 생성");
				logger.debug("ackhtml > " + ackhtml);
				ackhtml = ackhtml.replace("#CURTIME", Util.getDocFormatCurDateTime()).replace("#ACKTYPE", doc_type);
				if(!"".equals(ackdept)){
					ackhtml = ackhtml.replace("#ACKDEPT", Util.encodeBase64(ackdept));
				}
				if(!"".equals(ackname)){
					ackhtml = ackhtml.replace("#ACKNAME", Util.encodeBase64(ackname));
				}
				filename = filename.replace("#CREATETIME", Util.getTodayCurrTime());
				// 오늘 생성한  문서  순번  table 에서 불러와서  setting 하기.. 
			    filename = filename.replace("#ACKCNT", "00");
			    
			    //ACK File to SendTemp
			    FileHandling fhd = new FileHandling();
			    try {
			    	XmlParsing xmlParsing = new XmlParsing();
			    	
					org.w3c.dom.Document doc = xmlParsing.stringtToJdom(ackhtml);
					
					logger.info("	ACK Sample 저장  [ " + filename + "]");
					fhd.domToXmlFile(Configuration.getInstance().getValue("send_temp")+ "\\" ,  filename , doc);
					
					ackFile = filename;
					
				} catch (JDOMException e) {
					logger.error("	ACK 파일 생성 오류");
					e.printStackTrace();
				}
			} else {
				logger.debug(" ACK SAMPLE 빈값... ");
				
			}
		} catch (NotesException e) {
			e.printStackTrace();
		}    
		return ackFile;
	}

	private PackVo packDocument(Document send_doc) {
		PackVo send_packVo = new PackVo();
		try {
			//p_send_orgcode = doc.GetItemValue("xml_send_orgcode")(0)
			send_packVo.setSend_orgcode(send_doc.getItemValueString("xml_send_orgcode"));
			send_packVo.setSend_id(send_doc.getItemValueString("xml_send_id"));
			send_packVo.setSend_name(Util.encodeBase64(send_doc.getItemValueString("xml_send_name")));
			
			String Receive_id = send_doc.getItemValueString("xml_receive_id");
			String split_char = ";"; 
			if(Receive_id.contains(",")){
				split_char = ",";
			}
			
			send_packVo.setReceive_id(Receive_id.split(split_char));
			send_packVo.setDate(Util.getDocFormatCurDateTime());
			
			String title = send_doc.getItemValueString("xml_title");
			title = Pattern.compile("\\[\n]").matcher(title).replaceAll("");
			send_packVo.setTitle(Util.encodeBase64(title));			
			
			send_packVo.setDoc_id(send_doc.getItemValueString("xml_doc_id"));			
			send_packVo.setDoc_type_type(send_doc.getItemValueString("xml_doc_type_type"));
			send_packVo.setDoc_type_name(Util.encodeBase64(send_doc.getItemValueString("xml_doc_type_name")));
			send_packVo.setDoc_type_dept(Util.encodeBase64(send_doc.getItemValueString("xml_doc_type_dept")));
			
			String gwfullname = send_doc.getItemValueString("xml_gwfullname")==null?hmCode.get("gwfullname").toString():send_doc.getItemValueString("xml_gwfullname");
			send_packVo.setSend_gw(Util.encodeBase64(gwfullname));
			
			String dtd_version = send_doc.getItemValueString("xml_dtd_version");
			if("".equals(dtd_version)){
				dtd_version = hmCode.get("dtd_version").toString()==""?"2.0" : hmCode.get("dtd_version").toString();
			}
			
			send_packVo.setDtd_version(Util.encodeBase64(dtd_version));
			
			String xsl_version = send_doc.getItemValueString("xml_xsl_version");
			if("".equals(xsl_version)){
				xsl_version = hmCode.get("xsl_version").toString()==""?"2.0" : hmCode.get("xsl_version").toString();
			}
			send_packVo.setXsl_version(Util.encodeBase64(xsl_version));
			
			logger.info("	pack data 세팅 완료");
		} catch (NotesException e) {
			logger.error("	pack data 세팅중  Notes 에러.....");
			e.printStackTrace();
		}
		return send_packVo;
	}


	public String generateAckXml(PackVo packVo, String ackType, String body, int seq) {
		String filename = packVo.getPack_xmlname();
		String ackFileName = "";
		StringBuilder sbXml = new StringBuilder();
		sbXml.append("<?xml version=\"1.0\" encoding=\"euc-kr\"?>\n");
		sbXml.append("<!DOCTYPE pack SYSTEM \"pack.dtd\">\n");
		sbXml.append("<pack>\n");
		sbXml.append("<header>\n");
		sbXml.append("<send-orgcode>" + hmCode.get("send_orgcode").toString() + "</send-orgcode>\n");
		
		sbXml.append("<send-id>" + filename.substring(7, 14) + "</send-id>\n");
		sbXml.append("<send-name>" + Util.encodeBase64(hmCode.get("send_name").toString()) + "</send-name>\n");
		sbXml.append("<receive-id>" + filename.substring(0, 7) + "</receive-id>\n");
		sbXml.append("<date>" + Util.getDocFormatCurDateTime() + "</date>\n");
		sbXml.append("<title>" + Util.encodeBase64(packVo.getTitle()) + "</title>\n");
		sbXml.append("<doc-id>" + packVo.getDoc_id() + "</doc-id>\n");
		
		if("receive".equals(ackType) | "fail".equals(ackType)){
			sbXml.append("<doc-type type=\"" + ackType + "\" dept=\"\" name=\"\"></doc-type>\n");
		} else {
			// CN=184377 금향숙/OU=U/OU=KO/O=KOTRA
			
			String name = Util.encodeBase64((hmCode.get("recipient").toString().split("/OU")[0].replace("CN=","").split(" ")[1]));			
			sbXml.append("<doc-type type=\"" + ackType + "\" dept=\"" + Util.encodeBase64(hmCode.get("defaultrecipientdept").toString())
			+ "\" name=\""+ name + "\"></doc-type>\n"); 
		}
		
		logger.debug("  ::: defaultrecipientdept :::: " + hmCode.get("defaultrecipientdept").toString());
		
		sbXml.append("<send-gw>" + Util.encodeBase64(hmCode.get("gwfullname").toString()) + "</send-gw>\n");
		sbXml.append("<dtd-version>2.0</dtd-version>\n");
		sbXml.append("<xsl-version>2.0</xsl-version>\n");
		sbXml.append("</header>\n");
		sbXml.append("<contents>\n");
		
		if("req-resend".equals(ackType)){
			sbXml.append("<content content-role=\"return\" filename=\"" +Util.encodeBase64("return.txt")+ "\" content-transfer-encoding=\"base64\" "
					+ "	content-type=\"text/xml\"  charset=\"euc-kr\">" +Util.encodeBase64(body)+ "</content>\n");
		}
		if("fail".equals(ackType)){
			String rtnFile = Configuration.getInstance().getValue("attach_dir")+ "\\return.txt";
			
			sbXml.append("<content content-role=\"fail\" filename=\"" +Util.encodeBase64("return.txt")+ "\" content-transfer-encoding=\"base64\" "
					+ "	content-type=\"text/xml\"  charset=\"euc-kr\">" +Util.fileToBase64String(rtnFile)+ "</content>\n");
		}
		
		sbXml.append("</contents>\n");
		sbXml.append("</pack>");
		
		String xmlString = sbXml.toString();
		
		XmlParsing xmlParse = new XmlParsing();
		org.w3c.dom.Document packDom;
		
		try {
			packDom = xmlParse.stringtToJdom(xmlString);
			FileHandling fhdl = new FileHandling();	
			
			String seqNo = String.valueOf(seq);
			if(seq < 10){
				seqNo = "0"+seq;
			} else if (seq>99){
				seqNo = seqNo.substring(1, 3);
			}
			
			ackFileName = filename.substring(7, 14) + filename.substring(0, 7) + Util.getTodayCurrTime() +  seqNo + ".xml";
			
			fhdl.domToXmlFile(Configuration.getInstance().getValue("send_temp")+"\\", ackFileName , packDom );
			logger.info("	> ack 파일 생성 완료 : " + ackFileName);
			
		} catch (JDOMException e) {
			e.printStackTrace();
			logger.error("	> ack 파일 생성 실패  : " + e.getMessage());
		}
		return ackFileName;
	}
		
}
