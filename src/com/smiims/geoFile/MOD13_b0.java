package com.smiims.geoFile;

import javax.swing.tree.DefaultMutableTreeNode;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;

public class MOD13_b0 extends MODFileShortBase {
	
	public MOD13_b0(String filePath,int w,int h,int b) {
		path=filePath;
		width=w;
		height=h;
		bandNO=b;
		data=readBandSigned(path, b);
	}

	@Override
	public short[] readData(int width, int height, int bandNO) {
		return data;
	}

	// 读取MODIS13 第0波段的数据
	@SuppressWarnings("deprecation")
	private static short[] readBandSigned(String fileName, int bandNO) {
		FileFormat fileFormat = FileFormat
				.getFileFormat(FileFormat.FILE_TYPE_HDF4);
		FileFormat file = null;
		Group root = null, mGrid = null, dField = null;
		try {
			file = fileFormat.open(fileName, FileFormat.READ);
			file.open();
			root = (Group) ((DefaultMutableTreeNode) file.getRootNode())
					.getUserObject();
			mGrid = (Group) root.getMemberList().get(0);
			dField = (Group) mGrid.getMemberList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Dataset dataset = (Dataset) dField.getMemberList().get(bandNO);
		short data[] = null;
		try {
			data = (short[]) dataset.read();
		} catch (OutOfMemoryError | Exception e) {
			e.printStackTrace();
			return null;
		}
		dataset.clear();
		dField.clear();
		mGrid.clear();
		try {
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}
