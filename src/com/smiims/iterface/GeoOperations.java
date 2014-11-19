package com.smiims.iterface;

public interface GeoOperations <T>{
	//读取数据，给定数据读取的起始x、y、offset以及波段号读取返回数组
	T[] getData(int startx,int starty,int offsetX,int offsetY,int bandNO);
}
