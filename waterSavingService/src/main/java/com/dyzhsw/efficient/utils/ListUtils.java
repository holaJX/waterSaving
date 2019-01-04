package com.dyzhsw.efficient.utils;

import java.util.List;

public class ListUtils {

	public static boolean isNotEmpty(List<?> list) {
		if(null!=list && list.size()>0) {
			return true;
		}else {
			return false;
		}
	}
}
