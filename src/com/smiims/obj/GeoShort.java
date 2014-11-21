package com.smiims.obj;

public class GeoShort extends GeoObj<Short> {

	private static final long serialVersionUID = 5297339697694709336L;

	public GeoShort(int px, int py, int id,Short dv) {
		pos = new GeoPos(px, py);
		value = dv;
		idx=id;
	}
}
