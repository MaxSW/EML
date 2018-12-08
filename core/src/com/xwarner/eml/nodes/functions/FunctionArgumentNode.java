package com.xwarner.eml.nodes.functions;

import com.xwarner.eml.nodes.Node;

public class FunctionArgumentNode extends Node {

	public String type;
	public String name;

	public FunctionArgumentNode(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}

	public FunctionArgumentNode() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "argument - type: " + type + "; name: " + name;
	}

	public String toSaveString() {
		return "4 " + type + " " + name;
	}

	public void fromSaveString(String[] split, String str) {
		type = split[2];
		name = split[3];
	}

}
