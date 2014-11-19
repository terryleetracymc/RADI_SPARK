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

	/**
	 * 接口TSSparkOperation实现，将d1Data中的数据载入到spark集群的内存中
	 */
	@Override
	public JavaRDD<GeoShort> data1DDirectlyToJSC(JavaSparkContext jsc,
			int startx, int starty, int offsetx, int offsety) {
		if (d1Data == null) {
			System.err.println("d1Data is null");
			return null;
		} else {
			return jsc.parallelize(Arrays.asList(d1Data));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JavaRDD<GeoShort>[] dataDirectlyToJSC(JavaSparkContext jsc,
			int startx, int starty, int offsetx, int offsety, int rank) {
		JavaRDD rdds[]=new JavaRDD[rank];
		for(int i=0;i<rank;i++){
			if(readData(i, startx, starty, offsetx, offsety)==0){
				rdds[i]=data1DDirectlyToJSC(jsc, startx, starty, offsetx, offsety);
			}
			else{
				System.err.println("read rank"+i+" error");
				return null;
			}
		}
		return rdds;
	}
}
