package cn.edu.ustc.wade.ssh;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

/** 
 * @author wade 
 * @version Dec 4, 2014 1:56:11 PM 
 */
public class ReadFileTest2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		smbGet1("smb://dev:pass4etl2@10.25.192.33/data2/dev/LogParser/tables");
//		smbGet("smb://192.168.75.204/test/新建 文本文档.txt","e:/");
	}

	/**
	 * 方法一：
	 * 
	 * @param remoteUrl
	 *            远程路径 smb://192.168.75.204/test/新建 文本文档.txt
	 * @throws IOException
	 */
	public static void smbGet1(String remoteUrl) throws IOException {
		SmbFile smbFile = new SmbFile(remoteUrl);
		int length = smbFile.getContentLength();// 得到文件的大小
		byte buffer[] = new byte[length];
		SmbFileInputStream in = new SmbFileInputStream(smbFile);
		// 建立smb文件输入流
		while ((in.read(buffer)) != -1) {

			System.out.write(buffer);
			System.out.println(buffer.length);
		}
		in.close();
	}

	// 从共享目录下载文件
	/**
	 * 方法二：
	 *    路径格式：smb://192.168.75.204/test/新建 文本文档.txt
	 *    			smb://username:password@192.168.0.77/test
	 * @param remoteUrl
	 *            远程路径
	 * @param localDir
	 *            要写入的本地路径
	 */
	public static void smbGet(String remoteUrl, String localDir) {
		InputStream in = null;
		OutputStream out = null;
		try {
			SmbFile remoteFile = new SmbFile(remoteUrl);
			if (remoteFile == null) {
				System.out.println("共享文件不存在");
				return;
			}
			String fileName = remoteFile.getName();
			File localFile = new File(localDir + File.separator + fileName);
			in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
			out = new BufferedOutputStream(new FileOutputStream(localFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 向共享目录上传文件
	public static void smbPut(String remoteUrl, String localFilePath) {
		InputStream in = null;
		OutputStream out = null;
		try {
			File localFile = new File(localFilePath);

			String fileName = localFile.getName();
			SmbFile remoteFile = new SmbFile(remoteUrl + "/" + fileName);
			in = new BufferedInputStream(new FileInputStream(localFile));
			out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 远程url smb://192.168.0.77/test
	// 如果需要用户名密码就这样：
	// smb://username:password@192.168.0.77/test



}
