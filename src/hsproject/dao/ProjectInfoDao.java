package hsproject.dao;

import hsproject.bean.ProjectCommonFieldBean;
import hsproject.bean.ProjectFieldBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;
/**
 * 获取项目信息
 * @author jianyong.tang 2018-01-16
 *
 */
public class ProjectInfoDao {
	/**
	 * 获取项目通用字段值
	 * 
	 * @param prjid
	 * @return
	 */
	public Map<String, String> getProjetCommonData(String prjid) {
		if ("".equals(prjid)) {
			return null;
		}
		Map<String, String> commonMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		ProjectCommonFieldDao pdfd = new ProjectCommonFieldDao();
		String sql = "select * from hs_projectinfo where id=" + prjid;
		rs.executeSql(sql);
		if (rs.next()) {
			commonMap.put("id", Util.null2String(rs.getString("id")));
			List<ProjectCommonFieldBean> list = pdfd.getCommonPrjField();
			for (int i = 0; i < list.size(); i++) {
				ProjectCommonFieldBean pcfb = list.get(i);
				commonMap.put(pcfb.getFieldname(),
						Util.null2String(rs.getString(pcfb.getFieldname())));
			}
		}
		return commonMap;
	}

	/**
	 * 获取项目自定义字段值
	 * 
	 * @param prjid
	 * @return
	 */
	public Map<String, String> getProjetDefineData(String prjid) {
		if ("".equals(prjid)) {
			return null;
		}
		Map<String, String> definedMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		ProjectFieldDao pfd = new ProjectFieldDao();
		String sql = "select * from hs_project_fielddata where prjid=" + prjid;
		rs.executeSql(sql);
		if (rs.next()) {
			definedMap.put("id", Util.null2String(rs.getString("id")));
			definedMap.put("prjid", Util.null2String(rs.getString("prjid")));
			List<ProjectFieldBean> list = pfd.getAllDefineField();
			for (int i = 0; i < list.size(); i++) {
				ProjectFieldBean pfb = list.get(i);
				definedMap.put(pfb.getFieldname(),
						Util.null2String(rs.getString(pfb.getFieldname())));
			}
		}
		return definedMap;
	}
	/**
	 * 根据项目id获取项目名称
	 * @param prjid
	 * @return
	 */
	public String getPrjName(String prjid){
		RecordSet rs = new RecordSet();
		String projectName = "";
		String sql="select name from hs_projectinfo where id="+prjid;
		rs.executeSql(sql);
		if(rs.next()){
			projectName = Util.null2String(rs.getString("name"));
		}
		return projectName;				
	}
	public String getPrjNameWithLink(String prjid){
		String result = "";
		RecordSet rs = new RecordSet();
		String projectName = "";
		String sql="select name from hs_projectinfo where id="+prjid;
		rs.executeSql(sql);
		if(rs.next()){
			projectName = Util.null2String(rs.getString("name"));
		}
		result ="<a href=\"javascript:openFullWindowForXtable('/hsproject/project/view/hs-project-info-url.jsp?id="+prjid+"')\">"+projectName+"</a>";
		//result="<a href= \"/hsproject/project/view/hs-project-info-url.jsp?id="+prjid+"\" target=\"_fullwindow\">"+projectName+"</a>";
		return result;				
	}
	
	/**
	 * 获取项目字段值
	 * @param prjid
	 * @param fieldid
	 * @return
	 */
	public String getFieldValue(String prjid,String fieldid){
		String fieldValue = "";
		RecordSet rs = new RecordSet();
		String sql = "";
		if("".equals(Util.null2String(prjid)) || "".equals(Util.null2String(fieldid))){
			return "";
		}
		ProjectFieldBean pfb = new ProjectFieldDao().getPrjFieldBean(fieldid);
		String iscommon = pfb.getIscommon();
		String fieldname = pfb.getFieldname();
		if(!"".equals(fieldname)){
			if("0".equals(iscommon)){
				sql="select "+fieldname+" from hs_projectinfo where id="+prjid;
			}else{
				sql="select "+fieldname+" from hs_project_fielddata where prjid="+prjid;
			}
			rs.executeSql(sql);
			if(rs.next()){
				fieldValue = Util.null2String(rs.getString(fieldname));
			}
		}
		return fieldValue;
	}
}
