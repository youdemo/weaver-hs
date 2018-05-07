<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.*"%>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ page import="hsproject.util.*" %>
<%@ page import="hsproject.impl.*" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<%@ taglib uri="/browserTag" prefix="brow"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="rs_dt" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page" />

<%
Integer lg=(Integer)user.getLanguage();
int user_id = user.getUID();
String tmc_pageId = "hspareport";
weaver.general.AccountType.langId.set(lg);

String reportid = Util.null2String(request.getParameter("reportid"));
String iscreate_mt = Util.null2String(request.getParameter("iscreate"));
String sql="";
String sql_dt = "";
int fieldcount=0;
sql="select COUNT(1) as count from uf_prj_all_reportmt_dt1 where mainid="+reportid+" and ishow=0";
rs.executeSql(sql);
if(rs.next()){
fieldcount = rs.getInt("count");
}

int with1=fieldcount*120;
int with2=40+fieldcount*120;
StringBuffer sb= new StringBuffer();
PrjReportDao prd= new PrjReportDao();
ProcessTypeDao ptd = new ProcessTypeDao();
PrjAllReportBean parb =  prd.getPrjAllReportBean(reportid);
Map<String, List<PrjAllReportDtBean>> dtMap = parb.getDtMap();
List<PrjAllReportDtBean> prjlist = dtMap.get("prj");
PrjReportImpl pri = new PrjReportImpl();
ProjectFieldDao pfd = new ProjectFieldDao();
BrowserInfoUtil biu = new BrowserInfoUtil();
EditTransMethod etm = new EditTransMethod();
ProjectInfoDao pid = new ProjectInfoDao();

