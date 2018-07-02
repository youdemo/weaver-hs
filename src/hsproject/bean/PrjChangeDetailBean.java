package hsproject.bean;

public class PrjChangeDetailBean {
	private String id = "";
	private String changetype = "";//变更类型
	private String keyid = "";//项目/阶段id
	private String fieldid = "";//变更字段id
	private String type = "";//项目/阶段类型
	private String seqno = "";//变更序号
	private String name = "";//项目/阶段名称
	private String fieldname = "";//字段名称
	private String oldvalue = "";//原值
	private String newvalue = "";//新值
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChangetype() {
		return changetype;
	}
	public void setChangetype(String changetype) {
		this.changetype = changetype;
	}
	public String getKeyid() {
		return keyid;
	}
	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}
	public String getFieldid() {
		return fieldid;
	}
	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSeqno() {
		return seqno;
	}
	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
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
	
	
	
}
