package test;

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

import sun.util.logging.resources.logging;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.ByteArrayDataSource;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.system.SystemComInfo;

public class SendEmailFJ {
	public void sendEmail(){
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
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String filerealpath = "";
		String iszip = "";
		String name = "";
		String docids="17781,17786,17787";
		ArrayList<String> filenames = new ArrayList<String>();
    	ArrayList<InputStream> filecontents = new ArrayList<InputStream>();
    	String filepath="D:\\weaver\\ecology\\emailfile\\";
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
					filenames.add(new String(name.getBytes("UTF-8"),"GBK"));
					filecontents.add(is);

				} catch (Exception e) { 
					
					e.printStackTrace();
				}
			}

		}
    	
		boolean result=sm.sendMiltipartHtml(mailfrom,"1129520048@qq.com","270970426@qq.com","","测试","测试aaa",3,
				filenames,filecontents,"3");
		
		for(String fielname:filenames){
			File file = new File(filepath+fielname);
			if (file.exists()) {
				file.delete();
			}
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
