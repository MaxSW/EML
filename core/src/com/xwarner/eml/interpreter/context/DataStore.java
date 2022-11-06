package com.xwarner.eml.interpreter.context;

import java.util.HashMap;

import com.xwarner.eml.interpreter.context.variables.Variable;

public class DataStore {

	// temporary
	private HashMap<Integer, Variable> map;

	private int nextPos;

	public DataStore() {
		map = new HashMap<Integer, Variable>();
		nextPos = 0;
	}

	public int put(Variable var) {
		map.put(nextPos, var);
		nextPos++;
		return nextPos - 1;
	}

	public Variable get(int key) {
		return map.get(key);
	}

}
