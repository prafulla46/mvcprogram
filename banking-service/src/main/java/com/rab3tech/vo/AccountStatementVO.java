package com.rab3tech.vo;

import java.sql.Timestamp;

public class AccountStatementVO {
	//trx id , account no ,  card/credit , amount , date 
	private int transactionId;
	private String accountNo;
	private String transactionType;
	private int amount;
	private Timestamp date;
	private String remarks;
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "AccountStatementVO [transactionId=" + transactionId + ", accountNo=" + accountNo + ", transactionType="
				+ transactionType + ", amount=" + amount + ", date=" + date + "]";
	}

	
}
