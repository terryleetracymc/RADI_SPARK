package com.smiims.interapp;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaRDDLike;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.smiims.constants.MOD13Constants;
import com.smiims.iterface.sparkOperation;
import com.smiims.obj.GeoShortChunk;
import com.smiims.obj.GeoTSShortChunk;
import com.smiims.obj.GeoTSShortFile;
import com.smiims.obj.GeoTSShortVector;

public class GenerateTS extends BaseApp implements sparkOperation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3439103471623851068L;

	public static void main(String args[]) {
		// SparkConf conf = new SparkConf();
		// conf.setAppName("GenerateTS");
		// conf.setMaster("local[4]");
		// conf.set("spark.executor.memory", "5g");
		// JavaSparkContext jsc = new JavaSparkContext(conf);
		//
		// String inputPath = "/Users/liteng/Desktop/GeoTSShortFile";
		//
		// JavaRDD<GeoTSShortFile> rdd = jsc.objectFile(inputPath);
		//
		// GenerateTS ts = new GenerateTS();
		// JavaRDD<GeoTSShortVector> result = (JavaRDD<GeoTSShortVector>) ts
		// .operate(rdd, args);
		//
		// result.saveAsObjectFile("/Users/liteng/Desktop/GeoTSShortVector");
		// jsc.stop();
	}

	/**
	 * 输入为JavaRDD<GeoTSShortFile>对象
	 * 
	 * 生成时间序列RDD对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JavaRDDLike operate(JavaRDDLike input, String args[]) {
		JavaRDD<GeoTSShortFile> rdd = (JavaRDD<GeoTSShortFile>) input;
		// 以300行为单位成块
		JavaRDD<GeoTSShortChunk> chunkRdd = rdd
				.flatMap(new FlatMapFunction<GeoTSShortFile, GeoTSShortChunk>() {
					/**
					 * 分块
					 */
					private static final long serialVersionUID = 1667433976354502792L;

					@Override
					public Iterable<GeoTSShortChunk> call(GeoTSShortFile v)
							throws Exception {
						ArrayList<GeoTSShortChunk> list = new ArrayList<GeoTSShortChunk>();
						short data[] = v.oriData.data;
						int idx = v.idx;
						// 300行为一块数据
						int chunk_size = 300;
						// 块数
						int block_num = MOD13Constants.height / chunk_size;
						// 每一分块的大小
						int size = MOD13Constants.width * chunk_size;
						for (int b = 0; b < block_num; b++) {
							// 临时空间
							short tmp[] = new short[size];
							// 起始位置点
							int start = b * size;
							// 给临时空间赋值
							for (int m = 0; m < size; m++) {
								tmp[m] = data[m + start];
							}
							// chunk
							GeoShortChunk chunk = new GeoShortChunk(b, tmp);
							list.add(new GeoTSShortChunk(idx, chunk));
						}
						return list;
					}
				});
		// 将chunkRdd映射为键值对形式
		JavaPairRDD<String, GeoTSShortChunk> chunkKVRdd = chunkRdd
				.mapToPair(new PairFunction<GeoTSShortChunk, String, GeoTSShortChunk>() {
					private static final long serialVersionUID = 7186433186629797967L;

					@Override
					public Tuple2<String, GeoTSShortChunk> call(
							GeoTSShortChunk v) throws Exception {
						String key = String.valueOf(v.chunk.start);
						GeoTSShortChunk value = new GeoTSShortChunk(v.idx,
								v.chunk);
						return new Tuple2<String, GeoTSShortChunk>(key, value);
					}
				});
		//
		JavaPairRDD<String, Iterable<GeoTSShortChunk>> gkvTRDD = chunkKVRdd
				.groupByKey();
		JavaRDD<Tuple2<String, Iterable<GeoTSShortVector>>> kvectorsRDD = gkvTRDD
				.map(new Function<Tuple2<String, Iterable<GeoTSShortChunk>>, Tuple2<String, Iterable<GeoTSShortVector>>>() {
					private static final long serialVersionUID = 8059692538861435384L;

					@Override
					public Tuple2<String, Iterable<GeoTSShortVector>> call(
							Tuple2<String, Iterable<GeoTSShortChunk>> v)
							throws Exception {
						String key = v._1;
						int start = Integer.parseInt(key) * 300;

						Iterable<GeoTSShortChunk> chunks = v._2;
						Iterator<GeoTSShortChunk> iter = chunks.iterator();

						GeoShortChunk datas[] = new GeoShortChunk[250];

						while (iter.hasNext()) {
							GeoTSShortChunk chunkTmp = iter.next();
							datas[chunkTmp.idx] = chunkTmp.chunk;
						}

						ArrayList<GeoTSShortVector> results = new ArrayList<GeoTSShortVector>();

						int total = datas[0].data.length;
						for (int m = 0; m < total; m++) {
							short vector[] = new short[250];
							for (int n = 0; n < 250; n++) {
								vector[n] = datas[n].data[m];
							}
							int x = m % MOD13Constants.width;
							int y = start + m / MOD13Constants.width;
							GeoTSShortVector tmp = new GeoTSShortVector(x, y,
									vector);
							results.add(tmp);
						}

						return new Tuple2<String, Iterable<GeoTSShortVector>>(
								key, results);
					}
				});

		JavaRDD<GeoTSShortVector> vectorsRDD = kvectorsRDD
				.flatMap(new FlatMapFunction<Tuple2<String, Iterable<GeoTSShortVector>>, GeoTSShortVector>() {
					private static final long serialVersionUID = -6039497056267219992L;

					@Override
					public Iterable<GeoTSShortVector> call(
							Tuple2<String, Iterable<GeoTSShortVector>> v)
							throws Exception {
						return v._2;
					}
				});
		return vectorsRDD;
	}
}
