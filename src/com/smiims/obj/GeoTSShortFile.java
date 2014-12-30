package com.smiims.obj;

public class GeoTSShortFile extends GeoTSFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 482303920920252892L;
	public GeoShortOri oriData;

	public GeoTSShortFile(int id, GeoShortOri d) {
		idx = id;
		oriData = d;
	}
}
