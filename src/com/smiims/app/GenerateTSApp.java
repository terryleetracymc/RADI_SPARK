package com.smiims.app;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.smiims.interapp.BaseApp;
import com.smiims.interapp.DataToTSShortFile;
import com.smiims.interapp.GenerateTS;
import com.smiims.obj.GeoTSShortFile;
import com.smiims.obj.GeoTSShortVector;

public class GenerateTSApp extends BaseApp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 715901196093569584L;

	@SuppressWarnings("unchecked")
	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("you don't have enough argments...");
			return;
		}
		String subPath = args[0];
		String inputRootPath = "hdfs://192.168.1.140:9000/user/terry/radi_data/MODIS/MOD13/";
		String outputRootPath = "hdfs://192.168.1.140:9000/user/terry/ts_data/MODIS/MOD13/";
		String argments[] = { "", "" };

		SparkConf conf = new SparkConf();
		conf.setAppName(GenerateTSApp.class.getName()+"@"+subPath);
		conf.set("spark.executor.memory", "10g");
		conf.set("spark.cores.max", "12");
		conf.set("spark.ui.port", "4041");

		JavaSparkContext jsc = new JavaSparkContext(conf);

		argments[0] = inputRootPath + subPath + "/";
		DataToTSShortFile dataToTSShortFile = new DataToTSShortFile();
		GenerateTS generateTS = new GenerateTS();

		JavaRDD<GeoTSShortFile> rdd = (JavaRDD<GeoTSShortFile>) dataToTSShortFile
				.input(jsc, argments);
		JavaRDD<GeoTSShortVector> vectorRDD = (JavaRDD<GeoTSShortVector>) generateTS
				.operate(rdd, argments);
		argments[1] = outputRootPath + subPath + "/b3/";
		vectorRDD.repartition(240).saveAsObjectFile(argments[1]);
		jsc.stop();
	}
}