//out.print(sqlquery);
%>
<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="AccountType" class="weaver.general.AccountType" scope="page" />
<jsp:useBean id="LicenseCheckLogin" class="weaver.login.LicenseCheckLogin" scope="page" />
<HTML>
	<HEAD>
		<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
		<script type="text/javascript" src="/appres/hrm/js/mfcommon_wev8.js"></script>
		<script language=javascript src="/js/ecology8/hrm/HrmSearchInit_wev8.js"></script>
		<link rel="stylesheet" href="/css/init_wev8.css" type="text/css" />
		<style>
		.checkbox {
			display: none
		}
		TABLE.ViewForm1 {
			WIDTH: <%=with1%>px;
			border:0;margin:0;
			border-collapse:collapse;
		
	   }
		TABLE.ViewForm1 TD {
			padding:0 0 0 5px;
		}
		TABLE.ViewForm1 TR {
			height: 35px;
		}
		.table-head{padding-right:17px}
         .table-body{width:100%;overflow-y:auto;overflow-x: hidden}
		</style>
		
	</head>
	<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
    <link href="/js/swfupload/default_wev8.css" rel="stylesheet" type="text/css" />
  <link type="text/css" rel="stylesheet" href="/css/crmcss/lanlv_wev8.css" />
  <link type="text/css" rel="stylesheet" href="/wui/theme/ecology8/skins/default/wui_wev8.css" />
	<BODY >
		<div id="tabDiv">
			<span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>"><%=SystemEnv.getHtmlLabelName(20536,user.getLanguage()) %></span>
		</div>
		<div id="dialog">
			<div id='colShow'></div>
		</div>
		<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
		<%
			RCMenu += "{刷新,javascript:refersh(),_self} " ;
		//RCMenu += "{导出,javascript:_xtable_getAllExcel(),_self} " ;
		RCMenuHeight += RCMenuHeightStep ;
		%>
		<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
		<FORM id=report name=report action="/hsproject/project/report/hs-show-prj-all-report-list.jsp" method=post>
			<input type="hidden" name="reportid" value="<%=reportid%>">
			<table id="topTitle" cellpadding="0" cellspacing="0">
				<tr>
					<td></td>
					<td class="rightSearchSpan" style="text-align:right;">
					<span id="advancedSearch" class="advancedSearch"><%=SystemEnv.getHtmlLabelName(21995,user.getLanguage())%></span>
						<span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
					</td>
				</tr>
			</table>
			
			<% // 查询条件 %>
			<div class="advancedSearchDiv" id="advancedSearchDiv" style="display:none;">
				<wea:layout type="4col">
				<wea:group context="查询条件">
				<%
					for(PrjAllReportDtBean prdb:prjlist){
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
     	if("1".equals(iscreate_mt)){
     %>
	<h3 align="left" >报表地址:/hsproject/project/report/hs-show-prj-all-report-Url.jsp?reportid=<%=reportid%> </h3><br/>
	<%
		}
	%>
	<div style="width:<%=with2%>px; ">	
	<div class="table-head" style="width:<%=with1%>px; ">
			<table class="ViewForm1  outertable" scrolling="auto">
    <tbody>		
        <tr>			
            <td>
            <table class="ViewForm1  maintable" scrolling="auto" id='table11'>
                <colgroup> 
				<%
				  for(int i=0;i<fieldcount;i++){
				  %>
				 <col width="120px">
				<%  	  
				  }
				%>
				 
				 </colgroup>
                <tbody>
					 <tr>
					 	<%
					 	for(PrjAllReportDtBean prdb:prjlist){
					 		String fieldname =prdb.getFieldname();
					 		String ishow = prdb.getIshow();
					 		String showname = prdb.getShowname();
					 		if(!"0".equals(ishow)){
								continue;
							}
						%>
							<td class="fname" rowspan="2" ><%=showname%></td>
						<%

					 	}
					 	List<ProcessTypeBean> prblist = ptd.getUsedProcessType(parb.getPrjtype());
						for(ProcessTypeBean ptb:prblist){
						    String processid=ptb.getId();
						    String processname=ptd.getTypeName(processid);
						    if(dtMap.get(processid) != null){
						    	int listsize = dtMap.get(processid).size();
						  %>
						  	<td class="fname" colspan="<%=listsize%>" align="center"><%=processname%></td>
						  <%  	
							}
						}

					 	%>	                  
                 </tr>
				 <tr>
					 <%
						for(ProcessTypeBean ptb:prblist){
						    String processid=ptb.getId();
						    String processname=ptd.getTypeName(processid);
						    if(dtMap.get(processid) != null){
						    	List<PrjAllReportDtBean> dtlist = dtMap.get(processid);
						    	for(PrjAllReportDtBean prdb:dtlist){	
						    		String showname = prdb.getShowname();


						  %>
						  	<td class="fname"  ><%=showname%></td>
						  <%  	
						 		 }
							}
						}
				%>
					 
				</tr>
				</tbody>
            </table>
            </td>
        </tr>
    </tbody>
</table>
	</div>
	<div class="table-body" id="div22" style="width:<%=with2%>px; ">
	<table class="ViewForm1  outertable" scrolling="auto">
    <tbody>		
        <tr>			
            <td>
            <table class="ViewForm1  maintable" scrolling="auto">
                <colgroup>  
				<%
				  for(int i=0;i<fieldcount;i++){
				  %>
				 <col width="120px">
				<%  	  
				  }
				%>
				 </colgroup>
                <tbody>
					 <%
					 	String backfields = "a.id";
					 	for(PrjAllReportDtBean prdb:prjlist){
					 		String fieldname =prdb.getFieldname();
							String ishow = prdb.getIshow();
							String showname = prdb.getShowname();
							String iscommon = prdb.getIscommon();
							String fieldid=prdb.getFieldid();
							if("0".equals(iscommon)){
								backfields = backfields+",a."+fieldname+"";
							}else{
								backfields = backfields+",b."+fieldname+" as def_"+fieldname+"";
							}

					 	}
					 	String sqlWhere = " a.id=b.prjid and a.prjtype='"+parb.getPrjtype()+"' ";

					 	for(PrjAllReportDtBean prdb:prjlist){
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
								fieldname = "a."+fieldname;			
							}else{
								fieldname = "b."+fieldname;
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
					 	sql="select "+backfields+" from hs_projectinfo a,hs_project_fielddata b where "+sqlWhere+" order by a.id desc";
					 	//out.print(sql);
					 	rs.executeSql(sql);
					 	while(rs.next()){
					 		String prjid = Util.null2String(rs.getString("id"));

					 	
					 %>
					 <tr>
					<%
							for(PrjAllReportDtBean prdb:prjlist){
							 		String fieldname =prdb.getFieldname();
							 		String ishow = prdb.getIshow();
							 		String showname = prdb.getShowname();
							 		String iscommon = prdb.getIscommon();
							 		String fieldid=prdb.getFieldid();
							 		if(!"0".equals(ishow)){
										continue;
									}

									if("0".equals(iscommon)){
										if("name".equals(fieldname)){
						%>
										<td class="fvalue"><%=pid.getPrjNameWithLink(prjid)%></td>
						<%
										}else{
						%>
											<td class="fvalue"><%=pri.prjReportTransMethod(Util.null2String(rs.getString(fieldname)),fieldid)%></td>
						<%				
										}
									}else{

									fieldname = "def_"+fieldname;
						%>
									<td class="fvalue"><%=pri.prjReportTransMethod(Util.null2String(rs.getString(fieldname)),fieldid)%></td>
						<%
										
									}

							    }
							    for(ProcessTypeBean ptb:prblist){
							    	String processid=ptb.getId();
								    if(dtMap.get(processid) != null){
								    	backfields = "a.id";							  
								    	List<PrjAllReportDtBean> dtlist = dtMap.get(processid);
								    	for(PrjAllReportDtBean prdb:dtlist){	
								    		String fieldname =prdb.getFieldname();
											String ishow = prdb.getIshow();
											String showname = prdb.getShowname();
											String iscommon = prdb.getIscommon();
											String fieldid=prdb.getFieldid();
											if("0".equals(iscommon)){
												backfields = backfields+",a."+fieldname+"";
											}else{
												backfields = backfields+",b."+fieldname+" as def_"+fieldname+"";
											}
								    	}
								    	sql_dt = "select "+backfields+" from hs_prj_process a,hs_prj_process_fielddata b where a.id=b.processid and a.prjid="+prjid+" and a.processtype="+processid;
								    	rs_dt.executeSql(sql_dt);
								    	//out.print(sql_dt);
								    	if(rs_dt.next()){
								    		for(PrjAllReportDtBean prdb:dtlist){
								    	    	String fieldname =prdb.getFieldname();
										 		String showname = prdb.getShowname();
										 		String iscommon = prdb.getIscommon();
										 		String fieldid=prdb.getFieldid();
										 		if("0".equals(iscommon)){
										%>
													<td class="fvalue"><%=pri.processReportTransMethod(Util.null2String(rs_dt.getString(fieldname)),fieldid)%></td>
										<%				
													}else{

													fieldname = "def_"+fieldname;
										%>
													<td class="fvalue"><%=pri.processReportTransMethod(Util.null2String(rs_dt.getString(fieldname)),fieldid)%></td>
										<%
														
													}
								    	    }
								    	}
								    }


							    }

					%>	  
						
					 </tr>
					 <%
					 	}
					 %>
					
					
                </tbody>
            </table>
			 </td>
        </tr>
    </tbody>
</table>
           
</div>
</div>	

	<script type="text/javascript">
	window.onload=function(){
		var clienthei= document.body.clientHeight;
		var height1=Number(clienthei)-100;
		height1=height1+'px';
		document.getElementById('div22').style.height=height1;
	}
	  
		var parentWin;
		try{
		parentWin = parent.getParentWindow(window);
		}catch(e){}
		function onBtnSearchClick() {
				var endDate=jQuery("#endDate").val();
			var beginDate=jQuery("#beginDate").val();
			if(endDate == '' && beginDate !=''){
				alert("请选择结束时间");
				return;
			}
			if(beginDate == '' && endDate !=''){
				alert("请选择开始时间");
				return;
			}
			if(endDate == '' && beginDate ==''){
				alert("请选择查询时间");
				return;
			}
			report.submit();
		}
        	var parentWin="";
		function refersh() {
  			window.location.reload();
  		}
		  function onBtnSaveClick() {	
			$('#report1').submit();	
			window.close();
		}
	   


	</script>
<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
</BODY>
</HTML>