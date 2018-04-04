package hsproject.dao;

import hsproject.bean.OutDataLogBean;
import weaver.conn.ConnStatement;

public class OutDataLogDao {
	public void insertLog(OutDataLogBean odlb) {
		ConnStatement cs = new ConnStatement();
		String sql="insert into zoa_wffield_map(mtid,type,fieldtype,prjid,processid,sysnfield,oldvalue,newvalue,sysnsql,updatesql,sysndate,sysntime,description,logtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			cs.setStatementSql(sql);
			cs.setString(1,odlb.getMtid());
			cs.setString(2,odlb.getType());
			cs.setString(3,odlb.getFieldtype());
			cs.setString(4,odlb.getPrjid());
			cs.setString(5,odlb.getProcessid());
			cs.setString(6,odlb.getSysnfield());
			cs.setString(7,odlb.getOldvalue());
			cs.setString(8,odlb.getNewvalue());
			cs.setString(9,odlb.getSysnsql());
			cs.setString(10,odlb.getUpdatesql());
			cs.setString(11,odlb.getSysndate());
			cs.setString(12,odlb.getSysntime());
			cs.setString(13,odlb.getDescription());
			cs.setString(14,odlb.getLogtype());
			cs.executeUpdate();		
			cs.close();
		} catch (Exception e) {
			cs.close();
			e.printStackTrace();
		}				
		
	}

	public void writeLog(String mtid,String type,String fieldtype,String prjid,String processid,String sysnfield,String oldvalue,String newvalue,String sysnsql,String updatesql,String sysndate,String sysntime,String description,String logtype ){
		ConnStatement cs = new ConnStatement();
		String sql="insert into zoa_wffield_map(mtid,type,fieldtype,prjid,processid,sysnfield,oldvalue,newvalue,sysnsql,updatesql,sysndate,sysntime,description,logtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			cs.setStatementSql(sql);
			cs.setString(1,mtid);
			cs.setString(2,type);
			cs.setString(3,fieldtype);
			cs.setString(4,prjid);
			cs.setString(5,processid);
			cs.setString(6,sysnfield);
			cs.setString(7,oldvalue);
			cs.setString(8,newvalue);
			cs.setString(9,sysnsql);
			cs.setString(10,updatesql);
			cs.setString(11,sysndate);
			cs.setString(12,sysntime);
			cs.setString(13,description);
			cs.setString(14,logtype);
			cs.executeUpdate();		
			cs.close();
		} catch (Exception e) {
			cs.close();
			e.printStackTrace();
		}				
	}
	
}
