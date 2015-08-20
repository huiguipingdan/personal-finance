package com.fusheng.personal.finance.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fusheng.personal.finance.model.TradeRecord;
import com.fusheng.personal.finance.model.TransEnum;
@Repository("tradeRecordDao")
public class TradeRecordDao {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<TradeRecord> getTradeRecordList() {
		String sql = "select id as id,reason as reason,catagory as catagory,amt_flag as amtFlag,amt as amt,date_format(update_time,'%Y-%m-%d') as updateTime from trade_record order by update_time desc";
		return (List<TradeRecord>)jdbcTemplate.query(sql, new BeanPropertyRowMapper(TradeRecord.class));
	}
	public int saveTradeRecord(TradeRecord tradeRecord) {
		String sql = "INSERT INTO trade_record  ( reason ,  catagory ,"
					+"  amt_flag ,  amt ,  update_time ) VALUES (?, ?, ?, ?, ?)";
		return jdbcTemplate.update(sql, new Object[]{tradeRecord.getReason(),tradeRecord.getCatagory(),tradeRecord.getAmtFlag()
				,tradeRecord.getAmt(),tradeRecord.getUpdateTime()});
	}
	public int updateTradeRecord(TradeRecord tradeRecord) {
		String sql = "UPDATE trade_record  SET reason=? ,  catagory=? ,"
					+"  amt_flag=? ,  amt=? ,  update_time=? where id=?";
		return jdbcTemplate.update(sql, new Object[]{tradeRecord.getReason(),tradeRecord.getCatagory(),tradeRecord.getAmtFlag()
				,tradeRecord.getAmt(),tradeRecord.getUpdateTime(),tradeRecord.getId()});
	}
	public int delTradeRecord(TradeRecord tradeRecord) {
		int resultNum = 0;
		if(StringUtils.isNotBlank(tradeRecord.getId())){
			String sql = "delete from trade_record where id=?";
			resultNum = jdbcTemplate.update(sql, new Object[]{tradeRecord.getId()});
		}
		return resultNum;
	}
	public TradeRecord getTradeRecordById(String id) {
		TradeRecord model = new TradeRecord();
		if(Integer.parseInt(id)>0){
			String sql = "select id as id,reason as reason,catagory as catagory,amt_flag as amtFlag,amt as amt,date_format(update_time,'%Y-%m-%d') as updateTime from trade_record where id=?";
			model = jdbcTemplate.queryForObject(sql, new Object[]{id}, ParameterizedBeanPropertyRowMapper.newInstance(TradeRecord.class));
		}
		return model;
	}
	
	public List<TransEnum> getTransWordList() {
		String sql = "select id,enum_key,enum_value,enum_catagory from pf_enum";
		return (List<TransEnum>)jdbcTemplate.query(sql, new BeanPropertyRowMapper(TransEnum.class));
	}
	public List<TransEnum> getTransWordListByCatagory(String catagory) {
		String sql = "select id,enum_key,enum_value,enum_catagory from pf_enum where enum_catagory='"+catagory+"'";
		return (List<TransEnum>)jdbcTemplate.query(sql, new BeanPropertyRowMapper(TransEnum.class));
	}
}
