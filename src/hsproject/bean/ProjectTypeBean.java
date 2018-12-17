package hsproject.bean;
/**
 * 项目类型对象类
 * @author tangjianyong 2018-03-01
 *
 */
public class ProjectTypeBean {
	private String id = "";
	private String typename = "";//类型名称
	private String protypecode = "";//类型编码
	private String description = "";//描述
	private String department = "";//负责部门
	private String seclevelstart = "";//安全级别开始
	private String seclevelend = "";//安全级别结束
	private String manager = "";//负责人
	private String attach = "";//附件
	private String creater = "";//创建人
	private String createdate = "";//创建日期
	private String dsporder = "";//显示顺序
	private String isused = "";//是否启用
	private String subcompany = "";//所属分部
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getProtypecode() {
		return protypecode;
	}
	public void setProtypecode(String protypecode) {
		this.protypecode = protypecode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSeclevelstart() {
		return seclevelstart;
	}
	public void setSeclevelstart(String seclevelstart) {
		this.seclevelstart = seclevelstart;
	}
	public String getSeclevelend() {
		return seclevelend;
	}
	public void setSeclevelend(String seclevelend) {
		this.seclevelend = seclevelend;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getDsporder() {
		return dsporder;
	}
	public void setDsporder(String dsporder) {
		this.dsporder = dsporder;
	}
	public String getIsused() {
		return isused;
	}
	public void setIsused(String isused) {
		this.isused = isused;
	}
	public String getSubcompany() {
		return subcompany;
	}
	public void setSubcompany(String subcompany) {
		this.subcompany = subcompany;
	}
	
}
