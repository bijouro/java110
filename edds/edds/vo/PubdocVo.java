package com.saerom.edds.edds.vo;

import java.util.List;
import java.util.Map;

public class PubdocVo {
	// head
	private String organ;
	private String receipient;
	private String receipient_refer;
	private String rec;
	private String via;
	
	//body
	private String body_seperate;
	private String body_title;
	private String body_content;
	
	//foot
	private String sendername;
	private String seal_omit;
	private String seal_img;
	
	//foot/approvalinfo
	private List<Map<String, Object>> approval;
	private List<Map<String, Object>> assist;
	
	//foot/processinfo
	private String regnumber;
	private String regnumber_code;
	private String enforcedate;
	private String receipt;
	private String receipt_number;
	private String receipt_date;
	private String receipt_time;
	
	//foot/sendinfo
	private String zipcode;
	private String address;
	private String homeurl;
	private String telephone;
	private String fax;
	private String email;
	private String publication;
	private String [] publication_code;
	private String symbol;
	private String logo;
	
	//foot/campaign
	private String headcampaign;
	private String footcampaign;
	
	//attach
	private List<String> attach_title;
	
	private String noVal;

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getReceipient() {
		return receipient;
	}

	public void setReceipient(String receipient) {
		this.receipient = receipient;
	}

	public String getReceipient_refer() {
		return receipient_refer;
	}

	public void setReceipient_refer(String receipient_refer) {
		this.receipient_refer = receipient_refer;
	}

	public String getRec() {
		return rec;
	}

	public void setRec(String rec) {
		this.rec = rec;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getBody_seperate() {
		return body_seperate;
	}

	public void setBody_seperate(String body_seperate) {
		this.body_seperate = body_seperate;
	}

	public String getBody_title() {
		return body_title;
	}

	public void setBody_title(String body_title) {
		this.body_title = body_title;
	}

	public String getBody_content() {
		return body_content;
	}

	public void setBody_content(String body_content) {
		this.body_content = body_content;
	}

	public String getSendername() {
		return sendername;
	}

	public void setSendername(String sendername) {
		this.sendername = sendername;
	}

	public String getSeal_omit() {
		return seal_omit;
	}

	public void setSeal_omit(String seal_omit) {
		this.seal_omit = seal_omit;
	}

	public String getSeal_img() {
		return seal_img;
	}

	public void setSeal_img(String seal_img) {
		this.seal_img = seal_img;
	}

	public List<Map<String, Object>> getApproval() {
		return approval;
	}

	public void setApproval(List<Map<String, Object>> approval) {
		this.approval = approval;
	}

	public List<Map<String, Object>> getAssist() {
		return assist;
	}

	public void setAssist(List<Map<String, Object>> assist) {
		this.assist = assist;
	}

	public String getRegnumber() {
		return regnumber;
	}

	public void setRegnumber(String regnumber) {
		this.regnumber = regnumber;
	}

	public String getRegnumber_code() {
		return regnumber_code;
	}

	public void setRegnumber_code(String regnumber_code) {
		this.regnumber_code = regnumber_code;
	}
	
	public String getEnforcedate() {
		return enforcedate;
	}

	public void setEnforcedate(String enforcedate) {
		this.enforcedate = enforcedate;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getReceipt_number() {
		return receipt_number;
	}

	public void setReceipt_number(String receipt_number) {
		this.receipt_number = receipt_number;
	}

	public String getReceipt_date() {
		return receipt_date;
	}

	public void setReceipt_date(String receipt_date) {
		this.receipt_date = receipt_date;
	}

	public String getReceipt_time() {
		return receipt_time;
	}

	public void setReceipt_time(String receipt_time) {
		this.receipt_time = receipt_time;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHomeurl() {
		return homeurl;
	}

	public void setHomeurl(String homeurl) {
		this.homeurl = homeurl;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public String [] getPublication_code() {
		return publication_code;
	}

	public void setPublication_code(String [] publication_code) {
		this.publication_code = publication_code;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getHeadcampaign() {
		return headcampaign;
	}

	public void setHeadcampaign(String headcampaign) {
		this.headcampaign = headcampaign;
	}

	public String getFootcampaign() {
		return footcampaign;
	}

	public void setFootcampaign(String footcampaign) {
		this.footcampaign = footcampaign;
	}

	public List<String> getAttach_title() {
		return attach_title;
	}

	public void setAttach_title(List<String> attach_title) {
		this.attach_title = attach_title;
	}
	
	public String getNoVal() {
		return noVal;
	}

	public void setNoVal(String noVal) {
		this.noVal = noVal;
	}

	@Override
	public String toString() {
		return "PubdocVo [organ=" + organ + ", receipient=" + receipient + ", receipient_refer=" + receipient_refer
				+ ", rec=" + rec + ", via=" + via + ", body_seperate=" + body_seperate + ", body_title=" + body_title
				+ ", body_content=" + body_content + ", sendername=" + sendername + ", seal_omit=" + seal_omit
				+ ", seal_img=" + seal_img + ", approval=" + approval + ", assist=" + assist + ", regnumber="
				+ regnumber + ", regnumber_code=" + regnumber_code + ", enforcedate=" + enforcedate + ", receipt="
				+ receipt + ", receipt_number=" + receipt_number + ", receipt_date=" + receipt_date + ", receipt_time="
				+ receipt_time + ", zipcode=" + zipcode + ", address=" + address + ", homeurl=" + homeurl
				+ ", telephone=" + telephone + ", fax=" + fax + ", email=" + email + ", publication=" + publication
				+ ", publication_code=" + publication_code + ", symbol=" + symbol + ", logo=" + logo + ", headcampaign="
				+ headcampaign + ", footcampaign=" + footcampaign + ", attach_title=" + attach_title + "], noVal=" + noVal;
	}	
}

