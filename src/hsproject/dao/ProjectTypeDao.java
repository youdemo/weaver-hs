package hsproject.dao;

import hsproject.bean.ProjectTypeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ProjectTypeDao {
	/**
	 * 获取启用项目类型
	 * @return
	 */
	public List<ProjectTypeBean> getUsedProjectType(String subid){
		List<ProjectTypeBean> list = new ArrayList<ProjectTypeBean>();
		RecordSet rs = new RecordSet();
		String sql="select * from uf_project_type where isused='1' and subcompany='"+subid+"' order by dsporder asc";
		rs.executeSql(sql);
		while(rs.next()){
			ProjectTypeBean ptb = new ProjectTypeBean();
			ptb.setId( Util.null2String(rs.getString("id")));
			ptb.setTypename( Util.null2String(rs.getString("typename")));
			ptb.setProtypecode(Util.null2String(rs.getString("protypecode")));
			ptb.setDescription(Util.null2String(rs.getString("description")));
			ptb.setDepartment(Util.null2String(rs.getString("department")));
			ptb.setSeclevelstart(Util.null2String(rs.getString("seclevelstart")));
			ptb.setSeclevelend(Util.null2String(rs.getString("seclevelend")));
			ptb.setManager(Util.null2String(rs.getString("manager")));
			ptb.setAttach(Util.null2String(rs.getString("attach")));
			ptb.setCreater(Util.null2String(rs.getString("creater")));
			ptb.setCreatedate(Util.null2String(rs.getString("createdate")));
			ptb.setDsporder(Util.null2String(rs.getString("dsporder")));
			ptb.setIsused(Util.null2String(rs.getString("isused")));
			ptb.setSubcompany(Util.null2String(rs.getString("subcompany")));
			list.add(ptb);
		}
		return list;
	}
	
	public List<ProjectTypeBean> getUsedProjectType(String department,String subid){
		List<ProjectTypeBean> list = new ArrayList<ProjectTypeBean>();
		if("".equals(department)){
			return list;
		}
		RecordSet rs = new RecordSet();
		String sql="select * from uf_project_type where isused='1' and department in("+department+") and subcompany='"+subid+"' order by dsporder asc";
		rs.executeSql(sql);
		while(rs.next()){
			ProjectTypeBean ptb = new ProjectTypeBean();
			ptb.setId( Util.null2String(rs.getString("id")));
			ptb.setTypename( Util.null2String(rs.getString("typename")));
			ptb.setProtypecode(Util.null2String(rs.getString("protypecode")));
			ptb.setDescription(Util.null2String(rs.getString("description")));
			ptb.setDepartment(Util.null2String(rs.getString("department")));
			ptb.setSeclevelstart(Util.null2String(rs.getString("seclevelstart")));
			ptb.setSeclevelend(Util.null2String(rs.getString("seclevelend")));
			ptb.setManager(Util.null2String(rs.getString("manager")));
			ptb.setAttach(Util.null2String(rs.getString("attach")));
			ptb.setCreater(Util.null2String(rs.getString("creater")));
			ptb.setCreatedate(Util.null2String(rs.getString("createdate")));
			ptb.setDsporder(Util.null2String(rs.getString("dsporder")));
			ptb.setIsused(Util.null2String(rs.getString("isused")));
			ptb.setSubcompany(Util.null2String(rs.getString("subcompany")));
			list.add(ptb);
		}
		return list;
	}
	/**
	 * 过去项目类型信息
	 * @param department
	 * @param subid
	 * @return
	 */
	public ProjectTypeBean getProjectType(String prjtype){
		ProjectTypeBean ptb = new ProjectTypeBean();
		if("".equals(prjtype)){
			return ptb;
		}
		RecordSet rs = new RecordSet();
		String sql="select * from uf_project_type where id='"+prjtype+"'";
		rs.executeSql(sql);
		if(rs.next()){
			ptb.setId( Util.null2String(rs.getString("id")));
			ptb.setTypename( Util.null2String(rs.getString("typename")));
			ptb.setProtypecode(Util.null2String(rs.getString("protypecode")));
			ptb.setDescription(Util.null2String(rs.getString("description")));
			ptb.setDepartment(Util.null2String(rs.getString("department")));
			ptb.setSeclevelstart(Util.null2String(rs.getString("seclevelstart")));
			ptb.setSeclevelend(Util.null2String(rs.getString("seclevelend")));
			ptb.setManager(Util.null2String(rs.getString("manager")));
			ptb.setAttach(Util.null2String(rs.getString("attach")));
			ptb.setCreater(Util.null2String(rs.getString("creater")));
			ptb.setCreatedate(Util.null2String(rs.getString("createdate")));
			ptb.setDsporder(Util.null2String(rs.getString("dsporder")));
			ptb.setIsused(Util.null2String(rs.getString("isused")));
			ptb.setSubcompany(Util.null2String(rs.getString("subcompany")));
			
		}
		return ptb;
	}
	
	/**
	 * 获取全部项目类型
	 * @return
	 */
	public List<ProjectTypeBean> getAllProjectType(){
		List<ProjectTypeBean> list = new ArrayList<ProjectTypeBean>();
		RecordSet rs = new RecordSet();
		String sql="select * from uf_project_type  order by dsporder asc";
		rs.executeSql(sql);
		while(rs.next()){
			ProjectTypeBean ptb = new ProjectTypeBean();
			ptb.setTypename( Util.null2String(rs.getString("typename")));
			ptb.setProtypecode(Util.null2String(rs.getString("protypecode")));
			ptb.setDescription(Util.null2String(rs.getString("description")));
			ptb.setDepartment(Util.null2String(rs.getString("department")));
			ptb.setSeclevelstart(Util.null2String(rs.getString("seclevelstart")));
			ptb.setSeclevelend(Util.null2String(rs.getString("seclevelend")));
			ptb.setManager(Util.null2String(rs.getString("manager")));
			ptb.setAttach(Util.null2String(rs.getString("attach")));
			ptb.setCreater(Util.null2String(rs.getString("creater")));
			ptb.setCreatedate(Util.null2String(rs.getString("createdate")));
			ptb.setDsporder(Util.null2String(rs.getString("dsporder")));
			ptb.setIsused(Util.null2String(rs.getString("isused")));
			ptb.setSubcompany(Util.null2String(rs.getString("subcompany")));
			list.add(ptb);
		}
		return list;
	}
	
	/**
	 * 获取项目类型公司集合
	 * @return
	 */
	public List<Map<String,String>> getPrjTypeCompany(){
		RecordSet rs = new RecordSet();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		String subcompanyid = "";
		String subcompanyname = "";
		String sql = "select distinct b.id,b.subcompanyname from uf_project_type a,hrmsubcompany b  where a.subcompany=b.id ";
		rs.executeSql(sql);
		while(rs.next()){
			Map<String, String> map = new HashMap<String, String>();
			subcompanyid = Util.null2String(rs.getString("id"));
			subcompanyname = Util.null2String(rs.getString("subcompanyname"));
			map.put("subcompanyid", subcompanyid);
			map.put("subcompanyname", subcompanyname);
			list.add(map);
		}
		return list;
		
	}
	
	/**
	 * 根据id获取类型名
	 * @param typeid
	 * @return
	 */
	public String getTypeName(String typeid){
		RecordSet rs = new RecordSet();
		String typeName="";
		if("".equals(typeid)){
			return typeName;
		}
		String sql="select typename from uf_project_type  where id="+typeid;
		rs.executeSql(sql);
		if(rs.next()){
			typeName = Util.null2String(rs.getString("typename"));
		}
		return typeName;
	}
}
