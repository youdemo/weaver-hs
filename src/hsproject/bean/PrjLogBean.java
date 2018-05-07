package hsproject.bean;

import java.util.List;

public class PrjLogBean {
	private String id = "";
	private String logtype = "";//日志类型
	private String operater = "";//操作者
	private String createdate = "";//创建日期
	private String createtime = "";//创建时间
	private String prjtype = "";//项目类型
	private String prjprocess = "";//项目过程
	private String dataid = "";//数据id
	private String description = "";//描述
	private String type = "";//类型
	private List<PrjLogDtBean> dtList = null;//明细集合
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogtype() {
		return logtype;
	}
	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPrjtype() {
		return prjtype;
	}
	public void setPrjtype(String prjtype) {
		this.prjtype = prjtype;
	}
	public String getPrjprocess() {
		return prjprocess;
	}
	public void setPrjprocess(String prjprocess) {
		this.prjprocess = prjprocess;
	}
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<PrjLogDtBean> getDtList() {
		return dtList;
	}
	public void setDtList(List<PrjLogDtBean> dtList) {
		this.dtList = dtList;
	}
	
	
	
}
