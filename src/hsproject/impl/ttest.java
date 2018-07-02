package hsproject.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.activation.FileTypeMap;

import weaver.general.ByteArrayDataSource;

public class ttest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String ctype = FileTypeMap.getDefaultFileTypeMap().getContentType("英语教学故事.doc".toLowerCase());
		System.out.println(ctype);
		InputStream in = new FileInputStream("D:/weaver/ecology/emailfile/英语教学故事.doc");
        new ByteArrayDataSource(in, "application/octet-stream");
	}

}
