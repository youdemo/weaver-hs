<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ page import="hsproject.util.*" %>
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
   
	String prjname = Util.null2String(request.getParameter("prjname"));
	String procode = Util.null2String(request.getParameter("procode"));
	String prjtype = Util.null2String(request.getParameter("prjtype"));
	String status = Util.null2String(request.getParameter("status"));
	String beginDate = Util.null2String(request.getParameter("beginDate"));
	String endDate = Util.null2String(request.getParameter("endDate"));
	String beginDate1 = Util.null2String(request.getParameter("beginDate1"));
	String endDate1 = Util.null2String(request.getParameter("endDate1"));
	String department =Util.null2String(request.getParameter("department"));

	String reportid = Util.null2String(request.getParameter("reportid"));
	//out.print(reportid);
	ProjectFieldDao pfd = new ProjectFieldDao();
	PrjReportDao prd = new PrjReportDao();
	PrjReportBean prb = prd.getPrjReportBean(reportid);
	List<PrjReportDtBean> dtList = prb.getDtList();
	EditTransMethod etm = new EditTransMethod();
	BrowserInfoUtil biu = new BrowserInfoUtil();
	 String out_pageId = "cprjreport1_"+reportid;

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
		<FORM id=report name=report action="/hsproject/project/report/hs-show-prj-report-list.jsp" method=post>
			<input type="hidden" name="reportid" value="<%=reportid%>">
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
					<%
						for(PrjReportDtBean prdb:dtList){
							String fieldid=prdb.getFieldid();
							String fieldname =prdb.getFieldname();
							String iscommon =prdb.getIscommon();
							String isquery = prdb.getIsquery();
							String ishow = prdb.getIshow();
							String showname = prdb.getShowname();
							if(!"0".equals(isquery)){
								continue;
							}

							if("0".equals(iscommon)){
							
							}else{
								fieldname = "idef_"+fieldname;
							}
							ProjectFieldBean pfb=pfd.getPrjFieldBean(fieldid);
							pfb.setIsreadonly("0");
							pfb.setIsmust("0");
							String buttontype=pfb.getButtontype();
							if("4".equals(pfb.getFieldtype())){
								continue;
							}
							if("1".equals(pfb.getFieldtype())){
								if("2".equals(buttontype)){
								%>
									<wea:item><%=showname%></wea:item>
									 <wea:item>
					                    <button type="button" class=Calendar id="selectBegin<%=fieldname%>" onclick="onshowPlanDate1('begin_<%=fieldname%>','begin_<%=fieldname%>Span','1')"></BUTTON>
					                        <SPAN id="begin_<%=fieldname%>Span" ><%=Util.null2String(request.getParameter("begin_"+fieldname))%></SPAN>
					                        <INPUT type="hidden" name="begin_<%=fieldname%>" id="begin_<%=fieldname%>" value="<%=Util.null2String(request.getParameter("begin_"+fieldname))%>">
					                    &nbsp;-&nbsp;
					                    <button type="button" class=Calendar id="selectEnd<%=fieldname%>" onclick="onshowPlanDate1('end_<%=fieldname%>','end_<%=fieldname%>Span','1')"></BUTTON>
					                        <SPAN id="end_<%=fieldname%>Span"><%=Util.null2String(request.getParameter("end_"+fieldname))%></SPAN>
					                        <INPUT type="hidden" name="end_<%=fieldname%>" id="end_<%=fieldname%>" value="<%=Util.null2String(request.getParameter("end_"+fieldname))%>">
					                </wea:item>

								<%
							    }else if("3".equals(buttontype)){
 									continue;
							    }else{
							    %>
							    	<wea:item><%=showname%></wea:item>
									<wea:item>
									<brow:browser name='<%=fieldname%>'
									viewType='0'
									browserValue='<%=Util.null2String(request.getParameter(fieldname))%>'
									isMustInput='1'
									browserSpanValue="<%=biu.getBrowserShowValue(Util.null2String(request.getParameter(fieldname)),buttontype)%>"
									hasInput='true'
									linkUrl='<%=biu.getLinkUrl(buttontype)%>' 
									completeUrl='<%=biu.getCompleteUrl(buttontype)%>'
									width='80%'
									isSingle='<%=biu.getIsSingle(buttontype)%>'
									hasAdd='false'
									browserUrl='<%=biu.getBrowserUrl(buttontype)%>'>
									</brow:browser>
									<input type=hidden name="<%=fieldname%>_name" value="">
									</wea:item>
							    <%

								}
							}else{
					%>
						<wea:item><%=showname%></wea:item>
						<wea:item><%=etm.doProjectEditTrans(pfb,Util.null2String(request.getParameter(fieldname)))%>	    	
						</wea:item>		
					<%			

							}

						}
					%>

				

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
		String backfields = "t1.id ";
		for(PrjReportDtBean prdb:dtList){
			String fieldid=prdb.getFieldid();
			String fieldname =prdb.getFieldname();
			String iscommon =prdb.getIscommon();
			String isquery = prdb.getIsquery();
			String ishow = prdb.getIshow();
			if("0".equals(iscommon)){
				backfields = backfields+",t1."+fieldname+",'"+fieldid+"' as id_"+fieldname;
			}else{
				backfields = backfields+",t2."+fieldname+" as def_"+fieldname+",'"+fieldid+"' as id_def_"+fieldname;
			}
		}
		
		String fromSql  =  " from hs_projectinfo t1,hs_project_fielddata t2";
		String sqlWhere =  " t1.id=t2.prjid ";
		for(PrjReportDtBean prdb:dtList){
			String fieldid=prdb.getFieldid();
			String fieldname =prdb.getFieldname();
			String fieldnamepara=fieldname;
			String iscommon =prdb.getIscommon();
			String isquery = prdb.getIsquery();
			String ishow = prdb.getIshow();
			String showname = prdb.getShowname();
			String fieldvalue= "";
			if(!"0".equals(isquery)){
					continue;
			}

			if("0".equals(iscommon)){
				fieldname = "t1."+fieldname;			
			}else{
				fieldname = "t2."+fieldname;
				fieldnamepara = "idef_"+fieldnamepara;
			}
			ProjectFieldBean pfb=pfd.getPrjFieldBean(fieldid);
			pfb.setIsreadonly("0");
			pfb.setIsmust("0");
			String buttontype=pfb.getButtontype();
			String fieldtype = pfb.getFieldtype();
			if("4".equals(fieldtype)){
					continue;
			}
			if("0".equals(fieldtype)){
				fieldvalue = Util.null2String(request.getParameter(fieldnamepara));
				if(!"".equals(fieldvalue)){
					sqlWhere +=" and upper("+fieldname+") like upper('%"+fieldvalue+"%')"; 
				}

			}else if("1".equals(fieldtype)){
				if("2".equals(buttontype)){
					String beginvalue = Util.null2String(request.getParameter("begin_"+fieldnamepara));
					String endvalue = Util.null2String(request.getParameter("end_"+fieldnamepara));
					if(!"".equals(beginvalue)){
						sqlWhere +=" and "+fieldname+" >= '"+beginvalue+"'"; 
					}
					if(!"".equals(endvalue)){
						sqlWhere +=" and "+fieldname+" <= '"+endvalue+"'"; 
					}
				}else if("3".equals(buttontype)){

				}else{
					fieldvalue = Util.null2String(request.getParameter(fieldnamepara));
					if(!"".equals(fieldvalue)){
						sqlWhere +=" and "+fieldname+" = '"+fieldvalue+"'"; 
					}

				}
			}else if("3".equals(fieldtype)){
				fieldvalue = Util.null2String(request.getParameter(fieldnamepara));
				if(!"".equals(fieldvalue)){
					sqlWhere +=" and "+fieldname+" = '"+fieldvalue+"'"; 
				}
			}
		
		}
		//out.print("select "+backfields+fromSql+" where "+sqlWhere);
		String orderby =  " t1.id desc "  ;
		String tableString = "";
		String operateString= "";
		  tableString =" <table tabletype=\"none\" pagesize=\""+ PageIdConst.getPageSize(out_pageId,user.getUID(),PageIdConst.HRM)+"\" pageId=\""+out_pageId+"\" >"+         
				   "	   <sql backfields=\""+backfields+"\" sqlform=\""+fromSql+"\" sqlwhere=\""+Util.toHtmlForSplitPage(sqlWhere)+"\"  sqlorderby=\""+orderby+"\"  sqlprimarykey=\"t1.id\" sqlsortway=\"desc\" sqlisdistinct=\"false\" />"+
		operateString+
		"			<head>";
					for(PrjReportDtBean prdb:dtList){
						String fieldid=prdb.getFieldid();
						String fieldname =prdb.getFieldname();
						String iscommon =prdb.getIscommon();
						String isquery = prdb.getIsquery();
						String ishow = prdb.getIshow();
						String showname = prdb.getShowname();
						if(!"0".equals(ishow)){
							continue;
						}
						if("0".equals(iscommon)){
							
						}else{
							fieldname = "def_"+fieldname;
						}
						//ProjectFieldBean pfb=pfd.getPrjFieldBean(fieldid);

						if("name".equals(fieldname)){
							tableString +="<col width=\"10%\" text=\""+showname+"\" column=\""+fieldname+"\" orderkey=\""+fieldname+"\"  otherpara=\"column:id_"+fieldname+"\"  transmethod=\"hsproject.impl.PrjReportImpl.prjReportTransMethod\" linkvaluecolumn=\"id\" linkkey=\"id\" href= \"/hsproject/project/view/hs-project-info-url.jsp\" target=\"_fullwindow\"/>";
						}else{

							tableString +="<col width=\"10%\" text=\""+showname+"\" column=\""+fieldname+"\" orderkey=\""+fieldname+"\"  otherpara=\"column:id_"+fieldname+"\"  transmethod=\"hsproject.impl.PrjReportImpl.prjReportTransMethod\"/>";
						}

					}


					tableString +=	"</head>"+
		 "</table>";
	//showExpExcel="false"
	%>

	<h3 align="left" >报表地址:/hsproject/project/report/hs-show-prj-report-Url.jsp?reportid=<%=reportid%> </h3><br/>
	<wea:SplitPageTag isShowTopInfo="false" tableString="<%=tableString%>" mode="run"  />
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
	
	  
   </script>
		<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>

		<script type="text/javascript" src="/formmode/js/modebrow_wev8.js"></script>
<script language=javascript src="/js/ecology8/request/e8.browser_wev8.js"></script>
	
</BODY>
</HTML>