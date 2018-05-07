<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="hsproject.util.BrowserInfoUtil"%>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<%@ taglib uri="/browserTag" prefix="brow"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page" />
<jsp:useBean id="projectUtil" class="hsproject.util.ProjectUtil" scope="page" />

<%
Integer lg=(Integer)user.getLanguage();
weaver.general.AccountType.langId.set(lg);
%>
<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="AccountType" class="weaver.general.AccountType" scope="page" />
<jsp:useBean id="LicenseCheckLogin" class="weaver.login.LicenseCheckLogin" scope="page" />
<HTML>
	<HEAD>
		<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
		<script type="text/javascript" src="/appres/hrm/js/mfcommon_wev8.js"></script>
		<script language=javascript src="/js/ecology8/hrm/HrmSearchInit_wev8.js"></script>
		<script type='text/javascript' src='/js/jquery-autocomplete/lib/jquery.bgiframe.min_wev8.js'></script>
<script type='text/javascript' src='/js/jquery-autocomplete/jquery.autocomplete_wev8.js'></script>
<script type='text/javascript' src='/js/jquery-autocomplete/browser_wev8.js'></script>
<link rel="stylesheet" type="text/css" href="/js/jquery-autocomplete/jquery.autocomplete_wev8.css" />
<link rel="stylesheet" type="text/css" href="/js/jquery-autocomplete/browser_wev8.css" />
		<SCRIPT language="JavaScript" src="/js/weaver_wev8.js"></SCRIPT>
		<link rel="stylesheet" href="/css/ecology8/request/requestTopMenu_wev8.css" type="text/css" />
		<link rel="stylesheet" href="/wui/theme/ecology8/jquery/js/zDialog_e8_wev8.css" type="text/css" />
		<style>
		.checkbox {
			display: none
		}
		</style>
	</head>
	<%
	int language =user.getLanguage();
	Calendar now = Calendar.getInstance();
	
	int userid = user.getUID();
	String imagefilename = "/images/hdReport_wev8.gif";
	String titlename =SystemEnv.getHtmlLabelName(20536,user.getLanguage());
	String needfav ="1";
	String needhelp ="";
	boolean flagaccount = weaver.general.GCONST.getMOREACCOUNTLANDING();
    String out_pageId = "hsxmlball";
	String prjname = Util.null2String(request.getParameter("prjname"));
	String procode = Util.null2String(request.getParameter("procode"));
	String prjtype = Util.null2String(request.getParameter("prjtype"));
	String status = Util.null2String(request.getParameter("status"));
	String beginDate = Util.null2String(request.getParameter("beginDate"));
	String endDate = Util.null2String(request.getParameter("endDate"));
	String beginDate1 = Util.null2String(request.getParameter("beginDate1"));
	String endDate1 = Util.null2String(request.getParameter("endDate1"));
	String department =Util.null2String(request.getParameter("department"));
	String isdelay = Util.null2String(request.getParameter("isdelay"));

	%>
	<BODY>
		<div id="tabDiv">
			<span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>"><%=SystemEnv.getHtmlLabelName(20536,user.getLanguage()) %></span>
		</div>
		<div id="dialog">
			<div id='colShow'></div>
		</div>
	    <input type="hidden" name="pageId" id="pageId" value="<%=out_pageId%>"/>
		<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>
		<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
		<%
		RCMenu += "{刷新,javascript:refersh(),_self} " ;
		//RCMenu += "{导出,javascript:_xtable_getAllExcel(),_self} " ;
		RCMenuHeight += RCMenuHeightStep ;
		%>
		<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
		<FORM id=report name=report action="/hsproject/project/list/hs-project-list.jsp" method=post>
			<input type="hidden" name="requestid" value="">
			<table id="topTitle" cellpadding="0" cellspacing="0">
				<tr>
					<td></td>
					<td class="rightSearchSpan" style="text-align:right;">
					<span id="advancedSearch" class="advancedSearch"><%=SystemEnv.getHtmlLabelName(21995,user.getLanguage())%></span>
						<span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
				</tr>
			</table>
			
			<% // 查询条件 %>
			<div class="advancedSearchDiv" id="advancedSearchDiv" style="display:none;">
				<wea:layout type="4col">
				<wea:group context="查询条件">
					<wea:item>项目名称</wea:item>
					<wea:item>
						<input name="prjname" id="prjname" class="InputStyle" type="text" value="<%=prjname%>"/>
					</wea:item>
					<wea:item>项目编号</wea:item>
					<wea:item>
						<input name="procode" id="procode" class="InputStyle" type="text" value="<%=procode%>"/>
					</wea:item>
					<wea:item>项目状态</wea:item>
					<wea:item>
						<input name="status" id="status" class="InputStyle" type="text" value="<%=status%>"/>
					</wea:item>
					<wea:item>项目类型</wea:item>
					<wea:item>
						<brow:browser viewType="0"  name="prjtype" browserValue="<%=prjtype%>"
						browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.hs_prj_type"
						hasInput="true"  hasBrowser = "true" isMustInput='1' isSingle="false"
						width="120px"
						linkUrl=""
						browserSpanValue="<%= new BrowserInfoUtil().getProjectType(prjtype)%>">
						</brow:browser>
               	   </wea:item>
					<wea:item>项目开始时间</wea:item>
					 <wea:item>
                    <button type="button" class=Calendar id="selectBeginDate" onclick="onshowPlanDate('beginDate','selectBeginDateSpan')"></BUTTON>
                        <SPAN id=selectBeginDateSpan ><%=beginDate%></SPAN>
                        <INPUT type="hidden" name="beginDate" id="beginDate" value="<%=beginDate%>">
                    &nbsp;-&nbsp;
                    <button type="button" class=Calendar id="selectEndDate" onclick="onshowPlanDate('endDate','endDateSpan')"></BUTTON>
                        <SPAN id=endDateSpan><%=endDate%></SPAN>
                        <INPUT type="hidden" name="endDate" id="endDate" value="<%=endDate%>">
                	</wea:item>
					<wea:item>项目结束时间</wea:item>
					 <wea:item>
                    <button type="button" class=Calendar id="selectBeginDate1" onclick="onshowPlanDate('beginDate1','selectBeginDateSpan1')"></BUTTON>
                        <SPAN id=selectBeginDateSpan1 ><%=beginDate1%></SPAN>
                        <INPUT type="hidden" name="beginDate1" id="beginDate1" value="<%=beginDate1%>">
                    &nbsp;-&nbsp;
                    <button type="button" class=Calendar id="selectEndDate1" onclick="onshowPlanDate('endDate1','endDateSpan1')"></BUTTON>
                        <SPAN id=endDateSpan1><%=endDate1%></SPAN>
                        <INPUT type="hidden" name="endDate1" id="endDate1" value="<%=endDate1%>">
                	</wea:item>
                	<wea:item>归属部门</wea:item>
                	<wea:item>
                    <brow:browser viewType="0" name="department" browserValue='<%=department%>'
                    browserurl="/systeminfo/BrowserMain.jsp?mouldID=hrm&url=/hrm/company/MutiDepartmentBrowser.jsp?selectedids="
                    hasInput="true" isSingle="false" hasBrowser = "true" isMustInput='1'
                    completeUrl="/data.jsp?type=4"
                    browserSpanValue="<%= new BrowserInfoUtil().getMulDepartname(department)%>" ></brow:browser>
                    </wea:item>

                    <wea:item>是否延期</wea:item>
                	<wea:item>
                      <select class="e8_btn_top middle" name="isdelay" id="isdelay">
		                <option value="" <%if("".equals(isdelay)){%> selected<%} %>>
		                    <%=""%></option>
		                <option value="0" <%if("0".equals(isdelay)){%> selected<%} %>>
		                    <%="是"%></option>
		                <option value="1" <%if("1".equals(isdelay)){%> selected<%} %>>
		                    <%="否"%></option>
                </select>

                    </wea:item>


				</wea:group>
				<wea:group context="">
				<wea:item type="toolbar">
				<input type="button" value="<%=SystemEnv.getHtmlLabelName(30947,user.getLanguage())%>" class="e8_btn_submit" onclick="onBtnSearchClick();"/>
				<input type="button" value="<%=SystemEnv.getHtmlLabelName(31129,user.getLanguage())%>" class="e8_btn_cancel" id="cancel"/>
				</wea:item>
				</wea:group>
				</wea:layout>
			</div>
		</FORM>
		<%
		String backfields = "id,name, procode,prjtype,manager,members,begindate,enddate,status,belongdepart,belongCompany,case when isdelay='0' then '是' else '否' end as isdelay";
		String fromSql  =  " from hs_projectinfo t1";
		String sqlWhere =  " 1=1 ";
		sqlWhere = sqlWhere + projectUtil.getPrjShareWhereByUser(String.valueOf(userid));
		if(!"".equals(prjname)){
			sqlWhere = sqlWhere+" and upper(name) like upper('%"+prjname+"%')";
		}
		if(!"".equals(procode)){
			sqlWhere = sqlWhere+" and upper(procode) like upper('%"+procode+"%')";
		}
		if(!"".equals(status)){
			sqlWhere = sqlWhere+" and upper(status) like upper('%"+status+"%')";
		}
		if(!"".equals(prjtype)){
			sqlWhere = sqlWhere+" and prjtype='"+prjtype+"'";
		}
		if(!"".equals(beginDate)){
			sqlWhere = sqlWhere+" and begindate >='"+beginDate+"'";
		}
		if(!"".equals(endDate)){
			sqlWhere = sqlWhere+" and begindate <='"+endDate+"'";
		}
		if(!"".equals(beginDate1)){
			sqlWhere = sqlWhere+" and enddate >='"+beginDate1+"'";
		}
		if(!"".equals(endDate)){
			sqlWhere = sqlWhere+" and endDate <='"+endDate1+"'";
		}

		if(!"".equals(department)){
			sqlWhere+=" and belongdepart in("+department+") ";
		}
		if(!"".equals(isdelay)){
			if("0".equals(isdelay)){
				sqlWhere+=" and isdelay ='0' ";
			}else{
				sqlWhere+=" and (isdelay ='1' or isdelay is null) ";
			}
		}

		//out.print("select "+backfields+fromSql+" where "+sqlWhere);
		String orderby =  " id desc "  ;
		String tableString = "";
		String operateString= "";
		  tableString =" <table tabletype=\"none\" pagesize=\""+ PageIdConst.getPageSize(out_pageId,user.getUID(),PageIdConst.HRM)+"\" pageId=\""+out_pageId+"\" >"+         
				   "	   <sql backfields=\""+backfields+"\" sqlform=\""+fromSql+"\" sqlwhere=\""+Util.toHtmlForSplitPage(sqlWhere)+"\"  sqlorderby=\""+orderby+"\"  sqlprimarykey=\"id\" sqlsortway=\"desc\" sqlisdistinct=\"false\" />"+
		operateString+
		"			<head>";
				tableString +="<col width=\"12%\" text=\"项目名称\" column=\"name\" orderkey=\"name\"  otherpara=\"column:id\"  transmethod='hsproject.util.TransUtil.getPrjNameForColor' linkvaluecolumn=\"id\" linkkey=\"id\" href= \"/hsproject/project/view/hs-project-info-url.jsp\" target=\"_fullwindow\"/>"+ 
						"<col width=\"12%\" text=\"项目编号\" column=\"procode\" orderkey=\"procode\" />"+
						"<col width=\"12%\" text=\"项目类型\" column=\"prjtype\" orderkey=\"prjtype\" transmethod=\"hsproject.dao.ProjectTypeDao.getTypeName\" />"+
                		"<col width=\"12%\" text=\"项目经理\" column=\"manager\" orderkey=\"manager\" transmethod='weaver.proj.util.ProjectTransUtil.getResourceNamesWithLink'/>"+
                		"<col width=\"12%\" text=\"项目成员\" column=\"members\" orderkey=\"members\" transmethod='weaver.proj.util.ProjectTransUtil.getResourceNamesWithLink'/>"+
                		"<col width=\"12%\" text=\"项目开始时间\" column=\"begindate\" orderkey=\"begindate\" />"+
                		"<col width=\"12%\" text=\"项目结束时间\" column=\"enddate\" orderkey=\"enddate\" />"+
                		"<col width=\"12%\" text=\"项目状态\" column=\"status\" orderkey=\"status\" />"+
                		"<col width=\"12%\" text=\"是否延期\" column=\"isdelay\" orderkey=\"isdelay\"  />"+
                		"<col width=\"12%\" text=\"归属部门\" column=\"belongdepart\" orderkey=\"belongdepart\" transmethod='hsproject.util.BrowserInfoUtil.getMulDepartname' display=\"false\" />"+
                		"<col width=\"12%\" text=\"归属公司\" column=\"belongCompany\" orderkey=\"belongCompany\" transmethod='hsproject.util.BrowserInfoUtil.getMulSubCompany' display=\"false\" />"+
						
						"</head>"+
		 "</table>";
	//showExpExcel="false"
	%>
	<wea:SplitPageTag isShowTopInfo="false" tableString="<%=tableString%>" mode="run" />
	<script type="text/javascript">
		 function onBtnSearchClick() {
			report.submit();
		}

		function refersh() {
  			window.location.reload();
  		}
	
	  
   </script>
		<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
	
</BODY>
</HTML>