package hsproject.bean;

import java.util.List;

public class PrjReportBean {
	private String id = "";
	private String createdate = "";//创建日期
	private String createtime = "";//创建时间
	private String createrperson = "";//创建人
	private String prjtype = "";//项目类型
	private String reportname = "";//报表名称
	private String reporturl = "";
	private List<PrjReportDtBean> dtList = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<PrjReportDtBean> getDtList() {
		return dtList;
	}

	public void setDtList(List<PrjReportDtBean> dtList) {
		this.dtList = dtList;
	}

	public String getCreaterperson() {
		return createrperson;
	}

	public void setCreaterperson(String createrperson) {
		this.createrperson = createrperson;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String getReporturl() {
		return reporturl;
	}

	public void setReporturl(String reporturl) {
		this.reporturl = reporturl;
	}
	
}
