package com.smiims.iterface;

import org.apache.spark.api.java.JavaRDDLike;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @author liteng spark的输入操作接口
 */
public interface sparkOperation {
	@SuppressWarnings("rawtypes")
	JavaRDDLike operate(JavaSparkContext jsc, String[] args);
}
