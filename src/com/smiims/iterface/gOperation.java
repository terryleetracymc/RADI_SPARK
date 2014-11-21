package com.smiims.iterface;

import java.util.ArrayList;

/**
 * @author liteng
 * @param <T>
 * 对遥感时间序列图像数据的基本操作
 */
public interface gOperation <T>{
	ArrayList<T> readData(String path,int startx,int starty,int endx,int endy,int id);
}
