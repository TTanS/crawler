package com.cetc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtils {
	// 写文件
	/**
	 * 
	 * @param fileName
	 *            文件名
	 * @param filePath
	 *            文件路径
	 * @param coding
	 *            编码
	 * @return
	 */
	public static void writeFile(String fileName, String filePath, String fileContent, String coding) {
		// 创建文件夹
		File saveDir = new File(filePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		try {
			File f = new File(filePath + fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), coding);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(fileContent);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读文件
	/**
	 * 
	 * @param fileName
	 *            文件名
	 * @param filePath
	 *            文件路径
	 * @param coding
	 *            编码
	 * @return
	 */
	public static String readFile(String fileName, String filePath, String coding) {
		String fileContent = "";
		try {
			File f = new File(filePath + fileName);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(f), coding);
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent += line;
				}
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent;
	}

}
