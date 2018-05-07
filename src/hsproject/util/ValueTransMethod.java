package hsproject.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ValueTransMethod {
	/**
	 * 
	 * @param fieldname
	 * @param value
	 * @param type 0 项目信息 1 过程
	 */
	public String doTrans(String fieldid,String value,String type){
		String tablename = "";
		String fieldtype = "";
		String texttype = "";
		String buttontype = "";
		String selectbutton = "";
		RecordSet rs = new RecordSet();
		if(value==null||"".equals(value)){
			return "";
		}
		if("0".equals(type)){
			tablename = "uf_project_field";
		}else if("1".equals(type)){
			tablename = "uf_prj_porcessfield";
		}else{
			return value;
		}
		String sql="select  fieldtype,texttype,buttontype,selectbutton from "+tablename+" where id='"+fieldid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			fieldtype = Util.null2String(rs.getString("fieldtype"));
			texttype = Util.null2String(rs.getString("texttype"));
			buttontype = Util.null2String(rs.getString("buttontype"));
			selectbutton = Util.null2String(rs.getString("selectbutton"));
		}
		try {
			return getTransValue(fieldtype,texttype,buttontype,selectbutton,value);
		} catch (Exception e) {
			return value;
		}
	}
	
	public String getTransValue(String fieldtype,String texttype,String buttontype,String selectbutton,String value) throws Exception{
		//BaseBean log = new BaseBean();
		String result ="";
		if("0".equals(fieldtype)){
			result = value;
		}else if("1".equals(fieldtype)){
			result = new BrowserInfoUtil().getBrowserFieldValue(buttontype, value);
		}else if("3".equals(fieldtype)){
			result = new TransUtil().getSelectValue(selectbutton, value);
		}else if("4".equals(fieldtype)){
			result = new TransUtil().getAttachUrl(value);
		}else{
			result = value;
		}
		return result;
	}
}
