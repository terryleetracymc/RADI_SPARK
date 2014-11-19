package com.smiims.debug;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.smiims.TSFile.MOD13_b0_RSTS;
import com.smiims.iterface.TSSparkOperation;
import com.smiims.obj.GeoShort;

public class TerryDebug {
	public static void main(String[] args) {
		MOD13_b0_RSTS rsts = new MOD13_b0_RSTS("/Users/liteng/Desktop/modis");
		TSSparkOperation<GeoShort> tso = rsts;
		SparkConf conf = new SparkConf();
		conf.setAppName("MOD13_b0_GenerateTS");
		conf.setMaster("local[4]");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		JavaRDD<GeoShort> rdds[]=tso.dataDirectlyToJSC(jsc, 0, 0, 400, 400, 3);
		jsc.stop();
	}
}