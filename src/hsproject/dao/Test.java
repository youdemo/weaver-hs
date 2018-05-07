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
		DataSource ds = (DataSource) StaticObj.getServiceByFullname(("datasource.SSH"), DataSource.class);
		//  status < 4 and
		ra.executeSql("select workcode from hrmresource where status<5  and isnull(resourceimageid,0) < 100");
		int flag = 0;
		while(ra.next()){
			flag++;
			String code = Util.null2String(ra.getString("workcode"));
			out.println(code + " = "+flag+"<br>");
			if(code.length() < 1) continue;
			try {
				String uploadBuffer = "";
				Connection conn = null;
				conn = ds.getConnection();
				ResultSet rs1;
				String sql = "select Photo1 from tmc_1 where badge = '"+code+"'";
				if(code.length() == 6){
					sql = "select Photo1 from tmc_1 where badge like '%"+code+"%'";
				}
				rs1 = conn.createStatement().executeQuery(sql);
				InputStream aa = null;
					if(rs1.next()){
						aa= rs1.getBinaryStream(1);
					}
				if(aa !=null){
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					 byte[] buffer = new byte[1024];
					 int count1 = 0;
					 while((count1 = aa.read(buffer)) >= 0){
					 baos.write(buffer, 0, count1);
					 }
					 uploadBuffer = new String(Base64.encode(baos.toByteArray()));
			//		out.println(uploadBuffer);
					String name = code + ".jpg";
					String imageFileID = getDocId(name,uploadBuffer,"1");
				}
			try{
		               Thread.currentThread().sleep(500);
		        }catch(InterruptedException ie){
		        	ie.printStackTrace();
		        }
			} catch (Exception e) {
				e.printStackTrace();
				out.println(e.getMessage() +"<br>");
			}
		}

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
