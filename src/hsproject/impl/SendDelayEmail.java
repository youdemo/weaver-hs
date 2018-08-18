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
public class SendDelayEmail extends BaseCronJob{
	
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
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String remindday = "";
		String prjid = "";
		String status = "";
		String prjtype = "";
		String isdelay = "1"; //0延期 1 不延期
		String manager = "";
		String sql = "select remindday from uf_prj_delay_rmday ";
		rs.executeSql(sql);
		if(rs.next()){
			remindday = Util.null2String(rs.getString("remindday"));			
		}
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, Util.getIntValue(remindday,0));// num为增加的天数，可以改变的
		Date currdate = ca.getTime();
		nowDate = dateFormate.format(currdate);
		sql="select * from hs_projectinfo where status <> '完成' and (isdelay <>'0' or isdelay is null)";
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
				if("".equals(enddate)){
					isdelay = "1";
				}else{
					if(nowDate.compareTo(enddate)>0){
						isdelay = "0";
						try {
							sendEmail(prjid,manager,enddate);
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
	
	/**
	 * 发送延期邮件
	 * @param prjid 项目id
	 * @param manager 项目经理
	 * @param delaydate 延期日期
	 * @throws Exception
	 */
	public void sendEmail(String prjid,String manager,String delaydate) throws Exception{
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
		String sql = "select name,procode,prjtype from hs_projectinfo where id="+prjid;
		rs.executeSql(sql);
		if(rs.next()){
			name = Util.null2String(rs.getString("name"));
			procode = Util.null2String(rs.getString("procode"));
		}
		String subject = "项目延期提醒";
		StringBuffer body = new StringBuffer();
		body.append("项目延期提醒:<br>");
		body.append("以下项目即将延期，请及时处理:<br><br>");
		body.append("<table border=\"2\"  style=\"width: 480px;border-collapse: collapse;font-size:12px;\"> ");
		body.append("<tr>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width:120px;\"><strong>项目名称</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width:120px;\"><strong>项目编号</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width:120px;\"><strong>项目经理</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width:120px;\"><strong>延期日期</strong></td>"); 
		body.append("</tr>");
		body.append("<tr>"); 
		body.append("	<td style=\"text-align:left;width:120px;\">");body.append(name); body.append("</td>");
		body.append("	<td style=\"text-align:left;width:120px;\">");body.append(procode); body.append("</td>");
		body.append("	<td style=\"text-align:left;width:120px;\">");body.append(rc.getLastname(manager)); body.append("</td>");
		body.append("	<td style=\"text-align:left;width:120px;\">");body.append(delaydate); body.append("</td>");
		body.append("</tr>");
		
		body.append("</table><br><br>");
		body.append("以上信息请知悉<br>谢谢！<br>");
		String mailto = "";
		sql="select  email from hrmresource where id="+manager;
		rs.executeSql(sql);
		if(rs.next()){
			mailto = Util.null2String(rs.getString("email")).trim();
			if(!"".equals(mailto)){
				sm.sendhtml(mailfrom, mailto, "", "", subject, body.toString(), 3, "3");
			}
		}

	}
}
