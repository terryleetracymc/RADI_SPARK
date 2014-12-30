package com.smiims.debug;

import java.io.File;
import java.util.ArrayList;

import ncsa.hdf.object.FileFormat;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.smiims.constants.MOD13Constants;
import com.smiims.geoUtils.ReadMOD13;
import com.smiims.interapp.BaseApp;
import com.smiims.obj.GeoShortOri;

public class SaveHDF4GeoShortOriToHDFS extends BaseApp {

	/**
	 * 存储原生的HDF4一个波段数据到遥感数据到HDFS文件系统中
	 */
	private static final long serialVersionUID = -8582821947079595065L;

	public static void main(String args[]) {
		// String subPath[] = { "h23v04", "h24v04", "h25v03", "h25v05",
		// "h26v03",
		// "h26v05", "h27v04", "h27v06", "h28v06", "h29v06", "h23v05",
		// "h24v05", "h25v04", "h25v06", "h26v04", "h26v06", "h27v05",
		// "h28v05", "h28v07" };
		// String subPath[] = {"h25v05", "h26v03",
		// "h26v05", "h27v04", "h27v06", "h28v06", "h29v06", "h23v05",
		// "h24v05", "h25v04", "h25v06", "h26v04", "h26v06", "h27v05",
		// "h28v05", "h28v07" };
		String subPath[] = { "h27v05" };
		String inputRRPath = "/Users/liteng/Desktop/";
		String outputRRPath = "/Users/liteng/Desktop/output/";

		// 定义HDF4文件格式
		FileFormat fileFormat = FileFormat
				.getFileFormat(FileFormat.FILE_TYPE_HDF4);
		ReadMOD13 reader = new ReadMOD13(fileFormat);
		short data[] = new short[MOD13Constants.width * MOD13Constants.height];

		String inputRootPath;
		String inputPath;
		String outputPath;
		String outputRootPath;

		GeoShortOri geo = null;
		ArrayList<GeoShortOri> list = new ArrayList<GeoShortOri>();

		// 定义spark交互对象
		SparkConf conf = new SparkConf();
		conf.set("spark.executor.memory", "3g");
		conf.set("spark.akka.frameSize", "2000");
		conf.set("spark.cores.max", "4");
		conf.setAppName("GenGeoDataToHDFS");
		conf.setMaster("local[4]");
		JavaSparkContext jsc = new JavaSparkContext(conf);

		JavaRDD<GeoShortOri> rdd = null;

		for (int h = 0; h < subPath.length; h++) {
			inputRootPath = inputRRPath + subPath[h] + "/";
			outputRootPath = outputRRPath;
			File inputDirectory = new File(inputRootPath);
			String[] inputFileNames = inputDirectory.list();
			for (int m = 0; m < 3; m++) {
				// 对每个文件的操作
				inputPath = inputRootPath + inputFileNames[m];
				outputPath = outputRootPath + inputFileNames[m];
				reader.readData(inputPath, 1, data);
				geo = new GeoShortOri(data);
				list.add(geo);
				rdd = jsc.parallelize(list).repartition(1);
				rdd.saveAsObjectFile(outputPath);
				rdd.unpersist();
				list.clear();
			}
		}

		jsc.stop();
	}
}
