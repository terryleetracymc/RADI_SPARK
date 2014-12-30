package com.smiims.executor;

import java.io.FileWriter;
import java.io.IOException;

public class Executor {
	public static void main(String[] args) throws IOException {
//		String subPath[] = { "h24v04", "h25v03", "h25v05", "h26v03",
//				"h26v05", "h27v04", "h27v06", "h28v06", "h29v06", "h23v05",
//				"h24v05", "h25v04", "h25v06", "h26v04", "h26v06", "h27v05",
//				"h28v05", "h28v07" };
		String subPath[] = { "h23v04","h24v04", "h25v03", "h25v05", "h26v03",
				"h26v05", "h27v04", "h27v06", "h28v06", "h29v06", "h23v05",
				"h24v05", "h25v04", "h25v06", "h26v04", "h26v06", "h27v05",
				"h28v05", "h28v07" };
		System.out.println(subPath.length);
		FileWriter fw = new FileWriter("tcommand.sh");
//		String subcmd = "./spark-submit --class com.smiims.app.SaveHDF4GeoShortOriToHDFS --driver-library-path /usr/local/lib/ --master spark://192.168.1.140:7077 /home/terry/jars/RADI_SPARK.jar";
//		String subcmd="./spark-submit --class com.smiims.app.GenerateTSApp --driver-library-path /usr/local/lib/ --master spark://192.168.1.140:7077 /home/terry/jars/RADI_SPARK.jar";
//		String subcmd="./spark-submit --class com.smiims.app.SaveTSDataToMongoDB --driver-library-path /usr/local/lib/ --master spark://192.168.1.140:7077 /home/terry/jars/RADI_SPARK.jar";
		String subcmd="./spark-submit --class com.terry.app.TSDataToSData /home/terry/jars/ts_process.jar";
		for(int i=0;i<subPath.length;i++){
			String cmd=subcmd+" "+subPath[i]+"\n";
			fw.write(cmd);
			fw.flush();
		}
		fw.close();
	}
}
