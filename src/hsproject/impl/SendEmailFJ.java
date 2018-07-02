package hsproject.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import sun.util.logging.resources.logging;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.ByteArrayDataSource;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.system.SystemComInfo;

public class SendEmailFJ {
	public void sendEmail() {
		SystemComInfo sci = new SystemComInfo();
		String mailip = sci.getDefmailserver();//
		String mailuser = sci.getDefmailuser();
		String password = sci.getDefmailpassword();
		String needauth = sci.getDefneedauth();// 是否需要发件认证
		String mailfrom = sci.getDefmailfrom();
		SendMail sm = new SendMail();
		sm.setMailServer(mailip);// 邮件服务器IP
		if (needauth.equals("1")) {
			sm.setNeedauthsend(true);
			sm.setUsername(mailuser);// 服务器的账号
			sm.setPassword(password);// 服务器密码
		} else {
			sm.setNeedauthsend(false);
		}
		String subject = "E.G.O.China培训人员报备通知  E.G.O.China Trainee Training Notice";
		StringBuffer body = new StringBuffer();
		body.append("Hello All,<br>");
		body.append("    应公司发展需要，为全面提升员工能力，现决定开展一项题为（");body.append("课程清单");body.append("）的专题培训。为此，我们真诚地邀请以下员工参与此次培训<br>");
		body.append("    培训名单:<br>");
		body.append("<table border=\"2\"  style=\"width: 360px;border-collapse: collapse;font-size:12px;\"> ");
		body.append("<tr>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>员工号</strong></td>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>姓名</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>部门</strong></td>" ); 
		body.append("</tr>");
		body.append("<tr>"); 
		body.append("	<td style=\"text-align:left;\">");body.append("20000"); body.append("</td>");
		body.append("	<td style=\"text-align:left;\">");body.append("张三"); body.append("</td>");
		body.append("	<td style=\"text-align:left;\">");body.append("开发"); body.append("</td>");
		body.append("</tr>");
		body.append("</table>");
		body.append("    培训类别：");body.append("开发");body.append("<br>");
		body.append("    培训起止时间： ");body.append("2018-06-07 15:23~2018-06-07 15:23");body.append("<br>");
		body.append("    培训地点：");body.append("上海");body.append("<br>");
		body.append("    联系人：");body.append("钟超");body.append("<br>");
		body.append("    培训师：");body.append("钟超");body.append("<br>");
		body.append("    备注：");body.append("请准时参加");body.append("<br>");
		body.append("    感谢各部门的支持<br><br>");
		body.append("    此邮件为系统自动发送，请不要回复！如果有疑问，请直接联系Sissi Qian或者Julie Yi。<br><br><br><br>");
		body.append("Hello All,<br>");
		body.append("    In order to improve employees' ability to meet company's development, Now we decide to start a topic training(");body.append("Course list");body.append(").<br>");
		body.append("    For this purpose, we sincerely invite the following employees to participate in this training.");
		body.append("    Trainees' list:<br>");
		body.append("<table border=\"2\"  style=\"width: 360px;border-collapse: collapse;font-size:12px;\"> ");
		body.append("<tr>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>Personal NO.</strong></td>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>Name</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>Department</strong></td>" ); 
		body.append("</tr>");
		body.append("<tr>"); 
		body.append("	<td style=\"text-align:left;\">");body.append("20000"); body.append("</td>");
		body.append("	<td style=\"text-align:left;\">");body.append("张三"); body.append("</td>");
		body.append("	<td style=\"text-align:left;\">");body.append("开发"); body.append("</td>");
		body.append("</tr>");
		body.append("</table>");
		body.append("    Training type: ");body.append("开发");body.append("<br>");
		body.append("    Training start time and finish time: ");body.append("2018-06-07 15:23~2018-06-07 15:23");body.append("<br>");
		body.append("    Training center: ");body.append("上海");body.append("<br>");
		body.append("    Contact Person: ");body.append("钟超");body.append("<br>");
		body.append("    Trainer: ");body.append("钟超");body.append("<br>");
		body.append("    Comment:");body.append("请准时参加");body.append("<br>");
		body.append("    Thanks for your support<br><br>");
		body.append("    This mail was sent by system automatically,Please do not reply! Please contact Sissi Qian/Julie Yi about any questions.");
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String filerealpath = "";
		String iszip = "";
		String name = "";
		String docids = "17781,17786,17787";
		ArrayList<String> filenames = new ArrayList<String>();
		ArrayList<InputStream> filecontents = new ArrayList<InputStream>();
		//String filepath = "D:\\weaver\\ecology\\emailfile\\";
		String zipName = "D:\\weaver\\ecology\\emailfile\\attach1234.zip";
		File zipFile = new File(zipName);
		ZipOutputStream zipOut = null;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sql = " select b.filerealpath,b.iszip,b.imagefilename,b.imagefileid from  "
				+ " imagefile b  where b.imagefileid in(select max(imagefileid) "
				+ "from docimagefile where docid in("
				+ docids
				+ ") group by docid)";
		rs.executeSql(sql);
		while (rs.next()) {
			filerealpath = Util.null2String(rs.getString("filerealpath"));
			iszip = Util.null2String(rs.getString("iszip"));
			name = Util.null2String(rs.getString("imagefilename"));
			if (filerealpath.length() > 0) {

				try {
					InputStream is = getFile(filerealpath, iszip);
					zipOut.setEncoding("GBK");
					zipOut.putNextEntry(new ZipEntry(name));
					int temp = 0;
					byte[] buffer = new byte[1024];
					while ((temp = is.read(buffer)) >= 0) {
						zipOut.write(buffer, 0, temp);
					}
					is.close();

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		}
		try {
			zipOut.close();
			InputStream fi = new FileInputStream(new File(zipName));
			filenames.add("attach.zip");
			filecontents.add(fi);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean result = sm.sendMiltipartHtml(mailfrom, "1129520048@qq.com",
				"270970426@qq.com", "", subject, body.toString(), 3, filenames,
				filecontents, "3");

		File file = new File(zipName);
		if (file.exists()) {
			file.delete();
		}

	}

	private InputStream getFile(String filerealpath, String iszip)
			throws Exception {
		ZipInputStream zin = null;
		InputStream imagefile = null;
		File thefile = new File(filerealpath);
		if (iszip.equals("1")) {
			zin = new ZipInputStream(new FileInputStream(thefile));
			if (zin.getNextEntry() != null)
				imagefile = new BufferedInputStream(zin);
		} else {
			imagefile = new BufferedInputStream(new FileInputStream(thefile));
		}
		return imagefile;
	}

	public boolean createFile(String destFileName) throws IOException {
		File file = new File(destFileName);
		if (file.exists()) {
			file.delete();
		}
		if (destFileName.endsWith(File.separator)) {
			return false;
		}
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
