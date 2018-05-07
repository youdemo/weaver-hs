package hsproject.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class UpdateProjectStatus extends BaseCronJob{
	public void execute(){
		BaseBean log = new BaseBean();
		log.writeLog("开始更新是否延期开始");
		updateStatus();
		log.writeLog("开始更新是否延期结束");
	}
	
	public void updateStatus(){
		RecordSet rs = new RecordSet();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String prjid = "";
		String status = "";
		String prjtype = "";
		String isdelay = "1"; //0延期 1 不延期
		String sql = "select * from hs_projectinfo where status <> '完成'";
		rs.executeSql(sql);
		while(rs.next()){
			isdelay = "1";
			prjid = Util.null2String(rs.getString("id"));
			status = Util.null2String(rs.getString("status"));
			prjtype = Util.null2String(rs.getString("prjtype"));
			if("".equals(status)){
				isdelay = "1";
			}else{
				String enddate = getCurrentProcessEndTime(prjid,status,prjtype);
				if("".equals(enddate)){
					isdelay = "1";
				}else{
					if(nowDate.compareTo(enddate)>0){
						isdelay = "0";
					}
				}
			}
			updateDelayStatus(prjid,isdelay);
		}
	}
	
	public void updateDelayStatus(String prjid,String isDelay){
		RecordSet rs = new RecordSet();
		String sql = "update hs_projectinfo set isdelay='"+isDelay+"' where id='"+prjid+"'";
		rs.executeSql(sql);
		
	}
	public String getCurrentProcessEndTime(String prjid,String status,String prjtype){
		RecordSet rs = new RecordSet();
		String processid = "";
		String enddate = "";
		String sql = "select id from uf_prj_process where prjtype='"+prjtype+"' and isused='1' and processname='"+status+"'";
		rs.executeSql(sql);
		if(rs.next()){
			processid = Util.null2String(rs.getString("id"));
		}
		if("".equals(processid)){
			enddate = "";
		}
		sql="select enddate from hs_prj_process where prjid="+prjid+" and processtype='"+processid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			enddate = Util.null2String(rs.getString("enddate"));
		}
		
		return enddate;
		
	}
}
