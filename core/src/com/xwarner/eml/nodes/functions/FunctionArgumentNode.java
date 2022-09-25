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

	public void fromSaveString(String[] split, String str) {
		type = split[2];
		name = split[3];
	}

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("a", getClass().getSimpleName());
		obj.put("type", type);
		obj.put("name", name);
		for (Node n : getChildren()) {
			obj.accumulate("z", n.toJSON());
		}
		return obj;
	}

}
