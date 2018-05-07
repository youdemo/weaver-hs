<%@ page import="weaver.general.Util" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONException" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ page import="hsproject.impl.*" %>
<%@ page import="hsproject.util.*" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", -10);
%>

<%
	BaseBean log = new BaseBean();
	int userid = user.getUID();
	String prjtype_mt = Util.null2String(request.getParameter("prjtype_mt"));
	String reportname_mt = Util.null2String(request.getParameter("reportname_mt"));
	String reportid_mt = Util.null2String(request.getParameter("reportid_mt"));
	String type_mt = Util.null2String(request.getParameter("type_mt"));
	PrjReportImpl pri = new PrjReportImpl();
	Map<String, String> showMap = new HashMap<String, String>();
	Map<String, String> queryMap = new HashMap<String, String>();
	ProjectFieldDao pfd = new ProjectFieldDao();
    List<ProjectFieldBean>  pfblist = pfd.getUsedPrjField(prjtype_mt,"");
	for(ProjectFieldBean pfb:pfblist){
		String fieldname =pfb.getFieldname();
		String iscommon = pfb.getIscommon();
		if("0".equals(iscommon)){
		}else{
			fieldname = "def_"+fieldname;
		}
		String isShow = Util.null2String(request.getParameter(fieldname+"_checkbox1"));
		String fieldid = Util.null2String(request.getParameter(fieldname));
		String isquery = Util.null2String(request.getParameter(fieldname+"_checkbox2"));
		String dsporder = Util.null2String(request.getParameter(fieldname+"_dsporder"));
		//log.writeLog("fieldname "+fieldname+" isShow:"+isShow+" isquery:"+isquery);
		if("".equals(dsporder)){
			dsporder = "0.00";
		}
		fieldid=fieldid+"_"+dsporder;
		if("1".equals(isShow)){
			showMap.put(fieldname,fieldid);
		}
		if("1".equals(isquery)){
			queryMap.put(fieldname,fieldid);
		}
	}
	 if("add".equals(type_mt)){
		 reportid_mt=pri.addPrjConfig(prjtype_mt,String.valueOf(userid),showMap,queryMap,reportname_mt);
	 }else if("edit".equals(type_mt)){
	 	pri.editPrjConfig(reportid_mt,showMap,queryMap,reportname_mt);
	 }

	



%>
<script type="text/javascript">
parent.window.location.href="/hsproject/project/report/hs-create-prj-report-Url.jsp?reportid=<%=reportid_mt%>";
try{
	var parentWin = parent.parent.getParentWindow(parent);
	var dialog = parent.parent.getDialog(parent);
	parentWin._table.reLoad();
}catch (e) {
	
}
</script>