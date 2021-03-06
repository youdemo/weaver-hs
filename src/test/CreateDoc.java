package test;

import org.apache.axis.encoding.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CreateDoc implements Action {

	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();//流程类型唯一id
		String requestid = info.getRequestid();//具体单据的唯一id
		String tablename = info.getRequestManager().getBillTableName();//表名
		//tablename_dt1 tablename
		RecordSet rs = new RecordSet();
		String id = "";
		String xtbh = "";
		String doccategory = "";
		String sql = "select * from " + tablename + " where requestid="
				+ requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			id = Util.null2String(rs.getString("id"));
			xtbh = Util.null2String(rs.getString("xtbh"));
		}
//		sql="select * from "+tablename+"_dt1 where mainid="+id;
//		rs.executeSql(sql);
//		while (rs.next()) {
//			id = Util.null2String(rs.getString("id"));
//		}
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("test");
		Element empName = rootElement.addElement("id");
		empName.setText(id);
		Element empAge = rootElement.addElement("requestid");
		empAge.setText(requestid);
		Element empTitle = rootElement.addElement("workflowID");
		empTitle.setText(workflowID);
		rootElement.addElement("xtbh").setText(xtbh);
		//String aatext="<?xml version=\"1.0\" encoding=\"UTF-8\"?><test><id>3</id><requestid>76094</requestid><workflowID>192</workflowID></test>";
		//取目录id
		sql = "select doccategory from workflow_base   where id=" + workflowID;
		rs.executeSql(sql);
		if (rs.next()) {
			doccategory = Util.null2String(rs.getString("doccategory"));
		}
		String dcg[] = doccategory.split(",");
		String seccategory = dcg[dcg.length - 1];
		try {
			String docid = getDocId(xtbh+"text.xml", document.asXML().getBytes(),
					"1", seccategory);
			sql = "update " + tablename + " set fjsc='" + docid
					+ "' where requestid=" + requestid;
			rs.executeSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
/**
 * 
 * @param name 文件名
 * @param buffer  文件字节数组
 * @param createrid 文件创建人id
 * @param seccategory 创建目录
 * @return
 * @throws Exception
 */
	private String getDocId(String name, byte[] buffer, String createrid,
			String seccategory) throws Exception {
		String docId = "";
		DocInfo di = new DocInfo();
		di.setMaincategory(0);
		di.setSubcategory(0);
		di.setSeccategory(Integer.valueOf(seccategory));
		di.setDocSubject(name.substring(0, name.lastIndexOf(".")));
		DocAttachment doca = new DocAttachment();
		doca.setFilename(name);
		// byte[] buffer = new BASE64Decoder().decodeBuffer(value);
		String encode = Base64.encode(buffer);
		doca.setFilecontent(encode);
		DocAttachment[] docs = new DocAttachment[1];
		docs[0] = doca;
		di.setAttachments(docs);
		String departmentId = "-1";
		String sql = "select departmentid from hrmresource where id="
				+ createrid;
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
		User user = new User();
		if (rs.next()) {
			departmentId = Util.null2String(rs.getString("departmentid"));
		}
		user.setUid(Integer.parseInt(createrid));
		user.setUserDepartment(Integer.parseInt(departmentId));
		user.setLanguage(7);
		user.setLogintype("1");
		user.setLoginip("127.0.0.1");
		DocServiceImpl ds = new DocServiceImpl();
		try {
			docId = String.valueOf(ds.createDocByUser(di, user));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return docId;
	}

}
