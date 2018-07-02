package hsproject.dao;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.company.DepartmentComInfo;
import weaver.workflow.workflow.WfFunctionManageUtil;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
	}
	 public void doCancelForGW(int requestid, User user){
	        WfFunctionManageUtil wfu = new WfFunctionManageUtil();
	        RecordSet rs1= new RecordSet();
	       String creater = "";
	       String currentnodetype = "";
	       String currentstatus = "";
	       String workflowid = "";
	       String currentnodeid = "";
	       String sql="select creater,currentnodetype,currentstatus,workflowid,currentnodeid from workflow_requestbase b where b.requestid=" + requestid;
	       rs1.executeSql(sql);
	       if(rs1.next()){
	           creater = Util.null2String(rs1.getString("creater"));
	           currentnodetype = Util.null2String(rs1.getString("currentnodetype"));
	           currentstatus = Util.null2String(rs1.getString("currentstatus"));
	           workflowid = Util.null2String(rs1.getString("workflowid"));
	           currentnodeid = Util.null2String(rs1.getString("currentnodeid"));
	       }
	       sql = "update workflow_currentoperator set lastisremark=isremark,isremark='' where requestid=" + requestid;
	       rs1.executeSql(sql);
	       sql = "update workflow_requestbase set laststatus=status,status='撤销',currentstatus=1 where requestid=" + requestid;
	        rs1.executeSql(sql);
	        int usertype1= ((user.getLogintype().equals("1")) ? 0 : 1);
	        wfu.saveOtherOperator(""+requestid,""+workflowid,""+currentnodeid,""+user.getUID(),""+usertype1,"c");
	    }
	public String getMulDepartname(String value) throws Exception {
		String departmentUrl = "";
		String departmentvalue = "";
		DepartmentComInfo dci = new DepartmentComInfo();
		String flag = "&nbsp;&nbsp;";
		if (!"".equals(value)) {
			String departArr[] = value.split(",");
			for (int i = 0; i < departArr.length; i++) {
				departmentvalue = departArr[i];
				if (!"".equals(departmentvalue)) {
					if ("".equals(departmentUrl)) {
						departmentUrl = "<a href=\"javascript:openFullWindowForXtable('/hrm/company/HrmDepartmentDsp.jsp?id="
								+ departmentvalue
								+ "')\">"
								+ dci.getDepartmentname(departmentvalue)
								+ "</a>";
					} else {
						departmentUrl = departmentUrl
								+ flag
								+ "<a href=\"javascript:openFullWindowForXtable('/hrm/company/HrmDepartmentDsp.jsp?id="
								+ departmentvalue + "')\">"
								+ dci.getDepartmentname(departmentvalue)
								+ "</a>";
					}
				}
			}
		}
		return departmentUrl;

	}
}
