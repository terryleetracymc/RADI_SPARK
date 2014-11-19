package com.smiims.geoFile;

import com.smiims.iterface.GeoOperations;

/**
 * @author liteng
 * @param <T>
 *            遥感图像文件操作的基础类，只操作图像的一个波段
 */
abstract public class RSDataFileBase<T> implements GeoOperations<T> {
	// 数据的基本信息
	// 文件路径
	public String path;
	// 图像尺寸大小以及所需要读取的波段
	int width, height, bandNO;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getBandNO() {
		return bandNO;
	}

	public void setBandNO(int bandNO) {
		this.bandNO = bandNO;
	}
}
