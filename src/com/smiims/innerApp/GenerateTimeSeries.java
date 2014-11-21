package com.smiims.innerApp;

import java.util.ArrayList;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaRDDLike;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.gdal.gdal.gdal;
import org.jblas.DoubleMatrix;

import scala.Tuple2;

import com.smiims.geoFile.TifShortFileBase;
import com.smiims.iterface.sparkInput;
import com.smiims.obj.GeoShort;

public class GenerateTimeSeries implements sparkInput{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JavaRDD<Tuple2<String, DoubleMatrix>> input(JavaSparkContext jsc, String[] args) {
		if(args.length<7){
			System.out.println("you don't have enough argments");
			return null;
		}
		else{
			gdal.AllRegister();
			//根目录
			String rootPath = args[0];
			String inputPath;
			TifShortFileBase tifReader = new TifShortFileBase();
			ArrayList<GeoShort> data = null;
			//各种数值
			int rank = Integer.parseInt(args[1]), 
				startx = Integer.parseInt(args[2]), 
				starty = Integer.parseInt(args[3]),
				offsetx = Integer.parseInt(args[4]), 
				offsety = Integer.parseInt(args[5]);
			JavaRDDLike rdds[] = new JavaRDDLike[rank];
			for (int i = 0; i < rank; i++) {
				System.out.println("reading " + i + ".tiff");
				inputPath = rootPath + i + ".tiff";
				data = tifReader.readData(inputPath, startx, starty, offsetx,
						offsety, i);
				System.out.println("parallelizing data " + i
						+ ".tiff to spark clusters");
				rdds[i] = jsc.parallelize(data);
			}
			JavaRDD<GeoShort> rdd = (JavaRDD<GeoShort>) rdds[0];
			for (int i = 1; i < rank; i++) {
				System.out.println("union rdds[" + i + "] to rdd");
				rdd = rdd.union((JavaRDD<GeoShort>) rdds[i]);
			}
			// 转换为key-value的形式
			// key: x:y
			// value: idx:dv
			JavaPairRDD<String, String> pairRDD = rdd
					.mapToPair(new PairFunction<GeoShort, String, String>() {

						private static final long serialVersionUID = -4552423482206276879L;

						@Override
						public Tuple2<String, String> call(GeoShort v)
								throws Exception {
							return new Tuple2<String, String>(v.pos.x + ":"
									+ v.pos.y, v.idx + ":" + v.value);
						}
					});
			// reduce所有的value值
			// key: x:y
			// value: value1 value2 ... valuen
			JavaPairRDD<String, String> pairResult = pairRDD
					.reduceByKey(new Function2<String, String, String>() {

						private static final long serialVersionUID = 83554559243672796L;

						@Override
						public String call(String s1, String s2)
								throws Exception {
							return s1 + " " + s2;
						}
					});
			//
			JavaRDD<Tuple2<String, DoubleMatrix>> vPairResult = pairResult
					.map(new Function<Tuple2<String, String>, Tuple2<String, DoubleMatrix>>() {

						private static final long serialVersionUID = 1638386157346181793L;

						@Override
						public Tuple2<String, DoubleMatrix> call(
								Tuple2<String, String> v) throws Exception {
							String key = v._1;
							String value = v._2;
							String values[] = value.split(" ");
							double dvalues[] = new double[values.length];
							// 向量
							int colonPos = -1;
							for (int i = 0; i < values.length; i++) {
								colonPos = values[i].indexOf(':');
								dvalues[i] = Double.parseDouble(values[i]
										.substring(colonPos + 1));
							}
							return new Tuple2<String, DoubleMatrix>(key,
									new DoubleMatrix(dvalues));
						}
					});
			return vPairResult;
		}
	}
}
