package com.fusheng.personal.finance.model;

public class TradeRecord {
	private String id;
	private String reason;
	private String catagory;
	private int amtFlag;
	private double amt;
	private String updateTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCatagory() {
		return catagory;
	}
	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}
	public int getAmtFlag() {
		return amtFlag;
	}
	public void setAmtFlag(int amtFlag) {
		this.amtFlag = amtFlag;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
}
