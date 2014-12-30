package com.smiims.interapp;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaRDDLike;
import org.apache.spark.api.java.JavaSparkContext;

import com.smiims.function.ToGeoShortFileF;
import com.smiims.iterface.sparkInput;
import com.smiims.obj.GeoShortOri;
import com.smiims.obj.GeoTSShortFile;

public class DataToTSShortFile extends BaseApp implements sparkInput {

	/**
	 * 读取数据
	 */
	private static final long serialVersionUID = -2043124504367004114L;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String argments[] = { "hdfs://192.168.1.101:9000/user/terry/radi_data/MODIS/MOD13/h23v04/" };
		DataToTSShortFile readToChunk = new DataToTSShortFile();

		SparkConf conf = new SparkConf();
//		conf.setMaster("local[4]");
		conf.setAppName(DataToTSShortFile.class.getName());
		conf.set("spark.executor.memory", "5g");
		conf.set("spark.cores.max", "8");
		conf.set("spark.ui.port", "4041");

		JavaSparkContext jsc = new JavaSparkContext(conf);
		JavaRDD<GeoTSShortFile> rdd=(JavaRDD<GeoTSShortFile>) readToChunk.input(jsc, argments);
		rdd=rdd.cache();
//		rdd=rdd.repartition(300);
		rdd.saveAsObjectFile("hdfs://192.168.1.101:9000/user/terry/debug/GeoTSShortFile");
		jsc.stop();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public JavaRDDLike input(JavaSparkContext jsc, String[] args) {
		if (args.length < 1) {
			return null;
		}
		String inputRootPath = args[0];
		// 获取HDSF文件系统的信息
		Configuration hdfsConf = new Configuration();
		FileSystem hdfs;
		try {
			hdfs = FileSystem.get(URI.create(inputRootPath), hdfsConf);
			FileStatus[] fs = hdfs.listStatus(new Path(inputRootPath));
			Path[] listPath = FileUtil.stat2Paths(fs);
			String paths[] = new String[listPath.length];
			paths[0] = listPath[0].toString() + "/b3/";
			JavaRDD<GeoShortOri> geoShortRDD = jsc.objectFile(paths[0]);
			JavaRDD<GeoTSShortFile> resultRDD=geoShortRDD.map(new ToGeoShortFileF(0));
			JavaRDD<GeoShortOri> tmp=null;
			JavaRDD<GeoTSShortFile> stmp=null;
			for (int i = 1; i < listPath.length; i++) {
				paths[i] = listPath[i].toString() + "/b3/";
				tmp=jsc.objectFile(paths[i]);
				stmp=tmp.map(new ToGeoShortFileF(i));
				resultRDD=resultRDD.union(stmp);
			}
			return resultRDD;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
