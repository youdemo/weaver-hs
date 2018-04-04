package hsproject.dao;

import hsproject.bean.ProjectTypeBean;

import java.util.ArrayList;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ProjectTypeDao {
	/**
	 * 获取启用项目类型
	 * @return
	 */
	public List<ProjectTypeBean> getUsedProjectType(){
		List<ProjectTypeBean> list = new ArrayList<ProjectTypeBean>();
		RecordSet rs = new RecordSet();
		String sql="select * from uf_project_type where isused='1' order by dsporder asc";
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
			list.add(ptb);
		}
		return list;
	}
	
	public List<ProjectTypeBean> getUsedProjectType(String department){
		List<ProjectTypeBean> list = new ArrayList<ProjectTypeBean>();
		RecordSet rs = new RecordSet();
		String sql="select * from uf_project_type where isused='1' and department='"+department+"' order by dsporder asc";
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
			list.add(ptb);
		}
		return list;
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
			list.add(ptb);
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
