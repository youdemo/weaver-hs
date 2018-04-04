package hsproject.util;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class TransUtil {
	BaseBean log = new BaseBean();
	/**
	 * 获取附件下载链接
	 * 
	 * @param value
	 * @return
	 */
	public String getAttachUrl(String value) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String url = "";
		String fileName = "";
		String flag = "";
		String fileId = "";
		if (!"".equals(value)) {
			sql = "select b.imagefileid,b.imagefilename from docimagefile a,imagefile b where a.imagefileid=b.imagefileid and a.docid in ("
					+ value + ")";
			rs.execute(sql);
			while (rs.next()) {
				url = url + flag;
				fileName = Util.null2String(rs.getString("imagefilename"));
				fileId = Util.null2String(rs.getString("imagefileid"));
				url = url
						+ "<a target=\"_blank\" href=\"/weaver/weaver.file.FileDownload?fileid="
						+ fileId + "&coworkid=-1&requestid=0&desrequestid=0\">"
						+ fileName + "</a>";
				flag = " <br/>";
			}
		}
		return url;
	}
	
	public String getSelectValue(String selectbutton,String value){
		String result = "";
		String sql="";
		String selectId = "";
		RecordSet rs = new RecordSet();
		if("".equals(selectbutton)||"".equals(value)){
			result = "";
		}else{
			sql="select id from uf_prj_selectbox where id="+selectbutton;
			rs.executeSql(sql);
			if(rs.next()){
				selectId = Util.null2String(rs.getString("id"));
			}
			sql="select value from uf_prj_selectbox_dt1 where mainid="+selectId+" and id="+value;
			rs.executeSql(sql);
			if(rs.next()){
				result = Util.null2String(rs.getString("value"));
			}
		}
		return result;
		
	}
	
	/**
	 * 新建编辑页面单行文本转换html
	 * @param fieldname
	 * @param texttype
	 * @param textlength
	 * @param floatdigit
	 * @param isCommon
	 * @param isMust
	 * @param isEdit
	 * @param value
	 * @return
	 */
	public String getTextTransResult(String fieldname,String texttype,String textlength,String floatdigit,String isCommon,String isMust,String isEdit,String value){
		StringBuffer transResult = new StringBuffer();
		transResult.append("");
		String idName=fieldname;
		if(!"0".equals(isCommon)){
			idName="idef_"+fieldname;
		}
		String imageName=idName+"image";
		if("0".equals(texttype)){
			if("1".equals(isMust)){
				transResult.append("<input class=\"inputstyle\" maxlength=\""+textlength+"\" size=\""+textlength+"\" id=\""+idName+"\" name=\""+idName+"\" onblur=\"checkLength()\" onchange=\"checkinput(&quot;"+idName+"&quot;,&quot;"+imageName+"&quot;)\" value=\""+value+"\"> "+
			                   "<span id=\""+imageName+"\">");
				if("".equals(value)){
					transResult.append("<img src=\"/images/BacoError_wev8.gif\" align=\"absMiddle\">");
				}
				transResult.append("</span>");
			}else{
				transResult.append("<input class=\"inputstyle\" maxlength=\""+textlength+"\" size=\""+textlength+"\" id=\""+idName+"\" name=\""+idName+"\" onblur=\"checkLength()\"  value=\""+value+"\"> "+
		                   "<span id=\""+imageName+"\">");
			}
			transResult.append( "<span id=\"remind\" style=\"cursor:hand\" title=\"文本长度不能超过"+textlength+"(1个中文字符等于3个长度)\">"+
	        			   "<img src=\"/images/remind_wev8.png\" align=\"absmiddle\">"+
	        			   "</span>");
		}else if("1".equals(texttype)){
			String viewtype = "0";
			if("1".equals(isMust)){
				viewtype = "1";
			}
			transResult.append("<input datatype=\"int\" onafterpaste=\"if(isNaN(value))execCommand('undo')\" style=\"ime-mode:disabled\" viewtype=\""+viewtype+"\" type=\"text\" class=\"InputStyle\" id=\""+idName+"\" name=\""+idName+"\" onkeypress=\"ItemCount_KeyPress()\" onblur=\"checkcount1(this);checkItemScale(this,'整数位数长度不能超过9位，请重新输入！',-999999999,999999999);checkinput2('"+idName+"','"+idName+"span',this.getAttribute('viewtype'))\" value=\""+value+"\" onchange=\"\" onpropertychange=\"\" _listener=\"\">");
			transResult.append("<span id=\""+idName+"span\" style=\"word-break:break-all;word-wrap:break-word\">");
			if("".equals(value)&&"1".equals(viewtype)){
				transResult.append("<img src=\"/images/BacoError_wev8.gif\" align=\"absMiddle\">") ;
			}
			transResult.append("</span>");
		}else if("2".equals(texttype)){
			String viewtype = "0";
			if("1".equals(isMust)){
				viewtype = "1";
			}
			transResult.append("<input datalength=\""+floatdigit+"\" datatype=\"float\" style=\"ime-mode:disabled\" onafterpaste=\"if(isNaN(value))execCommand('undo')\" viewtype=\""+viewtype+"\" type=\"text\" class=\"InputStyle\" id=\""+idName+"\" name=\""+idName+"\" onkeypress=\"ItemDecimal_KeyPress('"+idName+"',15,"+floatdigit+")\" onblur=\"checkFloat(this);checkinput2('"+idName+"','"+idName+"span',this.getAttribute('viewtype'))\" value=\""+value+"\" onchange=\"\" onpropertychange=\"\" _listener=\"\">");
			transResult.append("<span id=\""+idName+"span\" style=\"word-break:break-all;word-wrap:break-word\">");
			if("".equals(value)&&"1".equals(viewtype)){
				transResult.append("<img src=\"/images/BacoError_wev8.gif\" align=\"absMiddle\">") ;
			}
			transResult.append("</span>");
		}
		return transResult.toString();
	}
	
	public String getSelectFieldHtml(String fieldname,String selectbutton,String isCommon,String isMust,String isEdit,String value ){
		String sql = "";
		String selectId = "";
		String selectkey="";
		String selectvalue="";
		RecordSet rs = new RecordSet();
		StringBuffer transResult = new StringBuffer();
		transResult.append("");
		String idName=fieldname;
		if(!"0".equals(isCommon)){
			idName="idef_"+fieldname;
		}
		String viewtype = "0";
		if("1".equals(isMust)){
			viewtype = "1";
		}
		transResult.append("<select class=\"e8_btn_top middle\" id=\""+idName+"\" name=\""+idName+"\" viewtype=\""+viewtype+"\" onblur=\"checkinput2('"+idName+"','"+idName+"span',this.getAttribute('viewtype'));\">");
		transResult.append(" <option value=\"\"");
		if("".equals(value)){
			transResult.append("selected");
		} 
		transResult.append("></option>");
		sql="select id from uf_prj_selectbox where id="+selectbutton;
		rs.executeSql(sql);
		if(rs.next()){
			selectId = Util.null2String(rs.getString("id"));
		}
		sql="select id,value from uf_prj_selectbox_dt1 where mainid="+selectId;
		rs.executeSql(sql);
		while(rs.next()){
			selectkey = Util.null2String(rs.getString("id"));
			selectvalue = Util.null2String(rs.getString("value"));
			transResult.append(" <option value=\""+selectkey+"\"");
			if(selectkey.equals(value)){
				transResult.append("selected");
			} 
			transResult.append(">"+selectvalue+"</option>");
		}
		transResult.append("</select>");
		transResult.append("<span id=\""+idName+"span\" style=\"word-break:break-all;word-wrap:break-word\">");
		if("".equals(value)&&"1".equals(viewtype)){
			transResult.append("<img src=\"/images/BacoError_wev8.gif\" align=\"absMiddle\">") ;
		}
		transResult.append("</span>");
		return transResult.toString();
	}
}
