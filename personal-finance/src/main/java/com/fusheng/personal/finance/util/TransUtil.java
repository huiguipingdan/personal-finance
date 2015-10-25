package com.fusheng.personal.finance.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fusheng.personal.finance.dao.TradeRecordDao;
import com.fusheng.personal.finance.model.TransEnum;
@Component
public class TransUtil {
	private final static Logger log = LoggerFactory.getLogger(TransUtil.class);
	@Autowired
	private TradeRecordDao tradeRecordDao;
	
	private List<TransEnum> transEnumList;
	
	public final static String TYPE_CATAGORY = "catagory";
	public final static String TYPE_AMT_FLAG = "amtFlag";
	
	
	public String getTransWord(String catagoryType, String enumKey) {
		if(transEnumList==null) {
			transEnumList = tradeRecordDao.getTransWordList();
		}
		String enumValue = "";
		if(isNotNullCondition(catagoryType, enumKey)) {
			for(TransEnum e : transEnumList) {
				if(isResultEnum(catagoryType, enumKey, e)) {
					enumValue = e.getEnumValue();
					break;
				}
			}
		}
		return enumValue;
	}
	private boolean isNotNullCondition(String catagoryType, String enumKey) {
		return !(StringUtils.isBlank(catagoryType) || StringUtils.isBlank(enumKey)); 
	}
	private boolean isResultEnum(String catagoryType, String enumKey, TransEnum e) {
		//log.debug(catagoryType+"-"+enumKey+"|currentEnum->"+e.getEnumCatagory()+"-"+e.getEnumKey());
		return (catagoryType.equals(e.getEnumCatagory()) && Integer.valueOf(enumKey)==(e.getEnumKey()));
	}
}
