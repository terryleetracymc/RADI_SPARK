package com.smiims.app;

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

public class SaveHDF4GeoShortOriToHDFS extends BaseApp{

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
		if(args.length<1){
			System.out.println("you don't have enough argments...");
			return;
		}
		String subPath = args[0];
		// String subPath[] = {"h29v06", "h23v05",
		// "h24v05", "h25v04", "h25v06", "h26v04", "h26v06", "h27v05",
		// "h28v05", "h28v07" };
		// String subPath[] = { "h28v05", "h28v07" };
		String inputRRPath = "/media/terry/MODIS_VI (2000-2010)/MOD13_2000049_2010353/";
		String outputRRPath = "hdfs://192.168.1.140:9000/user/terry/radi_data/MODIS/";

		// 定义HDF4文件格式
		FileFormat fileFormat = FileFormat
				.getFileFormat(FileFormat.FILE_TYPE_HDF4);
		ReadMOD13 reader = new ReadMOD13(fileFormat);
		short data[] = new short[MOD13Constants.width * MOD13Constants.height];

		String inputRootPath;
		String inputPath;
		String outputPath;
		String outputRootPath;
		int band = 3;

		GeoShortOri geo = null;
		ArrayList<GeoShortOri> list = new ArrayList<GeoShortOri>();

		// for (int h = 0; h < subPath.length; h++) {
		// 定义spark交互对象
		SparkConf conf = new SparkConf();
		conf.set("spark.executor.memory", "2g");
		conf.set("spark.akka.frameSize", "2000");
		conf.set("spark.cores.max", "4");
		conf.setAppName("GenGeoDataToHDFS");
		JavaSparkContext jsc = new JavaSparkContext(conf);

		JavaRDD<GeoShortOri> rdd = null;
		inputRootPath = inputRRPath + subPath + "/";
		outputRootPath = outputRRPath + "MOD13/" + subPath + "/";
		File inputDirectory = new File(inputRootPath);
		String[] inputFileNames = inputDirectory.list();
		for (int m = 0; m < inputFileNames.length; m++) {
			// 对每个文件的操作
			inputPath = inputRootPath + inputFileNames[m];
			outputPath = outputRootPath + inputFileNames[m] + "/b" + band;
			reader.readData(inputPath, band, data);
			geo = new GeoShortOri(data);
			list.add(geo);
			rdd = jsc.parallelize(list).repartition(1);
			rdd.saveAsObjectFile(outputPath);
			list.clear();
		}

		jsc.stop();
		// }

	}
}
