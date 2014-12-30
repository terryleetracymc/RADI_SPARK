package com.smiims.obj;

public class GeoTSShortVector extends GeoTSVector {

	/**
	 * 
	 */
	private static final long serialVersionUID = -278539596966336134L;
	public short[] data;

	public GeoTSShortVector(int px, int py, short[] d) {
		x = px;
		y = py;
		data = d;
	}
}
