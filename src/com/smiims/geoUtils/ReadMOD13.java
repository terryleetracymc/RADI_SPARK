package com.smiims.geoUtils;

import javax.swing.tree.DefaultMutableTreeNode;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;

import com.smiims.iterface.HDFShortOperator;

public class ReadMOD13 implements HDFShortOperator {
	FileFormat fileFormat;

	public ReadMOD13(FileFormat fm) {
		fileFormat = fm;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean readData(String path, int band, short[] data) {
		try {
			FileFormat file = fileFormat.open(path, FileFormat.READ);
			if (null == file) {
				System.err.println("Failed to open file " + path);
				return false;
			}
			file.open();
			// 获得HDF4 root目录
			Group root = (Group) ((DefaultMutableTreeNode) file.getRootNode())
					.getUserObject();
			Group group1 = (Group) root.getMemberList().get(0);
			// 获得数据目录
			Group group2 = (Group) group1.getMemberList().get(0);
			// 获得数据集
			// 为了和gdal统一，减一
			Dataset dataset = (Dataset) group2.getMemberList().get(band - 1);
			// 读取数据
			short tmp[] = (short[]) dataset.read();
			for (int i = 0; i < tmp.length; i++) {
				data[i] = tmp[i];
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
