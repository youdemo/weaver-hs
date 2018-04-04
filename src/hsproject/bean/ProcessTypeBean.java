package hsproject.bean;

public class ProcessTypeBean {
	String id = "";
	String prjtype = "";//项目类型
	String processname = "";//过程名称
	String description = "";//过程描述
	String isused = "";//启用
	String dsporder = "";//显示顺序
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrjtype() {
		return prjtype;
	}
	public void setPrjtype(String prjtype) {
		this.prjtype = prjtype;
	}
	public String getProcessname() {
		return processname;
	}
	public void setProcessname(String processname) {
		this.processname = processname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsused() {
		return isused;
	}
	public void setIsused(String isused) {
		this.isused = isused;
	}
	public String getDsporder() {
		return dsporder;
	}
	public void setDsporder(String dsporder) {
		this.dsporder = dsporder;
	}
	
	
}
