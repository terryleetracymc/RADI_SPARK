package com.smiims.obj;


public class GeoShort extends GeoData<Short>{
	/**
	 * 地理数据信息Short类型
	 * x表示地理坐标x
	 * y表示地理坐标y
	 * value为DV值，数值类型为Short类型
	 */
	private static final long serialVersionUID = 8344825574830309181L;
	
	public GeoShort(int x,int y,short value) {
		this.x=x;
		this.y=y;
		this.value=value;
	}
}
