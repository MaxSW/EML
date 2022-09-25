package com.xwarner.eml.nodes.values;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.Node;

public class NumberNode extends Node {

	public float value;

	private BigDecimal cache;

	public NumberNode(float value) {
		super();
		this.value = value;
	}

	public NumberNode(BigDecimal value) {
		super();
	}

	public NumberNode() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "number - value: " + value;
	}

	public Object invoke2(Bundle bundle) {
		if (cache == null)
			cache = BigDecimal.valueOf(value);
		return cache;
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
