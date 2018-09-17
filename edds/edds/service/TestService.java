package com.saerom.edds.edds.service;

import java.util.Map;

import org.springframework.ui.ModelMap;

public interface TestService {

	public ModelMap selectList(Map<String,Object> defaultMap);

}
