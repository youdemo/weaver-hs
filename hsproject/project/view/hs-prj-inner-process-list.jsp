<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
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
		 <SCRIPT language="javascript" src="/proj/js/common_wev8.js"></SCRIPT>
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
    String out_pageId = "hsxmgc-inner";
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
		<FORM id=report name=report action="/hsproject/project/view/hs-prj-inner-process-list.jsp" method=post>
			<input type="hidden" name="requestid" value="">
			<input type="hidden" name="prjid" value="<%=prjid%>">
			<input type="hidden" name="prjtype" value="<%=prjtype%>">
			<table id="topTitle" cellpadding="0" cellspacing="0">
				<tr>
					<td></td>
					<td class="rightSearchSpan" style="text-align:right;">
					<!--<input type="button" value="添加阶段" class="e8_btn_top_first" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px;" onclick="addprocess(<%=prjid%>);"/>-->
					<!--<span id="advancedSearch" class="advancedSearch"><%=SystemEnv.getHtmlLabelName(21995,user.getLanguage())%></span>-->
						<span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
						</td>
				</tr>
			</table>
			
			<% // 查询条件 %>
			<div class="advancedSearchDiv" id="advancedSearchDiv" style="display:none;">
				<wea:layout type="4col">
				<wea:group context="查询条件">
				
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
		String backfields = "id,prjid,processname,prjtype,fzrid,processtype,begindate,enddate ";
		String fromSql  =  " from hs_prj_process";
		String sqlWhere =  " where 1=1 and prjid='"+prjid+"' ";

		String orderby =  " id "  ;
		String tableString = "";
		String operateString= "";
		if("1".equals(canedit)){
           operateString = "<operates width=\"20%\">"+
		                    " <popedom transmethod=\"weaver.hrm.common.SplitPageTagOperate.getBasicOperate\" otherpara=\""+String.valueOf(user.isAdmin())+":true:true:true:true\"></popedom> "+
		                     "     <operate isalwaysshow=\"true\" href=\"javascript:editprocess();\" linkkey=\"id\" linkvaluecolumn=\"id\" text=\"编辑\" index=\"1\"/> "+
							//"     <operate isalwaysshow=\"true\" href=\"javascript:deleteprocess();\" linkkey=\"id\" linkvaluecolumn=\"id\" text=\"删除\" index=\"2\"/> "+
							
		                    "</operates>";   
		  }

		  tableString =" <table tabletype=\"none\" pagesize=\""+ PageIdConst.getPageSize(out_pageId,user.getUID(),PageIdConst.HRM)+"\" pageId=\""+out_pageId+"\" >"+         
				   "	   <sql backfields=\""+backfields+"\" sqlform=\""+fromSql+"\" sqlwhere=\""+Util.toHtmlForSplitPage(sqlWhere)+"\"  sqlorderby=\""+orderby+"\"  sqlprimarykey=\"id\" sqlsortway=\"asc\" sqlisdistinct=\"false\" />"+
		operateString+
		"			<head>";
				tableString +="<col width=\"12%\" text=\"阶段名称\" column=\"processname\" orderkey=\"processname\" linkvaluecolumn=\"id\" linkkey=\"id\" href= \"/hsproject/project/view/hs-process-info-url.jsp\" target=\"_fullwindow\"/>"+ 
						"<col width=\"12%\" text=\"阶段类型\" column=\"processtype\" orderkey=\"processtype\" transmethod=\"hsproject.dao.ProcessTypeDao.getTypeName\"/>"+
						"<col width=\"12%\" text=\"负责人\" column=\"fzrid\" orderkey=\"fzrid\" transmethod='weaver.proj.util.ProjectTransUtil.getResourceNamesWithLink'/>"+
						"<col width=\"12%\" text=\"开始时间\" column=\"begindate\" orderkey=\"begindate\" />"+
						"<col width=\"12%\" text=\"结束时间\" column=\"enddate\" orderkey=\"enddate\"/>"+
                		
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
		
		function editprocess(processid){
			openFullWindowForXtable("/hsproject/project/view/hs-edit-process-url.jsp?id="+processid);
		}
		function deleteprocess(id){
			if(confirm("是否确定要删除")){
			}else{
				return false;
			}
			var xhr = null;
			if (window.ActiveXObject) {//IE浏览器
				xhr = new ActiveXObject("Microsoft.XMLHTTP");
			} else if (window.XMLHttpRequest) {
			xhr = new XMLHttpRequest();
			}
			if (null != xhr) {
			xhr.open("GET","/hsproject/project/aciton/delete-process-info.jsp?processid="+id, false);
			xhr.onreadystatechange = function () {
			if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var text = xhr.responseText;  
				text = text.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
						//alert(text);	
				}
			}
			}
			xhr.send(null);
			}
			window.location.reload();

		}
		function addprocess(){
		    var title = "新建阶段";
      	    var url = "/hsproject/project/list/hs-add-process-list.jsp?prjtype=<%=prjtype%>&id=<%=prjid%>";                    
			if(window.top.Dialog){
				diag_vote = new window.top.Dialog();
			} else {
				diag_vote = new Dialog();
			};
			diag_vote.currentWindow = window;
        
			diag_vote.maxiumnable = true;
			diag_vote.Width = 1000;
			diag_vote.Height = 700;
			diag_vote.Model = true;
			diag_vote.Title = title;
			diag_vote.URL = url;
			diag_vote.CancelEvent=function(){diag_vote.close();window.location.reload();};
			diag_vote.show(""); 
		}
		
	  
   </script>
		<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
	
</BODY>
</HTML>