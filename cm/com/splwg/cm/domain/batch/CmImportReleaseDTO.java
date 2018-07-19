package com.splwg.cm.domain.batch;

public class CmImportReleaseDTO {

	
	private String  tenderCtrlId;
	public String getTenderIdCiPay() {
		return TenderIdCiPay;
	}
	public void setTenderIdCiPay(String tenderIdCiPay) {
		TenderIdCiPay = tenderIdCiPay;
	}
	private String  acctId;
	private String  payEventId;
	private String tenderAmt;
	private String TenderIdCiPay;
	public String getTenderCtrlId() {
		return tenderCtrlId;
	}
	public void setTenderCtrlId(String tenderCtrlId) {
		this.tenderCtrlId = tenderCtrlId;
	}
	public String getAcctId() {
		return acctId;
	}
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	public String getPayEventId() {
		return payEventId;
	}
	public void setPayEventId(String payEventId) {
		this.payEventId = payEventId;
	}
	public String getTenderAmt() {
		return tenderAmt;
	}
	public void setTenderAmt(String tenderAmt) {
		this.tenderAmt = tenderAmt;
	}
	
	
	
	
	
}
