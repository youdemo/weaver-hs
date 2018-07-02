package hsproject.util;


import hsproject.dao.ProjectInfoDao;
import weaver.conn.RecordSet;
import weaver.docs.docs.DocComInfo;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.resource.ResourceComInfo;

public class BrowserInfoUtil {
	public String getBrowserFieldValue(String buttontype,String value) throws Exception{
		//BaseBean log = new BaseBean();
		String result="";
		if("0".equals(buttontype)||"6".equals(buttontype)){
			result="<span id=\"hsprj_span\" style=\"display:block;word-break:break-all;\">"+new ResourceComInfo().getMulResourcename1(value)+"</span>";
		}else if("1".equals(buttontype)||"7".equals(buttontype)){
			result="<span id=\"hsprj_span\" style=\"display:block;word-break:break-all;\">"+getMulDepartname(value)+"</span>";
		}else if("2".equals(buttontype)||"3".equals(buttontype)){
			result = value;
		}else if("4".equals(buttontype)){
			result="<span id=\"hsprj_span\" style=\"display:block;word-break:break-all;\">"+getWorkflowUrl(value)+"</span>";
		}else if("5".equals(buttontype)){
			result = getProjectType(value);
		}else if("8".equals(buttontype)){
			result = getProcessType(value);
		}else if("9".equals(buttontype)){
			result = getStatusName(value);
		}else if("10".equals(buttontype)||"11".equals(buttontype)){
			result="<span id=\"hsprj_span\" style=\"display:block;word-break:break-all;\">"+getMulSubCompany(value)+"</span>";
		}else if("12".equals(buttontype)){
			result="<span id=\"hsprj_span\" style=\"display:block;word-break:break-all;\">"+getMulDoc(value)+"</span>";
		}else if("13".equals(buttontype)){
			result = new ProjectInfoDao().getPrjNameWithLink(value);
		}
		
		return result;
	}
	public String getBrowserFieldValue2(String buttontype,String value) throws Exception{
		//BaseBean log = new BaseBean();
		String result="";
		if("0".equals(buttontype)||"6".equals(buttontype)){
			result=new ResourceComInfo().getMulResourcename(value);
		}else if("1".equals(buttontype)||"7".equals(buttontype)){
			result=getMulDepartname2(value);
		}else if("2".equals(buttontype)||"3".equals(buttontype)){
			result = value;
		}else if("4".equals(buttontype)){
			result= getWorkflowNames(value);
		}else if("5".equals(buttontype)){
			result = getProjectType(value);
		}else if("8".equals(buttontype)){
			result = getProcessType(value);
		}else if("9".equals(buttontype)){
			result = getStatusName(value);
		}else if("10".equals(buttontype)||"11".equals(buttontype)){
			result=getMulSubCompanyName(value);
		}else if("12".equals(buttontype)){
			result=getMulDocName(value);
		}else if("13".equals(buttontype)){
			result = new ProjectInfoDao().getPrjName(value);
		}
		
		return result;
	}
	public String getLinkUrl(String buttontype){
		String linkUrl="";
		if("0".equals(buttontype)){//人力资源
			linkUrl="/hrm/resource/HrmResource.jsp?id=";
		}else if("1".equals(buttontype)){//部门
			linkUrl="/hrm/company/HrmDepartmentDsp.jsp?id=";
		}else if("2".equals(buttontype)){//日期
			linkUrl="";
		}else if("3".equals(buttontype)){//时间
			linkUrl="";
		}else if("4".equals(buttontype)){//流程
			linkUrl="/workflow/request/ViewRequest.jsp?isrequest=1&requestid=";
		}else if("5".equals(buttontype)){//项目类型
			linkUrl="";
		}else if("6".equals(buttontype)){//多人力资源
			linkUrl="/hrm/resource/HrmResource.jsp?id=";
		}else if("7".equals(buttontype)){//多部门
			linkUrl="/hrm/company/HrmDepartmentDsp.jsp?id=";
		}else if("8".equals(buttontype)){//过程类型
			linkUrl="";
		}else if("9".equals(buttontype)){//项目状态
			linkUrl="";
		}else if("10".equals(buttontype)){//分部
			linkUrl="/hrm/company/HrmSubCompanyDsp.jsp?id=";
		}else if("11".equals(buttontype)){//多分部
			linkUrl="/hrm/company/HrmSubCompanyDsp.jsp?id=";
		}else if("12".equals(buttontype)){//多文档
			linkUrl="/docs/docs/DocDsp.jsp?isrequest=1&id=";
		}else if("13".equals(buttontype)){//项目
			linkUrl="";
		}
		return linkUrl;
	}
	
