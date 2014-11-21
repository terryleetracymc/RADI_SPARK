package com.smiims.geoFile;

import java.util.ArrayList;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;

import com.smiims.obj.GeoShort;

public class TifShortFileBase extends RSDataFileBase<GeoShort> {
	public TifShortFileBase() {
	}

	@Override
	public ArrayList<GeoShort> readData(String path, int startx, int starty,
			int offsetx, int offsety, int id) {
		Dataset dataset = gdal.Open(path);
		Band band = dataset.GetRasterBand(1);
		short data[] = new short[offsetx * offsety];
		ArrayList<GeoShort> gdata = new ArrayList<GeoShort>();
		band.ReadRaster(startx, starty, offsetx, offsety, data);
		int idx = 0;
		for (int i = 0; i < offsetx; i++) {
			for (int j = 0; j < offsety; j++) {
				gdata.add(new GeoShort(i, j, id, data[idx]));
				idx++;
			}
		}
		band.delete();
		dataset.delete();
		return gdata;
	}
}
