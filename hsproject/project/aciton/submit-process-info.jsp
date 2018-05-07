<%@ page import="weaver.general.Util" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONException" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ page import="hsproject.impl.*" %>
<%@ page import="hsproject.util.*" %>

<%@ page import="weaver.file.FileUpload" %>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", -10);
%>

<%
	int userid = user.getUID();
	FileUpload fu = new FileUpload(request);
	ProcessInfoImpl  pii = new ProcessInfoImpl();
    String submitType_mt = Util.null2String(fu.getParameter("submitType_mt"));//提交类型
	String prjtype = Util.null2String(fu.getParameter("prjtype"));
	String prjid = Util.null2String(fu.getParameter("prjid"));//
	String processtype = Util.null2String(fu.getParameter("processtype"));//
	String processid = Util.null2String(fu.getParameter("processid"));;
	Map<String, String> pidComMap = new HashMap<String, String>();
	Map<String, String> pidDefMap = new HashMap<String, String>();
	ProcessFieldDao pfd = new ProcessFieldDao();
	List<ProcessFieldBean>  pfblist = pfd.getUsedProcessField(prjtype,processtype,"");
	for(ProcessFieldBean pfb:pfblist){
		String isCommon = pfb.getIscommon();
		String fieldName = pfb.getFieldname();
		if("1".equals(pfb.getIsreadonly())){
			continue;
		}
		if("0".equals(isCommon)){
			pidComMap.put(fieldName,Util.null2String(fu.getParameter(fieldName)));
		}else{
			pidDefMap.put(fieldName,Util.null2String(fu.getParameter("idef_"+fieldName)));
		}
	}
	if("add".equals(submitType_mt)){
		
		processid=pii.addProcessInfo(pidComMap,pidDefMap,prjtype,processtype,prjid,""+userid);
	}else if("edit".equals(submitType_mt)){
		pii.editProcessInfo(pidComMap,pidDefMap,prjtype,processtype,processid,""+userid);
	}

	//response.sendRedirect("/hsproject/project/view/hs-project-info-url.jsp?id="+prjid);
	//	return;
%>
<script type="text/javascript">
parent.window.location.href="/hsproject/project/view/hs-process-info-url.jsp?id=<%=processid%>";
try{
	var parentWin = parent.parent.getParentWindow(parent);
	var dialog = parent.parent.getDialog(parent);
	parentWin._table.reLoad();
}catch (e) {
	
}
</script>