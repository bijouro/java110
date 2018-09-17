package com.saerom.edds.comm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
	
	private static Logger logger = Logger.getLogger(Class.class.getName());
	
	
	private static Configuration instance = null;
    private Properties properties_conf;
    private Properties properties_custom;


    protected Configuration() throws IOException{
    	FileInputStream fis = null;
    	String fileName = "C:/emate/config.properties";
    	properties_conf = new Properties();
    	properties_custom = new Properties();
        
        try{
        	fis = new FileInputStream(fileName);
        	properties_conf.load(fis);
        	properties_custom.load(getClass().getResourceAsStream("/emate/edds/config/config.properties"));
        	properties_conf.putAll(properties_custom);
        	
        	logger.debug("properties >> " + properties_conf);
        	
        } catch(IOException e){
        	e.printStackTrace();
        } finally {
        	if(fis != null){
        		try{
        			fis.close();
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        }
    }

    public static Configuration getInstance() {
        if(instance == null) {
            try {
                instance = new Configuration();
                
                logger.debug("########   Configuration Values Road ......");
                
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return instance;
    }

    public String getValue(String key) {
        return properties_conf.getProperty(key);
    }
	
}
