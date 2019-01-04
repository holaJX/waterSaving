package com.dyzhsw.efficient.config;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoqingjie
 */
public class Constant {
	
	public static final String JWT_SECRETE_KEY = "7786df7fc3a34e26a61c034d5ec8245d";

	// ChronoUnit 的 Map格式
	public final static Map<String, TemporalUnit> jwtValidityMap = new HashMap<String, TemporalUnit>();

	static {
		for(ChronoUnit ct : ChronoUnit.values()){
			jwtValidityMap.put(ct.toString(), ct);
		}
	}
	
}
