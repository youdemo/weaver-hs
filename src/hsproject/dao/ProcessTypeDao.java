package hsproject.dao;

import hsproject.bean.ProcessTypeBean;

import java.util.ArrayList;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ProcessTypeDao {
	
	public List<ProcessTypeBean> getUsedProcessType(String prjtype){
		List<ProcessTypeBean> list = new ArrayList<ProcessTypeBean>();
		RecordSet rs = new RecordSet();
		String sql="select * from uf_prj_process where prjtype="+prjtype+" and isused='1'order by dsporder asc";
		rs.executeSql(sql);
		while(rs.next()){
			ProcessTypeBean ptb = new ProcessTypeBean();
			ptb.setId(Util.null2String(rs.getString("id")));
			ptb.setDescription(Util.null2String(rs.getString("description")));
			ptb.setDsporder(Util.null2String(rs.getString("dsporder")));
			ptb.setIsused(Util.null2String(rs.getString("isused")));
			ptb.setPrjtype(Util.null2String(rs.getString("prjtype")));
			ptb.setProcessname(Util.null2String(rs.getString("processname")));
			list.add(ptb);
		}
		return list;
	}
	
	
	/**
	 * 根据id获取过程类型
	 * @param typeid
	 * @return
	 */
	public String getTypeName(String typeid){
		RecordSet rs = new RecordSet();
		String typeName="";
		if("".equals(typeid)){
			return typeName;
		}
		String sql="select processname from uf_prj_process where id="+typeid;
		rs.executeSql(sql);
		if(rs.next()){
			typeName = Util.null2String(rs.getString("processname"));
		}
		return typeName;
	}
}
