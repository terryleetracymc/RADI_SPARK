package com.smiims.obj;

import java.io.Serializable;

abstract public class GeoObj<T> implements Serializable {

	private static final long serialVersionUID = 7670043837831330931L;
	public GeoPos pos;
	public T value;
	public int idx;

}
