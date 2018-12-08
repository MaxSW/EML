package com.xwarner.eml.nodes.values;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.Bundle;
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

	public String toSaveString() {
		return "13 " + invoke2(null);
	}
	
	public void fromSaveString(String[] split, String str) {
		value = Float.parseFloat(split[2]);
	}

}
