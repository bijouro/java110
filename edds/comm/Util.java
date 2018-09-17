package com.saerom.edds.comm;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import sun.misc.BASE64Decoder;

public class Util {
	
	private static Logger logger = Logger.getLogger(Class.class.getName());
	
	/**
	 * base64 decoding
	 * 
	 * @param arg
	 * @return
	 * @throws IOException
	 */
	
	// base64String => string
/*	public static String decodeBase64(String arg) {
		String str = null;
		byte[] decod = null;
		BASE64Decoder decoder = new BASE64Decoder();
		System.out.println("%%%%% str #### ==>" + str);
		try {
			decod = decoder.decodeBuffer(arg);
			str = new String(decod, "EUC-KR");
			
			System.out.println("%%%%% str ==>" + str);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return str;
    }
    */
	
	
	// base64String => string
	public static String decodeBase64(String arg) {
		String str = null;
		try {
			str = new String(Base64.decodeBase64(arg), "euc-kr");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return str;
    }
	
	//string=> base64String
	public static String encodeBase64(String arg) {
		
		if(arg!=null){
			return Base64.encodeBase64String(arg.getBytes());
		}else{
			return "";
		}
    }
	
	//file => base64String 
	public static String fileToBase64String(String path) {		
		InputStream fis= null;
		ByteArrayOutputStream bos = null;
		
		File file = new File(path);
		
		String bas64String = null;
		if(file.exists()){
			try {
				fis = new FileInputStream(file);
				bos = new ByteArrayOutputStream();
				
				int len = 0;
				byte[] buf = new byte[1024];
				while((len = fis.read(buf)) != -1){
					bos.write(buf, 0, len);
				}
				
				byte[] arrFile = bos.toByteArray();
				bas64String = new String(Base64.encodeBase64(arrFile));
				//logger.debug(bas64String);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
	 	        try {
	 	        	if(fis!=null){
	 	        		fis.close();
	 	        	}
	 	        	if(bos!=null){
	 	        		bos.close();
	 	        	}
	 	        } catch (IOException e) {
	 	            e.printStackTrace();
	 	        }
	 	    }
		}
		return bas64String;
	}
	    
	
    
	// base64String => file
 	public static boolean base64StringToFile(String encodedCont, String path, String fileName){
 	    BufferedOutputStream bos = null;
 	    boolean rst = false;
 	    
 	    try {
			FileHandling.makePath(path);
			File file = new File(path + fileName);
			
			file.createNewFile();
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(Base64.decodeBase64(encodedCont));
			
			rst = true;
 	    } catch (IOException e) {
 			e.printStackTrace();
 		} finally {
 	        try {
 	        	if(bos!=null){
 	        		bos.close();
 	        	}
 	        } catch (IOException e) {
 	            e.printStackTrace();
 	        }
 	    }
		return rst;
 	}
 	
 	//array => string
 	public static String arrayToString(String args[], String mark) {
	    StringBuilder builder = new StringBuilder(); 
	    for (int i = 0; i < args.length;) { 
	        builder.append(args[i]); 
	        if (++i < args.length) { 
	            builder.append(mark); 
	        } 
	    } 
	    return builder.toString(); 
	}
	
 	
 	// 현재 날짜
 	public static String getToday(){
 		Calendar cal = Calendar.getInstance();		
 		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
 	}
 	
 	// 현재 시간
 	public static String getCurrTime(){
 		Calendar cal = Calendar.getInstance();
 		return new SimpleDateFormat("HHmmss").format(cal.getTime());
 	}
 	
 	// 현재 날짜+시간
 	public static String getTodayCurrTime(){
 		Calendar cal = Calendar.getInstance();
 		return new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
 	}
 	
 	// 현재 날짜+시간
  	public static String getDocFormatCurDateTime(){
  		Calendar cal = Calendar.getInstance();
  		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
  	}
  	
  	// value일 전 날짜
  	public static String dateCal(int value) {
 		Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    //cal.add(Calendar.YEAR, 1); // 1년을 더한다.
	    //cal.add(Calendar.MONTH, 1); // 한달을 더한다. 
	    cal.add(Calendar.DAY_OF_YEAR, value); // 하루를 더한다. 
	    //cal.add(Calendar.HOUR, 1); // 시간을 더한다
	     
	    SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
	    String strDate = fm.format(cal.getTime());    
	    
	    return strDate;
	}

 	// 파일 사이즈 계산
	public static long getFileSize(String path) {
		long fSize = 0;
		
		File f = new File(path);
		if(f.exists()){
			fSize = f.length();
		}
		
		return fSize;
	}
	
	
	public static int attachFileDownload(String host, String path, String targetPath){
		String filePath = null;
		FileOutputStream fos = null;
		InputStream is = null;
		
		int size = 0;
		
		try {
			URL url = new URL(host + path);
		 		  
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			
			String filepath = ""; 
			String filename = "";
			
			if(path.contains("/")){
				filepath = path.substring(0, path.lastIndexOf("/")+1);
				filename = path.substring(path.lastIndexOf("/")+1, path.length());
			}
			
			File targetDir = new File(targetPath);
			if(!targetDir.exists()){
				targetDir.mkdirs();
			}

			File fileRoot = new File(targetDir+"\\"+filename);
			fileRoot.createNewFile();

			fos = new FileOutputStream(fileRoot);
			is = urlConnection.getInputStream();
			
			size = urlConnection.getContentLength();
			byte[] buffer = new byte[1024];

			int bufferLength = 0;
			
			while((bufferLength = is.read(buffer))>0){

				fos.write(buffer, 0, bufferLength);
				size +=bufferLength;
			}
			fos.flush();
			fos.close();
			
			is.close();	 
			
			if(size == bufferLength)filePath = fileRoot.getPath();			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
 	        try {
 	        	if(fos != null){
 	        		fos.flush();
 	        		fos.close();
 	        	}
 	        	if(is != null){
 	        		is.close();
 	        	}
 	        } catch (IOException e) {
 	            e.printStackTrace();
 	        }
 	    }
		return size;
	}
	
	public static boolean delFile(String source) {
		boolean isSucc = false;
		File f = new File(source);
		if(f.isFile()){
			isSucc = f.delete();
		}
		return isSucc;
	}	
	
	public static boolean copyFile(String source, String target) {
		boolean isSucc = false;
		
		File sourceFile = new File(source);
		File targetFile = new File(target);
		
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		
		try {
			
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(targetFile);
		   
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();

			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
			
			isSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fcout != null) try{fcout.close();}catch(IOException e){}
			if(fcin != null) try{fcin.close();}catch(IOException e){}
			if(outputStream!= null) try{outputStream.close();}catch(IOException e){}
			if(inputStream!= null) try{inputStream.close();}catch(IOException e){}
		}
		return isSucc;
	}
	
