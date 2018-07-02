package hsproject.impl;

import hsproject.bean.PrjChangeDetailBean;
import hsproject.bean.ProcessFieldBean;
import hsproject.dao.PrjChangeDetailDao;
import hsproject.dao.PrjLogDao;
import hsproject.dao.ProcessFieldDao;
import hsproject.util.InsertUtil;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class ChangePrjProcessInfo implements Action{

	@Override
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();//表单建模模块iid
		String billId = info.getRequestid();//建模数据id
		RecordSet rs = new RecordSet();
		ProcessFieldDao pfd = new ProcessFieldDao();
		String tableName = "";//建模表名
		String processfield = "";
		String operater = "";
		String oldvalue = "";
		String newvalue = "";
		String processid = "";
		String processtype = "";
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
			operater = Util.null2String(rs.getString("operater"));
			processid = Util.null2String(rs.getString("processid"));
			processtype = Util.null2String(rs.getString("processtype"));
		}
		Map<String, String> oldComMap = new HashMap<String, String>();
		Map<String, String> oldDefMap = new HashMap<String, String>();
		Map<String, String> comMap = new HashMap<String, String>();
		Map<String, String> defMap = new HashMap<String, String>();
		PrjChangeDetailDao pcdd = new PrjChangeDetailDao();
		sql="select * from "+tableName+"_dt1 where mainid="+billId;
		rs.executeSql(sql);
		while(rs.next()){
			processfield = Util.null2String(rs.getString("processfield"));
			sysno = Util.null2String(rs.getString("sysno"));
			PrjChangeDetailBean pcdb = pcdd.getChangeInfo(sysno);
			if("".equals(pcdb.getFieldid())){
				continue;
			}	
			oldvalue =	pcdb.getOldvalue();
			newvalue = pcdb.getNewvalue();
			ProcessFieldBean pfb = pfd.getProcessFieldBean(processfield);
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
			iu.updateGen(comMap, "hs_prj_process", "id", processid);
		}
		if(defMap.size()>0){
			iu.updateGen(defMap, "hs_prj_process_fielddata", "processid", processid);
		}
		PrjLogDao pld = new PrjLogDao();
		pld.writePrjLog("2", "1", processid, comMap, defMap, oldComMap, oldDefMap, processtype, operater);
		sql="update "+tableName+" set isnew = '1' where id="+billId;
		rs.executeSql(sql);
		return SUCCESS;
	}
	

}
