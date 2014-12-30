package com.smiims.obj;

public class GeoShortChunk extends GeoChunk {

	/**
	 * 
	 */
	private static final long serialVersionUID = 194364988808005599L;
	public short data[];
	public GeoShortChunk(int s,short d[]) {
		start=s;
		data=d;
	}
}
