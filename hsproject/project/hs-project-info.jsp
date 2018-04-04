<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.conn.RecordSet"%>
<%@ page import="weaver.general.BaseBean"%>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page"/>
<HTML><HEAD>
<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
<LINK href="/wui/theme/ecology8/jquery/js/e8_zDialog_btn_wev8.css" type=text/css rel=STYLESHEET>
<script language="javascript" src="../../js/weaver_wev8.js"></script>
</HEAD>
<%
String imagefilename = "/images/hdMaintenance_wev8.gif";
String titlename = SystemEnv.getHtmlLabelName(16579,user.getLanguage());
String needfav ="1";
String needhelp ="";
	
String prjid = Util.null2String(request.getParameter("id"));
String prjtype = Util.null2String(request.getParameter("prjtype"));
ProjectInfoDao pid = new ProjectInfoDao();


%>
<body scroll="no">
<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>
<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>

<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>

<FORM id=weaver action="" method=post style="width:100%;">
  <input type="hidden" name="method" value="add">
	<table id="topTitle" cellpadding="0" cellspacing="0">
		<tr>
			<td></td>
			<td class="rightSearchSpan" style="text-align:right; width:500px!important">				
				<span title="菜单" class="cornerMenu"></span>
			</td>
		</tr>
	</table>  
		<wea:layout type="twoCol">


		    <wea:group context="其他信息">
		    	<wea:item>项目目前阶段</wea:item>
			    <wea:item>
			    		<%=process%>		    	
			    </wea:item>
		    	<wea:item>收款金额</wea:item>
		    	<wea:item>
			    		<%=money%>
		    	</wea:item>
				<wea:item>项目组长</wea:item>
		    	<wea:item>
			    		<%=ResourceComInfo.getResourcename(xmzz)%>
		    	</wea:item>
				<wea:item>销售员</wea:item>
		    	<wea:item>
			    		<%=ResourceComInfo.getResourcename(xsy)%>
		    	</wea:item>
				<wea:item>项目工程师</wea:item>
		    	<wea:item>
			    		<%=ResourceComInfo.getResourcename(proer)%>
		    	</wea:item>
				<wea:item>产品类型</wea:item>
		    	<wea:item>
			    		<%=cplx%>
		    	</wea:item>
				<wea:item>市场已试样次数</wea:item>
		    	<wea:item>
			    		<%=scysycs%>
		    	</wea:item>
				<wea:item>市场已试样片数</wea:item>
		    	<wea:item>
			    		<%=scysyps%>
		    	</wea:item>
				
		    </wea:group>
		</wea:layout>
		<wea:layout type="twoCol">
		    <wea:group context="客户信息">
		    	<wea:item>客户名称</wea:item>
			    <wea:item>
			    		<%=cus_name%>		    	
			    </wea:item>
		    	<wea:item>客户代码</wea:item>
		    	<wea:item>
			    		<%=khdm%>
		    	</wea:item>
				<wea:item>客户联系人</wea:item>
		    	<wea:item>
			    		<%=khlxr%>
		    	</wea:item>
				<wea:item>客户联系人电话</wea:item>
		    	<wea:item>
			    		<%=khlxrdh%>
		    	</wea:item>
				<wea:item>客户联系人邮箱</wea:item>
		    	<wea:item>
			    		<%=khlxryx%>
		    	</wea:item>
				
		    </wea:group>
		</wea:layout>			
<iframe id="checkType" src="" style="display: none"></iframe>
</FORM>

<jsp:include page="/systeminfo/commonTabFoot.jsp"></jsp:include>  
</div>
<div id="zDialog_div_bottom" class="zDialog_div_bottom">
	<wea:layout needImportDefaultJsAndCss="false">
		<wea:group context=""  attributes="{\"groupDisplay\":\"none\"}">
			<wea:item type="toolbar">
		    	<input type="button" value="<%=SystemEnv.getHtmlLabelName(309,user.getLanguage())%>" id="zd_btn_cancle"  class="zd_btn_cancle" onclick="dialog.closeByHand()">
			</wea:item>
		</wea:group>
	</wea:layout>
</div>

<script language="javascript">

	var dialog = parent.getDialog(window);
	var parentWin = parent.getParentWindow(window);
	function btn_cancle(){
		parentWin.closeDialog();
	}


	var dialog = parent.getDialog(window);
	var parentWin = parent.getParentWindow(window);
	parentWin.location="/workflow/workflow/ListWorkTypeTab.jsp";
	parentWin.closeDialog();	

function submitData()
{
	if (check_form(weaver,'type,desc')){
	    //通过iframe验证类型名称是否重复
		document.getElementById("checkType").src="/workflow/workflow/WorkTypeOperation.jsp?method=valRepeat&type="+myescapecode(document.all("type").value);
    }
}
function onReturn(){
	location="/workflow/workflow/ListWorkTypeTab.jsp";
}

//类型名称已经存在
function typeExist(){
    alert("<%=SystemEnv.getHtmlLabelName(24256,user.getLanguage())%><%=SystemEnv.getHtmlLabelName(24943,user.getLanguage())%>");
    return ;
}

//提交表单
function submitForm(){
    weaver.submit();
}
</script>
</BODY>
</HTML>
