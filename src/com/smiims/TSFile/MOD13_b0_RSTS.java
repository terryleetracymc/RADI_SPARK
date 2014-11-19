package com.smiims.TSFile;

import java.util.ArrayList;

import com.smiims.constants.MOD13_CONSTANTS;
import com.smiims.geoFile.MOD13_b0;
import com.smiims.utils.FileUtils;

public class MOD13_b0_RSTS extends RSTimeSeriesShortBase {
	public MOD13_b0_RSTS(String rPath) {
		sequenceFiles = getSquencePath(rPath);
	}

	public int readData(int rank, int startx, int starty, int offsetx,
			int offsety) {
		if (sequenceFiles == null) {
			System.err.println("the sequenceFiles is null");
			return -1;
		} else {
			if (rank >= sequenceFiles.size()) {
				System.err.println("the rank is not property");
				return -1;
			} else {
				// 读取数据
				String path = sequenceFiles.get(rank);
				MOD13_b0 mod13 = new MOD13_b0(path, MOD13_CONSTANTS.width,
						MOD13_CONSTANTS.height);
				d1Data=mod13.getData(startx, starty, offsetx, offsety, 0);
			}
		}
		return 0;
	}

	@Override
	public ArrayList<String> getSquencePath(String path) {
		return FileUtils.getFiles(path, "MOD13*.hdf");
	}
}
