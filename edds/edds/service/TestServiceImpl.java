package com.saerom.edds.edds.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.saerom.edds.edds.dao.TestDao;

@Service("testService")
public class TestServiceImpl implements TestService {
	private static Logger logger = Logger.getLogger(Class.class);
	
	@Autowired
	private TestDao testDao;
	 

    @Transactional
	@Override
	public ModelMap selectList(Map<String, Object> reqMap) {
		ModelMap resultMap = new ModelMap();

		Map<String, Object> input = (Map<String, Object>) reqMap.get("input");
		
		logger.debug("input_data > "+reqMap);
		
		List<Map<String,Object>> testData = testDao.selectList(input);
		
		
		logger.debug("result_data > "+testData);
		resultMap.put("result_data", testData); 
		
		return resultMap;

	}
}
