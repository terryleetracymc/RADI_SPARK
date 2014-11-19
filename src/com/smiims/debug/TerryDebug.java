package com.smiims.debug;

import java.util.ArrayList;

import com.smiims.TSFile.MOD13_b0_RSTS;


public class TerryDebug {
	public static void main(String []args){
		MOD13_b0_RSTS rsts=new MOD13_b0_RSTS("/Users/liteng/Desktop/modis/");
		ArrayList<String> list=rsts.getSequenceFiles();
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
		}
	}
}