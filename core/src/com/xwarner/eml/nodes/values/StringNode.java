package com.xwarner.eml.nodes.values;

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

	public Object invoke() {
		return value;
	}

	public String toString() {
		return "string - value: " + value;
	}

}
