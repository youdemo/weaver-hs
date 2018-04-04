<%@ page language="java contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="weaver.conn.RecordSet"%>
<%@ page import="weaver.general.BaseBean"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*,weaver.hrm.common.*" %>
<%@ page import="javax.servlet.http.HttpServletRequest %>
<%@ page import="javax.servlet.http.HttpServletResponse %>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ include file="/systeminfo/init_wev8.jsp"%>
<%@ taglib uri="/WEB-INF/weaver.tld prefix="wea"%>
<%@ taglib uri="/browserTag prefix="brow"%>
<jsp:useBean id="rs class="weaver.conn.RecordSet scope="page />
<jsp:useBean id="ResourceComInfo class="weaver.hrm.resource.ResourceComInfo scope="page />
<jsp:useBean id="DepartmentComInfo class="weaver.hrm.company.DepartmentComInfo scope="page"/>
<jsp:useBean id="SubCompanyComInfo class="weaver.hrm.company.SubCompanyComInfo scope="page"/>
<jsp:useBean id="xssUtil class="weaver.filter.XssUtil scope="page />
<jsp:useBean id="HrmDataSource class="weaver.hrm.HrmDataSource scope="page />
<%
out.print("aaa----1111111");
	String id = Util.null2String(request.getParameter("id"));
	String prjtype = Util.null2String(request.getParameter("prjtype"));
	out.print("aaa----1111111");
	out.print("prjtype----" + prjtype);
%>
<HTML>
	<HEAD>
		<LINK href="/css/Weaver_wev8.css type=text/css rel=STYLESHEET>
		<script language="javascript src="/js/weaver_wev8.js"></script>
		<script type="text/javascript src="/appres/hrm/js/mfcommon_wev8.js"></script>
		<script language=javascript src="/js/ecology8/hrm/HrmSearchInit_wev8.js"></script>
		<style>
		.checkbox {
			display: none
		}
		</style>
	</HEAD>
	<%
	String imagefilename = "/images/hdMaintenance_wev8.gif";
	String titlename = SystemEnv.getHtmlLabelName(21039,user.getLanguage())
	+ SystemEnv.getHtmlLabelName(480, user.getLanguage())
	+ SystemEnv.getHtmlLabelName(18599, user.getLanguage())
	+ SystemEnv.getHtmlLabelName(352, user.getLanguage());
	String needfav = 1";
	String needhelp = "";
	%>
	<BODY>
		<%@ include file="/systeminfo/TopTitle_wev8.jsp"%>
		<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp"%>
		<%@ include file="/systeminfo/RightClickMenu_wev8.jsp"%>
		<FORM id=weaver name=weaver method=post action="" >
			<table id="topTitle cellpadding="0 cellspacing="0">
				<tr>
					<td>
					</td>
					<td class="rightSearchSpan" style="text-align:right;">
						<input type=button class="e8_btn_top onclick="doEdit(this);" value="编辑">
						<span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
					</td>
				</tr>
			</table>
			<wea:layout type="2col attributes="{'expandAllGroup':'true'}">
			<%
			ProjectGroupDao pgd = new ProjectGroupDao();
			out.print("aaa----1111111");
			List<ProjectGroupBean> list = new ArrayList<ProjectGroupBean>();
			list = pgd.getGroupData();
			out.print("aaa----"+list.size());
			ProjectFieldDao pfd = new ProjectFieldDao();
			ProjectInfoDao pid = new ProjectInfoDao();
			int i=0；
			for(i=0;i<list.size();i++){
				String groupname = list.get(i).getGroupname();
				out.print("groupname----" + groupname);
				String groupid = list.get(i).getId();
				out.print("aaa----222222");
			%>
			<wea:group context="<%=groupname%>">
				<%
				String fieldname = pfd.getUsedPrjField(prjtype,groupid).get(i).getFieldname();
				String iscommon = pfd.getUsedPrjField(prjtype,groupid).get(i).getIscommon();
				out.print(fieldname);
				for(i=0;i<list.size();i++){
				%>
				<wea:item><%=pfd.getUsedPrjField(prjtype,groupid).get(i).getFieldname()%></wea:item>
				<%if("0".equals(iscommon)){%>
				<wea:item><%=pid.getProjetCommonData(id).get(fieldname)%></wea:item>
				<%}else{%>
				<wea:item><%=pid.getProjetDefineData(id).get(fieldname)%></wea:item>
				<%}%>
				<%}%>
			</wea:group>
			<%}%>
			</wea:layout>
		</FORM>
		<SCRIPT language="javascript">
		</script>	
		<SCRIPT language="javascript src="/js/datetime_wev8.js"></script>
		<SCRIPT language="javascript defer="defer src="/js/JSDateTime/WdatePicker_wev8.js"></script>
		<script type="text/javascript src="/js/selectDateTime_wev8.js"></script>
	</BODY>
</HTML>