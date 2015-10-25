package com.fusheng.personal.finance.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final static Logger log = LoggerFactory.getLogger(TradeRecordController.class);
	@Autowired
	private TradeRecordDao tradeRecordDao;
	@Autowired
	private TransUtil transUtil;
	TradeRecord tradeRecord;
	@RequestMapping("/getList.do")
	public void getTradeRecordList(HttpServletResponse response,HttpServletRequest request) {
		JSONObject jsonObj = new JSONObject();
		// request前台的参数含义
		// http://datatables.net/manual/server-side
		int jqStart = Integer.parseInt(request.getParameter("start"));
		int jqDraw = Integer.parseInt(request.getParameter("draw"));
		int jqLength = Integer.parseInt(request.getParameter("length"));
		String searchValue="";
		try {
			//request 默认使用ISO-8859-1编码
			searchValue = new String(request.getParameter("search[value]").getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		};
		String orderColumn = request.getParameter("order[0][column]");
		String orderName = request.getParameter("columns["+orderColumn+"][data]"); 
		String orderDir = request.getParameter("order[0][dir]");
		List<TradeRecord> resultList = tradeRecordDao.getTradeRecordList(jqStart, jqLength, orderName, orderDir, searchValue);
		JSONArray jsonArray = JSONArray.fromObject(resultList);
		jsonObj.put("data", jsonArray);
		jsonObj.put("recordsTotal", tradeRecordDao.getTradeRecordListTotalCount(""));
		jsonObj.put("draw", jqDraw);
		jsonObj.put("recordsFiltered", tradeRecordDao.getTradeRecordListTotalCount(searchValue));
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
	@RequestMapping(value = "/getEchartsBarByMon.do")
	public void getEchartsBarByMon(HttpServletResponse response) {
		String jsonStr = tradeRecordDao.getEchartsBarByMon();
		log.debug("echars getEchartsBarbyMon options-->" + jsonStr);
		JSONObject json = new JSONObject();
		json.put("options", jsonStr);
		writeJson(response, json);
	}
	
	@RequestMapping(value = "/getEchartsPieByType.do")
	public void getEchartsPieByType(HttpServletResponse response) {
		String jsonStr = tradeRecordDao.getEchartsPieByType();
		log.debug("echars getEchartsPieByType options-->" + jsonStr);
		JSONObject json = new JSONObject();
		json.put("options", jsonStr);
		writeJson(response, json);
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
