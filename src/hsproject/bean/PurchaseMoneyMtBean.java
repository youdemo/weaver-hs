package hsproject.bean;

public class PurchaseMoneyMtBean {
	private String id = "";
	private String mark = "";//标识
	private String datasource = "";//数据源
	private String mapsql = "";//转换sql
	private String description = "";//描述
	private String isused = "";//是否启用
	private String month = "";//月
	private String day = "";//日
	private String hour = "";//时
	private String halfhour = "";//半小时
	
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
	
	
}
