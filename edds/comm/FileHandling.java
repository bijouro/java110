package com.saerom.edds.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class FileHandling {
	
	private static Logger logger = Logger.getLogger(Class.class.getName());
	
	//xml 파일 리스트 반환
	public static ArrayList<File> getFileList(String path){
		ArrayList<File> arrFileList = new ArrayList<File>();
		
        File f = new File(path);        
        File[] files = f.listFiles();
        
        for (int i = 0; i<files.length; i++){
            if(files[i].getName().endsWith(".xml")){
            	arrFileList.add(files[i]);
            }
        }
		return arrFileList;
	}
	
	public static void makePath(String path){
		File d = new File(path);		
	    if (!d.isDirectory()) {
	    	d.mkdirs();
	    }
	}
	
	
	public boolean hasFile(String path){
		boolean hasFile = false;
		File f = new File(path);
		if(f.isFile()){
			hasFile = true;
		}
		return hasFile;
	}
	
	
	// xmlString => 파일로 저장
	public void domToXmlFile(String path, String fileName, Document doc){
		Document document = null;
	    Source source = new DOMSource(doc);
	    
	    makePath(path);
	    
	    File file = new File(path+fileName);
	    Result result = new StreamResult(file);
	    Transformer xformer;
		try {
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"pack.dtd");
			xformer.transform(source, result);
			
			logger.debug("Doc type system :: " + xformer.getOutputProperty(OutputKeys.DOCTYPE_SYSTEM));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	

	// 파일 이동
	public static boolean moveFile(String source, String target){
		
		File f = new File(source);
		File tf = new File(target);
		
		
		// 파일이 있으면 기존 파일의 이름을 현재 날짜, 시간을 붙인 이름으로 변경.
		if(tf.exists()){
			String tName = tf.getPath();
			tName = tName.substring(0, target.lastIndexOf("."))+ "_" 
					+ Util.getTodayCurrTime() + tName.substring(target.lastIndexOf("."), tName.length());
			tf.renameTo(new File(tName));
		}
		
		boolean isSucc = copyFile(f.getPath(), target);
		isSucc = delFile(source);
		
		return isSucc;
	}
	
	public static boolean copyFile(String source, String target) {
		boolean isSucc = false;
		
		File sourceFile = new File(source);
		File targetFile = new File(target);
		
		/*if (!targetFile.exists()) {
		    try{
		    	targetFile.mkdir();
		    } 
		    catch(SecurityException se){
		    	se.printStackTrace();
		    }
		}*/
		
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		
		try {
			
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(targetFile);
		   
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();
		    
			// 파일 사이즈
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
	
	public static boolean delFile(String source) {
		boolean isSucc = false;
		File f = new File(source);
		if(f.isFile()){
			isSucc = f.delete();
		}
		return isSucc;
	}
	
}
