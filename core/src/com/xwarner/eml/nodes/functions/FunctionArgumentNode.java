package com.xwarner.eml.nodes.functions;

import org.json.JSONObject;

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

}
