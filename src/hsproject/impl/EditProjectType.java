package hsproject.impl;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class EditProjectType implements Action{
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();
		String billId = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String department = "";
		String olddepartment = "";
		String sql = "select b.tablename from modeinfo a,workflow_bill b where a.formid=b.id and a.id="
				+ modeId;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where id=" + billId;
		rs.executeSql(sql);
		if (rs.next()) {
			department = Util.null2String(rs.getString("department"));
			olddepartment = Util.null2String(rs.getString("olddepartment"));
		}
		
		if(!"".equals(olddepartment)){
			if(!department.equals(olddepartment)){
				AddDocShare ads = new AddDocShare();
				ads.addShareByType(department, billId);
			}
		}
		sql="update "+tableName+" set olddepartment=department where id="+billId;
		rs.executeSql(sql);
		
		
		return SUCCESS;
	}
}
