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
	public GeoShort[] getData(int startx, int starty, int offsetx, int offsety,
			int bandNO) {
		if(data==null){
			return null;
		}
		GeoShort sData[]=new GeoShort[offsetx*offsety];
		int idx;
		for(int i=0;i<offsetx;i++){
			for(int j=0;j<offsety;j++){
				idx=(startx+i)+(starty+j)*width;
				sData[i+offsetx*j]=new GeoShort(startx+i,starty+j,data[idx]);
			}
		}
		return sData;
	}
}
