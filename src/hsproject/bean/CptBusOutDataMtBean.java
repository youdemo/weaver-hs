package hsproject.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangj
 *
 */
public class CptBusOutDataMtBean {
	private String id = "";
	private String mark = "";//标识
	private String datasource = "";//数据源
	private String type = "";//同步类型 0 固定资产 1 商务信息
	private String prjtype = "";//项目类型
	private String mapsql = "";//转换sql
	private String description = "";//描述
	private String isused = "";//是否启用
	private String month = "";//月
	private String day = "";//日
	private String hour = "";//时
	private String halfhour = "";//半小时	
	private List<CptBusOutDataMtDtBean> dtList = new ArrayList<CptBusOutDataMtDtBean>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrjtype() {
		return prjtype;
	}
	public void setPrjtype(String prjtype) {
		this.prjtype = prjtype;
	}
	public String getMapsql() {
		return mapsql;
	}
	public void setMapsql(String mapsql) {
		this.mapsql = mapsql;
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
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getHalfhour() {
		return halfhour;
	}
	public void setHalfhour(String halfhour) {
		this.halfhour = halfhour;
	}
	public List<CptBusOutDataMtDtBean> getDtList() {
		return dtList;
	}
	public void setDtList(List<CptBusOutDataMtDtBean> dtList) {
		this.dtList = dtList;
	}
	
	
}
