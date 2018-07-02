package hsproject.impl;

import weaver.conn.RecordSet;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.system.SystemComInfo;

public class SendEmail {
	public void SendEmailByChangePerson(String prjid,String operater,String oldManager,String newManager) throws Exception{
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
		String prjtype = "";
		String sql="select name,procode,prjtype from hs_projectinfo where id="+prjid;
		rs.executeSql(sql);
		if(rs.next()){
			name = Util.null2String(rs.getString("name"));
			procode = Util.null2String(rs.getString("procode"));
			prjtype = Util.null2String(rs.getString("prjtype"));
		}
		String subject = "项目经理变更提醒";
		StringBuffer body = new StringBuffer();
		body.append("项目经理变更提醒:<br><br>");
		body.append("<table border=\"2\"  style=\"width: 910px;border-collapse: collapse;font-size:12px;\"> ");
		body.append("<tr>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;\"><strong>操作人</strong></td>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;\"><strong>项目名称</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;\"><strong>项目编号</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;\"><strong>原项目经理</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;\"><strong>新项目经理</strong></td>" ); 
		body.append("</tr>");
		body.append("<tr>"); 
		body.append("	<td style=\"text-align:left;\">");body.append(rc.getLastname(operater)); body.append("</td>");
		body.append("	<td style=\"text-align:left;\">");body.append(name); body.append("</td>");
		body.append("	<td style=\"text-align:left;\">");body.append(procode); body.append("</td>");
		body.append("	<td style=\"text-align:left;\">");body.append(rc.getLastname(oldManager)); body.append("</td>");
		body.append("	<td style=\"text-align:left;\">");body.append(rc.getLastname(newManager)); body.append("</td>");
		body.append("</tr>");
		
		body.append("</table><br><br>");
		body.append("以上信息请知悉<br>谢谢！<br>");
		String mailto = "";
		sql="select distinct mail from uf_prj_remindmail where prjtype="+prjtype;
		rs.executeSql(sql);
		while(rs.next()){
			mailto = Util.null2String(rs.getString("mail")).trim();
			sm.sendhtml(mailfrom, mailto, "", "", subject, body.toString(), 3, "3");
		}

	}
	
}
