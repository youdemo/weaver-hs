package test;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;


public class Test213 {

	public static void main(String[] args) throws Exception {
		System.out.println(encrypt("V0006961"));
		String ulXf = URLEncoder.encode(encrypt("V0006961"),"UTF-8");
		System.out.println(ulXf);
	

}
public static String encrypt(String sSrc) throws Exception {
	String sKey = "7227730012772198";  //  加密
	 if (sKey == null) {
		 System.out.print("Key为空null");
		 return null;
	 }
	 // 判断Key是否为16位
	 if (sKey.length() != 16) {
		 System.out.print("Key长度不是16位");
		 return null;
	 }
	 byte[] raw = sKey.getBytes("UTF-8");
	 SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	 Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
	 cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	 byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
	 return (new BASE64Encoder()).encodeBuffer(encrypted);
}

}
