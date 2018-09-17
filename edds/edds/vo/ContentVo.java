package com.saerom.edds.edds.vo;

public class ContentVo {
	private String content_role;
	private String content_transfer_encoding;
	private String filename;
	private String content_type;
	private String charset;
	private Object content;
	
	public String getContent_role() {
		return content_role;
	}
	public void setContent_role(String content_role) {
		this.content_role = content_role;
	}
	public String getContent_transfer_encoding() {
		return content_transfer_encoding;
	}
	public void setContent_transfer_encoding(String content_transfer_encoding) {
		this.content_transfer_encoding = content_transfer_encoding;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "ContentVo [content_role=" + content_role + ", content_transfer_encoding=" + content_transfer_encoding
				+ ", filename=" + filename + ", content_type=" + content_type + ", charset=" + charset + ", content="
				+ content + "]";
	}
}