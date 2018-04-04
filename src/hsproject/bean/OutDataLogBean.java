package hsproject.bean;

public class OutDataLogBean {
	private String id = "";
	private String mtid = "";//同步配置id
	private String type = "";//同步类型
	private String fieldtype = "";//字段类型
	private String prjid = "";//项目id
	private String processid = "";//过程id
	private String sysnfield = "";//同步字段名
	private String oldvalue = "";//oldvalue
	private String newvalue = "";//newvalue
	private String sysnsql = "";//同步sql
	private String updatesql = "";//更新sql
	private String sysndate = "";//同步日期
	private String sysntime = "";//同步时间
	private String description = "";//描述
	private String logtype = "";//日志类型
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMtid() {
		return mtid;
	}
	public void setMtid(String mtid) {
		this.mtid = mtid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFieldtype() {
		return fieldtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	public String getPrjid() {
		return prjid;
	}
	public void setPrjid(String prjid) {
		this.prjid = prjid;
	}
	public String getProcessid() {
		return processid;
	}
	public void setProcessid(String processid) {
		this.processid = processid;
	}
	public String getSysnfield() {
		return sysnfield;
	}
	public void setSysnfield(String sysnfield) {
		this.sysnfield = sysnfield;
	}
	public String getOldvalue() {
		return oldvalue;
	}
	public void setOldvalue(String oldvalue) {
		this.oldvalue = oldvalue;
	}
	public String getNewvalue() {
		return newvalue;
	}
	public void setNewvalue(String newvalue) {
		this.newvalue = newvalue;
	}
	public String getSysnsql() {
		return sysnsql;
	}
	public void setSysnsql(String sysnsql) {
		this.sysnsql = sysnsql;
	}
	public String getUpdatesql() {
		return updatesql;
	}
	public void setUpdatesql(String updatesql) {
		this.updatesql = updatesql;
	}
	public String getSysndate() {
		return sysndate;
	}
	public void setSysndate(String sysndate) {
		this.sysndate = sysndate;
	}
	public String getSysntime() {
		return sysntime;
	}
	public void setSysntime(String sysntime) {
		this.sysntime = sysntime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLogtype() {
		return logtype;
	}
	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}
	
	
	
}
