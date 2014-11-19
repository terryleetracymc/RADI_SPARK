package com.smiims.geoFile;

import com.smiims.obj.GeoShort;

/**
 * 
 * @author liteng
 * 继承RSDataFileBase的抽象类，实现泛型Short
 */
abstract public class MODFileShortBase extends MODFileBase<GeoShort> {
	public short data[];
	abstract short[] readData(int width, int height, int bandNO);
	@Override
	public GeoShort[] getData(int startx, int starty, int offsetX, int offsetY,
			int bandNO) {
		if(data==null){
			return null;
		}
		GeoShort sData[]=new GeoShort[offsetX*offsetY];
		int idx;
		for(int i=0;i<offsetX;i++){
			for(int j=0;j<offsetY;j++){
				idx=(startx+i)+(starty+j)*width;
				sData[idx]=new GeoShort(startx+i,starty+j,data[idx]);
			}
		}
		return sData;
	}
}
