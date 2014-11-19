package com.smiims.obj;

import java.io.Serializable;

public class GeoData<T> implements Serializable{
	/**
	 * 地理数据信息Short类型
	 * x表示地理坐标x
	 * y表示地理坐标y
	 * value为DV值，数值类型为T泛型类型
	 */
	private static final long serialVersionUID = -6301052851962010897L;
	int x;
	int y;
	T value;
}
