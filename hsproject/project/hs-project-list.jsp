<%@ page language="java contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="weaver.conn.RecordSet"%>
<%@ page import="weaver.general.BaseBean"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*,weaver.hrm.common.*" %>
<%@ include file="/systeminfo/init_wev8.jsp"%>
<%@ taglib uri="/WEB-INF/weaver.tld prefix="wea"%>
<%@ taglib uri="/browserTag prefix="brow"%>
<jsp:useBean id="rs class="weaver.conn.RecordSet scope="page />
<jsp:useBean id="ResourceComInfo class="weaver.hrm.resource.ResourceComInfo scope="page />
<jsp:useBean id="DepartmentComInfo class="weaver.hrm.company.DepartmentComInfo scope="page"/>

<%
    int userid = user.getUID();
    String info = Util.null2String(request.getParameter("info")); 
    
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
		
		<!--<script language="JavaScript">
			<%if(info!=null && !"".equals(info)){

				if("0".equals(info)){%>
					top.Dialog.alert("删除成功！")
				<%}

				else if("1".equals(info)){%>
					top.Dialog.alert("删除失败！")
				<%}
			}%>
	</script>
-->
	</head>
	<%
	String imagefilename = "/images/hdMaintenance_wev8.gif";
	String titlename = SystemEnv.getHtmlLabelName(21039,user.getLanguage())
	+ SystemEnv.getHtmlLabelName(480, user.getLanguage())
	+ SystemEnv.getHtmlLabelName(18599, user.getLanguage())
	+ SystemEnv.getHtmlLabelName(352, user.getLanguage());
	String needfav = 1";
	String needhelp = "";
	
	String guard_pageId = receive_info";
	%>
	<BODY>
		<%@ include file="/systeminfo/TopTitle_wev8.jsp"%>
		<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp"%>
		<%
		RCMenu += "{新建,javascript:doEdit(this),_TOP} ";
		RCMenuHeight += RCMenuHeightStep;
		%>
		<%
		RCMenu += "{删除,javascript:doDelete(this),_TOP} ";
		RCMenuHeight += RCMenuHeightStep;
		%>
		<%@ include file="/systeminfo/RightClickMenu_wev8.jsp"%>
		<FORM id=weaver name=weaver method=post action="" >
			<table id="topTitle cellpadding="0 cellspacing="0">
				<tr>
					<td class="rightSearchSpan style="text-align:right;">
						<input type=button class="e8_btn_top onclick="doEdit(this);" value="新建">
						<span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
					</td>
					
				</tr>
			</table>
			
		</FORM>
		<%
		
		String backfields = id,name, procode,prjtype,manager,members,begindate,enddate,status";
		String fromSql  =  from hs_projectinfo";
		String sqlWhere =  1=1 ";

		String orderby =  id  ;
		String tableString = "";
		String operateString= "";
		tableString =" <table tabletype=\"checkbox\" pagesize=\""+ PageIdConst.getPageSize(guard_pageId,user.getUID(),PageIdConst.HRM)+"\" pageId=\""+guard_pageId+"\">"+
				   <sql backfields=\""+backfields+"\" sqlform=\""+fromSql+"\" sqlwhere=\""+Util.toHtmlForSplitPage(sqlWhere)+"\"  sqlorderby=\""+orderby+"\"  sqlprimarykey=\"id\" sqlsortway=\"asc\" sqlisdistinct=\"false\"/>"+
			operateString +
						<head>";
				tableString+="<col width=\"12%\" text=\"项目名称\" column=\"name\" orderkey=\"name\" linkvaluecolumn=\"id\" linkkey=\"id\" href= \"/hsproject/hsproject.jsp\" target=\"_fullwindow\"/>"+
						<col width=\"12%\" text=\"项目编号\" column=\"procode\" orderkey=\"procode\" />"+
						<col width=\"12%\" text=\"项目类型\" column=\"prjtype\" orderkey=\"prjtype\" />"+
                		<col width=\"12%\" text=\"项目经理\" column=\"manager\" orderkey=\"manager\" />"+
                		<col width=\"12%\" text=\"项目成员\" column=\"members\" orderkey=\"members\" />"+
                		<col width=\"12%\" text=\"项目开始时间\" column=\"begindate\" orderkey=\"begindate\" />"+
                		<col width=\"12%\" text=\"项目结束时间\" column=\"enddate\" orderkey=\"enddate\" />"+
                		<col width=\"12%\" text=\"项目状态\" column=\"status\" orderkey=\"status\" />"+
						</head>"+
		 </table>";
		%>
		<wea:SplitPageTag isShowTopInfo="false tableString="<%=tableString%>" mode="run"/>
		<script type="text/javascript">
		
			function doEdit(){
				document.weaver.submit();
			} 
			function doCheck() {  
				if(ids=='') {  
					return;  
				}  
				  
				var deleteIds = "";  
				var idList = ids.split(",");  
				var len = idList.length;  
				var checkall = $("checkall");  
				for(var i=0; i<len; i++) {  
					if(!$("checkbox_ + idList[i]).checked) {  
						checkall.checked = false;  
						return;  
					}  
				}  
				checkall.checked = true;  
			}  
			function checkAll() {  
				if(ids=='') {  
					return;  
				}  
			  
				var idList = ids.split(",");  
				var len = idList.length;  
				var checkall = $("checkall");  
				for(var i=0; i<len; i++) {  
					$("checkbox_ + idList[i]).checked = checkall.checked;  
				}  
			}  
			function $(id) {  
				return document.getElementById(id);  
			}  

			function doDelete(){
				var ids = _xtable_CheckedCheckboxId();
				if(ids == ""){
					window.top.Dialog.alert("<%=SystemEnv.getHtmlLabelName(19689,user.getLanguage())%>");
					return false;
				}

				Dialog.confirm("确定删除?", function (){
					weaver.action="ksdata_ListDel.jsp?id="+ids;
					weaver.submit();
				}, function () {}, 320, 90,false);
			}	

		</script>
		<SCRIPT language="javascript src="/js/datetime_wev8.js"></script>
		<SCRIPT language="javascript defer="defer src="/js/JSDateTime/WdatePicker_wev8.js"></script>
		<script type="text/javascript src="/js/selectDateTime_wev8.js"></script>
	</BODY>
</HTML>