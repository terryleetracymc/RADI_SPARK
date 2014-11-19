package com.smiims.utils;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {
	public static ArrayList<String> getFiles(String dir, String s) {
		s = s.replace('.', '#');
		s = s.replaceAll("#", "\\\\.");
		s = s.replace('*', '#');
		s = s.replaceAll("#", ".*");
		s = s.replace('?', '#');
		s = s.replaceAll("#", ".?");
		s = "^" + s + "$";
		ArrayList<String> matchList = new ArrayList<String>();
		File directory = new File(dir);
		if (directory.isDirectory()) {
			File files[] = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().matches(s)) {
					matchList.add(files[i].getAbsolutePath());
				}
			}
		}
		else {
		}
		return matchList;
	}
	
	//上传文件到HDFS文件系统中
	public static int uploadFilesToHDFS(String src,String dst){
		return 0;
	}
	
	//删除HDFS文件系统中的文件夹
	public static int deleteDirectoryInHDFS(String path){
		return 0;
	}
}
