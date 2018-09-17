package com.saerom.edds.edds.vo;

import java.util.Arrays;

public class PackVo {
	private String pack_xmlname;
	private String pack_filename;
	private String send_orgcode;
	private String send_id;
	private String send_name;
	private String[] receive_id;
	private String date;
	private String title;
	private String doc_id;
	private String doc_type_type;
	private String doc_type_dept;
	private String doc_type_name;
	private String send_gw;
	private String dtd_version;
	private String xsl_version;
	private ContentsVo contents;
	private String contents_msg;
	private String noVal;
	
	public String getPack_xmlname() {
		return pack_xmlname;
	}
	public void setPack_xmlname(String pack_xmlname) {
		this.pack_xmlname = pack_xmlname;
	}
	public String getPack_filename() {
		return pack_filename;
	}
	public void setPack_filename(String pack_filename) {
		this.pack_filename = pack_filename;
	}
	public String getSend_orgcode() {
		return send_orgcode;
	}
	public void setSend_orgcode(String send_orgcode) {
		this.send_orgcode = send_orgcode;
	}
	public String getSend_id() {
		return send_id;
	}
	public void setSend_id(String send_id) {
		this.send_id = send_id;
	}
	public String getSend_name() {
		return send_name;
	}
	public void setSend_name(String send_name) {
		this.send_name = send_name;
	}
	public String[] getReceive_id() {
		return receive_id;
	}
	public void setReceive_id(String [] receive_id) {
		this.receive_id = receive_id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDoc_id() {
		return doc_id;
	}
	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}
	public String getDoc_type_type() {
		return doc_type_type;
	}
	public void setDoc_type_type(String doc_type_type) {
		this.doc_type_type = doc_type_type;
	}
	public String getDoc_type_dept() {
		return doc_type_dept;
	}
	public void setDoc_type_dept(String doc_type_dept) {
		this.doc_type_dept = doc_type_dept;
	}
	public String getDoc_type_name() {
		return doc_type_name;
	}
	public void setDoc_type_name(String doc_type_name) {
		this.doc_type_name = doc_type_name;
	}
	public String getSend_gw() {
		return send_gw;
	}
	public void setSend_gw(String send_gw) {
		this.send_gw = send_gw;
	}
	public String getDtd_version() {
		return dtd_version;
	}
	public void setDtd_version(String dtd_version) {
		this.dtd_version = dtd_version;
	}
	public String getXsl_version() {
		return xsl_version;
	}
	public void setXsl_version(String xsl_version) {
		this.xsl_version = xsl_version;
	}
	public ContentsVo getContents() {
		return contents;
	}
	public void setContents(ContentsVo contents) {
		this.contents = contents;
	}
	public String getContents_msg() {
		return contents_msg;
	}
	public void setContents_msg(String contents) {
		this.contents_msg = contents_msg;
	}
	public String getNoVal() {
		return noVal;
	}
	public void setNoVal(String noVal) {
		this.noVal = noVal;
	}
	
	@Override
	public String toString() {
		return "PackVo [pack_xmlname=" + pack_xmlname + ", pack_filename=" + pack_filename + ", send_orgcode="
				+ send_orgcode + ", send_id=" + send_id + ", send_name=" + send_name + ", receive_id="
				+ Arrays.toString(receive_id) + ", date=" + date + ", title=" + title + ", doc_id=" + doc_id
				+ ", doc_type_type=" + doc_type_type + ", doc_type_dept=" + doc_type_dept + ", doc_type_name="
				+ doc_type_name + ", send_gw=" + send_gw + ", dtd_version=" + dtd_version + ", xsl_version="
				+ xsl_version + ", contents_msg=" + contents_msg + ", noVal=" + noVal + "]"; //contents=" + contents + ",
	}
	
	
}