	public static void StringToFile(String fileName, String content){
			
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter( new FileWriter(fileName));
			bw.write(content);				
			bw.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {  if(bw != null) bw.close(); } catch (IOException e) {e.printStackTrace();}
		}
	
	}
	
	public static void delFoler(String path){
		boolean rst = false;
		File file = new File(path);
		
		if(file.exists()){
			//폴더내 파일을 배열로 가져온다.
			File[] tempFile = file.listFiles();

			if(tempFile.length >0){
				for (int i = 0; i < tempFile.length; i++) {
					
					if(tempFile[i].isFile()){
						tempFile[i].delete();
					}else{
						//재귀함수
						delFoler(tempFile[i].getPath());
					}
					tempFile[i].delete();				
				}
				file.delete();
			}
		}
	}
	
	
	public static String unitaryReplace(String type, String unit, String attr) {
	
 		String tmp = "";
 		if("px".equals(type)){
	 		if("mm".equals(unit)){
	 			tmp = unitaryConvert(attr.substring(0, attr.length()-2), "px");
			} else {
				tmp = attr;
			}
	 		tmp.replace("px", "");	
 		} else { 			
 			if("mm".equals(unit)){
	 			tmp = unitaryConvert(attr, "mm");
			} 
 		}
 		return tmp;
 	}
 	
 	// 1mm = 3.779528px, 1px=0.264583mm
 	public static String unitaryConvert(String num, String toUnit) {
 		
		double  criteria_mm = 3.78;
		double  criteria_px = 0.264583;
		Double dbnum =(double) 0;
		if("px".equals(toUnit)){ // mm => px
			 dbnum = (Double.valueOf(num).doubleValue() * criteria_mm);
		} else { // px => mm
			dbnum = (Double.valueOf(num).doubleValue() * criteria_px);			
		}
		
		return String.format("%.1f", dbnum);
 	}
 	
 	public static String[] imgUnitConvert(Element element, String wdefault, String hdefault){
		String width = element.attr("width") !=""? element.attr("width"):wdefault;
		String height = element.attr("height") !=""? element.attr("height"):hdefault;
		String width_unit = width.substring(width.length()-2, width.length());
		String height_unit = height.substring(height.length()-2, height.length());
		
		if("mm".equals(width_unit)){
			width = unitaryConvert(width.substring(0, width.length()-2), "px");
		}
		
		if("mm".equals(height_unit)){
			height = Util.unitaryConvert(height.substring(0, height.length()-2), "px");
		}
		
		width.replace("px", "");
		height.replace("px", "");
		
		String[] result= new String[2];
		result[0] = width;
		result[1] = height;
		return result;
 	}
 	
	
	// p, table 태그와 style의 속성 값의 단위를 변환. px=>mm (발신문서)	
	public static String bodyContUnitConvertToMm(org.jsoup.nodes.Document docTag, String[] tagList, String toUnit) {
		org.jsoup.nodes.Document doc = docTag;	
		String result="";		
		Elements tagEl;
		
		for(String tag : tagList){
			tagEl = doc.select(tag);
			
			for(int i=0;i<tagEl.size();i++){
				
				String width = tagEl.get(i).attr("width");
				String height = tagEl.get(i).attr("height");
				String style = tagEl.get(i).attr("style");
				
				if(width.length()>0){
					String width_unit = "mm";
					//String width_unit = width.substring(width.length()-2, width.length());
					tagEl.get(i).attr("width", unitaryReplace(toUnit, width_unit, width)+"mm");
				}
				
				if(height.length()>0){
					String height_unit = "mm";
					//String height_unit = height.substring(height.length()-2, height.length());
					tagEl.get(i).attr("height",unitaryReplace(toUnit, height_unit, height)+"mm");
				}
				
				if(style.length()>0){
					String styleVal[] = style.split(";");
					
					String styleAttr = "";
					for(String iAttr : styleVal){
						String attrVal[]=iAttr.split(":");
						String convertVal=iAttr;						
					
						if(attrVal.length>0){
							if(attrVal[1].length()>2){							
								String style_unit = attrVal[1].substring(attrVal[1].length()-2, attrVal[1].length());
								
								if("px".equals(style_unit)){
									attrVal[1] = unitaryConvert(attrVal[1].substring(0, attrVal[1].length()-2), "mm")+"mm";
								}
								
								//attrVal[1] = attrVal[1].replace("px", "");
								
								convertVal = String.join(":", String.join(":", attrVal));
							}								
						}							
						styleAttr += convertVal+";";
					}
					tagEl.get(i).attr("style",styleAttr);
				}	
			}	
		}		
			
		result = pushAlignAttr(doc).replace("<body>", "").replace("</body>", "");

		return result;
	}
	
	
	/*
	 * table 태그가  p태그 노드  자식으로  존재 할 경우  상위 p 태그가 강제로 닫히고, table 태그 다음 닫히는 </p> 태그 때문에 p 노드가 하나 더 생김 
	 *  예 )  <p... align="adjust"> <table ...> </table></p> => <p ...align="adjust" ></p> <table ....></table><p></p>
	 *  상위 노드가 같은 레벨 노드로 되면서  align 속성이 table에 먹히지 않아서  table 태그에 직접 넣어줌.     
	 */	
	public static String pushAlignAttr(org.jsoup.nodes.Document doc){
		org.jsoup.nodes.Document rst = doc;
		
		for(int i=0;i<rst.select("table").size();i++){
			if(rst.select("table").get(i).previousElementSibling().hasAttr("align")){
				rst.select("table").get(i).attr("align",rst.select("table").get(i).previousElementSibling().attr("align"));
			}			
		}
		
		return rst.select("body").toString();
	}
	
	
	// p, table 태그와 style의 속성 값의 단위를 변환. mm=>px (수신문서)	
	public static String bodyContUnitConvertToPx(org.jsoup.nodes.Document docTag, String[] tagList, String toUnit) {
		org.jsoup.nodes.Document doc = docTag;	
		String result="";		
		Elements tagEl;
		
		for(String tag : tagList){
			tagEl = doc.select(tag);
			
			for(int i=0;i<tagEl.size();i++){
				
				String width = tagEl.get(i).attr("width");
				String height = tagEl.get(i).attr("height");
				String style = tagEl.get(i).attr("style");
				
				if(width.length()>2){
					String width_unit = width.substring(width.length()-2, width.length());
					if(!"mm".equals(width_unit)){
						width_unit = "px";
					}
					tagEl.get(i).attr("width", unitaryReplace(toUnit, width_unit, width));
				}
				
				if(height.length()>2){
					String height_unit = height.substring(height.length()-2, height.length());	
					if(!"mm".equals(height_unit)){
						height_unit = "px";
					}
					tagEl.get(i).attr("height",unitaryReplace(toUnit, height_unit, height));
				}
				
				if(style.length()>0){
					String styleVal[] = style.split(";");
					
					String styleAttr = "";
					for(String iAttr : styleVal){
						String attrVal[]=iAttr.split(":");
						String convertVal=iAttr;						
					
						if(attrVal.length>1){
							if(attrVal[1].length()>2){							
								String style_unit = attrVal[1].substring(attrVal[1].length()-2, attrVal[1].length());
								
								if("mm".equals(style_unit)){
									attrVal[1] = unitaryConvert(attrVal[1].substring(0, attrVal[1].length()-2), "px")+"px";
								}
								
								//attrVal[1] = attrVal[1].replace("px", "");
								
								convertVal = String.join(":", String.join(":", attrVal));
							}								
						}							
						styleAttr += convertVal+";";
					}
					tagEl.get(i).attr("style",styleAttr);
				}	
			}	
		}
				
		/*
		 * table 태그가  p태그 노드  자식으로  존재 할 경우  상위 p 태그가 강제로 닫히고, table 태그 다음 닫히는 </p> 태그 때문에 p 노드가 하나 더 생김 
		 *  예 )  <p... align="adjust"> <table ...> </table></p> => <p ...align="adjust" ></p> <table ....></table><p></p>
		 *  상위 노드가 같은 레벨 노드로 되면서  align 속성이 table에 먹히지 않아서  table 태그에 직접 넣어줌.     
		 */		
		
		
		Elements eleTable = doc.select("table");
		
		for(Element el : eleTable){			
			if(el.previousElementSibling().hasAttr("align")){
				eleTable.attr("align",el.previousElementSibling().attr("align"));
			}			
		}
		
		result = doc.select("body").toString().replace("<body>", "").replace("</body>", "");
		
		return result;
	}
	
	public static String sendXmlBody(String excptList[], String recvList[], org.jsoup.nodes.Document doc) {
		String tagList[] = Configuration.getInstance().getValue("convert_tag").split(",");
		String toUnit = "".equals(Configuration.getInstance().getValue("doc_unit"))?"px":Configuration.getInstance().getValue("doc_unit");
		
		String rst = "";
		
		boolean excptFlag = false;
		int iCnt = 0;
		
		for(int i=0; i<recvList.length;i++){
			for(int j=0;j<excptList.length;j++){
				if(excptList[j].equals(recvList[i])){
					iCnt++;
					break;
				}
			}			
		}
		
		if(iCnt == recvList.length){
			excptFlag = true;
		}
		
		logger.info("	> 예외기관으로 처리 : " + excptFlag);
		
		
		if("px".equals(toUnit)){
			if(excptFlag){ //변환 함
				logger.info("	> mm 단위로 변환 함");
				rst = bodyContUnitConvertToMm(doc, tagList, "mm");
			} else {// 변환 안암
				logger.info("	> 변환 함 하지 않음");
				rst = pushAlignAttr(doc).replace("<body>", "").replace("</body>", "").toString();
			}
		} else { // mm
			if(excptFlag){ //변환 안함
				logger.info("	> 변환 함 하지 않음");
				rst = pushAlignAttr(doc).replace("<body>", "").replace("</body>", "").toString();
			} else {
				logger.info("	> mm 단위로 변환 함");
				rst = bodyContUnitConvertToMm(doc, tagList, "mm");
			}
		}
		
		return rst;
	}

	
	/*{regex}로 구분되는 string 문자열의 빈값을 없애고 join.
	* String str 문자열
	* String regex 구분자
	* ex) 1,2,,3,4,, => 1,2,3,4
	*/ 
	public static String joinRegex(String str, String regex){
	        String [] str_arr = str.split(regex);       
	        ArrayList<String> alrec = new ArrayList<String>((Arrays.asList(str_arr)));
	        return String.join(regex, alrec);
	}


	public static String getMimeType(String filename) {
		String fileExt = filename.substring(filename.lastIndexOf("."), filename.length());
		String mimeType = "";
		
		switch(fileExt) {
		
		case ".*"	:
			mimeType = "application/octet-stream";
			break;
		
		case ".323"	:
			mimeType = "text/h323";
			break;
		
		case ".acx"	:
			mimeType = "application/internet-property-stream";
			break;
		
		case ".ai"	:
			mimeType = "application/postscript";
			break;
		
		case ".aif"	:
			mimeType = "audio/x-aiff";
			break;
		
		case ".aifc":
			mimeType = "audio/aiff";
			break;
		
		case ".aiff":
			mimeType = "audio/aiff";
			break;
		
		case ".asf"	:
			mimeType = "video/x-ms-asf";
			break;
		
		case ".asr"	:
			mimeType = "video/x-ms-asf";
			break;
		
		case ".asx"	:
			mimeType = "video/x-ms-asf";
			break;
		
		case ".au"	:
			mimeType = "audio/basic";
			break;
		
		case ".avi"	:
			mimeType = "video/x-msvideo";
			break;
		
		case ".axs"	:
			mimeType = "application/olescript";
			break;
		
		case ".bas"	:
			mimeType = "text/plain";
			break;
		
		case ".bcpio":
			mimeType = "application/x-bcpio";
			break;
		
		case ".bin"	:
			mimeType = "application/octet-stream";
			break;
		
		case ".bmp"	:
			mimeType = "image/bmp";
			break;
		
		case ".c"	:
			mimeType = "text/plain";
			break;
		
		case ".cat"	:
			mimeType = "application/vndms-pkiseccat";
			break;
		
		case ".cdf"	:
			mimeType = "application/x-cdf";
			break;
		
		case ".cer"	:
			mimeType = "application/x-x509-ca-cert";
			break;
		
		case ".clp"	:
			mimeType = "application/x-msclip";
			break;
		
		case ".cmx"	:
			mimeType = "image/x-cmx";
			break;
		
		case ".cod"	:
			mimeType = "image/cis-cod";
			break;
		
		case ".cpio":
			mimeType = "application/x-cpio";
			break;
		
		case ".crd"	:
			mimeType = "application/x-mscardfile";
			break;
		
		case ".crl"	:
			mimeType = "application/pkix-crl";
			break;
		
		case ".crt"	:
			mimeType = "application/x-x509-ca-cert";
			break;
		
		case ".csh"	:
			mimeType = "application/x-csh";
			break;
		
		case ".css"	:
			mimeType = "text/css";
			break;
		
		case ".dcr"	:
			mimeType = "application/x-director";
			break;
		
		case ".der"	:
			mimeType = "application/x-x509-ca-cert";
			break;
		
		case ".dib"	:
			mimeType = "image/bmp";
			break;
		
		case ".dir"	:
			mimeType = "application/x-director";
			break;
		
		case ".dll"	:
			mimeType = "application/x-msdownload";
			break;
		
		case ".doc"	:
			mimeType = "application/msword";
			break;
		
		case ".docx":
			mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			break;	
		
		case ".dot"	:
			mimeType = "application/msword";
			break;
		
		case ".dotx":
			mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
			break;
		
		case ".dvi"	:
			mimeType = "application/x-dvi";
			break;
		
		case ".dxr"	:
			mimeType = "application/x-director";
			break;
		
		case ".eml"	:
			mimeType = "message/rfc822";
			break;	
		
		case ".eps"	:
			mimeType = "application/postscript";
			break;	
		
		case ".etx"	:
			mimeType = "text/x-setext"	;
			break;
		
		case ".evy"	:
			mimeType = "application/envoy";
			break;
		
		case ".exe"	:
			mimeType = "application/octet-stream";
			break;	
		
		case ".fif"	:
			mimeType = "application/fractals";
			break;	
		
		case ".flr"	:
			mimeType = "x-world/x-vrml";
			break;	
		
		case ".gif"	:
			mimeType = "image/gif";
			break;	
		
		case ".gtar":
			mimeType = "application/x-gtar";
			break;
		
		case ".gz"	:
			mimeType = "application/x-gzip";
			break;	
		
		case ".h"	:
			mimeType = "text/plain";
			break;	
		
		case ".hdf"	:
			mimeType = "application/x-hdf";
			break;
		
		case ".hlp"	:
			mimeType = "application/winhlp";
			break;
		
		case ".hqx"	:
			mimeType = "application/mac-binhex40";
			break;	
		
		case ".hta"	:
			mimeType = "application/hta";
			break;	
		
		case ".htc"	:
			mimeType = "text/x-component";
			break;
		
		case ".htm"	:
			mimeType = "text/html";
			break;	
		
		case ".html":
			mimeType = "text/html";
			break;
		
		case ".htt"	:
			mimeType = "text/webviewhtml";
			break;
		
		case ".ico"	:
			mimeType = "image/x-icon";
			break;
		
		case ".ief"	:
			mimeType = "image/ief";
			break;	
		
		case ".iii"	:
			mimeType = "application/x-iphone";
			break;	
		
		case ".ins"	:
			mimeType = "application/x-internet-signup";
			break;
		
		case ".isp"	:
			mimeType = "application/x-internet-signup";
			break;	
		
		case ".IVF"	:
			mimeType = "video/x-ivf";
			break;
		
		case ".jfif":
			mimeType = "image/pjpeg"	;
			break;
		
		case ".jpe"	:
			mimeType = "image/jpeg";
			break;
		
		case ".jpeg"	:
			mimeType = "image/jpeg";
			break;
		
		case ".jpg"	:
			mimeType = "image/jpg";
			break;
		
		case ".js"	:
			mimeType = "application/x-javascript";
			break;
		
		case ".latex"	:
			mimeType = "application/x-latex";
			break;
		
		case ".lsf"	:
			mimeType = "video/x-la-asf";
			break;
		
		case ".lsx"	:
			mimeType = "video/x-la-asf";
			break;
		
		case ".m13"	:
			mimeType = "application/x-msmediaview";
			break;
		
		case ".m14"	:
			mimeType = "application/x-msmediaview";
			break;
		
		case ".m1v"	:
			mimeType = "video/mpeg";
			break;
		
		case ".m3u"	:
			mimeType = "audio/x-mpegurl";
			break;
		
		case ".man"	:
			mimeType = "application/x-troff-man";
			break;
		
		case ".mdb"	:
			mimeType = "application/x-msaccess";
			break;
		
		case ".me"	:
			mimeType = "application/x-troff-me";
			break;
		
		case ".mht"	:
			mimeType = "message/rfc822";
			break;
		
		case ".mhtml"	:
			mimeType = "message/rfc822";
			break;
		
		case ".mid"	:
			mimeType = "audio/mid";
			break;
		
		case ".mny"	:
			mimeType = "application/x-msmoney";
			break;
		
		case ".mov"	:
			mimeType = "video/quicktime";
			break;
		
		case ".movie"	:
			mimeType = "video/x-sgi-movie";
			break;
		
		case ".mp2"	:
			mimeType = "video/mpeg";
			break;
		
		case ".mp3"	:
			mimeType = "audio/mpeg";
			break;
		
		case ".mpa"	:
			mimeType = "video/mpeg";
			break;
		
		case ".mpe"	:
			mimeType = "video/mpeg";
			break;
		
		case ".mpeg"	:
			mimeType = "video/mpeg";
			break;
		
		case ".mpg"	:
			mimeType = "video/mpeg";
			break;
		
		case ".mpp"	:
			mimeType = "application/vnd.ms-project";
			break;
		
		case ".mpv2"	:
			mimeType = "video/mpeg";
			break;
		
		case ".ms"	:
			mimeType = "application/x-troff-ms";
			break;
		
		case ".mvb"	:
			mimeType = "application/x-msmediaview";
			break;
		
		case ".nc"	:
			mimeType = "application/x-netcdf";
			break;
		
		case ".nws"	:
			mimeType = "message/rfc822";
			break;
		
		case ".oda"	:
			mimeType = "application/oda";
			break;
		
		case ".ods"	:
			mimeType = "application/oleobject";
			break;
		
		case ".p10"	:
			mimeType = "application/pkcs10";
			break;
		
		case ".p12"	:
			mimeType = "application/x-pkcs12";
			break;
		
		case ".p7b"	:
			mimeType = "application/x-pkcs7-certificates";
			break;
		
		case ".p7c"	:
			mimeType = "application/pkcs7-mime";
			break;
		
		case ".p7m"	:
			mimeType = "application/pkcs7-mime";
			break;
		
		case ".p7r"	:
			mimeType = "application/x-pkcs7-certreqresp";
			break;
		
		case ".p7s"	:
			mimeType = "application/pkcs7-signature";
			break;
		
		case ".pbm"	:
			mimeType = "image/x-portable-bitmap";
			break;
		
		case ".pdf"	:
			mimeType = "application/pdf";
			break;
		
		case ".pfx"	:
			mimeType = "application/x-pkcs12";
			break;
		
		case ".pgm"	:
			mimeType = "image/x-portable-graymap";
			break;
		
		case ".pko"	:
			mimeType = "application/vndms-pkipko";
			break;
		
		case ".pma"	:
			mimeType = "application/x-perfmon";
			break;
		
		case ".pmc"	:
			mimeType = "application/x-perfmon";
			break;
		
		case ".pml"	:
			mimeType = "application/x-perfmon";
			break;
		
		case ".pmr"	:
			mimeType = "application/x-perfmon";
			break;
		
		case ".pmw"	:
			mimeType = "application/x-perfmon";
			break;
		
		case ".pnm"	:
			mimeType = "image/x-portable-anymap";
			break;
		
		case ".pot"	:
			mimeType = "application/vnd.ms-powerpoint";
			break;
		
		case ".pots"	:
			mimeType = "application/vnd.openxmlformats-officedocument.presentationml.template";
			break;
		
		case ".ppm"	:
			mimeType = "image/x-portable-pixmap";
			break;
		
		case ".pps"	:
			mimeType = "application/vnd.ms-powerpoint";
			break;
		
		case ".ppsx"	:
			mimeType = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
			break;
		
		case ".ppt"	:
			mimeType = "application/vnd.ms-powerpoint";
			break;
		
		case ".ppts"	:
			mimeType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
			break;
		
		case ".prf"	:
			mimeType = "application/pics-rules";
			break;
		
		case ".ps"	:
			mimeType = "application/postscript";
			break;
		
		case ".pub"	:
			mimeType = "application/x-mspublisher";
			break;
		
		case ".qt"	:
			mimeType = "video/quicktime";
			break;
		
		case ".ra"	:
			mimeType = "audio/x-pn-realaudio";
			break;
		
		case ".ram"	:
			mimeType = "audio/x-pn-realaudio";
			break;
		
		case ".ras"	:
			mimeType = "image/x-cmu-raster";
			break;
		
		case ".rgb"	:
			mimeType = "image/x-rgb";
			break;
		
		case ".rmi"	:
			mimeType = "audio/mid";
			break;
		
		case ".roff"	:
			mimeType = "application/x-troff";
			break;
		
		case ".rtf"	:
			mimeType = "application/rtf";
			break;
		
		case ".rtx"	:
			mimeType = "text/richtext";
			break;
		
		case ".scd"	:
			mimeType = "application/x-msschedule";
			break;
		
		case ".sct"	:
			mimeType = "text/scriptlet";
			break;
		
		case ".setpay"	:
			mimeType = "application/set-payment-initiation";
			break;
		
		case ".setreg"	:
			mimeType = "application/set-registration-initiation";
			break;
		
		case ".sh"	:
			mimeType = "application/x-sh";
			break;
		
		case ".shar"	:
			mimeType = "application/x-shar";
			break;
		
		case ".sit"	:
			mimeType = "application/x-stuffit";
			break;
		
		case ".snd"	:
			mimeType = "audio/basic";
			break;
		
		case ".spc"	:
			mimeType = "application/x-pkcs7-certificates";
			break;
		
		case ".spl"	:
			mimeType = "application/futuresplash";
			break;
		
		case ".src"	:
			mimeType = "application/x-wais-source";
			break;
		
		case ".sst"	:
			mimeType = "application/vndms-pkicertstore";
			break;
		
		case ".stl"	:
			mimeType = "application/vndms-pkistl";
			break;
		
		case ".stm"	:
			mimeType = "text/html";
			break;
		
		case ".sv4cpi"	:
			mimeType = "application/x-sv4cpio";
			break;
		
		case ".sv4crc"	:
			mimeType = "application/x-sv4crc";
			break;
		
		case ".t"	:
			mimeType = "application/x-troff";
			break;
		
		case ".tar"	:
			mimeType = "application/x-tar";
			break;
		
		case ".tcl"	:
			mimeType = "application/x-tcl";
			break;
		
		case ".tex"	:
			mimeType = "application/x-tex";
			break;
		
		case ".texi"	:
			mimeType = "application/x-texinfo";
			break;
		
		case ".texinf"	:
			mimeType = "application/x-texinfo";
			break;
		
		case ".tgz"	:
			mimeType = "application/x-compressed";
			break;
		
		case ".tif"	:
			mimeType = "image/tiff";
			break;
		
		case ".tiff"	:
			mimeType = "image/tiff";
			break;
		
		case ".tr"	:
			mimeType = "application/x-troff";
			break;
		
		case ".trm"	:
			mimeType = "application/x-msterminal";
			break;
		
		case ".tsv"	:
			mimeType = "text/tab-separated-values";
			break;
		
		case ".txt"	:
			mimeType = "text/plain";
			break;
		
		case ".uls"	:
			mimeType = "text/iuls";
			break;
		
		case ".ustar"	:
			mimeType = "application/x-ustar";
			break;
		
		case ".vcf"	:
			mimeType = "text/x-vcard";
			break;
		
		case ".wav"	:
			mimeType = "audio/wav";
			break;
		
		case ".wcm"	:
			mimeType = "application/vnd.ms-works";
			break;
		
		case ".wdb"	:
			mimeType = "application/vnd.ms-works";
			break;
		
		case ".wks"	:
			mimeType = "application/vnd.ms-works";
			break;
		
		case ".wmf"	:
			mimeType = "application/x-msmetafile";
			break;
		
		case ".wps"	:
			mimeType = "application/vnd.ms-works";
			break;
		
		case ".wri"	:
			mimeType = "application/x-mswrite";
			break;
		
		case ".wrl"	:
			mimeType = "x-world/x-vrml";
			break;
		
		case ".wrz"	:
			mimeType = "x-world/x-vrml";
			break;
		
		case ".xaf"	:
			mimeType = "x-world/x-vrml";
			break;
		
		case ".xbm"	:
			mimeType = "image/x-xbitmap";
			break;
		
		case ".xla"	:
			mimeType = "application/vnd.ms-excel";
			break;
		
		case ".xlam"	:
			mimeType = "application/vnd.ms-excel.addin.macroEnabled.12";
			break;
		
		case ".xlc"	:
			mimeType = "application/vnd.ms-excel";
			break;
		
		case ".xlm"	:
			mimeType = "application/vnd.ms-excel";
			break;
		
		case ".xls"	:
			mimeType = "application/vnd.ms-excel";
			break;
		
		case ".xlsb"	:
			mimeType = "application/vnd.ms-excel.sheet.binary.macroEnabled.12";
			break;
		
		case ".xlsx"	:
			mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			break;
		
		case ".xlt"	:
			mimeType = "application/vnd.ms-excel";
			break;
		
		case ".xlts"	:
			mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
			break;
		
		case ".xlw"	:
			mimeType = "application/vnd.ms-excel";
			break;
		
		case ".xml"	:
			mimeType = "text/xml";
			break;
		
		case ".xof"	:
			mimeType = "x-world/x-vrml";
			break;
		
		case ".xpm"	:
			mimeType = "image/x-xpixmap";
			break;
		
		case ".xsl"	:
			mimeType = "text/xml";
			break;
		
		case ".xwd"	:
			mimeType = "image/x-xwindowdump";
			break;
		
		case ".z"	:
			mimeType = "application/x-compress";
			break;
		
		case ".zip"	:
			mimeType = "application/x-zip-compressed";
			break;
		
		default :
			mimeType = "application/octet-stream";
			break;
		}		
		return mimeType;
	}
}
