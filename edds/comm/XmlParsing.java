package com.saerom.edds.comm;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.saerom.edds.edds.vo.ContentVo;
import com.saerom.edds.edds.vo.ContentsVo;
import com.saerom.edds.edds.vo.PackVo;
import com.saerom.edds.edds.vo.PubdocVo;




public class XmlParsing {
	private static Logger logger = Logger.getLogger(Class.class.getName());
	
	public PackVo unPack(String path, File file) {
		PackVo pack = new PackVo();
		
		File input = new File(path+"\\"+file.getName());
		pack.setPack_xmlname(file.getName());
		logger.info("["+file.getName() + "] pack 해제 시작");
		
		String noVal = "";
		try {
			org.jsoup.nodes.Document doc = Jsoup.parse(input, "UTF-8");
			
			Element element = doc.select("pack").first();
			
			if(element.hasAttr("pack_filename")){
				pack.setPack_filename(element.attr("pack-filename"));
				
			}
			
			String send_orgcode = element.select("send-orgcode").text();// "".equals(element.select("send-orgcode").text())?err+="send_orgcode;":element.select("send-orgcode").text();
			if("".equals(send_orgcode))noVal+= "send-orgcode;";			
			
			String send_id = element.select("send-id").text();
			if("".equals(send_id))noVal+= "send-id;";
			
			String send_name = element.select("send-name").text(); //Util.decodeBase64(
			if("".equals(send_id))noVal+= "send-name;";
			logger.info("	> send-name : " + Util.decodeBase64(send_name));
			
			String [] receive_id = element.select("receive-id").text().split(";");
			if(receive_id.length==0)noVal+= "send_id;";
			
			String date = element.select("date").text();
			if("".equals(date))noVal+= "date;";
			
			String title = element.select("title").text();
			if("".equals(title))noVal+= "title;";
			
			String doc_id = element.select("doc-id").text();
			if("".equals(doc_id))noVal+= "doc-id;";
			
			pack.setSend_orgcode(send_orgcode);
			pack.setSend_id(send_id);
			pack.setSend_name(Util.decodeBase64(send_name));
			pack.setReceive_id(receive_id);
			pack.setDate(date); 
			pack.setTitle(Util.decodeBase64(title));
			pack.setDoc_id(doc_id);
			
			
			String doc_type = element.select("doc-type").attr("type");
			if("".equals(doc_type))noVal+= "doc-type;";
			
			logger.info("	> doc_type : " + doc_type);
			
			String doc_dept = element.select("doc-type").attr("dept");
			//if("".equals(doc_dept))noVal+= "doc-dept;";
			
			String doc_name = element.select("doc-type").attr("name");
			if("".equals(doc_name))noVal+= "doc-name;";
			
			String send_gw = element.select("send-gw").text();			
			if("".equals(send_gw))noVal+= "send-gw;";
			
			String dtd = element.select("dtd-version").text();
			if("".equals(dtd))noVal+= "dtd-version;";
			
			String xsl = element.select("xsl-version").text();
			if("".equals(xsl))noVal+= "xsl-version;";
			
			pack.setDoc_type_type(doc_type);
			pack.setDoc_type_dept(Util.decodeBase64(doc_dept));
			pack.setDoc_type_name(Util.decodeBase64(doc_name));
			pack.setSend_gw(Util.decodeBase64(send_gw));
			pack.setDtd_version(dtd);
			pack.setXsl_version(xsl);
			
			if("return".equals(doc_type) | "req-resend".equals(doc_type) | "fail".equals(doc_type)){
				pack.setContents_msg(element.select("contents").toString());
			} else {
				pack.setContents(getContents(element.select("contents").toString(), pack.getDoc_id()));
			}
			
			pack.setNoVal(noVal);
			
			logger.info("["+file.getName() + "] pack 해제 끝");
			
		} catch (IOException e) {			
			logger.error(" 	pack 분해 에러 : "+ e.getMessage());
			e.printStackTrace();
		}
		return pack;
	}

