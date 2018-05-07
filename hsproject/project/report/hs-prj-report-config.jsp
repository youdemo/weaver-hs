<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="hsproject.util.BrowserInfoUtil"%>
<%@ page import="hsproject.dao.ProjectFieldDao" %>
<%@ page import="hsproject.bean.ProjectFieldBean" %>
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
    String out_pageId = "xmxxbbpz";
    String prjtype_mt = Util.null2String(request.getParameter("prjtype_mt"));

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
		<FORM id=report  name=report action="/hsproject/project/aciton/submit-prj-report-config.jsp" method=post>
			<input type="hidden" name="prjtype_mt" value="<%=prjtype_mt%>">
			<input type="hidden" name="type_mt" value="add">
			<input type="hidden" name="reportid_mt" value="">
			<table id="topTitle" cellpadding="0" cellspacing="0">
				<tr>
					<td></td>
					<td class="rightSearchSpan" style="text-align:right;">
					 <input type="button" id="submitbutton"  value="提交" class="e8_btn_top_first" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px;" onclick="submitform()"/>
					</td>
				</tr>
			</table>
				<wea:layout type="twoCol">
				<wea:group context="报表设置">
				<wea:item>报表名称</wea:item>
                <wea:item>
                 <input name="reportname_mt" id="reportname_mt" class="InputStyle" type="text" value="" onchange="checkinput('reportname_mt','reportname_mtspan')" />
                 <span id="reportname_mtspan"><img src="/images/BacoError_wev8.gif" align="absMiddle"></span>
                </wea:item>

				</wea:group>
				</wea:layout>
				<wea:layout >
					<wea:group context="字段设置">
						<wea:item attributes="{'isTableList':'true'}">
						    <wea:layout type="table" attributes="{'cols':'5','cws':'20%,20%,20%,20%,20%'}">
								<wea:group context="报表设置" attributes="{\"groupDisplay\":\"none\"}">
									<wea:item type="thead">显示</wea:item>
								    <wea:item type="thead">查询条件</wea:item>
								    <wea:item type="thead">字段</wea:item>
									<wea:item type="thead">是否通用</wea:item>
									<wea:item type="thead">显示顺序</wea:item>
									<%
									ProjectFieldDao pfd = new ProjectFieldDao();
									List<ProjectFieldBean>  pfblist = pfd.getUsedPrjField(prjtype_mt,"");
									for(ProjectFieldBean pfb:pfblist){
										String fieldname =pfb.getFieldname();
										String showname = pfb.getShowname();
										String iscommon = pfb.getIscommon();
										if("0".equals(iscommon)){
											iscommon = "是";
										}else{
											fieldname = "def_"+fieldname;
											iscommon = "否";
										}
									%>
										<wea:item >
											<input type="checkbox" notbeauty="true" class=InputStyle id="<%=fieldname%>_checkbox1" name="<%=fieldname%>_checkbox1" value="0" onclick="checkchange('<%=fieldname%>_checkbox1')" />
										</wea:item>
									    <wea:item >
									    	<input type="checkbox" notbeauty="true" class=InputStyle id="<%=fieldname%>_checkbox2" name="<%=fieldname%>_checkbox2" value="0" onclick="checkchange('<%=fieldname%>_checkbox2')" />
									    </wea:item>
									    <wea:item >
									    <%=showname%><input type="hidden" name="<%=fieldname%>" value="<%=pfb.getId()%>">
										</wea:item>
										<wea:item ><%=iscommon%></wea:item>
										<wea:item >
										<input datalength="2" datatype="float" style="ime-mode:disabled" onafterpaste="if(isNaN(value))execCommand('undo')"  type="text" class="InputStyle" id="<%=fieldname%>_dsporder" name="<%=fieldname%>_dsporder" onkeypress="ItemDecimal_KeyPress('<%=fieldname%>_dsporder',15,2)" onblur="checkFloat(this);"  value="0.00" >
										</wea:item>
									<%
									}
									%>
								</wea:group>
							</wea:layout>
						</wea:item>
					</wea:group>
				</wea:layout>
		</FORM>
		
	<script type="text/javascript">

		function submitform(){
			jQuery("#submitbutton").removeAttr("onclick");
			if(!check_form(report,'reportname_mt')){
				jQuery("#submitbutton").addAttr("onclick","submitform()");
				return false;

			} 
			report.submit();
		}

		function checkchange(fieldid){
	
			var field_val=jQuery("#"+fieldid).val();
			if(field_val==0){
				jQuery("#"+fieldid).val("1");
			}else{
				jQuery("#"+fieldid).val("0");
			}
	
		}
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