package com.smiims.TSFile;

import java.util.Arrays;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.smiims.obj.GeoShort;

abstract public class RSTimeSeriesShortBase extends RSTimeSeriesBase<GeoShort> {
	// 一次只能处理一个维度的数据
	GeoShort[] d1Data;

	abstract public int readData(int rank, int startx, int starty, int offsetx,
			int offsety);

	@Override
	public JavaRDD<GeoShort> data1DDirectlyToJSC(JavaSparkContext jsc,
			int startx, int starty, int offsetx, int offsety) {
		if (d1Data == null) {
			return null;
		} else {
			return jsc.parallelize(Arrays.asList(d1Data));
		}
	}

	@Override
	public JavaRDD<GeoShort>[] dataDirectlyToJSC(JavaSparkContext jsc,
			int startx, int starty, int offsetx, int offsety, int rank) {
		return null;
	}
}
