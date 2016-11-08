/**
 * 
 */
package com.primeton.devops.openshift.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class YamlJsonUtil {
	
	private YamlJsonUtil(){}
	
	@SuppressWarnings("rawtypes")
	public static String json2yaml(String json) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		Map obj = null;
		try {
			obj = mapper.readValue(json, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null == obj ? null : new Yaml().dump(obj);
	}
	
	@SuppressWarnings("rawtypes")
	public static String yaml2json(String yaml) {
		if (StringUtils.isBlank(yaml)) {
			return null;
		}
		Map obj = (Map)new Yaml().load(yaml);
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
