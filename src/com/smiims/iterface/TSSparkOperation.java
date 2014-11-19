package com.smiims.iterface;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public interface TSSparkOperation<T> {
	JavaRDD<T> data1DDirectlyToJSC(JavaSparkContext jsc,int startx,int starty,int offsetx,int offsety);
	JavaRDD<T>[] dataDirectlyToJSC(JavaSparkContext jsc,int startx,int starty,int offsetx,int offsety);
}
