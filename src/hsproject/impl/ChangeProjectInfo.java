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
		InsertUtil iu = new InsertUtil();	
		if(comMap.size()>0){
			iu.updateGen(comMap, "hs_projectinfo", "id", prjid);
		}
		if(defMap.size()>0){
			iu.updateGen(defMap, "hs_project_fielddata", "prjid", prjid);
		}
		PrjLogDao pld = new PrjLogDao();
		pld.writePrjLog("2", "0", prjid, comMap, defMap, oldComMap, oldDefMap, projecttype, operater);
		sql="update "+tableName+" set isnew = '1' where id="+billId;
		rs.executeSql(sql);
		return SUCCESS;
	}
	

}
