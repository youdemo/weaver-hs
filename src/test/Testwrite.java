package test;

import weaver.general.MD5;
import weaver.general.Util;

public abstract class Testwrite {
	public static void main(String[] args) {
		String str = "123";
		//能够将null转换成""防止空指针异常
		str = Util.null2String(str);
		//能够将String转换成int 发生异常时返回-1
		int i=Util.getIntValue(str, -1);
		//能够将String转换成float 发生异常时返回-1
		float f=Util.getFloatValue(str, -1);
		//能够将String转换成double 发生异常时返回-1
		double d=Util.getDoubleValue(str, -1);
		//千分位转换，若包含非数字，则直接返回
		//Util.itemDecimal("123123");
		//BaseBean log = new BaseBean();
		//将测试日志写入异常文件
		//log.writeLog("测试日志");
		//try{
			
		//}catch(Exception e){
			//将异常信息写入日志文件
		//	log.writeLog(e);
		//}
		String str12 = "Ec123";
		MD5 md5 = new MD5();
		md5.getMD5ofStr(str12);
		System.out.println(md5.getMD5ofStr(str12));
		//System.out.println(Util.getEncrypt("Ec123"));
		

	}
}
