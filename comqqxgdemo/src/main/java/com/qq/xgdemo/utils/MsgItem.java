package com.qq.xgdemo.utils;

public class MsgItem {
	private String path;
	private String open_id;
	private String jsonobj;
	private String type;
	private String ver;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getJsonobj() {
		return jsonobj;
	}
	public void setJsonobj(String jsonobj) {
		this.jsonobj = jsonobj;
	}
	
	
	
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MsgItem(String path, String open_id, String jsonobj, String type,
			String ver) {
		super();
		this.path = path;
		this.open_id = open_id;
		this.jsonobj = jsonobj;
		this.type = type;
		this.ver = ver;
	}
	
	
	
	
}
