package hsproject.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.db2.jcc.am.v;

import weaver.hrm.company.DepartmentComInfo;

import hsproject.bean.ProjectFieldBean;
import hsproject.bean.ProjectGroupBean;
import hsproject.util.BrowserInfoUtil;
import hsproject.util.EditTransMethod;
import hsproject.util.ValueTransMethod;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		String prjtype = "";
		Map<String, String> pidComMap= new HashMap<String, String>();
		Map<String, String> pidDefMap= new HashMap<String, String>();
		ProjectFieldDao pfd = new ProjectFieldDao();
		List<ProjectFieldBean>  pfblist = pfd.getUsedPrjField(prjtype,"");
		for(ProjectFieldBean pfb:pfblist){
			String isCommon = pfb.getIscommon();
			String fieldName = pfb.getFieldname();
			pfb.getIsreadonly();
			if("0".equals(isCommon)){
				pidComMap.put(fieldName, "");
			}else{
				fieldName="idef_"+fieldName;
			}
		}
	}
	

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
}
