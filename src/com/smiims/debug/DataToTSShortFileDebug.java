package com.smiims.debug;

import java.io.File;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaRDDLike;
import org.apache.spark.api.java.JavaSparkContext;

import com.smiims.function.ToGeoShortFileF;
import com.smiims.interapp.BaseApp;
import com.smiims.iterface.sparkInput;
import com.smiims.obj.GeoShortOri;
import com.smiims.obj.GeoTSShortFile;

public class DataToTSShortFileDebug extends BaseApp implements sparkInput {

	/**
	 * 读取数据
	 */
	private static final long serialVersionUID = -2043124504367004114L;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String argments[] = { "/Users/liteng/Desktop/output/" };
		DataToTSShortFileDebug readToChunk = new DataToTSShortFileDebug();

		SparkConf conf = new SparkConf();
		conf.setMaster("local[8]");
		conf.setAppName(DataToTSShortFileDebug.class.getName());
		// conf.set("spark.executor.memory", "5g");
		// conf.set("spark.cores.max", "8");
		// conf.set("spark.ui.port", "4041");

		JavaSparkContext jsc = new JavaSparkContext(conf);
		JavaRDD<GeoTSShortFile> rdd = (JavaRDD<GeoTSShortFile>) readToChunk
				.input(jsc, argments);
//		rdd = rdd.repartition(400);
		rdd.saveAsObjectFile("/Users/liteng/Desktop/GeoTSShortFile/");
		jsc.stop();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public JavaRDDLike input(JavaSparkContext jsc, String[] args) {
		if (args.length < 1) {
			return null;
		}
		String inputRootPath = args[0];
		String inputPath;
		File directory = new File(inputRootPath);
		String paths[] = directory.list();
		inputPath = inputRootPath + paths[0];
		JavaRDD<GeoShortOri> geoShortRDD = jsc.objectFile(inputPath);
		JavaRDD<GeoTSShortFile> resultRDD = geoShortRDD
				.map(new ToGeoShortFileF(0));
		JavaRDD<GeoShortOri> tmp = null;
		JavaRDD<GeoTSShortFile> stmp = null;
		for (int i = 1; i < 3; i++) {
			inputPath = inputRootPath + paths[i];
			tmp = jsc.objectFile(inputPath);
			stmp = tmp.map(new ToGeoShortFileF(i));
			resultRDD = resultRDD.union(stmp);
		}
		return resultRDD;
	}

}
