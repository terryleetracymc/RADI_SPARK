package com.smiims.function;

import org.apache.spark.api.java.function.Function;

import com.smiims.obj.GeoShortOri;
import com.smiims.obj.GeoTSShortFile;

public class ToGeoShortFileF implements Function<GeoShortOri, GeoTSShortFile>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -262270487995067214L;

	int idx;
	public ToGeoShortFileF(int id) {
		idx=id;
	}
	
	@Override
	public GeoTSShortFile call(GeoShortOri v) throws Exception {
		return new GeoTSShortFile(idx, v);
	}

}
