package com.smiims.geoFile;

/**
 * @author liteng
 * @param <T>
 * modis 文件的基础操作类
 */
abstract public class MODFileBase<T> extends RSDataFileBase<T>{
	//网格编号，默认网格编号
	public String grid="h00v00";

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}
}
