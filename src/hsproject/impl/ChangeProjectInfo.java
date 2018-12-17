package hsproject.impl;

import java.util.HashMap;
import java.util.Map;

import hsproject.bean.PrjChangeDetailBean;
import hsproject.bean.ProjectFieldBean;
import hsproject.dao.PrjChangeDetailDao;
import hsproject.dao.PrjLogDao;
import hsproject.dao.ProjectFieldDao;
import hsproject.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class ChangeProjectInfo implements Action{

	@Override
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();//表单建模模块iid
		String billId = info.getRequestid();//建模数据id
		RecordSet rs = new RecordSet();
		ProjectFieldDao pfd = new ProjectFieldDao();
		String tableName = "";//建模表名
		String prjid = "";
		String projecttype = "";
		String prjfield = "";
		String operater = "";
		String oldvalue = "";
		String newvalue = "";
		String sysno = "";
		String bgzds = "";//项目变更字段
		String changestatus = "";
		String otherinfo = "";
		String flag = "";
		String sql = "select b.tablename from modeinfo a,workflow_bill b where a.formid=b.id and a.id="
				+ modeId;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from "+tableName+" where id="+billId;
		rs.executeSql(sql);
		if(rs.next()){
			prjid = Util.null2String(rs.getString("prjid"));
			projecttype = Util.null2String(rs.getString("projecttype"));
			operater = Util.null2String(rs.getString("operater"));
			changestatus = Util.null2String(rs.getString("changestatus"));
			otherinfo = Util.null2String(rs.getString("otherinfo"));
		}
		Map<String, String> oldComMap = new HashMap<String, String>();
		Map<String, String> oldDefMap = new HashMap<String, String>();
		Map<String, String> comMap = new HashMap<String, String>();
		Map<String, String> defMap = new HashMap<String, String>();
		PrjChangeDetailDao pcdd = new PrjChangeDetailDao();
		sql="select * from "+tableName+"_dt1 where mainid="+billId;
		rs.executeSql(sql);
		while(rs.next()){
			prjfield = Util.null2String(rs.getString("prjfield"));
			sysno = Util.null2String(rs.getString("sysno"));
			PrjChangeDetailBean pcdb = pcdd.getChangeInfo(sysno);
			if("".equals(pcdb.getFieldid())){
				continue;
			}	
			oldvalue =	pcdb.getOldvalue();
			newvalue = pcdb.getNewvalue();
			ProjectFieldBean pfb = pfd.getPrjFieldBean(prjfield);
			bgzds=bgzds+flag+pfb.getShowname();
			flag=",";
			String fieldname = pfb.getFieldname();
			String isCommon = pfb.getIscommon();
			if("0".equals(isCommon)){
				comMap.put(fieldname, newvalue);
				oldComMap.put(fieldname, oldvalue);
			}else{
				defMap.put(fieldname, newvalue);
				oldDefMap.put(fieldname, oldvalue);
			}
		}
		if("0".equals(changestatus)){
			comMap.put("status", "项目终止");
			bgzds=bgzds+flag+"项目终止";
			flag=",";
		}else if("1".equals(changestatus)){
			comMap.put("status", "项目暂停");
			bgzds=bgzds+flag+"项目暂停";
			flag=",";
		}else if("2".equals(changestatus)){
			comMap.put("status", getPrjCurrentStatus(prjid,projecttype));
			bgzds=bgzds+flag+"取消暂停";
			flag=",";
		}
		if(!"".equals(otherinfo)) {
			bgzds=bgzds+flag+"其他信息变更";
		}
		InsertUtil iu = new InsertUtil();	
		if(comMap.size()>0){
			iu.updateGen(comMap, "hs_projectinfo", "id", prjid);
		}
		if(defMap.size()>0){
			iu.updateGen(defMap, "hs_project_fielddata", "prjid", prjid);
		}
		PrjLogDao pld = new PrjLogDao();
		pld.writePrjLog("2", "0", prjid, comMap, defMap, oldComMap, oldDefMap, projecttype, operater);
		sql="update "+tableName+" set isnew = '1',bgzds='"+bgzds+"' where id="+billId;
		rs.executeSql(sql);
		return SUCCESS;
	}
	
	public String getPrjCurrentStatus(String prjid,String prjtype) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql = "";
		String processid = "";
		String id = "";
		String sql_dt = "";
		String processname = "";
		String statusname = "";
		sql = "select id from hs_prj_process where prjid='"+prjid+"' and status='进行中'";
		rs.executeSql(sql);
		if(rs.next()) {
			processid = Util.null2String(rs.getString("id"));
		}
		if("".equals(processid)) {
			sql="select id,processname from uf_prj_process where prjtype='"+prjtype+"' and isused='1' order by dsporder asc,id asc";
			rs.executeSql(sql);
			while(rs.next()) {
				id = Util.null2String(rs.getString("id"));
				int count =0;
				sql_dt = "select COUNT(1) as count from hs_prj_process where prjid='"+prjid+"' and processtype='"+id+"' and nvl(isused,'0')<>1";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()){
					count = rs_dt.getInt("count");
				}
				if(count == 0){
					continue;
				}
				count =0;
				sql_dt = "select COUNT(1) as count from hs_prj_process where prjid='"+prjid+"' and processtype='"+id+"' and isdone='1'";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()){
					count = rs_dt.getInt("count");
				}
				if(count == 1){
					continue;
				}else {
					sql_dt = "select id from hs_prj_process where prjid='"+prjid+"' and processtype='"+id+"'";
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						processid = Util.null2String(rs_dt.getString("id"));
					}
					break;
				}
			}
		}
		if(!"".equals(processid)) {
			sql = "select b.processname from hs_prj_process a,uf_prj_process b where a.processtype=b.id and a.id="+processid;
			rs.executeSql(sql);
			if(rs.next()) {
				processname = Util.null2String(rs.getString("processname"));
			}
			sql="select statusname from uf_prj_proc_status where processname='"+processname+"'";
			rs.executeSql(sql);
			if(rs.next()){
				statusname = Util.null2String(rs.getString("statusname"));
			}
		}else {
			statusname = "完成";
		}
		return statusname;
		
	}

}
