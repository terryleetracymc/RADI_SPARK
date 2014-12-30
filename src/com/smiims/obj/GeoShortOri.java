package com.smiims.obj;

public class GeoShortOri extends GeoOri {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7484245718264360887L;
	public short data[];

	//data不是完全拷贝
	public GeoShortOri(short d[]) {
//		data = new short[d.length];
//		for (int i = 0; i < d.length; i++) {
//			data[i] = d[i];
//		}
		data=d;
	}
}
