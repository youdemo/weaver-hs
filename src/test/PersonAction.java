package test;

import org.apache.tools.ant.taskdefs.Basename;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class PersonAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		log.writeLog("testaaa");
		String workflowID = info.getWorkflowid();//获取工作流程Workflowid的值
		String requestid = info.getRequestid();//获取requestid的值
		RecordSet rs = new RecordSet();
		User usr = info.getRequestManager().getUser();
		String tableName = "";
		String manager = "";
		log.writeLog("userid:"+usr.getUID());
		String sql  = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= "
				+ workflowID + ")";
		
		rs.execute(sql);
		if(rs.next()){
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			manager = Util.null2String(rs.getString("manager"));
		}
		log.writeLog("manager:"+manager);
		return SUCCESS;
	}

}
