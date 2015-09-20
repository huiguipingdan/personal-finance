package com.fusheng.personal.finance.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class CommonUtil {
	
	public static String[] getRecent6Mons() {
		List<String> array = new ArrayList<String>();
		Date d = new Date();
		for(int i=-5; i<1; i++) {
			array.add(DateFormatUtils.format(DateUtils.addMonths(d, i), "yyyyMM"));
		}
		return array.toArray(new String[6]);
	}
	
	public static String getCurrentMon() {
		return DateFormatUtils.format(new Date(), "yyyyMM");
	}
	
	public static List<Map<String, Object>> fillDefaultZero4EchartsList(List<Map<String, Object>> list, String[] nameCatagory) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for(String name : nameCatagory) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("name", name);
			resultMap.put("value", 0);
			for(Map<String, Object> map : list) {
				if(name.equals(map.get("name")) && map.get("value") != null) {
					resultMap = map;
					break;
				}
			}
			
			resultList.add(resultMap);
		}
		return resultList;
	}
}
