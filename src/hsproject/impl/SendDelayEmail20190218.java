package hsproject.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.system.SystemComInfo;
/**
 * 延期前多少天邮件提醒
 * @author tangj
 *
 */
public class SendDelayEmail20190218 extends BaseCronJob{
	
	public void execute(){
		BaseBean log = new BaseBean();
		log.writeLog("发送延期邮件开始");
		checkDelay();
		log.writeLog("发送延期邮件结束");
	}
	
	/**
	 * 检验项目是否将要延期
	 */
	public void checkDelay(){
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String remindday = "7";
		String prjid = "";
		String status = "";
		String prjtype = "";
		String isdelay = "1"; //0延期 1 不延期
		String manager = "";
		String sql = "select remindday from uf_prj_delay_rmday ";
		String sql_dt = "";
		rs.executeSql(sql);
		if(rs.next()){
			remindday = Util.null2String(rs.getString("remindday"));			
		}
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, Util.getIntValue(remindday,0));// num为增加的天数，可以改变的
		Date currdate = ca.getTime();
		nowDate = dateFormate.format(currdate);
		sql="select * from hs_projectinfo where status not in('完成','项目终止','项目暂停')  and (isdelay <>'0' or isdelay is null) and id=970";
		rs.executeSql(sql);
		while(rs.next()){
			isdelay = "1";
			prjid = Util.null2String(rs.getString("id"));
			status = Util.null2String(rs.getString("status"));
			prjtype = Util.null2String(rs.getString("prjtype"));
			manager = Util.null2String(rs.getString("manager"));
			if("".equals(manager)){
				continue;
			}
			if("".equals(status)){
				isdelay = "1";
			}else{
				String enddate = getCurrentProcessEndTime(prjid,status,prjtype);
				//writeLog("enddate:"+enddate+" nowDate:"+nowDate);
				if("".equals(enddate)){
					isdelay = "1";
				}else if(nowDate.compareTo(enddate)>=0){
					String mod = "";
					sql_dt = "select mod((to_date('"+nowDate+"','yyyy-mm-dd')-to_date('"+enddate+"','yyyy-mm-dd')),"+remindday+") as mod from dual";
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()) {
						mod = Util.null2String(rs_dt.getString("mod"));
					}
					writeLog("isdey 0 mod:"+mod);
					if("0".equals(mod)){
						
						isdelay = "0";
						try {
							writeLog("isdey 0 sendmailaaa:");
							boolean result = sendEmail(prjid,manager,enddate,"0");
							writeLog("isdey 0 result:"+result);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		String now = dateFormate.format(new Date());
		sql="select * from hs_projectinfo where status not in('完成','项目终止','项目暂停')  and isdelay ='0' and id=970";
		rs.executeSql(sql);
		while(rs.next()){
			isdelay = "1";
			prjid = Util.null2String(rs.getString("id"));
			status = Util.null2String(rs.getString("status"));
			prjtype = Util.null2String(rs.getString("prjtype"));
			manager = Util.null2String(rs.getString("manager"));
			if("".equals(manager)){
				continue;
			}
			
			if("".equals(status)){
				isdelay = "1";
			}else{
				String enddate = getCurrentProcessEndTime(prjid,status,prjtype);
				//writeLog("enddate:"+enddate+" nowDate:"+now);
				if("".equals(enddate)){
					isdelay = "1";
				}else{
					String mod = "";
					sql_dt = "select mod((to_date('"+now+"','yyyy-mm-dd')-to_date('"+enddate+"','yyyy-mm-dd')),"+remindday+") as mod from dual";
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()) {
						mod = Util.null2String(rs_dt.getString("mod"));
					}
					writeLog("isdey 1 mod:"+mod);
					if("0".equals(mod)){
						isdelay = "0";
						try {
							writeLog("isdey 1 sendmailaaa:");
							boolean result = sendEmail(prjid,manager,enddate,"1");
							writeLog("isdey 1 result:"+result);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 过去当前项目阶段结束时间
	 * @param prjid
	 * @param status
	 * @param prjtype
	 * @return
	 */
	public String getCurrentProcessEndTime(String prjid,String status,String prjtype){
		RecordSet rs = new RecordSet();
		String processid = "";
		String enddate = "";
		String sql="";
//		String sql = "select id from uf_prj_process where prjtype='"+prjtype+"' and isused='1' and processname='"+status+"'";
//		rs.executeSql(sql);
//		if(rs.next()){
//			processid = Util.null2String(rs.getString("id"));
//		}
//		if("".equals(processid)){
//			enddate = "";
//		}
		sql="select enddate from hs_prj_process where prjid="+prjid+" and status='进行中' order by id asc";
		rs.executeSql(sql);
		if(rs.next()){
			enddate = Util.null2String(rs.getString("enddate"));
		}
		
		return enddate;
		
	}
	
	/**
	 * 发送延期邮件
	 * @param prjid 项目id
	 * @param manager 项目经理
	 * @param delaydate 延期日期
	 * @throws Exception
	 */
	public boolean sendEmail(String prjid,String manager,String delaydate,String type) throws Exception{
		SystemComInfo sci = new SystemComInfo();
		String mailip = sci.getDefmailserver();//
		String mailuser = sci.getDefmailuser();
		String password = sci.getDefmailpassword();
		String needauth = sci.getDefneedauth();//是否需要发件认证
		String mailfrom = sci.getDefmailfrom();
		SendMail sm = new SendMail();
		sm.setMailServer(mailip);//邮件服务器IP
		if (needauth.equals("1")) {
			sm.setNeedauthsend(true);
			sm.setUsername(mailuser);//服务器的账号
			sm.setPassword(password);//服务器密码
		} else {
			sm.setNeedauthsend(false);
		}
		ResourceComInfo rc = new ResourceComInfo();
		RecordSet rs = new RecordSet();
		String name = "";
		String procode = "";
		String belongdepart = "";
		String mailto = "";
		String sql = "select name,procode,prjtype,belongdepart from hs_projectinfo where id="+prjid;
		rs.executeSql(sql);
		if(rs.next()){
			name = Util.null2String(rs.getString("name"));
			procode = Util.null2String(rs.getString("procode"));
			belongdepart = Util.null2String(rs.getString("belongdepart"));
		}
		sql = "select email from uf_prj_departperson a,hrmresource b where a.person=b.id and a.department='"+belongdepart+"'";
		rs.executeSql(sql);
		if(rs.next()){
			mailto = Util.null2String(rs.getString("email")).trim();;
		}
		String subject = "项目延期提醒";
		StringBuffer body = new StringBuffer();
		body.append("项目延期提醒:<br>");
		if("1".equals(type)) {
			body.append("以下项目已经延期，请及时处理:<br><br>");
		}else {
			body.append("以下项目即将延期，请及时处理:<br><br>");
		}	
		body.append("<table border=\"2\"  style=\"width: 680px;border-collapse: collapse;font-size:15px;\"> ");
		body.append("<tr>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width:170px;height:18px;\"><strong>项目名称</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width:170px;height:18px;\"><strong>项目编号</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width:170px;height:18px;\"><strong>项目经理</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width:170px;height:18px;\"><strong>延期日期</strong></td>"); 
		body.append("</tr>");
		body.append("<tr>"); 
		body.append("	<td style=\"text-align:left;width:170px;height:18px;\">");body.append(name); body.append("</td>");
		body.append("	<td style=\"text-align:left;width:170px;height:18px;\">");body.append(procode); body.append("</td>");
		body.append("	<td style=\"text-align:left;width:170px;height:18px;\">");body.append(rc.getLastname(manager)); body.append("</td>");
		body.append("	<td style=\"text-align:left;width:170px;height:18px;\">");body.append(delaydate); body.append("</td>");
		body.append("</tr>");
		
		body.append("</table><br><br>");
		body.append("以上信息请知悉<br>谢谢！<br>");
		sql="select  email from hrmresource where id="+manager;
		rs.executeSql(sql);
		if(rs.next()){
			if("".equals(mailto)){
				mailto = Util.null2String(rs.getString("email")).trim();
			}else{
				mailto = mailto+","+Util.null2String(rs.getString("email")).trim();
			}
			
		}
		boolean result = false;
		if(!"".equals(mailto)){
			result = sm.sendhtml(mailfrom, mailto, "", "", subject, body.toString(), 3, "3");
		}
		return result;
	}
	private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }
}
