package com.smiims.TSFile;

import java.io.File;
import java.util.ArrayList;

import com.smiims.iterface.TSSparkOperation;

abstract public class RSTimeSeriesBase<T> implements TSSparkOperation<T> {
	// 根文件路径
	String rootPath;
	// 顺序文件列表
	ArrayList<String> sequenceFiles;

	public ArrayList<String> getSequenceFiles() {
		return sequenceFiles;
	}

	// 默认以系统排列的顺序列出文件列表
	public ArrayList<String> getSquencePath(String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			String[] files = file.list();
			ArrayList<String> fList = new ArrayList<String>();
			for (int i = 0; i < files.length; i++) {
				fList.add(files[i]);
			}
			return fList;
		} else {
			System.err.println("you select a invalid path to process...");
			return null;
		}
	}
}