	public String getCompleteUrl(String buttontype){
		String completeUrl="";
		if("0".equals(buttontype)){//人力资源
			completeUrl="/data.jsp";
		}else if("1".equals(buttontype)){//部门
			completeUrl="/data.jsp?type=4";
		}else if("2".equals(buttontype)){//日期
			completeUrl="";
		}else if("3".equals(buttontype)){//时间
			completeUrl=""; 
		}else if("4".equals(buttontype)){//流程
			completeUrl="/data.jsp?type=16";
		}else if("5".equals(buttontype)){//项目类型
			completeUrl="";
		}else if("6".equals(buttontype)){//多人力资源
			completeUrl="/data.jsp?type=17";
		}else if("7".equals(buttontype)){//多部门
			completeUrl="/data.jsp?type=57";
		}else if("8".equals(buttontype)){//过程类型
			completeUrl="";
		}else if("9".equals(buttontype)){//项目状态
			completeUrl="";
		}else if("10".equals(buttontype)){//分部
			completeUrl="/data.jsp?type=164";
		}else if("11".equals(buttontype)){//多分部
			completeUrl="/data.jsp?type=194";
		}else if("12".equals(buttontype)){//多文档
			completeUrl="/data.jsp?type=9";
		}else if("13".equals(buttontype)){//项目
			completeUrl="";
		}
		return completeUrl;
	}
	public String getIsSingle(String buttontype){
		String isSingle="true";
		if("0".equals(buttontype)){//人力资源
			 isSingle="true";
		}else if("1".equals(buttontype)){//部门
			isSingle="true";
		}else if("2".equals(buttontype)){//日期
			isSingle="true";
		}else if("3".equals(buttontype)){//时间
			isSingle="true";
		}else if("4".equals(buttontype)){//流程
			isSingle="true";
		}else if("5".equals(buttontype)){//项目类型
			isSingle="true";
		}else if("6".equals(buttontype)){//多人力资源
			isSingle="false";
		}else if("7".equals(buttontype)){//多部门
			isSingle="false";
		}else if("8".equals(buttontype)){//过程类型
			isSingle="true";
		}else if("9".equals(buttontype)){//项目状态
			isSingle="true";
		}else if("10".equals(buttontype)){//分部
			isSingle="true";
		}else if("11".equals(buttontype)){//多分部
			isSingle="false";
		}else if("12".equals(buttontype)){//多文档
			isSingle="false";
		}else if("13".equals(buttontype)){//项目
			isSingle="true";
		}
		return isSingle;
	}
	public String getBrowserUrl(String buttontype){
		String browserUrl="";
		if("0".equals(buttontype)){//人力资源
			browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/resource/ResourceBrowser.jsp?resourceid=#id#";
		}else if("1".equals(buttontype)){//部门
			browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/company/DepartmentBrowser.jsp?resourceids=#id#";
		}else if("2".equals(buttontype)){//日期
			browserUrl="";
		}else if("3".equals(buttontype)){//时间
			browserUrl="";
		}else if("4".equals(buttontype)){//流程
			browserUrl="/systeminfo/BrowserMain.jsp?url=/workflow/request/RequestBrowser.jsp?isrequest=1?resourceids=#id#";
		}else if("5".equals(buttontype)){//项目类型
			browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.hs_prj_type";
		}else if("6".equals(buttontype)){//多人力资源
			browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/resource/MutiResourceBrowser.jsp?resourceids=#id#";
		}else if("7".equals(buttontype)){//多部门
			browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/company/MutiDepartmentBrowser.jsp?resourceids=#id#";
		}else if("8".equals(buttontype)){//过程类型
			browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.hs_prjprocess2";
		}else if("9".equals(buttontype)){//项目状态
			browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.hs_prj_status";
		}else if("10".equals(buttontype)){//分部
			browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/company/SubcompanyBrowser.jsp?resourceids=#id#";
		}else if("11".equals(buttontype)){//多分部
			browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/company/SubcompanyBrowser3.jsp?resourceids=#id#";
		}else if("12".equals(buttontype)){//多文档
			browserUrl="/systeminfo/BrowserMain.jsp?url=/docs/docs/MutiDocBrowser.jsp?isworkflow=1?resourceids=#id#";
		}else if("13".equals(buttontype)){//项目
			browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.hs_project";
		}
		return browserUrl;
	}
	
	
	
