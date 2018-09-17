package com.saerom.edds.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.saerom.edds.edds.dao.LdapBatchDao;

public class LdapSearch {
	@Autowired
	private LdapBatchDao ldapBatchDao;
	
	private static Logger logger = Logger.getLogger(Class.class);
	
	// ldap 계정
	private static String id = Configuration.getInstance().getValue("ldap_id");
	
	// ldap 암호
	private static String PW = Configuration.getInstance().getValue("ldap_pw");
    
    //ldap 설치된 서버 ip주소
	private static String ldapServer = Configuration.getInstance().getValue("ldap_ip");
	
	//ldap port 번호
	private static String port = Configuration.getInstance().getValue("ldap_port");
	
	public ArrayList ldapSearch(String base_dn, String filter, int scop, String [] resultAttribute) {
		
		//-------------------------------------------------------------
		// Set up the environment for creating the initial context
		//-------------------------------------------------------------
		
		DirContext ctx = null;
		
		Properties props = new Properties();
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		props.setProperty(Context.PROVIDER_URL, "ldap://"+ldapServer+":"+port);
		
		//-------------------------------------------------------------
		// specify the root username
		//-------------------------------------------------------------
        // 검색이기 때문에 그냥 로그인 함
		props.setProperty(Context.SECURITY_PRINCIPAL,""); 
		
		//-------------------------------------------------------------
		// specify the root password
		//-------------------------------------------------------------
        // 검색이기 때문에 그냥 로그인 함
		props.setProperty(Context.SECURITY_CREDENTIALS,""); 
		
		//-------------------------------------------------------------
		// Get the environment properties for creation initial
		// context and specifying LDAP service provider parameters.
		//-------------------------------------------------------------
		
		int searchCount = 0;
		ArrayList alList = new ArrayList();
		
		try {
			ctx = new InitialDirContext(props);
		
			//-------------------------------------------------------------
			// LDAP Search
			//-------------------------------------------------------------
			// 검색 제약 조건 설정
			// 2 : SUBTREE_SCOPE : 기본 엔트리에서 시작하여 기본 엔트리 및 이 및에 있는 모든 것을 검색한다. 
			//				    이것은 가정느리고 가장 비싼 검색이다.
			// 1 : ONELEVEL_SCOPE : 기본 엔트리 밑에 있는 엔트리들을 검색한다.
			// 0 : OBJECT_SCOPE : 기본 엔트리만 검색한다. 가장 빠르고 저렴한 검색이다.
			SearchControls cons = new SearchControls();			
			
			cons.setSearchScope(scop);
			
			NamingEnumeration result = ctx.search(base_dn, filter, cons);
			
			while (result.hasMore()){
				SearchResult nextEntry = null;
				HashMap hmAttr = new HashMap();
				
				
				int iException=0;
				try{
					nextEntry = (SearchResult)result.next();

					searchCount++;					

					Attributes attrs = nextEntry.getAttributes();
	                
					String dn = nextEntry.getNameInNamespace();
					
	        		//특수 문자 포함한 dn 값 "\" 붙여줌.. 
					String reg [] = {"/"};
	        		for(int j=0;j<reg.length;j++){
	        			dn = dn.replaceAll(reg[j], "\\\\"+reg[j]);
	        		}	        		
	                
	        		hmAttr.put("dn", dn);
	                
	                
	                if (attrs != null) {
                    	for(int i=0;i<resultAttribute.length;i++){
                    		String attr = (resultAttribute[i]).toString();
                    		String val = "";
                    		try {
                    			 val = attrs.get(attr).get().toString();
                    		} catch (NullPointerException e)  {}
                    		
                    		hmAttr.put(attr, val);
                    	}
                    	alList.add(hmAttr);
	                }
				}
				catch(Exception e){
					iException++;
					logger.error("++++ Result :::  Exception 발생... Continue... filter : " + filter);
					exceptionSet(filter, "Exception");				
					
					continue;
				}
				
				if(iException>0){
					logger.debug("iException Continue : " + iException);
				}
			}
		} catch (NamingException e) {
			logger.error("++++ Search :::  NamingException 발생..... filter : " + filter);
			logger.error(e.getMessage());
			e.printStackTrace();
			//exceptionSet(filter, "NamingException");
		}finally{
			//logger.debug("검색 총 건수 >" +searchCount );
			try {
				logger.debug("==ldap seach sleep==s");
				Thread.sleep(50);
				logger.debug("==ldap seach sleep==e");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
                if (null != ctx)
                    ctx.close();
            } catch (Exception e2) {
            }
		}
		return alList;
	}

	private void exceptionSet(String filter, String exceptionString) {
		HashMap hmException = new HashMap();
		hmException.put("filter", filter);
		hmException.put("e", exceptionString);
		hmException.put("state_0", "0");
		hmException.put("regdate", Util.getToday());
		hmException.put("user", "batch");
		hmException.put("retry", "0");
		
		ldapBatchDao.insertExceptionFilter(hmException);
	}
}

