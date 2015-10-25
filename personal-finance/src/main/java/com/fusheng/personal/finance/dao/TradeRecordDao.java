package com.fusheng.personal.finance.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fusheng.personal.finance.model.TradeRecord;
import com.fusheng.personal.finance.model.TransEnum;
import com.fusheng.personal.finance.util.CommonUtil;
import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonUtil;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Funnel;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.ItemStyle;
@Repository("tradeRecordDao")
public class TradeRecordDao {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private final static Logger log = LoggerFactory.getLogger(TradeRecordDao.class);
	
	public List<TradeRecord> getTradeRecordList(int start, int length, String orderColName, String orderDir, String searchValue) {
		String sql = "select t.id as id,t.reason as reason,t.catagory as catagory,t.amt_flag as amtFlag,"
				+"t.amt as amt,date_format(t.update_time,'%Y-%m-%d') as updateTime "
				+"from trade_record t left join pf_enum p on t.catagory=p.enum_key and p.enum_catagory='catagory' where 1=1";
		StringBuilder sqlBuilder = new StringBuilder(sql);
		if(StringUtils.isNotBlank(searchValue)) {
			sqlBuilder.append(" and reason like '%"+searchValue+"%' or date_format(update_time,'%Y-%m-%d') like '%"
					+searchValue+"%' or p.enum_value like '%"+searchValue+"%'");
		}
		sqlBuilder.append(" order by "+orderColName+" "+orderDir+" limit "+start+","+(start+length));
		log.debug("getTradeRecordList page sql-->"+sqlBuilder);
		return (List<TradeRecord>)jdbcTemplate.query(sqlBuilder.toString(), new BeanPropertyRowMapper(TradeRecord.class));
	}
	public int getTradeRecordListTotalCount(String searchValue) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select count(t.id) from trade_record t left join pf_enum p on t.catagory=p.enum_key and p.enum_catagory='catagory' where 1=1");
		if(StringUtils.isNotBlank(searchValue)) {
			sqlBuilder.append(" and reason like '%"+searchValue+"%' or date_format(update_time,'%Y-%m-%d') like '%"
					+searchValue+"%' or p.enum_value like '%"+searchValue+"%'");
		}
		return jdbcTemplate.queryForInt(sqlBuilder.toString());
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
	/**
	 * 得到最近6个月支出的记录
	 * @return
	 */
	public List<Map<String, Object>> getPayAmtBy6Mon() {
		String sql = "select DATE_FORMAT(update_time,'%Y%m') as name,sum(amt) as value from trade_record where "
					+"DATE_FORMAT(update_time,'%Y%m') >= DATE_FORMAT(DATE_SUB(now(),INTERVAL 5 MONTH),'%Y%m')"
				+"and DATE_FORMAT(update_time,'%Y%m') <= DATE_FORMAT(now(),'%Y%m') and amt_flag = 2 group by DATE_FORMAT(update_time,'%Y%m')";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 返回echarts柱形图json串
	 * @return
	 */
	public String getEchartsBarByMon() {
		List<Map<String, Object>> list = CommonUtil.fillDefaultZero4EchartsList(getPayAmtBy6Mon(), CommonUtil.getRecent6Mons());
		JSONArray mons = new JSONArray();
		JSONArray payAmt = new JSONArray();
		for(Map<String, Object> map : list) {
			mons.add(map.get("name"));
			payAmt.add(map.get("value"));
		}
		
		Option option = new Option();
		ItemStyle dataStyle = new ItemStyle();
	    dataStyle.normal().label(new Label().show(false)).labelLine().show(false);
	    
	    option.title().text("月支出统计").subtext("近6个月支出明细");
	    
	    option.tooltip().trigger(Trigger.axis).show(true).formatter("{a} <br/>{b} : &yen;{c} ");
	    option.legend().data("支出");
	    option.toolbox().show(true).feature(Tool.mark, Tool.dataView, Tool.restore, Tool.saveAsImage);
	    CategoryAxis categoryAxis = new CategoryAxis();
	    categoryAxis.data(mons.toArray());
	    option.xAxis(categoryAxis);
	    ValueAxis valueAxis = new ValueAxis();
	    option.yAxis(valueAxis);
	    Bar bar = new Bar();
	    bar.name("支出").barWidth(70).data(payAmt.toArray());
	    option.series(bar);
	    return GsonUtil.format(option);
	}
	public List<Map<String, Object>> getCurrentMonPayByType() {
		String sql = "select p.enum_value as name,sum(t.amt) as value from pf_enum p left join trade_record t "
						+"on p.enum_key=t.catagory where p.enum_catagory='catagory' and DATE_FORMAT(t.update_time,'%Y%m')='"
						+CommonUtil.getCurrentMon()+"' group by t.catagory order by enum_key";
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 返回echarts饼图json串
	 * @return
	 */
	public String getEchartsPieByType() {
		List<Map<String, Object>> list = getCurrentMonPayByType();
		JSONArray type = new JSONArray();
		JSONArray payAmt = new JSONArray();
		for(Map<String, Object> map : list) {
			type.add(map.get("name"));
			payAmt.add(map.get("value"));
		}
		
		Option option = new Option();
		option.title().text("支出构成").subtext("当月支出类型明细").x(X.center);
		
	    option.tooltip().show(true).trigger(Trigger.item).formatter("{a} <br/>{b} : &yen;{c} ({d}%)");
	    option.legend().data(type.toArray()).x(X.left).orient(Orient.vertical);
	    
	    MagicType magic = new MagicType(Magic.pie, Magic.funnel);
	    Funnel funnel = new Funnel();
	    funnel.x("25%").width("50%").funnelAlign(X.left).max(1548);
	    magic.option(new MagicType.Option().funnel(funnel));
	    
	    option.toolbox().show(true).feature(Tool.mark, Tool.dataView, magic, Tool.restore, Tool.saveAsImage);
	    option.calculable(true);
	    
	    Pie pie = new Pie();
	    List<PieData> pieDataList = new ArrayList<PieData>();
	    for(int index = 0; index < type.size(); index++) {
	    	pieDataList.add(new PieData(type.getString(index), payAmt.get(index)));
	    }
	    
	    pie.name("支出").type(SeriesType.pie).radius("70%").center(new Object[]{"50%", "60%"})
	    .data(pieDataList.toArray());
	    
	    option.series(pie);
	    return GsonUtil.format(option);
	}
}
