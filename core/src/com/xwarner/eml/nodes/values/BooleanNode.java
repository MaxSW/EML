package com.xwarner.eml.nodes.values;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.Node;

public class BooleanNode extends Node {

	public boolean value;

	public BooleanNode(boolean value) {
		super();
		this.value = value;
	}

	public BooleanNode() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "boolean - value: " + value;
	}

	public Object invoke2(Bundle bundle) {
		return value;
	}

	public String toSaveString() {
		return "12 " + value;
	}
	
	public void fromSaveString(String[] split, String str) {
		value = Boolean.parseBoolean(split[2]);
	}

}
