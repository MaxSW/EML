package com.xwarner.eml.nodes.values;

import org.json.JSONObject;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.Node;

public class StringNode extends Node {

	public String value;

	public StringNode(String value) {
		super();
		this.value = value;
	}

	public StringNode() {
		// TODO Auto-generated constructor stub
	}

	public Object invoke2(Bundle bundle) {
		return value;
	}

	public String toString() {
		return "string - value: " + value;
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("a", getClass().getSimpleName());
		obj.put("value", value);
		for (Node n : getChildren()) {
			obj.accumulate("z", n.toJSON());
		}
		return obj;
	}


}