	private ContentsVo getContents(String contents, String doc_id) {
		ContentsVo contsVo = new ContentsVo();
		org.jsoup.nodes.Document doc = Jsoup.parse(contents, "UTF-8");
		
		Elements elements = doc.select("content");
		
		List<ContentVo> alAttach = new ArrayList<ContentVo>();
		List<ContentVo> alAttach_body = new ArrayList<ContentVo>();
		List<ContentVo> alSign = new ArrayList<ContentVo>();
		
		int attachCnt = 0;
		
		logger.info("	> contents  처리 시작 ");
		for(int i=0; i<elements.size();i++){
			String fileExt = "";
			Element e = elements.get(i);
			ContentVo contVo= new ContentVo();
			String cont_role = e.attr("content-role");			
			contVo.setContent_role(cont_role);
			contVo.setContent_transfer_encoding(e.attr("content-transfer-encoding"));
			String filename = Util.decodeBase64(e.attr("filename"));
			contVo.setFilename(filename);
			
			String cont_type = e.attr("content-type");
			if("attach".equals(cont_role) & (cont_type.contains("multipart"))){
				cont_type="application/octet-stream";
			}			
			contVo.setContent_type(cont_type);
			contVo.setCharset(e.attr("charset"));
			
			
			// 첨부저장
			String cont = e.text();		
			
			if(!"".equals(cont) & !"pubdoc".equals(cont_role)){
				String upfilename = cont_role+"^"+filename;
				
				if("attach".equals(cont_role)){
					if(filename.indexOf(".") > -1){
						fileExt = filename.substring(filename.lastIndexOf("."), filename.length());
					}
										
					upfilename = "^attach"+attachCnt+fileExt;
					attachCnt++;
				}
				
				logger.info("	[첨부 파일 저장 ] "+ doc_id+upfilename );
				
				writeFile(cont, doc_id, upfilename);
				
			}
			
			if("pubdoc".equals(cont_role)){
				String err = "";
				try {
					err = validateWithDTDUsingDOM(Util.decodeBase64(e.text().toString()));
					
					logger.info("	pubdoc validation : " + "".equals(err));
					
					if(!"".equals(err)){
						logger.debug("	pubdoc / return.txt 생성..");
						Util.StringToFile(Configuration.getInstance().getValue("attach_dir")+ "\\return.txt", err);
					} else {
						//pubdoc xml 분해
						
						logger.debug(" pubdoc name " + filename);
						contVo.setContent(getPubdoc(e.text().toString(), filename,  doc_id));
						contsVo.setPubdoc(contVo);
					}
				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
							
			} else if("attach".equals(cont_role)){
				alAttach.add(contVo);
			} else if("attach_body".equals(cont_role)){
				alAttach_body.add(contVo);
			} else if("seal".equals(cont_role)){
				contsVo.setSeal(contVo);
			} else if("gpki".equals(cont_role)){
				contsVo.setGpki(contVo);
			} else if("fail".equals(cont_role)){
				contsVo.setFail(contVo);
			} else if("sign".equals(cont_role)){
				alSign.add(contVo);
			} else if("symbol".equals(cont_role)){
				contsVo.setSymbol(contVo);
			} else if("logo".equals(cont_role)){
				contsVo.setLogo(contVo);
			}
		}
		logger.info("	> contents 처리  끝 ");
		
		contsVo.setAttach(alAttach);
		contsVo.setAttach_body(alAttach);
		contsVo.setSign(alSign);
		
		return contsVo;
	}

	synchronized private boolean writeFile(String cont, String doc_id, String filename) {
		
		String path = Configuration.getInstance().getValue("attach_dir")+"\\" + Util.getToday()+ "\\";
		filename = doc_id + filename;
		return Util.base64StringToFile(cont, path , filename);	
	}

	private Object getPubdoc(String pubdoc, String filename, String doc_id) {
		String strPubdoc = Util.decodeBase64(pubdoc);
		String noVal = "";
		try {
			
			logger.info("	pubdoc.xml 저장..." +  doc_id+"_"+filename);
			// pubdoc.xml 저장
			Document xmldoc = stringtToJdom(strPubdoc);
			FileHandling fhdl = new FileHandling();			
			fhdl.domToXmlFile(Configuration.getInstance().getValue("pubdoc_dir")+"\\"+ Util.getToday()+"\\", doc_id+"_"+filename, xmldoc);
			
		} catch (JDOMException e) {
			logger.error("	pubdoc.xml 저장 에러 : " + e.getMessage());
			e.printStackTrace();
		}
		
		
		logger.info("	pubdoc 분해 시작...");
		
		PubdocVo pubdocVo = new PubdocVo();
		
		org.jsoup.nodes.Document doc = Jsoup.parse(strPubdoc,"euc-kr",Parser.xmlParser());		
		Element element = doc.select("pubdoc > head").first();
		
		String organ = element.select("organ").text();
		if("".equals(organ)) noVal+= "organ;";
		
		String refer = element.select("receiptinfo>recipient").attr("refer");
		if("".equals(refer)) noVal+= "refer;";
		
		String rec = element.select("receiptinfo>recipient>rec").text();
		if("".equals(rec)) noVal+= "rec;";
		
		String via = element.select("receiptinfo>via").text();
		// if("".equals(rec)) noVal+= "via;";
		
		//head
		pubdocVo.setOrgan(organ);
		pubdocVo.setReceipient_refer(organ);
		pubdocVo.setRec(rec);
		pubdocVo.setVia(rec);
		
		// body
		element.empty();
		element = doc.select("pubdoc > body").first();
		
		String separate = element.attr("separate");
		if("".equals(separate)) separate = "false";
		
		String title = element.select("title").text();
		if("".equals(title)) noVal+= "pubdoc_title;";
		
		String content = element.select("content").toString();
		if(!"".equals(content)){
			content = content.replace("<content>", "").replace("</content>", "");
		} else {
			noVal+= "content;";
		}
		
		pubdocVo.setBody_seperate(separate);
		pubdocVo.setBody_title(title);
		pubdocVo.setBody_content(content);
		
		// foot
		element.empty();
		element = doc.select("pubdoc > foot").first();
		
		String sendername = element.select("sendername").text();
		if("".equals(sendername)) noVal+= "sendername;";
		
		String omit = element.select("seal").attr("omit");
		if("".equals(omit)) noVal+= "omit;";
		
		String seal_img = element.select("seal > img").toString();
		//if("".equals(omit)) noVal+= "seal_img;";
		
		
		pubdocVo.setSendername(sendername);
		pubdocVo.setSeal_omit(omit);
		pubdocVo.setSeal_img(seal_img);
		
		// foot/approvalinfo
		Elements elements = doc.select("approvalinfo > approval");
		ArrayList alApproval = new ArrayList();
		
		if(elements.size()==0)  noVal+= "approval;";
		
		for(int i=0;i<elements.size();i++){
			Element e = elements.get(i);			
			HashMap hmappr = new HashMap();				
			String order = e.attr("order");
			
			if("final".equals(order)){
				order = Integer.toString(elements.size());
			}
			
			String appr_signposition = e.select("signposition").text();
			// if("".equals(appr_signposition)) noVal+= i+"_appr_signposition;";
			
			String appr_type = e.select("type").text();
			// if("".equals(appr_type)) noVal+= i+"_appr_type;";
					
			hmappr.put("order",order);
			hmappr.put("signposition",appr_signposition);
			hmappr.put("type",appr_type);
			
			String signimage= e.select("signimage > img").attr("src");
			if((!"".equals(signimage)) & (signimage.indexOf(".")<0)){ //파일명이 인코딩 값일 경우
				signimage = Util.decodeBase64(signimage);
			}
			String appr_name = e.select("name").text(); 
			// if("".equals(appr_name)) noVal+= i+"_appr_name;";
			
			String appr_date = e.select("date").text(); 
			// if("".equals(appr_date)) noVal+= i+"_appr_date;";
			
			hmappr.put("signimage",signimage);
			hmappr.put("name", appr_name);
			hmappr.put("date", appr_date);
			hmappr.put("time",e.select("time").text());
			
			alApproval.add(hmappr);
		}
		pubdocVo.setApproval(alApproval);
		
		// foot/assist
		elements.empty();
		elements = doc.select("approvalinfo > assist");
		ArrayList alAssist = new ArrayList();
		for(int i=0;i<elements.size();i++){
			Element e = elements.get(i);			
			HashMap hmappr = new HashMap();				
			String order = e.attr("order");
			
			if("final".equals(order)){
				order = Integer.toString(elements.size());
			}
			String assist_signposition = e.select("signposition").text();
			// if("".equals(assist_signposition)) noVal+= i+"_assist_signposition;";
			
			String type = e.select("type").text();
			// if("".equals(type)) noVal+= i+"type;";
			
			hmappr.put("order",order);
			hmappr.put("signposition",assist_signposition);
			hmappr.put("type",type);
			
			String signimage= e.select("signimage > img").attr("src");
			if((!"".equals(signimage)) & (signimage.indexOf(".")<0)){ //파일명이 인코딩 값일 경우
				signimage = Util.decodeBase64(signimage);
			}
			
			String assist_date =  e.select("date").text();
			// if("".equals(assist_date)) noVal+= i+"_assist_date;";
			
			hmappr.put("signimage",signimage);
			hmappr.put("name",e.select("name").text());
			hmappr.put("date",assist_date);
			hmappr.put("time",e.select("time").text());
			
			alAssist.add(hmappr);
		}
		pubdocVo.setAssist(alAssist);
		
		String regNumber = element.select("processinfo > regnumber").text();
		if("".equals(regNumber)) noVal+= "regNumber;";
		
		String regnumbercode = element.select("processinfo > regnumber").attr("regnumbercode");
		if("".equals(regnumbercode)) noVal+= "regnumbercode;";
		
		String enforcedate = element.select("processinfo > enforcedate").text();
		if("".equals(enforcedate)) noVal+= "enforcedate;";		
		
		pubdocVo.setRegnumber(regNumber);
		pubdocVo.setRegnumber_code(regnumbercode);
		pubdocVo.setEnforcedate(enforcedate);		
		
		
		int iReceipt = element.select("processinfo > receipt").size();
		
		if(iReceipt>0){
			pubdocVo.setReceipt(element.select("processinfo > receipt").text());
			
			String receipt_number = element.select("processinfo > receipt > number").text();
			// if("".equals(receipt_number)) noVal+= "receipt_number;";
			
			String receipt_date = element.select("processinfo > receipt > date").text();
			// if("".equals(receipt_date)) noVal+= "receipt_date;";
			
			pubdocVo.setReceipt_number(receipt_number);
			pubdocVo.setReceipt_date(receipt_date);
			pubdocVo.setReceipt_time(element.select("processinfo > receipt > time").text());
		}
		
		// foot/sendinfo
		
		String zipcode = element.select("sendinfo > zipcode").text();
		// if("".equals(zipcode)) noVal+= "zipcode;";
		
		String address = element.select("sendinfo > address").text();
		// if("".equals(address)) noVal+= "address;";
		
		String telephone = element.select("sendinfo > telephone").text();
		// if("".equals(telephone)) noVal+= "telephone;";
		
		String fax = element.select("sendinfo > fax").text();
		// if("".equals(fax)) noVal+= "fax;";
		
		pubdocVo.setZipcode(zipcode);
		pubdocVo.setAddress(address);
		pubdocVo.setHomeurl(element.select("sendinfo > homeurl").text());
		pubdocVo.setTelephone(telephone);
		pubdocVo.setFax(fax);
		pubdocVo.setEmail(element.select("sendinfo > email").text());			
		
		String publication = element.select("sendinfo > publication").text();
		if(publication.contains("(")){
			publication = publication.substring(0, publication.indexOf("(")); // ? 어디에 쓰나...
		}
		if("".equals(publication)) noVal+= "publication;";
		
		
		// 공개 구분
		String publication_code = element.select("sendinfo > publication").attr("code");
		if("".equals(publication_code)) noVal+= "publication_code;";
		
		String publication_kind = publication_code.substring(0,1);
		publication_code = publication_code.substring(1, publication_code.length()-1);
		
		logger.debug(" publi code >> "+publication_code);
		
		String tmp="";
		for(int i=0;i<publication_code.length();i++){
			if("Y".equals(publication_code.substring(i,i+1))){
				tmp+=Integer.toString(i)+",";
			}
		}
		logger.debug("@@### publication >  " + tmp);
		String [] publication_code_arr={};
		if(tmp.length()>0){
			publication_code_arr = tmp.substring(0, tmp.length()-1).split(",");
		}
		pubdocVo.setPublication(publication_kind);
		pubdocVo.setPublication_code(publication_code_arr);
		
		pubdocVo.setSymbol(element.select("sendinfo > symbol > img").toString());
		pubdocVo.setLogo(element.select("sendinfo > logo > img").toString());
		
		pubdocVo.setHeadcampaign(element.select("campaign > headcampaign").text());
		pubdocVo.setFootcampaign(element.select("campaign > footcampaign").text());
		
		elements.empty();
		
		if(doc.select("pubdoc>attach").size()>0){
			elements = doc.select("pubdoc>attach>title");
			
			ArrayList<String> alList_attch = new ArrayList<String>();
			for (int i = 0; i < elements.size(); i++) {
				alList_attch.add( elements.get(i).text());
			}
			
			if(alList_attch.size()==0) noVal+= "attach_title;";
			pubdocVo.setAttach_title(alList_attch);
		}
		
		pubdocVo.setNoVal(noVal);
		
		logger.info("	pubdoc 분해 끝...");
		return pubdocVo;
	}
	
	
	/*
	 * string 문자열을 jdom 형식으로..
	 * */
	public Document stringtToJdom(String xmlString) throws JDOMException {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = null;
         
		 Document doc = null;

        try {
        	
        	//logger.debug("xmlString ++ " + xmlString);
        	/*
            ByteArrayInputStream bi = new ByteArrayInputStream(xmlString.getBytes());
            InputStreamReader isr = new InputStreamReader(bi);
            BufferedReader br = new BufferedReader(isr);*/
            
            builder = factory.newDocumentBuilder();
            doc =  builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception ex) {
        	logger.error("	xml 파일 생성 실패");
            ex.printStackTrace();
        }

        return doc;
    }
	
	public void makeXmlFile(String path, Document doc){
		XMLOutputter xo = new XMLOutputter();
		xo.setEncoding("euc-kr");
		xo.setIndent(" ");
		xo.setNewlines(true);
		xo.setTextTrim(true);
		
		if(doc !=null){
			try {
				
				FileOutputStream f = new FileOutputStream(path);
	            OutputStreamWriter writer = new OutputStreamWriter(f, "euc-kr");

	            //Document를 파일에 출력
	            xo.output((org.jdom.Document) doc, writer);
	            
	            writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String unionPubdocXml(ContentVo contVo, String [] receiveId) {
		StringBuilder sbXml = new StringBuilder();
		
		org.jsoup.nodes.Document doc;
		logger.info("	pubdoc 생성 시작. ");
		
		sbXml.append("<?xml version=\"1.0\" encoding=\"euc-kr\"?>\n");
		sbXml.append("<?xml-stylesheet type=\"text/xsl\" href=\"siheng.xsl\"?>\n");
		sbXml.append("<!DOCTYPE pubdoc SYSTEM \"pubdoc.dtd\">\n");
		sbXml.append("<pubdoc>\n");
		//pubod/head
		
		PubdocVo pubdocVo = (PubdocVo) contVo.getContent();
		
		logger.debug("!!@@ pubdoc " + pubdocVo.getBody_content());
		
		sbXml.append("<head>\n");
		sbXml.append("<organ>"+pubdocVo.getOrgan()+"</organ>\n");
		sbXml.append("<receiptinfo>\n");
		sbXml.append("<recipient refer=\""+pubdocVo.getReceipient_refer()+"\">\n");
		//sbXml.append("<rec>" +pubdocVo.getRec()+ "</rec>\n");
		sbXml.append("<rec>" + Util.joinRegex(pubdocVo.getRec(), ",")+ "</rec>\n");
		sbXml.append("</recipient>\n");
		sbXml.append("</receiptinfo>\n");
		sbXml.append("</head>\n");
		
		
		//pubdoc/body
		sbXml.append("<body separate=\"" + pubdocVo.getBody_seperate() + "\">\n");
		sbXml.append("<title>" + pubdocVo.getBody_title() + "</title>\n");

		String content = pubdocVo.getBody_content();
		
		String excptList [] = Configuration.getInstance().getValue("exception").split(",");
		doc = Jsoup.parse(content , "UTF-8");
		
		String rst  = Jsoup.parse(Util.sendXmlBody(excptList, receiveId, doc) , "UTF-8").select("body").toString();
		
		rst = rst.replace("<body>", "").replace("</body>", "");
		
		//logger.debug( "trans contents >>>>>>>>" + rst);
		
		sbXml.append("<content>" + rst+ "</content>\n");
		
		sbXml.append("</body>\n");
		
		//pubdoc/foot
		sbXml.append("<foot>\n");
		sbXml.append("<sendername>" + pubdocVo.getSendername()+ "</sendername>\n");
		
		doc = Jsoup.parse(pubdocVo.getSeal_img(), "UTF-8");        

        Element element = doc.select("img").first();
        String src = element.attr("src");
        src = src.substring(src.lastIndexOf("/"), src.length());

        element.attr("src",src);
        
        /* 일반직인, 사장직인 사이즈가 달라서 도미노에서 처리 하는것으로 함 
        // 도미노에서 가져오는 필드 값 <img alt="관인이미지" src="/S000000.gif" width="114px" height="114px">        
        element.attr("width","30.1625mm"); //114px to 30.1625mm ( 2016.06.03 )
        element.attr("height","30.1625mm"); //114px to 30.1625mm ( 2016.06.03 )
        */		
        
        sbXml.append("<seal omit=\"" + pubdocVo.getSeal_omit()+ "\">" + element.toString() + "</img></seal>\n");
        
		//pubdoc/foot/approvalinfo
		sbXml.append("<approvalinfo>\n");
		
		for(int i=0; i<pubdocVo.getApproval().size();i++){
			HashMap hmAppr = (HashMap)pubdocVo.getApproval().get(i);
			sbXml.append("<approval order=\"" + hmAppr.get("order") + "\">\n");
			sbXml.append("<signposition>" + hmAppr.get("signposition") + "</signposition>\n");
			sbXml.append("<type>" + hmAppr.get("type") + "</type>\n");
			sbXml.append("<name>" + hmAppr.get("name") + "</name>\n");
			sbXml.append("<date>" + hmAppr.get("date")+ "</date>\n");
			sbXml.append("<time>" + hmAppr.get("time")+ "</time>\n");
			sbXml.append("</approval>\n");
		}
		
		
		if(pubdocVo.getAssist()!=null){
			for(int i=0; i<pubdocVo.getAssist().size();i++){
				HashMap hmAssist = (HashMap)pubdocVo.getAssist().get(i);
				sbXml.append("<assist order=\"" + hmAssist.get("order") + "\">\n");
				sbXml.append("<signposition>" + hmAssist.get("signposition") + "</signposition>\n");
				sbXml.append("<type>" + hmAssist.get("type") + "</type>\n");
				sbXml.append("<name>" + hmAssist.get("name") + "</name>\n");
				sbXml.append("<date>" + hmAssist.get("date")+ "</date>\n");
				sbXml.append("<time>" + hmAssist.get("time")+ "</time>\n");
				sbXml.append("</assist>\n");
			}
		}
		
		sbXml.append("</approvalinfo>\n");
		
		
		// pubdoc/foot/processinfo
		sbXml.append("<processinfo>\n");
		sbXml.append("<regnumber regnumbercode=\"" + pubdocVo.getRegnumber_code() + "\">" + pubdocVo.getRegnumber()+ "</regnumber>\n");
		sbXml.append("<enforcedate>" + pubdocVo.getEnforcedate()+ "</enforcedate>\n");
		sbXml.append("</processinfo>\n");
		
		// pubdoc/foot/sendinfo
		sbXml.append("<sendinfo>\n");
		sbXml.append("<zipcode>" + pubdocVo.getZipcode()+ "</zipcode>\n");
		sbXml.append("<address>" + pubdocVo.getAddress()+ "</address>\n");
		sbXml.append("<homeurl>" + pubdocVo.getHomeurl()+ "</homeurl>\n");
		sbXml.append("<telephone>" + pubdocVo.getTelephone() + "</telephone>\n");
		sbXml.append("<fax>" + pubdocVo.getFax() + "</fax>\n");
		sbXml.append("<email>" + pubdocVo.getEmail() + "</email>\n");
		
		String [] pubCode = {"N","N","N","N","N","N","N","N"};
		String publType = pubdocVo.getPublication();
		String publication = ""; 
		
		if("1".equals(publType)| "공개".equals(publType)){
			publication = "공개";
		} else if("2".equals(publType)| "부분공개".equals(publType)){
			publication = "부분공개";
		} else if("3".equals(publType)| "비공개".equals(publType)){
			publication = "비공개";
		} else {
			publication = publType;
		}
		
		for(int i=0;i<pubdocVo.getPublication_code().length;i++){
			String code = pubdocVo.getPublication_code()[i].toString();
			
			if(!"".equals(code)){
				int idx = Integer.parseInt(pubdocVo.getPublication_code()[i]);
				// 7까지 밖에 올수 없음. 8이 오면 무시 함.
				if(idx<8){
					pubCode[idx]="Y";
				}
				
			}
			
		}
		
		logger.debug("publication code ::: " + publType + String.join("", pubCode));
		
		sbXml.append("<publication code=\"" + publType + String.join("", pubCode) +"\">" +  publication + "</publication>\n");
		if(!"".equals(pubdocVo.getSymbol())){
			
			doc = Jsoup.parse(pubdocVo.getSymbol(), "UTF-8");
            element = doc.select("img").first();
            src = element.attr("src");
            src = src.substring(src.lastIndexOf("/"), src.length());           

            element.attr("src",src);        

            sbXml.append("<symbol>" + element.toString() +"</img></symbol>\n");			
		}
		if(!"".equals(pubdocVo.getLogo())){
			doc = Jsoup.parse(pubdocVo.getLogo(), "UTF-8");
            element = doc.select("img").first();
            src = element.attr("src");
            src = src.substring(src.lastIndexOf("/"), src.length());           

            element.attr("src",src);            

            sbXml.append("<logo>" + element.toString() +"</img></logo>\n");
		}
		sbXml.append("</sendinfo>\n");
		
		// pubdoc/foot/campaign		
		sbXml.append("<campaign>\n");
		
		if(!"".equals(pubdocVo.getHeadcampaign())){
			sbXml.append("<headcampaign>" + pubdocVo.getHeadcampaign() +"</headcampaign>\n");
		}
		if(!"".equals(pubdocVo.getFootcampaign())){
			sbXml.append("<footcampaign>" + pubdocVo.getFootcampaign() +"</footcampaign>\n");
		}
		sbXml.append("</campaign>\n");
		sbXml.append("</foot>\n");
		
		
		//pubdoc/attach
		ArrayList<List<String>> title = new ArrayList<List<String>>();		
		String titleString = "";
		if(pubdocVo.getAttach_title()!=null){			
			
			titleString = pubdocVo.getAttach_title().get(0).toString();			
			titleString = titleString.substring(1, titleString.length()-1);			
			
			title.add(Arrays.asList(titleString.split(";")));
		}
		
		if(title.size()>0){
			sbXml.append("<attach>\n");
			for(int i=0; i<title.size();i++){
				logger.debug("title " + title.get(i));				
				sbXml.append("<title>" + title.get(i) + "</title>\n");
			}
			sbXml.append("</attach>\n");
		}
		sbXml.append("</pubdoc>\n");
		
		return sbXml.toString();		
	}
	
	
	public String unionPackXml(PackVo packVo) {
		StringBuilder sbXml = new StringBuilder();
		sbXml.append("<?xml version=\"1.0\" encoding=\"euc-kr\"?>\n");
		sbXml.append("<!DOCTYPE pack SYSTEM \"pack.dtd\">\n");
		sbXml.append("<pack>\n");
		sbXml.append("<header>\n");
		sbXml.append("<send-orgcode>" + packVo.getSend_orgcode() + "</send-orgcode>\n");
		
		String send_id = packVo.getSend_id();		
		if("".equals(send_id)){
			send_id = packVo.getSend_orgcode();
		}
		
		ArrayList<ContentVo> alConts = new ArrayList<ContentVo>();	
		alConts.add(packVo.getContents().getPubdoc());		
		
		List<ContentVo> attachList = packVo.getContents().getAttach();		
		if(attachList!=null){
			for(int i=0;i<attachList.size();i++){
				alConts.add(attachList.get(i));
			}
		}
		
		List<ContentVo> attach_bodyList = packVo.getContents().getAttach_body();
		if(attach_bodyList!=null){
			for(int i=0;i<attach_bodyList.size();i++){
				alConts.add(attach_bodyList.get(i));
			}
		}
		
		alConts.add(packVo.getContents().getSeal());
		alConts.add(packVo.getContents().getGpki());
		alConts.add(packVo.getContents().getFail());
		
		
		List<ContentVo> signList = packVo.getContents().getSign();
		if(signList!=null){
			for(int i=0;i<signList.size();i++){
				alConts.add(signList.get(i));
			}
		}
		
		alConts.add(packVo.getContents().getSymbol());
		alConts.add(packVo.getContents().getLogo());
		
		sbXml.append("<send-id>" +send_id+ "</send-id>\n");
		sbXml.append("<send-name>" + packVo.getSend_name() + "</send-name>\n");
		sbXml.append("<receive-id>" + String.join(";", packVo.getReceive_id()) + "</receive-id>\n");
		sbXml.append("<date>" + packVo.getDate() + "</date>\n");
		sbXml.append("<title>" + packVo.getTitle() + "</title>\n");
		sbXml.append("<doc-id>" + packVo.getDoc_id() + "</doc-id>\n");
		sbXml.append("<doc-type type=\"" + packVo.getDoc_type_type() + "\" dept=\"" + packVo.getDoc_type_dept() + "\" name=\""+ 
				packVo.getDoc_type_name() + "\"/>\n");
		sbXml.append("<send-gw>" + packVo.getSend_gw() + "</send-gw>\n");
		sbXml.append("<dtd-version>" + packVo.getDtd_version()+ "</dtd-version>\n");
		sbXml.append("<xsl-version>" + packVo.getXsl_version()+ "</xsl-version>\n");
		sbXml.append("</header>\n");
		
		sbXml.append("<contents>\n");
		
		for(int i=0;i<alConts.size();i++){
			ContentVo contVo = new ContentVo();
		
			contVo = (ContentVo) alConts.get(i);
			
			if(contVo != null){
				String cont_role = contVo.getContent_role();
				//if(!"".equals(cont_role)){
					sbXml.append("<content content-role=\"" + cont_role + "\" filename=\"" + contVo.getFilename()+ "\" content-type=\""
							+ contVo.getContent_type() + "\" content-transfer-encoding=\"" + contVo.getContent_transfer_encoding() +"\" "
							+ "charset=\"" +contVo.getCharset() + "\">" + contVo.getContent() + "</content>\n");
				//}
			}
		}		
		
		sbXml.append("</contents>\n");
		sbXml.append("</pack>\n");		
		
		return sbXml.toString();
	}
	
	public String validateWithDTDUsingDOM(String xml) throws ParserConfigurationException, IOException{
		String err = "";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			factory.setNamespaceAware(true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			builder.setErrorHandler(
				new ErrorHandler() {
					public void warning(SAXParseException e) throws SAXException {
						e.printStackTrace();
						System.out.println("WARNING : " + e.getMessage()); // do nothing
					}

					public void error(SAXParseException e) throws SAXException {
						e.printStackTrace();
						System.out.println("ERROR : " + e.getMessage());
						throw e;
					}

					public void fatalError(SAXParseException e) throws SAXException {
						e.printStackTrace();
						System.out.println("FATAL : " + e.getMessage());
						throw e;
					}
				});
			builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
			//return true;
		} catch (ParserConfigurationException pce) {
			err += pce.getMessage()+"\n";
			throw pce;
		} catch (IOException io) {
			err = io.getMessage()+"\n";
			throw io;
		} catch (SAXException se){
			err += se.getMessage()+"\n";
			//return false;
		}
		return err;
	}
}
