package com.saerom.edds.comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

public class LogFileWrite {
	private static Logger logger = Logger.getLogger(Class.class);
	String logFile = Configuration.getInstance().getValue("csv_log_dir")+"//"+Util.getToday()+".log";
	String logfile_use = (Configuration.getInstance().getValue("logfile_use"));
	
	//로그 파일 생성
	private void makeLogFile(){
		File f = new File(logFile);
		
		if(!f.isFile() & ("Y".equals(logfile_use))){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String toDay = Util.getToday();
			
			logger.debug(toDay);
			toDay = toDay.substring(0, 4)+"-"+toDay.substring(4, 6)+"-"+toDay.substring(6, 8);
			
			
			append("============================================================\n");
			append("  	"+toDay);
			append("============================================================\n"); 
		}
	}

	public void append(String str){		
		if("Y".equals(logfile_use)){
			//로그파일 유무 확인 - 없으면 생성.
			makeLogFile();
			BufferedWriter bw = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(logFile, true);
				bw = new BufferedWriter(new OutputStreamWriter(fos));
				String currTime = Util.getCurrTime();
				currTime = currTime.substring(0, 2)+":"+currTime.substring(2, 4)+":"+currTime.substring(4, 6);
				bw.write("["+currTime+"]\t" + str);
				bw.newLine();
				bw.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {  if(fos != null) fos.close(); } catch (IOException e) {e.printStackTrace();}
				try {  if(bw != null) bw.close(); } catch (IOException e) {e.printStackTrace();}
			}
		}
	}
}
