package com.smiims.debug;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.smiims.interapp.BaseApp;
import com.smiims.obj.GeoTSShortVector;

public class ReadObjectDebug extends BaseApp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1835885699896201837L;

	public static void main(String[] args) throws IOException {
		SparkConf conf = new SparkConf();
		conf.setAppName("ReadDataDebug");
		conf.setMaster("spark://192.168.1.140:7077");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		JavaRDD<GeoTSShortVector> rdd = jsc
				.objectFile("hdfs://192.168.1.140:9000/user/terry/ts_data/MODIS/2000049_2010353/MOD13/h23v04/b1");
		GeoTSShortVector vector=rdd.first();
		System.out.println(vector.x + ":" + vector.y);
		for (int i = 0; i < 250; i++) {
			System.out.println(vector.data[i]);
		}
		jsc.stop();
	}
}
