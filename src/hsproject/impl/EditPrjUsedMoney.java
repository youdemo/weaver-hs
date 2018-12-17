package hsproject.impl;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 更新项目发生额合计
 * @author tangjianyong 2018-11-06
 *
 */
public class EditPrjUsedMoney implements Action {

	@Override
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();
		String billId = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String prjid = "";
		String mainid = "";
		String money = "";
		String sql = "select b.tablename from modeinfo a,workflow_bill b where a.formid=b.id and a.id="
				+ modeId;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where id=" + billId;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
			prjid = Util.null2String(rs.getString("prjid"));
		}
		sql = "select nvl(sum(nvl(usedmoney,0)),0) as money from "+tableName+"_dt1 where mainid="+mainid;
		rs.executeSql(sql);
		if(rs.next()) {
			money = Util.null2String(rs.getString("money"));
		}
		
		sql = "update hs_projectinfo set prjamount='"+money+"' where id="+prjid;
		rs.executeSql(sql);
		
		return SUCCESS;
	}
	
	

}
