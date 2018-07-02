package hsproject.dao;

import hsproject.bean.ProcessCommonFieldBean;
import hsproject.bean.ProcessFieldBean;
import hsproject.bean.ProjectFieldBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;
/**
 * 获取项目过程信息
 * @author jianyong.tang 2018-01-16
 *
 */
public class ProcessInfoDao {
	/**
	 * 获取项目通用字段值
	 * 
	 * @param prjid
	 * @return 
	 */
	public Map<String, String> getProcessCommonData(String processId) {
		//BaseBean log = new BaseBean();
		if ("".equals(processId)) {
			return null;
		}
		Map<String, String> commonMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		ProcessCommonFieldDao pdfd = new ProcessCommonFieldDao();
		String sql = "select * from hs_prj_process where id=" + processId;
		rs.executeSql(sql);
		if (rs.next()) {
			commonMap.put("id", Util.null2String(rs.getString("id")));
			List<ProcessCommonFieldBean> list = pdfd.getCommonProcessField();
			for (int i = 0; i < list.size(); i++) {
				ProcessCommonFieldBean pcfb = list.get(i);
				commonMap.put(pcfb.getFieldname(),
						Util.null2String(rs.getString(pcfb.getFieldname())));
				//log.writeLog("fieldname:"+pcfb.getFieldname()+" fieldValue "+Util.null2String(rs.getString(pcfb.getFieldname())));
			}
		}
		return commonMap;
	}

	/**
	 * 获取项目自定义字段值
	 * 
	 * @param prjid
	 * @return
	 */
	public Map<String, String> getProcessDefineData(String processId) {
		if ("".equals(processId)) {
			return null;
		}
		Map<String, String> definedMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		ProcessFieldDao pfd = new ProcessFieldDao();
		String sql = "select * from hs_prj_process_fielddata where processid=" + processId;
		rs.executeSql(sql);
		if (rs.next()) {
			definedMap.put("id", Util.null2String(rs.getString("id")));
			definedMap.put("prjid", Util.null2String(rs.getString("prjid")));
			definedMap.put("processid", Util.null2String(rs.getString("processid")));
			List<ProcessFieldBean> list = pfd.getAllDefineField();
			for (int i = 0; i < list.size(); i++) {
				ProcessFieldBean pfb = list.get(i);
				definedMap.put(pfb.getFieldname(),
						Util.null2String(rs.getString(pfb.getFieldname())));
			}
		}
		return definedMap;
	}
	
	/**
	 * 根据阶段id获取阶段名称
	 * @param prjid
	 * @return
	 */
	public String getProcessName(String processid){
		RecordSet rs = new RecordSet();
		String processname = "";
		String sql="select processname from hs_prj_process where id="+processid;
		rs.executeSql(sql);
		if(rs.next()){
			processname = Util.null2String(rs.getString("processname"));
		}
		return processname;				
	}
	
	/**
	 * 获取阶段字段值
	 * @param processid
	 * @param fieldid
	 * @return
	 */
	public String getFieldValue(String processid,String fieldid){
		String fieldValue = "";
		RecordSet rs = new RecordSet();
		String sql = "";
		if("".equals(Util.null2String(processid)) || "".equals(Util.null2String(processid))){
			return "";
		}
		ProcessFieldBean pfb = new ProcessFieldDao().getProcessFieldBean(fieldid);
		String iscommon = pfb.getIscommon();
		String fieldname = pfb.getFieldname();
		if(!"".equals(fieldname)){
			if("0".equals(iscommon)){
				sql="select "+fieldname+" from hs_prj_process where id="+processid;
			}else{
				sql="select "+fieldname+" from hs_prj_process_fielddata where processid="+processid;
			}
			rs.executeSql(sql);
			if(rs.next()){
				fieldValue = Util.null2String(rs.getString(fieldname));
			}
		}
		return fieldValue;
	}
}
