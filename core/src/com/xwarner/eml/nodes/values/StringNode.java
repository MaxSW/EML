package com.xwarner.eml.nodes.values;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

}
