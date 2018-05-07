<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ page import="hsproject.util.*" %>
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
	String prjid = Util.null2String(request.getParameter("id"));
	String prjtype = Util.null2String(request.getParameter("prjtype"));
	ProjectInfoDao pid = new ProjectInfoDao();
	Map<String, String> pidComMap=pid.getProjetCommonData(prjid);
	Map<String, String> pidDefMap=pid.getProjetDefineData(prjid);
	ProjectGroupDao pgd = new ProjectGroupDao();
	ValueTransMethod vtm = new ValueTransMethod();
	List<ProjectGroupBean> pgblist = pgd.getGroupData();
	String canedit="0";
	if(userid != 1){
	 String userdp = projectUtil.getDepartmentId(String.valueOf(userid));
	 String prjdp = projectUtil.getPrjTypeDepart(prjtype);
	  if(!"".equals(userdp) && !"".equals(prjdp)){
	  		if(userdp.equals(prjdp)){
	  			canedit = "1";
	  		}
	  }
	}else{
		canedit = "1";
	}
	String isdjr = projectUtil.checkisDepartPerson(String.valueOf(userid),prjid);
	%>
	<BODY>
		<div id="tabDiv">
			<span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>"><%=SystemEnv.getHtmlLabelName(20536,user.getLanguage()) %></span>
		</div>
		<div id="dialog">
			<div id='colShow'></div>
		</div>
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
					<%
						if("1".equals(canedit)){
					%>
					<input type="button" value="编辑" class="e8_btn_top_first" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px;" onclick="editprj();"/>
					<%
						}else if("1".equals(isdjr)){
					%>
						<input type="button" value="编辑" class="e8_btn_top_first" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px;" onclick="editprj1();"/>
					<%
						}
					%>
						<span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
				</tr>
			</table>
			<%
				for(ProjectGroupBean pgb:pgblist){
					String groupId=pgb.getId();
					String groupName=pgb.getGroupname();
					ProjectFieldDao pfd = new ProjectFieldDao();
					List<ProjectFieldBean>  pfblist = pfd.getUsedPrjField(prjtype,groupId);
					if(pfblist.size()<=0){
						continue;
					}
			%>
					<wea:layout type="twoCol">
						<wea:group context="<%=groupName%>">
					
			<%
					for(ProjectFieldBean pfb:pfblist){
						String fieldValue = "";
						if("0".equals(pfb.getIscommon())){
							fieldValue=Util.null2String(pidComMap.get(pfb.getFieldname()));
						}else{
							fieldValue=Util.null2String(pidDefMap.get(pfb.getFieldname()));	
						}
			%>
					
					<wea:item><%=pfb.getShowname()%></wea:item>
					<wea:item><%=vtm.doTrans(pfb.getId(),fieldValue, "0")%>	    	
					</wea:item>		
			<%
					}
			%>
					</wea:group>
					</wea:layout>	
			<%
				}



			%>
		 
		</FORM>
		
	<script type="text/javascript">
	function onshowPlanDate1(inputname,spanname,isMustInput){
		var returnvalue;
			var oncleaingFun = function(){
				if(isMustInput== "2"){
				$ele4p(spanname).innerHTML = "<IMG src='/images/BacoError_wev8.gif' align=absMiddle>"; 
				}else{
				$ele4p(spanname).innerHTML = ""; 	
				}
				$ele4p(inputname).value = '';
				}
			WdatePicker({lang:languageStr,el:spanname,onpicked:function(dp){
				returnvalue = dp.cal.getDateStr();	
				$dp.$(spanname).innerHTML = returnvalue;
				$dp.$(inputname).value = returnvalue;},oncleared:oncleaingFun});

		var hidename = $ele4p(inputname).value;
			if(hidename != ""){
				$ele4p(inputname).value = hidename; 
				$ele4p(spanname).innerHTML = hidename;
			}else{
				if("2".equals(isMustInput)){
				$ele4p(spanname).innerHTML = "<IMG src='/images/BacoError_wev8.gif' align=absMiddle>"; 
				}else{
				$ele4p(spanname).innerHTML = ""; 	
				}
			}
    }
		 function onBtnSearchClick() {
			report.submit();
		}

		function refersh() {
  			window.location.reload();
  		}
		
		function editprj(){
			window.location.href="/hsproject/project/view/hs-edit-project.jsp?id=<%=prjid%>&prjtype=<%=prjtype%>";
		}
		function editprj1(){
			window.location.href="/hsproject/project/view/hs-edit-project-djr.jsp?id=<%=prjid%>&prjtype=<%=prjtype%>";
		}
	  
   </script>
	<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
    <SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
    <SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>

	
</BODY>
</HTML>