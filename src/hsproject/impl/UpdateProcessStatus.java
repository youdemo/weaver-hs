package hsproject.impl;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class UpdateProcessStatus {
	public String checkCanUpdate(String id) {
		String isused = "";
		String status = "";
		String isdone = "";
		String iscomplete = "";
		String prjtype = "";
		String processtype = "";
		String prjid = "";
		String result = "1";
		RecordSet rs = new RecordSet();
		String sql = "select prjid,isused,status,isdone,iscomplete,prjtype,processtype from hs_prj_process  where id="+id;
		rs.executeSql(sql);
		if(rs.next()) {
			isused = Util.null2String(rs.getString("isused"));
			status = Util.null2String(rs.getString("status"));
			isdone = Util.null2String(rs.getString("isdone"));
			iscomplete = Util.null2String(rs.getString("iscomplete"));
			prjtype = Util.null2String(rs.getString("prjtype"));
			processtype = Util.null2String(rs.getString("processtype"));
			prjid = Util.null2String(rs.getString("prjid"));
		}
		if("1".equals(isused)) {
			if(getNextProcess(prjtype,processtype,prjid)) {
				result = "2";//允许启用
			}else {
				result =  "3";//不允许启用
			}
		}else {
			if(!"完成".equals(status) && "".equals(isdone) && "".equals(iscomplete)) {
				result = "0";//允许停用
			}else {
				result = "1";//不允许停用
			}
		}
		
		return result;
	}
	
	public boolean getNextProcess(String prjtype,String processtype,String prjid) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String flag = "";
		String id = "";
		String sql_dt = "";
		String dtid = "";
		int count = 0;
		String status = "";
		String isdone = "";
		String iscomplete = "";
		String sql = "select id from uf_prj_process where prjtype='"+prjtype+"' and isused='1' order by dsporder asc,id asc";
		rs.executeSql(sql);
		while(rs.next()) {
			count = 0;
			id = Util.null2String(rs.getString("id"));
			if(processtype.equals(id)) {
				flag = "1";
			}
			if("1".equals(flag)&&!processtype.equals(id)) {
				sql_dt = "select COUNT(1) as count from hs_prj_process where prjid='"+prjid+"' and processtype='"+id+"' and nvl(isused,'0')<>1";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()){
					count = rs_dt.getInt("count");
				}
				if(count == 0){
					continue;
				}
				sql_dt = "select id from hs_prj_process where prjid='"+prjid+"' and processtype='"+id+"'";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()){
					dtid = Util.null2String(rs_dt.getString("id"));
				}
				break;
			}else {
				continue;
			}
		}
		if(!"".equals(dtid)) {
			sql = "select status,isdone,iscomplete from hs_prj_process where id="+dtid;
			log.writeLog(sql);
			rs.executeSql(sql);
			if(rs.next()) {
				status = Util.null2String(rs.getString("status"));
				isdone = Util.null2String(rs.getString("isdone"));
				iscomplete = Util.null2String(rs.getString("iscomplete"));
			}
			if("".equals(status)&&"".equals(isdone)&&"".equals(iscomplete)) {
				return true;
			}
		}else {
			String prjstatus = "";
			sql = "select status from hs_projectinfo where id="+prjid;
			rs.executeSql(sql);
			if(rs.next()) {
				prjstatus = Util.null2String(rs.getString("status"));
			}
			if(!"完成".equals(prjstatus)) {
				return true;
			}
		}
		return false;
	}
	
	public String updateProcessStatus(String processid,String type) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String status = "";
		String prjtype = "";
		String processtype = "";
		String prjid = "";
		if("0".equals(type)) {
			sql = "select prjid,isused,status,isdone,iscomplete,prjtype,processtype from hs_prj_process  where id="+processid;
			rs.executeSql(sql);
			if(rs.next()) {
				status = Util.null2String(rs.getString("status"));
				prjtype = Util.null2String(rs.getString("prjtype"));
				processtype = Util.null2String(rs.getString("processtype"));
				prjid = Util.null2String(rs.getString("prjid"));
			}
			if("".equals(status)) {
				sql = "update hs_prj_process set isused='1' where id="+processid;
				rs.executeSql(sql);
			}else {
				sql = "update hs_prj_process set isused='1',status='' where id="+processid;
				rs.executeSql(sql);
				updateProjectStatus(prjtype,processtype,prjid);
			}
		}else if("2".equals(type)){
			sql = "update hs_prj_process set isused='0' where id="+processid;
			rs.executeSql(sql);
		}
		return "S";
	}
	
	public void updateProjectStatus(String prjtype,String processtype,String prjid) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		BaseBean log = new BaseBean();
		String flag = "";
		String id = "";
		String sql_dt = "";
		String dtid = "";
		int count = 0;
		String processname = "";
		String statusname = "";
		String sql = "select id,processname from uf_prj_process where prjtype='"+prjtype+"' and isused='1' order by dsporder asc,id asc";
		rs.executeSql(sql);
		while(rs.next()) {
			count = 0;
			id = Util.null2String(rs.getString("id"));
			processname = Util.null2String(rs.getString("processname"));
			if(processtype.equals(id)) {
				flag = "1";
			}
			if("1".equals(flag)&&!processtype.equals(id)) {
				sql_dt = "select COUNT(1) as count from hs_prj_process where prjid='"+prjid+"' and processtype='"+id+"' and nvl(isused,'0')<>1";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()){
					count = rs_dt.getInt("count");
				}
				if(count == 0){
					continue;
				}
				sql_dt = "select id from hs_prj_process where prjid='"+prjid+"' and processtype='"+id+"'";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()){
					dtid = Util.null2String(rs_dt.getString("id"));
				}
				break;
			}else {
				continue;
			}
		}
		if(!"".equals(dtid)) {
			sql = "update hs_prj_process set status='进行中' where id="+dtid;
			rs.executeSql(sql);
			sql="select statusname from uf_prj_proc_status where processname='"+processname+"'";
			log.writeLog(sql);
			rs.executeSql(sql);
			if(rs.next()){
				statusname = Util.null2String(rs.getString("statusname"));
			}
			sql = "update hs_projectinfo set status='"+statusname+"' where id="+prjid;
			log.writeLog(sql);
			rs.executeSql(sql);
		}else {
			sql = "update hs_projectinfo set status='完成' where id="+prjid;
			rs.executeSql(sql);
		}
	}
}
