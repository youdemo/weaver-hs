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

		<link href="/js/swfupload/default_wev8.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/swfupload/swfupload_wev8.js"></script>
<script type="text/javascript" src="/js/swfupload/swfupload.queue_wev8.js"></script>
<script type="text/javascript" src="/js/swfupload/fileprogress_wev8.js"></script>
<script type="text/javascript" src="/js/swfupload/handlers_wev8.js"></script>
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
	EditTransMethod etm = new EditTransMethod();
	BrowserInfoUtil biu = new BrowserInfoUtil();

	String accsize ="20";
	String accsec="39";//目录
	String attach="";

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
		<FORM id=report name=report action="/hsproject/project/aciton/submit-project-info.jsp" method=post enctype="multipart/form-data">
			<input type="hidden" name="requestid" value="">
			<input type="hidden" name="submitType" id="submitType"  value="edit">
			<input type="hidden" name="prjtype" id="prjtype" value="<%=prjtype%>">
			<input type="hidden" name="prjid" id="prjid" value="<%=prjid%>">
			<input type="hidden" name="attach" id="attach" value="<%=attach %>">
			<table id="topTitle" cellpadding="0" cellspacing="0">
				<tr>
					<td></td>
					<td class="rightSearchSpan" style="text-align:right;">
						<input type="button" value="保存" class="e8_btn_top_first" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px;" onclick="saveprj();"/>
						<span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
				</tr>
			</table>
				<script type="text/javascript">
			var oUpload;
				
				window.onload = function() {
				  var settings = {
						flash_url : "/js/swfupload/swfupload.swf",
						upload_url: "/proj/data/uploadPrjAcc.jsp",	// Relative to the SWF file
						post_params: {"method" : "uploadPrjAcc","secid":"<%=accsec %>"},
						file_size_limit : "<%=accsize %> MB",
						file_types : "*.*",
						file_types_description : "All Files",
						file_upload_limit : 100,
						file_queue_limit : 0,
						custom_settings : {
							progressTarget : "fsUploadProgress",
							cancelButtonId : "btnCancel"
						},
						debug: false,
						 

						// Button settings
						
						button_image_url : "/js/swfupload/add_wev8.png",	// Relative to the SWF file
						button_placeholder_id : "spanButtonPlaceHolder",
		
						button_width: 100,
						button_height: 18,
						button_text : '<span class="button"><%=SystemEnv.getHtmlLabelName(21406,user.getLanguage())%></span>',
						button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
						button_text_top_padding: 0,
						button_text_left_padding: 18,
							
						button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
						button_cursor: SWFUpload.CURSOR.HAND,
						
						// The event handler functions are defined in handlers.js
						file_queued_handler : fileQueued,
						file_queue_error_handler : fileQueueError,
						file_dialog_complete_handler : fileDialogComplete_1,
						upload_start_handler : uploadStart,
						upload_progress_handler : uploadProgress,
						upload_error_handler : uploadError,
						upload_success_handler : uploadSuccess,
						upload_complete_handler : uploadComplete,
						queue_complete_handler : queueComplete	// Queue plugin event
					};

					
					
					try{
						oUpload = new SWFUpload(settings);
					} catch(e){alert(e)}
				}
		
				function fileDialogComplete_1(){
					document.getElementById("btnCancel1").disabled = false;
					fileDialogComplete
				}
				function uploadSuccess(fileObj,serverdata){
					var data=eval(serverdata);
					//alert(data);
					if(data){
						var a=data;
						if(a>0){
							if(jQuery("#attach").val() == ""){

								jQuery("#attach").val(a);
							}else{
							
								jQuery("#attach").val(jQuery("#attach").val()+","+a);
								
							}
						}
					}
				}
		
				function uploadComplete(fileObj) {
					try {
						/*  I want the next upload to continue automatically so I'll call startUpload here */
						if (this.getStats().files_queued === 0) {
							report.submit();
							document.getElementById(this.customSettings.cancelButtonId).disabled = true;
						} else {	
							this.startUpload();
						}
					} catch (ex) { this.debug(ex); }
		
				}
			</script>
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
						if(!"1".equals(pfb.getIsreadonly())&& "1".equals(pfb.getFieldtype())){
							String showfieldname=pfb.getFieldname();
							String isMustInput="1";
							if(!"0".equals(pfb.getIscommon())){
								showfieldname="idef_"+pfb.getFieldname();
							}
							if("1".equals(pfb.getIsmust())){
								isMustInput = "2";
							}
							String buttontype=pfb.getButtontype();
							if("2".equals(buttontype)){
			%>
				<wea:item><%=pfb.getShowname()%></wea:item>
				<wea:item>
					    <button type="button" class=Calendar id="select<%=showfieldname%>" onclick="onshowPlanDate1('<%=showfieldname%>','select<%=showfieldname%>Span','<%=isMustInput%>')"></BUTTON>
                        <SPAN id="select<%=showfieldname%>Span" >
						<%
					if("2".equals(isMustInput)&&"".equals(fieldValue)){
			%>
						<IMG src='/images/BacoError_wev8.gif' align=absMiddle>
			<%									
					}else{
			%>
					   <%=fieldValue%>
			<%

					}
			%>
						</SPAN>
                        <INPUT type="hidden" name="<%=showfieldname%>" id="<%=showfieldname%>" value=" <%=fieldValue%>">

				</wea:item>
			<%	
							}else if("3".equals(buttontype)){
			%>
				<wea:item><%=pfb.getShowname()%></wea:item>
				<wea:item>
					<BUTTON type="button" class=Clock onClick="onShowTime(<%=showfieldname%>span,<%=showfieldname%>)"></BUTTON>
					<SPAN id="<%=showfieldname%>span">
				<%
					if("2".equals(isMustInput)&&"".equals(fieldValue)){
			%>
						<IMG src='/images/BacoError_wev8.gif' align=absMiddle>
			<%
					}else{
			%>
					   <%=fieldValue%>
			<%

					}
			%>
					</span>
					<input type="hidden" name="<%=showfieldname%>" id="<%=showfieldname%>" value="<%=fieldValue%>" />   
				</wea:item>
			<%	
							}else{
					%>
								<wea:item><%=pfb.getShowname()%></wea:item>
								<wea:item>
								<brow:browser name='<%=showfieldname%>'
								viewType='0'
								browserValue='<%=fieldValue%>'
								isMustInput='<%=isMustInput%>'
								browserSpanValue="<%=biu.getBrowserShowValue(fieldValue,buttontype)%>"
								hasInput='true'
								linkUrl='<%=biu.getLinkUrl(buttontype)%>' 
								completeUrl='<%=biu.getCompleteUrl(buttontype)%>'
								width='20%'
								isSingle='<%=biu.getIsSingle(buttontype)%>'
								hasAdd='false'
								browserUrl='<%=biu.getBrowserUrl(buttontype)%>'>
								</brow:browser>
								<input type=hidden name="<%=showfieldname%>_name" value="">
								</wea:item>
					<%
							}
						}else{
							if("4".equals(pfb.getFieldtype())){
					%>
								<wea:item><%=SystemEnv.getHtmlLabelName(22194,user.getLanguage())%></wea:item>
								<wea:item>																
								<div>
									<span> 
										<span id="spanButtonPlaceHolder"></span><!--选取多个文件-->
									</span>
									&nbsp;&nbsp;
									<span style="color:#262626;cursor:hand;TEXT-DECORATION:none" disabled onclick="oUpload.cancelQueue();" id="btnCancel1">
										<span><img src="/js/swfupload/delete_wev8.gif"  border="0"></span>
										<span style="height:19px"><font style="margin:0 0 0 -1"><%=SystemEnv.getHtmlLabelName(21407,user.getLanguage())%></font><!--清除所有选择--></span>
									</span>
								</div>
								<div class="fieldset flash" id="fsUploadProgress"></div>
								<div id="divStatus"></div>
								
								<input class=inputstyle style="display:none;" type=file name="accessory1" onchange='accesoryChanage(this)' style="width:100%">
								<span id="shfj_span"></span>
								(<%=SystemEnv.getHtmlLabelName(18642,user.getLanguage())%>:<%=accsize %>M)
								<button type="button" class=AddDoc style="display:none;" name="addacc" onclick="addannexRow()"><%=SystemEnv.getHtmlLabelName(611,user.getLanguage())%></button>
								<input type=hidden name=accessory_num value="1">
								
							</wea:item>
					<%


						}else{
								String fieldhtml ="";
							if("prjtype".equals(pfb.getFieldname())){
								fieldhtml = new ProjectTypeDao().getTypeName(prjtype);
							}else{
								fieldhtml = etm.doProjectEditTrans(pfb,fieldValue);
							//log.writeLog("aa"+fieldValue);
							}
						
				%>
						
						<wea:item><%=pfb.getShowname()%></wea:item>
						<wea:item><%=fieldhtml%>	    	
						</wea:item>		
				<%
					    }
					  }
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
			
		}

		function refersh() {
  			window.location.reload();
  		}

		function saveprj(){
			if(!oUpload){
				report.submit();
			}else{
				try {
					if(oUpload.getStats().files_queued === 0){
						report.submit();
					}else {
						oUpload.startUpload();
					}
				} catch (e) {
					report.submit();
				}
			}
		}
	 
   </script>
		<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
    <SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
    <SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
	
</BODY>
</HTML>