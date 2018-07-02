package hsproject.dao;

import hsproject.bean.PrjChangeDetailBean;
import weaver.conn.RecordSet;
import weaver.general.Util;

public class PrjChangeDetailDao {
	public PrjChangeDetailBean getChangeInfo(String seqno){
		RecordSet rs = new RecordSet();
		PrjChangeDetailBean pcdb = new PrjChangeDetailBean();
		String sql = "";
		sql="select * from uf_prj_changedetail where seqno='"+seqno+"'";
		rs.executeSql(sql);
		if(rs.next()){
			pcdb.setChangetype(Util.null2String(rs.getString("changetype")));
			pcdb.setFieldid(Util.null2String(rs.getString("fieldid")));
			pcdb.setFieldname(Util.null2String(rs.getString("fieldname")));
			pcdb.setId(Util.null2String(rs.getString("id")));
			pcdb.setKeyid(Util.null2String(rs.getString("keyid")));
			pcdb.setName(Util.null2String(rs.getString("name")));
			pcdb.setNewvalue(Util.null2String(rs.getString("newvalue")));
			pcdb.setOldvalue(Util.null2String(rs.getString("oldvalue")));
			pcdb.setSeqno(Util.null2String(rs.getString("seqno")));
			pcdb.setType(Util.null2String(rs.getString("type")));
		}
		return pcdb;
	}
	
	public String  checkChangeExist(String seqno){
		RecordSet rs = new RecordSet();
		String sql = "";
		String result = "0";
		if("".equals(seqno)){
			return result;
		}
		sql="select count(1) as count from uf_prj_changedetail where seqno='"+seqno+"'";
		rs.executeSql(sql);
		if(rs.next()){
			if(rs.getInt("count")>0){
				result = "1";
			}
		}
		return result;
	}
}
