package com.smiims.obj;

import java.io.Serializable;

public class GeoIdObj<T> implements Serializable {
	private static final long serialVersionUID = -7271773062865236331L;

	public T value;
	public int idx;
}