	/**
	 * 流程链接
	 * @param value
	 * @return
	 */
	public String getWorkflowUrl(String value){
		RecordSet rs = new RecordSet();
		String requestName = "";
		String requestUrl = "";
		String sql = "";
		String flag="&nbsp;&nbsp;";
		if(!"".equals(value)){
			String requestArr[] = value.split(",");
			for(int i =0;i<requestArr.length;i++){
				String requestValue = requestArr[i];
				requestName = "";
				if(!"".equals(requestValue)){
					sql="select * from workflow_requestbase where requestid="+requestValue;
					rs.executeSql(sql);
					if(rs.next()){
						requestName = Util.null2String(rs.getString("requestName"));
					}
					if(!"".equals(requestName)){
						if("".equals(requestUrl)){
							requestUrl = "<a href=\"javascript:openFullWindowForXtable('/workflow/request/ViewRequest.jsp?requestid="+value+"')\">"+requestName+"</a>";
		    			}else{
		    				requestUrl = requestUrl + flag +"<a href=\"javascript:openFullWindowForXtable('/workflow/request/ViewRequest.jsp?requestid="+value+"')\">"+requestName+"</a>";
		    			}
					}			
				}			
			}
		}
//		String sql="select * from workflow_requestbase where requestid="+value;
//		rs.executeSql(sql);
//		if(rs.next()){
//			requestName = Util.null2String(rs.getString("requestName"));
//		}
//		if(!"".equals(requestName)){
//			requestUrl = "<a href=\"javascript:openFullWindowForXtable('/workflow/request/ViewRequest.jsp?requestid="+value+"')\">"+requestName+"</a>";
//		}
		return requestUrl;
	}
	
