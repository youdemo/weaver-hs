package hsproject.impl;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class AddCheckDocShare implements Action {

	@Override
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();
		String billId = info.getRequestid(); 
		RecordSet rs = new RecordSet();
		String tableName = "";
		String prjid = "";
		String attach = "";
		String sql = "select b.tablename from modeinfo a,workflow_bill b where a.formid=b.id and a.id="
				+ modeId;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where id=" + billId;
		rs.executeSql(sql);
		if (rs.next()) {
			prjid = Util.null2String(rs.getString("prjid"));
			attach = Util.null2String(rs.getString("attach"));
		}
		if(!"".equals(attach) && !"".equals(prjid)) {
			AddDocShare adShare = new AddDocShare();
			adShare.addShare(prjid, attach);
		}
		return SUCCESS;
	}

}
