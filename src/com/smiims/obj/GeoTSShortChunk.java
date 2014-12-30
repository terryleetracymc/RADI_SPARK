package com.smiims.obj;

public class GeoTSShortChunk extends GeoTSChunk {

	/**
	 * 
	 */
	private static final long serialVersionUID = -818828069444642839L;
	public GeoShortChunk chunk;

	public GeoTSShortChunk(int id, GeoShortChunk c) {
		idx = id;
		chunk = c;
	}
}
