package com.smiims.TSFile;

import java.util.ArrayList;

import com.smiims.utils.FileUtils;

public class MOD13_b0_RSTS extends RSTimeSeriesShortBase {
	public MOD13_b0_RSTS(String rPath, int r) {
		rank = r;
		sequenceFiles = getSquencePath(rPath);
	}

	@Override
	public ArrayList<String> getSquencePath(String path) {
		return FileUtils.getFiles(path, "MOD13*.hdf");
	}
}