	/**
	 * 流程名称
	 * @param value
	 * @return
	 */
	public String getWorkflowNames(String value){
		RecordSet rs = new RecordSet();
		String requestName = "";
		String requestUrl = "";
		String sql = "";
		String flag=" ";
		if(!"".equals(value)){
			String requestArr[] = value.split(",");
			for(int i =0;i<requestArr.length;i++){
				String requestValue = requestArr[i];
				requestName = "";
				if(!"".equals(requestValue)){
					sql="select * from workflow_requestbase where requestid="+requestValue;
					rs.executeSql(sql);
					if(rs.next()){
						requestName = Util.null2String(rs.getString("requestName"));
					}
					if(!"".equals(requestName)){
						if("".equals(requestUrl)){
							requestUrl = requestName;
		    			}else{
		    				requestUrl = requestUrl + flag +requestName;
		    			}
					}			
				}			
			}
		}

		return requestUrl;
	}
	/**
	 * 多部门只读链接
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String getMulDepartname(String value) throws Exception {
	    String departmentUrl = "";
	    String departmentvalue = "";
	    DepartmentComInfo dci= new DepartmentComInfo();
	    String flag="&nbsp;&nbsp;";
	    if(!"".equals(value)){
	    	String departArr[] = value.split(",");
	    	for(int i =0;i<departArr.length;i++){
	    		departmentvalue = departArr[i];
	    		if(!"".equals(departmentvalue)){
	    			if("".equals(departmentUrl)){
	    				departmentUrl = "<a href=\"javascript:openFullWindowForXtable('/hrm/company/HrmDepartmentDsp.jsp?id="+departmentvalue+"')\">"+dci.getDepartmentname(departmentvalue)+"</a>";
	    			}else{
	    				departmentUrl = departmentUrl + flag +"<a href=\"javascript:openFullWindowForXtable('/hrm/company/HrmDepartmentDsp.jsp?id="+departmentvalue+"')\">"+dci.getDepartmentname(departmentvalue)+"</a>";
	    			}
	    		}
	    	} 
	    }	      
	    return departmentUrl;
	   
	  }
	/**
	 * 多部门值
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String getMulDepartname2(String value) throws Exception {
	    String departmentUrl = "";
	    String departmentvalue = "";
	    DepartmentComInfo dci= new DepartmentComInfo();
	    String flag=" ";
	    if(!"".equals(value)){
	    	String departArr[] = value.split(",");
	    	for(int i =0;i<departArr.length;i++){
	    		departmentvalue = departArr[i];
	    		if(!"".equals(departmentvalue)){
	    			if("".equals(departmentUrl)){
	    				departmentUrl = dci.getDepartmentname(departmentvalue);
	    			}else{
	    				departmentUrl = departmentUrl + flag +dci.getDepartmentname(departmentvalue);
	    			}
	    		}
	    	} 
	    }	      
	    return departmentUrl;
	   
	  }
	public String getMulSubCompany(String value) throws Exception {
	    String subCompanyUrl = "";
	    String subCompanyvalue = "";
	    SubCompanyComInfo scc = new SubCompanyComInfo();
	    String flag="&nbsp;&nbsp;";
	    if(!"".equals(value)){
	    	String subArr[] = value.split(",");
	    	for(int i =0;i<subArr.length;i++){
	    		subCompanyvalue = subArr[i];
	    		if(!"".equals(subCompanyvalue)){
	    			if("".equals(subCompanyUrl)){
	    				subCompanyUrl = "<a href=\"javascript:openFullWindowForXtable('/hrm/company/HrmSubCompanyDsp.jsp?id="+subCompanyvalue+"')\">"+scc.getSubCompanyname(subCompanyvalue)+"</a>";
	    			}else{
	    				subCompanyUrl = subCompanyUrl + flag +"<a href=\"javascript:openFullWindowForXtable('/hrm/company/HrmSubCompanyDsp.jsp?id="+subCompanyvalue+"')\">"+scc.getSubCompanyname(subCompanyvalue)+"</a>";
	    			}
	    		}
	    	} 
	    }	      
	    return subCompanyUrl;
	   
	  }
	/**
	 * 获取多分部名称
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String getMulSubCompanyName(String value) throws Exception {
	    String subCompanyUrl = "";
	    String subCompanyvalue = "";
	    SubCompanyComInfo scc = new SubCompanyComInfo();
	    String flag=" ";
	    if(!"".equals(value)){
	    	String subArr[] = value.split(",");
	    	for(int i =0;i<subArr.length;i++){
	    		subCompanyvalue = subArr[i];
	    		if(!"".equals(subCompanyvalue)){
	    			if("".equals(subCompanyUrl)){
	    				subCompanyUrl = scc.getSubCompanyname(subCompanyvalue);
	    			}else{
	    				subCompanyUrl = subCompanyUrl + flag +scc.getSubCompanyname(subCompanyvalue);
	    			}
	    		}
	    	} 
	    }	      
	    return subCompanyUrl;
	   
	  }
	
	public String getMulDoc(String value) throws Exception {
	    String docUrl = "";
	    String docvalue = "";
	    DocComInfo dci = new DocComInfo();
	    String flag="&nbsp;&nbsp;";
	    if(!"".equals(value)){
	    	String docArr[] = value.split(",");
	    	for(int i =0;i<docArr.length;i++){
	    		docvalue = docArr[i];
	    		if(!"".equals(docvalue)){
	    			if("".equals(docUrl)){
	    				docUrl = "<a href=\"javascript:openFullWindowForXtable('/docs/docs/DocDsp.jsp?isrequest=1&amp;id="+docvalue+"')\">"+dci.getDocname(docvalue)+"</a>";
	    			}else{
	    				docUrl = docUrl + flag +"<a href=\"javascript:openFullWindowForXtable('/docs/docs/DocDsp.jsp?isrequest=1&amp;id="+docvalue+"')\">"+dci.getDocname(docvalue)+"</a>";
	    			}
	    		}
	    	} 
	    }	      
	    return docUrl;
	   
	  }
	/**
	 * 获取多文档名称
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String getMulDocName(String value) throws Exception {
	    String docUrl = "";
	    String docvalue = "";
	    DocComInfo dci = new DocComInfo();
	    String flag=" ";
	    if(!"".equals(value)){
	    	String docArr[] = value.split(",");
	    	for(int i =0;i<docArr.length;i++){
	    		docvalue = docArr[i];
	    		if(!"".equals(docvalue)){
	    			if("".equals(docUrl)){
	    				docUrl = dci.getDocname(docvalue);
	    			}else{
	    				docUrl = docUrl + flag +dci.getDocname(docvalue);
	    			}
	    		}
	    	} 
	    }	      
	    return docUrl;
	   
	  }
	/**
	 * 获取项目类型描述
	 * @param value
	 * @return
	 */
	public String getProjectType(String value){
		if("".equals(value)){
			return "";
		}
		String typeName = "";
		RecordSet rs = new RecordSet();
		String sql = "select typename from uf_project_type where id="+value;
		rs.executeSql(sql);
		if(rs.next()){
			typeName = Util.null2String(rs.getString("typename"));
		}
		return typeName;
	}
	
