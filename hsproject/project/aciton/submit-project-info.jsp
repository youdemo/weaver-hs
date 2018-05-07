<%@ page import="weaver.general.Util" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONException" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ page import="hsproject.impl.*" %>
<%@ page import="hsproject.util.*" %>
<%@ page import="weaver.general.BaseBean" %>
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
	BaseBean log = new BaseBean();
	FileUpload fu = new FileUpload(request);
	ProjectInfoImpl  pii = new ProjectInfoImpl();
    String submitType_mt = Util.null2String(fu.getParameter("submitType_mt"));//提交类型
	String prjtype = Util.null2String(fu.getParameter("prjtype"));
	String prjid = Util.null2String(fu.getParameter("prjid"));//
	//log.writeLog("begindate"+fu.getParameter("begindate"));
	//log.writeLog("enddate"+fu.getParameter("enddate"));
	Map<String, String> pidComMap = new HashMap<String, String>();
	Map<String, String> pidDefMap = new HashMap<String, String>();
	ProjectFieldDao pfd = new ProjectFieldDao();
	List<ProjectFieldBean>  pfblist = pfd.getUsedPrjField(prjtype,"");
	for(ProjectFieldBean pfb:pfblist){
		String isCommon = pfb.getIscommon();
		String fieldName = pfb.getFieldname();
		if("1".equals(pfb.getIsreadonly())){
			continue;
		}
		if("0".equals(isCommon)){
			if("edit1".equals(submitType_mt)){
				if(!"".equals(Util.null2String(fu.getParameter(fieldName)))){
					pidComMap.put(fieldName,Util.null2String(fu.getParameter(fieldName)));
				}
			}else{
				pidComMap.put(fieldName,Util.null2String(fu.getParameter(fieldName)));
			}
		}else{
			if("edit1".equals(submitType_mt)){
				if(!"".equals(Util.null2String(fu.getParameter("idef_"+fieldName)))){
					pidDefMap.put(fieldName,Util.null2String(fu.getParameter("idef_"+fieldName)));
				}
			}else{
				pidDefMap.put(fieldName,Util.null2String(fu.getParameter("idef_"+fieldName)));
			}
		}
	}
	if("add".equals(submitType_mt)){
		
		prjid=pii.addPorjectInfo(pidComMap,pidDefMap,prjtype,""+userid);
	}else if("edit".equals(submitType_mt)){
		pii.editProjectInfo(pidComMap,pidDefMap,prjid,""+userid);
	}else if("edit1".equals(submitType_mt)){
		pii.editProjectInfo(pidComMap,pidDefMap,prjid,""+userid);
	}

	//response.sendRedirect("/hsproject/project/view/hs-project-info-url.jsp?id="+prjid);
	//	return;
%>
<script type="text/javascript">
parent.window.location.href="/hsproject/project/view/hs-project-info-url.jsp?id=<%=prjid%>";
try{
	var parentWin = parent.parent.getParentWindow(parent);
	var dialog = parent.parent.getDialog(parent);
	parentWin._table.reLoad();
}catch (e) {
	
}
</script>