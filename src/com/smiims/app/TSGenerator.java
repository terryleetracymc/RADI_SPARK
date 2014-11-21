package com.smiims.app;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;


public class TSGenerator {
	public static void main(String[] args) {
		SparkConf conf=new SparkConf();
		JavaSparkContext jsc=new JavaSparkContext(conf);
		jsc.stop();
	}
}
