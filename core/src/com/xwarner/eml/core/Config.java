package com.xwarner.eml.core;

import java.util.HashMap;

public class Config {

	private HashMap<String, Object> map;

	public Config() {
		this.map = new HashMap<String, Object>();
		// set default values
		map.put("output", true);
	}

	public void set(String name, Object obj) {
		map.put(name, obj);
	}

	public boolean getBoolean(String name) {
		return (boolean) map.get(name);
	}

	public void setBoolean(String name, boolean value) {
		map.put(name, value);
	}

}