	/**
	 * 获取过程类型描述
	 * @param value
	 * @return
	 */
	public String getProcessType(String value){
		if("".equals(value)){
			return "";
		}
		String typeName = "";
		RecordSet rs = new RecordSet();
		String sql = "select processName from uf_prj_process where id="+value;
		rs.executeSql(sql);
		if(rs.next()){
			typeName = Util.null2String(rs.getString("processName"));
		}
		return typeName;
	}
	
	/**
	 * 获取项目状态名称
	 * @param value
	 * @return
	 */
	public String getStatusName(String value){
		if("".equals(value)){
			return "";
		}
		String statusName = "";
		RecordSet rs = new RecordSet();
		String sql = "select name from uf_project_status where id="+value;
		rs.executeSql(sql);
		if(rs.next()){
			statusName = Util.null2String(rs.getString("name"));
		}
		return statusName;
	}
	
	public String getBrowserShowValue(String ids,String buttontype){
		if("".equals(Util.null2String(ids))){
			return "";
		}
		String result="";
		if("0".equals(buttontype)){//人力资源
			result=getShowNames(ids,"hrmresource","id","lastname");
		}else if("1".equals(buttontype)){//部门
			result=getShowNames(ids,"hrmdepartment","id","departmentname");
		}else if("2".equals(buttontype)){//日期
			result=ids;
		}else if("3".equals(buttontype)){//时间
			result=ids;
		}else if("4".equals(buttontype)){//流程
			result=getShowNames(ids,"workflow_requestbase","requestid","requestname");
		}else if("5".equals(buttontype)){//项目类型
			result=getShowNames(ids,"uf_project_type","id","typename");
		}else if("6".equals(buttontype)){//多人力资源
			result=getShowNames(ids,"hrmresource","id","lastname");
		}else if("7".equals(buttontype)){//多部门
			result=getShowNames(ids,"hrmdepartment","id","departmentname");
		}else if("8".equals(buttontype)){//过程类型
			result=getShowNames(ids,"uf_prj_process","id","processname");
		}else if("9".equals(buttontype)){//项目状态
			result=getShowNames(ids,"uf_project_status","id","name");
		}else if("10".equals(buttontype)){//分部
			result=getShowNames(ids,"HrmSubCompany","id","subcompanyname");
		}else if("11".equals(buttontype)){//多分部
			result=getShowNames(ids,"HrmSubCompany","id","subcompanyname");
		}else if("12".equals(buttontype)){//多文档
			result=getShowNames(ids,"docdetail","id","docsubject");
		}else if("13".equals(buttontype)){//项目
			result=getShowNames(ids,"hs_projectinfo","id","name");
		}
		return result;
	}
	
	public String  getShowNames(String ids,String tablename,String key,String showfield){
		RecordSet rs = new RecordSet();
		String result = "";
		String flag = "";
		String sql="";
		String idarr[]=ids.split(",");
		for(int i=0;i<idarr.length;i++){
			String id=idarr[i];
			if("".equals(id)){
				continue;
			}
			sql="select "+showfield+" as name from "+tablename+" where "+key+" in("+id+")";
			rs.executeSql(sql);
			if(rs.next()){
				result =result+flag+ Util.null2String(rs.getString("name"));
				flag=",";
			}
		}
		
		return result;
	}
}
