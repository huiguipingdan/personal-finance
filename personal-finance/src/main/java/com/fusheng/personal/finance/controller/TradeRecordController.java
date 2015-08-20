package com.fusheng.personal.finance.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fusheng.personal.finance.dao.TradeRecordDao;
import com.fusheng.personal.finance.model.TradeRecord;
import com.fusheng.personal.finance.util.TransUtil;

@Controller
public class TradeRecordController {
	@Autowired
	private TradeRecordDao tradeRecordDao;
	@Autowired
	private TransUtil transUtil;
	TradeRecord tradeRecord;
	@RequestMapping("/getList.do")
	public void getTradeRecordList(HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(tradeRecordDao.getTradeRecordList());
		jsonObj.put("data", jsonArray);
		writeJson(response,jsonObj);
	}
	@RequestMapping(value = "/getTransWord.do")
	public void  getTransWord(String type, String enumKey, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject(); 
		jsonObj.put("enumValue", transUtil.getTransWord(type, enumKey));
		writeJson(response,jsonObj);
	}
	@RequestMapping(value = "/saveTrade.do")
	@ResponseBody
	public String saveTradeRecord(TradeRecord tradeRecord) {
		String result = "SUCCESS";
		if(tradeRecordDao.saveTradeRecord(tradeRecord) < 1) {
			result = "ERROR";
		} 
		return result;
	}
	@RequestMapping(value = "/updateTrade.do")
	@ResponseBody
	public String updateTradeRecord(TradeRecord tradeRecord) {
		String result = "SUCCESS";
		if(tradeRecordDao.updateTradeRecord(tradeRecord) < 1) {
			result = "ERROR";
		} 
		return result;
	}
	
	@RequestMapping(value = "/delTrade.do")
	@ResponseBody
	public String delTradeRecord(TradeRecord tradeRecord) {
		String result = "SUCCESS";
		if(tradeRecordDao.delTradeRecord(tradeRecord) < 1) {
			result = "ERROR";
		} 
		return result;
	}
	@RequestMapping(value = "/getMainTradeRecord.do")
	public ModelAndView  getMainTradeRecord(TradeRecord tradeRecord) {
		ModelAndView modelAndView = new ModelAndView("trade_main");  
        modelAndView.addObject("tradeModel", tradeRecordDao.getTradeRecordById(tradeRecord.getId()));
        modelAndView.addObject("catagoryList", tradeRecordDao.getTransWordListByCatagory(TransUtil.TYPE_CATAGORY));
        return modelAndView; 
	}
	
	public static void writeJson(HttpServletResponse response, JSON json) {
		response.setContentType("application/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			json.write(writer);
			writer.flush();
		} catch (IOException e) {
			throw new IllegalStateException("Write json to response error", e);
		}

	}
}
