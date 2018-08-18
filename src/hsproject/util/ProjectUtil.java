package hsproject.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ProjectUtil {
	public String getDepartmentId(String userid){
		String departmentid = "";
		if("".equals(userid)){
			return "";
		}
		RecordSet rs = new RecordSet();
		String sql = "select departmentid from hrmresource where id="+userid;
		rs.executeSql(sql);
		if(rs.next()){
			departmentid = Util.null2String(rs.getString("departmentid"));			
		}
		return departmentid;
	}
	
	public String getPrjTypeDepart(String prjtype){
		String departmentid = "";
		if("".equals(prjtype)){
			return "";
		}
		RecordSet rs = new RecordSet();
		String sql = "select department from uf_project_type where id="+prjtype;
		rs.executeSql(sql);
		if(rs.next()){
			departmentid = Util.null2String(rs.getString("department"));			
		}
		return departmentid;
	}
	
	public String getPrjShareWhereByUser(String userid){
		RecordSet rs = new RecordSet();
		StringBuffer sqlWhere = new StringBuffer();
		sqlWhere.append(" and ");
		if(userid == null||"".equals(userid) ){
			sqlWhere.append("1=2 ");
			return sqlWhere.toString();
		}
		if("1".equals(userid)){
			sqlWhere.append("1=1 ");
			return sqlWhere.toString();
		}

		String departmentid=getDepartmentId(userid);
		String supdepid = getSupperDepartmentId(departmentid);
		sqlWhere.append("( ");
		sqlWhere.append(" t1.manager='"+userid+"' or ','||t1.members||',' like '%,"+userid+",%' ");
		String str2 = new StringBuilder().append("getchilds(").append(userid).append(")").toString();
	    String str3 = new StringBuilder().append("getchilds_v(").append(userid).append(")").toString();
	    if ("oracle".equalsIgnoreCase(rs.getDBType())) {
	      str2 = new StringBuilder().append(" table(getchilds(").append(userid).append(")) ").toString();
	      str3 = new StringBuilder().append(" table(getchilds_v(").append(userid).append(")) ").toString();
	    }
	    sqlWhere.append("  or exists ( select 1 from ").append(str2).append(" t2 where t2.id =t1.manager ) ");
		sqlWhere.append("  or exists ( select 1 from ").append(str3).append(" t2 where t2.id =t1.manager ) ");
	    sqlWhere.append("  or exists(select 1 from uf_project_type t2 where t2.id=t1.prjtype and t2.department in ('"+departmentid+"','"+supdepid+"'))");
	    sqlWhere.append("  or exists(select 1 from uf_prj_departperson t2 where t2.department=t1.belongdepart and t2.person="+userid+")");
	    sqlWhere.append("  or belongdepart in (select department from uf_prj_departperson where general='"+userid+"')");
	    
		sqlWhere.append(" )");
		return sqlWhere.toString();
	}
	
	public String checkisDepartPerson(String userid,String prjid){
		RecordSet rs = new RecordSet();
		int count =0 ;
		String sql = "select count(1) as count from hs_projectinfo  a,uf_prj_departperson b  where a.belongdepart=b.department and a.id="+prjid+" and b.person="+userid;
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count>0){
			return "1";
		}else{
			return "0";
		}
	}
	
	public String getSupperDepartmentId(String departmentid){
		RecordSet rs = new RecordSet();
		String sql = "";
		String superid = departmentid;
		String supdepid = "";
		boolean flag = true;
		if("".equals(departmentid)){
			return "";
		}
		while(flag){
			supdepid = "";
			sql="select supdepid from hrmdepartment where id="+superid;
			rs.executeSql(sql);
			if(rs.next()){
				supdepid = Util.null2String(rs.getString("supdepid"));				
			}
			if("".equals(supdepid)||"0".equals(supdepid)){
				flag = false;
				break;
			}
			superid = supdepid;
		}
		
		return superid;
	}
}
