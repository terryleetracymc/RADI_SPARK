package com.smiims.obj;

import java.io.Serializable;

public class GeoPos implements Serializable, Comparable<GeoPos> {

	private static final long serialVersionUID = -2468803981761342056L;
	public int x;
	public int y;

	public GeoPos(int px, int py) {
		x = px;
		y = py;
	}

	@Override
	public int compareTo(GeoPos v) {
		if (x == v.x && y == v.y) {
			return 0;
		} else if (x + y > v.x + v.y) {
			return 1;
		} else {
			return -1;
		}
	}
}
