package com.saerom.edds.edds.vo;

import java.util.List;

public class ContentsVo {
	private ContentVo pubdoc;
	private List<ContentVo> attach;
	private List<ContentVo> attach_body;
	private ContentVo seal;
	private ContentVo gpki;
	private ContentVo fail;
	private List<ContentVo> sign;
	private ContentVo symbol;
	private ContentVo logo;
	
	public ContentVo getPubdoc() {
		return pubdoc;
	}
	public void setPubdoc(ContentVo pubdoc) {
		this.pubdoc = pubdoc;
	}
	public List<ContentVo> getAttach() {
		return attach;
	}
	public void setAttach(List<ContentVo> attach) {
		this.attach = attach;
	}
	public List<ContentVo> getAttach_body() {
		return attach_body;
	}
	public void setAttach_body(List<ContentVo> attach_body) {
		this.attach_body = attach_body;
	}
	public ContentVo getSeal() {
		return seal;
	}
	public void setSeal(ContentVo seal) {
		this.seal = seal;
	}
	public ContentVo getGpki() {
		return gpki;
	}
	public void setGpki(ContentVo gpki) {
		this.gpki = gpki;
	}
	public ContentVo getFail() {
		return fail;
	}
	public void setFail(ContentVo fail) {
		this.fail = fail;
	}
	public List<ContentVo> getSign() {
		return sign;
	}
	public void setSign(List<ContentVo> sign) {
		this.sign = sign;
	}
	public ContentVo getSymbol() {
		return symbol;
	}
	public void setSymbol(ContentVo symbol) {
		this.symbol = symbol;
	}
	public ContentVo getLogo() {
		return logo;
	}
	public void setLogo(ContentVo logo) {
		this.logo = logo;
	}
	
	@Override
	public String toString() {
		return "ContentsVo [pubdoc=" + pubdoc + ", attach=" + attach + ", attach_body=" + attach_body + ", seal=" + seal
				+ ", gpki=" + gpki + ", fail=" + fail + ", sign=" + sign + ", symbol=" + symbol + ", logo=" + logo
				+ "]";
	}	
}
