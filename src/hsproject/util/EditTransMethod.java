package hsproject.util;

import hsproject.bean.ProcessFieldBean;
import hsproject.bean.ProjectFieldBean;
import weaver.general.Util;

public class EditTransMethod {
	/**
	 * 
	 * @param fieldname
	 * @param type
	 * @param isCommon
	 * @return
	 */
	public String doProjectEditTrans(ProjectFieldBean pfb,String value){
		
		try {
			return getTransValue(pfb.getFieldname(),pfb.getFieldtype(),pfb.getTexttype(),pfb.getButtontype(),pfb.getSelectbutton(),pfb.getTextlength(),pfb.getFloatdigit(),pfb.getIscommon(),pfb.getIsmust(),pfb.getIsreadonly(),pfb.getIsedit(),value);
		} catch (Exception e) {
			return "";
		}
	}
	
	public String doProcessEditTrans(ProcessFieldBean pfb,String value){
			
			try {
				return getTransValue(pfb.getFieldname(),pfb.getFieldtype(),pfb.getTexttype(),pfb.getButtontype(),pfb.getSelectbutton(),pfb.getTextlength(),pfb.getFloatdigit(),pfb.getIscommon(),pfb.getIsmust(),pfb.getIsreadonly(),pfb.getIsedit(),value);
			} catch (Exception e) {
				return "";
			}
	}

	public String getTransValue(String fieldname,String fieldtype,String texttype,String buttontype,String selectbutton,String textlength,String floatdigit,String isCommon,String isMust,String isReadOnly,String isEdit,String value) throws Exception{
		//BaseBean log = new BaseBean();
		TransUtil eu = new TransUtil();
		String result ="";
		if("1".equals(isReadOnly)&&"".equals(value)){
			return "";
		}
		if("0".equals(fieldtype)){
			if("1".equals(isReadOnly)){
				result = value;
			}else{
				result = eu.getTextTransResult(fieldname, texttype, textlength, String.valueOf(Util.getIntValue(floatdigit,-1)+1),isCommon,isMust,isEdit,value);
			}
		}else if("1".equals(fieldtype)){
			if("1".equals(isReadOnly)){
				result = new BrowserInfoUtil().getBrowserFieldValue(buttontype, value);
			}else{
				result = "";
			}
		}else if("3".equals(fieldtype)){
			if("1".equals(isReadOnly)){
				result =  new TransUtil().getSelectValue(selectbutton, value);
			}else{
				result = new TransUtil().getSelectFieldHtml(fieldname, selectbutton, isCommon, isMust, isEdit, value);
			}
		}else if("4".equals(fieldtype)){
			if("1".equals(isReadOnly)){
				result =  new TransUtil().getAttachUrl(value);
			}else{
				result = "";
			}
		}else{
			result = value;
		}
		return result;
	}
}
